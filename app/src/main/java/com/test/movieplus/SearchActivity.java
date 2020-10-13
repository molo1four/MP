package com.test.movieplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.test.movieplus.adapter.RecyclerViewAdapter;
import com.test.movieplus.adapter.Search_RecyclerViewAdapter;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch;

    RequestQueue requestQueue;
    ArrayList<Movies> moviesArrayList = new ArrayList<>();
    RecyclerView recyclerView_Search;  // 메인 화면에 있는 리사이클러 뷰
    Search_RecyclerViewAdapter search_recyclerViewAdapter;    // 우리가 만든, 하나의 셀을 연결시키는 어댑터

    int offset = 0;
    int limit = 25;
    int cnt ;

    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearch = findViewById(R.id.btnSearch);
        editSearch = findViewById(R.id.editSearch);

        recyclerView_Search = findViewById(R.id.recyclerView_Search);
        recyclerView_Search.setHasFixedSize(true);
        recyclerView_Search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        requestQueue = Volley.newRequestQueue(SearchActivity.this);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String keyword = editSearch.getText().toString().trim();
                url = Utils.BASE_URL + Utils.PATH_SEARCHMOVIES+"/?offset=" + offset+ "&limit=" + limit + "&keyword=" + keyword;
                addNetworkData(url,offset);

            }
        });

        //페이징 처리
        recyclerView_Search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView_search, int newState) {
                super.onScrollStateChanged(recyclerView_search, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView_search, int dx, int dy) {
                super.onScrolled(recyclerView_search, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView_search.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView_search.getAdapter().getItemCount();

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

    // 영화목록 불러오기
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
                                        int id = jsonObject.getInt("movie_id");
                                        String title = jsonObject.getString("title");
                                        String year = jsonObject.getString("release_date");
                                        String photo_url = jsonObject.getString("poster_path");
                                        String backdrop_url = jsonObject.getString("backdrop_path");
                                        String overview = jsonObject.getString("overview");
                                        Movies movies = new Movies(id,title,year,overview,photo_url,backdrop_url);
                                        moviesArrayList.add(movies);
                                        Log.i("imok",""+id);


                                    }
                                    if(offset >=25){
                                        search_recyclerViewAdapter.setMoviesArrayList(moviesArrayList);
                                        search_recyclerViewAdapter.notifyDataSetChanged();
                                    }else {
                                        search_recyclerViewAdapter = new Search_RecyclerViewAdapter(SearchActivity.this, moviesArrayList);
                                        recyclerView_Search.setAdapter(search_recyclerViewAdapter);
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