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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment thirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RequestQueue requestQueue;
    ArrayList<Movies> moviesArrayList = new ArrayList<>();
    RecyclerView recyclerView;  // 메인 화면에 있는 리사이클러 뷰
    RecyclerViewAdapter recyclerViewAdapter;    // 우리가 만든, 하나의 셀을 연결시키는 어댑터

    int offset = 0;
    int limit = 25;
    int cnt ;
    String url ;

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
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_third,container,false);

        recyclerView = rootView.findViewById(R.id.recyclerView_Liked);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestQueue = Volley.newRequestQueue(getActivity());

        url=Utils.BASE_URL + Utils.PATH_GETLIKED+"/?offset=" + offset+ "&limit=" + limit;
        addNetworkData(url,offset);

        return  rootView;
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
                                        Log.i("AAA",""+movie_id);
                                        String title = jsonObject.getString("title");
                                        Log.i("AAA",""+title);
                                        String year = jsonObject.getString("release_date");
                                        String photo_url = jsonObject.getString("poster_path");
                                        Movies movies = new Movies(movie_id,title,year,photo_url);
                                        moviesArrayList.add(movies);
                                        Log.i("imok",""+movie_id);


                                    }
                                    if(offset >=25){
                                        recyclerViewAdapter.setMoviesArrayList(moviesArrayList);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }else {

                                        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), moviesArrayList);
                                        recyclerViewAdapter.setI("i");
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