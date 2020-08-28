package com.test.movieplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.test.movieplus.adapter.RecyclerViewAdapter;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteCheckActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    ArrayList<Movies> moviesArrayList = new ArrayList<>();

    RecyclerView recyclerView;  // 메인 화면에 있는 리사이클러 뷰
    RecyclerViewAdapter recyclerViewAdapter;    // 우리가 만든, 하나의 셀을 연결시키는 어댑터

    JSONArray jsonArray = new JSONArray();

    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_check);

        btnDone = findViewById(R.id.btnDone);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteCheckActivity.this));

        requestQueue = Volley.newRequestQueue(FavoriteCheckActivity.this);



        SharedPreferences sharedPreferences =
                getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", null);

        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Utils.BASE_URL + Utils.PATH_GETMOVIES + "/?offset=" + "0" + "&limit=" + "25", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                moviesArrayList.clear();
                                try{
                                JSONArray rows = response.getJSONArray("rows");
                                    for(int i = 0; i< rows.length(); i++){
                                        JSONObject jsonObject = rows.getJSONObject(i);
                                        int movie_id = jsonObject.getInt("movie_id");
                                        String title = jsonObject.getString("title");
                                        String year = jsonObject.getString("release_date");
                                        String photo_url = jsonObject.getString("poster_path");
                                        Movies movies = new Movies(movie_id,title,year,photo_url);
                                        moviesArrayList.add(movies);
                                        Log.i("ssibal",""+movie_id);


                                    }
                                    recyclerViewAdapter = new RecyclerViewAdapter(FavoriteCheckActivity.this, moviesArrayList);
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                }catch (Exception e){
                                    Log.i("ddd",e+"");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    // 발리 에러 시 헤더에 추가할 부분
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+token);
                        return headers;
                    }
                }
                ;
        requestQueue.add(request);

        //완료 버튼 눌렀을 시
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.getMoviesArrayList();
                Log.i("777","fuxk");
            for(int i = 0; moviesArrayList.size()>i; i++){
                Movies movies = moviesArrayList.get(i);
                    if( movies.isChecked() == true){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("movie_id",movies.getId());
                            jsonArray.put(jsonObject);
                            Log.i("777",""+jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                saveMovieLikes();
            }

            private void saveMovieLikes() {
                JSONObject body = new JSONObject();
                try {
                    body.put("movie_id", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestQueue = Volley.newRequestQueue(FavoriteCheckActivity.this);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.BASE_URL + Utils.PATH_ADDLIKES,
                        body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(FavoriteCheckActivity.this, "JSON 불러오기 실패", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else if(success == true){
                                        Toast.makeText(FavoriteCheckActivity.this, "좋아요 목록이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                    }

//                                    Intent i = new Intent(FavoriteCheckActivity.this,HomeActivity.class);
//                                    startActivity(i);
//                                       finish();
                                }catch (Exception e){
                                    Log.i("error", "error" + e);

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                )  {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Bearer " + token);
                        Log.i("aaa",token);
                        return params;
                    }
                } ;
                Volley.newRequestQueue(FavoriteCheckActivity.this).add(request);

            }
        });
    }
}