package com.test.movieplus;

import androidx.annotation.NonNull;
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
    JSONArray jsonArray = new JSONArray(); // 좋아요를 누른 영화 목록을 저장하는 어레이
    RecyclerView recyclerView;  // 메인 화면에 있는 리사이클러 뷰
    RecyclerViewAdapter recyclerViewAdapter;    // 우리가 만든, 하나의 셀을 연결시키는 어댑터

    Button btnDone;

    int offset = 0;
    int limit = 25;
    int cnt ;


    String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_check);

        btnDone = findViewById(R.id.btnDone);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteCheckActivity.this));

        requestQueue = Volley.newRequestQueue(FavoriteCheckActivity.this);

        url = Utils.BASE_URL + Utils.PATH_GETMOVIES+"/?offset=" + offset+ "&limit=" + limit;
        addNetworkData(url,offset);


        //완료 버튼 눌렀을 시
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.getMoviesArrayList();
                Log.i("777","check");
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
                // volloey로 api 송신
                saveMovieLikes();
                Intent i = new Intent(FavoriteCheckActivity.this, HomeActivity.class);
                // HomeActivity 실행
                startActivity(i);
                // 첫 로그인 시에만 작동하는 액티비티이기에, finish();
                finish();
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
                        SharedPreferences sharedPreferences =
                                getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);

                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Bearer " + token);
                        Log.i("aaa",token);
                        return params;
                    }
                } ;
                Volley.newRequestQueue(FavoriteCheckActivity.this).add(request);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition+1 == totalCount ){
                    //아이템 추가 ! 입맛에 맞게 설정하시면됩니다.
                    Log.i("777","맨 마지막 도착");
                    if (cnt == limit) {
                        offset = offset+cnt;
                        url = Utils.BASE_URL + Utils.PATH_GETMOVIES+ "/?offset=" + offset+ "&limit=" + limit;
                        //이 url로 네트워크 데이터 요청.
                        Log.i("AAA",url);
                        addNetworkData(url,offset);
                    }
                }

            }
        });
    }

    private void addNetworkData(String url, final int offset) {


        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                if(offset < 25) {
                                    moviesArrayList.clear();
                                }
                                try{
                                    JSONArray rows = response.getJSONArray("rows");
                                    int cnt1 = response.getInt("cnt");
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
                                    if(offset >=25){
                                        recyclerViewAdapter.setMoviesArrayList(moviesArrayList);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }else {
                                        recyclerViewAdapter = new RecyclerViewAdapter(FavoriteCheckActivity.this, moviesArrayList);
                                        recyclerView.setAdapter(recyclerViewAdapter);
                                    }

                                    cnt=cnt1;
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
                        SharedPreferences sharedPreferences =
                                getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);

                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+token);
                        return headers;
                    }
                };
        requestQueue.add(request);
    }
}