package com.test.movieplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.movieplus.adapter.RecyclerViewAdapter;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    MenuItem mitem_search;
    MenuItem mitem_reset;
    MenuItem mitem_logout;
    MenuItem mitem_withdrawal;

    RequestQueue requestQueue;

    String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();

        loadFragment(firstFragment);

        requestQueue = Volley.newRequestQueue(HomeActivity.this);




        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment =null;

                switch (menuItem.getItemId()){
                    case R.id.firstFragment:
                        fragment = firstFragment;
//                        getSupportActionBar().setTitle("Home");
                        break;
                    case R.id.secondFragment:
                        fragment = secondFragment;
//                        getSupportActionBar().setTitle("AR");
                        break;
                    case R.id.thirdFragment:
                        fragment = thirdFragment;
//                        getSupportActionBar().setTitle("Liked");
                        break;
                }
                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        mitem_search = menu.findItem(R.id.mitem_search);
        mitem_reset = menu.findItem(R.id.mitem_reset);
        mitem_logout = menu.findItem(R.id.mitem_logout);
        mitem_withdrawal = menu.findItem(R.id.mitem_withdrawal);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.mitem_search){
            Intent i = new Intent(this,SearchActivity.class);
            startActivity(i);
        }
        if(id == R.id.mitem_reset){
                    url = Utils.BASE_URL + Utils.PATH_LIKESRESET;
            addNetworkData_Reset(url);
        }
        if(id == R.id.mitem_logout){
                    url = Utils.BASE_URL + Utils.PATH_LOGOUT;
            addNetworkData_Logout(url);
            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("token",null);
            editor.putBoolean("cb",false);
            editor.apply();
            Intent i = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if(id == R.id.mitem_withdrawal){
                    url = Utils.BASE_URL + Utils.PATH_WITHDRAWAL;
            addNetworkData_withdrawal(url);
        }
        return super.onOptionsItemSelected(item);
    }
    private void addNetworkData_Logout(String url) {

        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                boolean success = false;
                                Log.i("AAA",response.toString());
                                try{
                                    success = response.getBoolean("success");
                                        if(success == false){
                                            Toast.makeText(HomeActivity.this,"처리실패",Toast.LENGTH_LONG).show();

                                        }

                                    Toast.makeText(HomeActivity.this, "로그아웃 완료", Toast.LENGTH_SHORT).show();
                                        finish();

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
                               getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);

                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+token);
                        return headers;
                    }
                };
        requestQueue.add(request);
    }
    private void addNetworkData_Reset(String url) {


        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                boolean success = false;
                                Log.i("AAA",response.toString());
                                try{
                                    success = response.getBoolean("success");
                                    if(success == false){
                                        Toast.makeText(HomeActivity.this,"처리실패",Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(HomeActivity.this, "초기화 완료", Toast.LENGTH_SHORT).show();

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
                                getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        final String token = sharedPreferences.getString("token", null);

                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization","Bearer "+token);
                        return headers;
                    }
                };
        requestQueue.add(request);
    }
    private void addNetworkData_withdrawal(String url) {


        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                boolean success = false;
                                Log.i("AAA",response.toString());
                                try{
                                    success = response.getBoolean("success");
                                    if(success == false){
                                        Toast.makeText(HomeActivity.this,"처리실패",Toast.LENGTH_LONG).show();

                                    }
                                    Toast.makeText(HomeActivity.this, "초기화 완료", Toast.LENGTH_SHORT).show();

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
                                getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
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