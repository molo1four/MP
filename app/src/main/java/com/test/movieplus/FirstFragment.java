package com.test.movieplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.test.movieplus.adapter.Recom_RecyclerViewAdapter;
import com.test.movieplus.adapter.RecyclerViewAdapter;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RequestQueue requestQueue;
    ArrayList<Movies> moviesArrayList1_Recom = new ArrayList<>();
    ArrayList<Movies> moviesArrayList_AR = new ArrayList<>();
    RecyclerView recyclerView_Recom;// 프래그먼트 화면에 있는 일반추천 리사이클러 뷰
    RecyclerView recyclerView_AR;// 프래그먼트 화면에 있는 연관추천 리사이클러 뷰
    Recom_RecyclerViewAdapter recyclerViewAdapter;    // 우리가 만든, 하나의 셀을 연결시키는 어댑터

    int offset = 0;
    int limit = 25;
    int cnt ;
    String url_Recom ;
    String url_AR;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_first,container,false);
        // 일반추천 리사이클러뷰 설정
        recyclerView_Recom = rootView.findViewById(R.id.recyclerView_Recom);
        recyclerView_Recom.setHasFixedSize(true);
        recyclerView_Recom.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false
        ));
        // 연관추천 리사이클러뷰 설정
        recyclerView_AR = rootView.findViewById(R.id.recyclerView_AR);
        recyclerView_AR.setHasFixedSize(true);
        recyclerView_AR.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false
        ));
        // 발리 설정
        requestQueue = Volley.newRequestQueue(getActivity());

        // 일반추천 api 실행 및 해당 리사이클러뷰 연결
        url_Recom = Utils.BASE_URL + Utils.PATH_GETRECOM+"/?offset=" + offset+ "&limit=" + limit;
        addNetworkData_Recom(url_Recom,offset);

        //moviesArrayList.clear();

        // 연관추천 api 실행 및 해당 리사이클러뷰 연결
        url_AR = Utils.BASE_URL + Utils.PATH_GETRECOM_AR+"/?offset=" + offset+ "&limit=" + limit;
        addNetworkData_AR(url_AR,offset);

        // Inflate the layout for this fragment
        return rootView;
    }
    // 일반추천 네트워크 연결 함수
    private void addNetworkData_Recom(String url_Recom, final int offset) {


        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, url_Recom, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                if(offset < 25) {
                                    moviesArrayList1_Recom.clear();
                                }
                                try{
                                    JSONArray rows = response.getJSONArray("rows");
                                    int cnt1 = response.getInt("cnt");
                                    for(int i = 0; i< rows.length(); i++){
                                        JSONObject jsonObject = rows.getJSONObject(i);
                                        int movie_id = jsonObject.getInt("movie_id");
                                        Log.i("AAA",""+movie_id);
                                        String title = jsonObject.getString("title");
                                        Log.i("AAA",""+title);
                                        String year = jsonObject.getString("release_date");
                                        String photo_url = jsonObject.getString("poster_path");
                                        Movies movies = new Movies(movie_id,title,year,photo_url);
                                        moviesArrayList1_Recom.add(movies);
                                        Log.i("imok",""+movie_id);


                                    }
                                    if(offset >=25){
                                        recyclerViewAdapter.setMoviesArrayList(moviesArrayList1_Recom);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }else {
                                        recyclerViewAdapter = new Recom_RecyclerViewAdapter(getActivity(), moviesArrayList1_Recom);
                                        recyclerView_Recom.setAdapter(recyclerViewAdapter);
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
                                getActivity().getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);

                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+token);
                        return headers;
                    }
                };
        requestQueue.add(request);
    }


    // 연관추천 네트워크 연결 함수
    private void addNetworkData_AR(String url_AR, final int offset) {


        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, url_AR, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                if(offset < 25) {
                                    moviesArrayList_AR.clear();
                                }
                                try{
                                    JSONArray rows = response.getJSONArray("rows");
                                    int cnt1 = response.getInt("cnt");
                                    for(int i = 0; i< rows.length(); i++){
                                        JSONObject jsonObject = rows.getJSONObject(i);
                                        int movie_id = jsonObject.getInt("movie_id");
                                        Log.i("AAA",""+movie_id);
                                        String title = jsonObject.getString("title");
                                        Log.i("AAA",""+title);
                                        String year = jsonObject.getString("release_date");
                                        String photo_url = jsonObject.getString("poster_path");
                                        Movies movies = new Movies(movie_id,title,year,photo_url);
                                        moviesArrayList_AR.add(movies);
                                        Log.i("imok",""+movie_id);


                                    }
                                    if(offset >=25){
                                        recyclerViewAdapter.setMoviesArrayList(moviesArrayList_AR);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }else {
                                        recyclerViewAdapter = new Recom_RecyclerViewAdapter(getActivity(), moviesArrayList_AR);
                                        recyclerView_AR.setAdapter(recyclerViewAdapter);
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
                                getActivity().getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
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