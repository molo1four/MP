package com.test.movieplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.test.movieplus.utils.Utils;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    private AdView mAdView;
    TextView txtLogo;
    EditText editEmail_Login;
    EditText editPw_Login;
    CheckBox checkBox_autoLogin;
    Button btnLogin;
    Button btnSignUp;
    String login_token;
    Boolean checkdone;
    Boolean cb;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        txtLogo = findViewById(R.id.txtLogo);
        editEmail_Login = findViewById(R.id.editEmail_Login);
        editPw_Login = findViewById(R.id.editPw_Login);
        checkBox_autoLogin = findViewById(R.id.checkBox_autoLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignuUp);
        sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        cb = sp.getBoolean("cb",false);
        checkdone = sp.getBoolean("checkdone",false);
        login_token = sp.getString("token",null);
        Log.i("cb",""+cb);
        editor = sp.edit();


        if(cb){
            if(login_token != null){
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.putExtra("token",login_token);
                startActivity(i);
                finish();
            }
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail_Login.getText().toString().trim();
                String pw = editPw_Login.getText().toString().trim();
                if(email.isEmpty() || pw.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.login_err1, Toast.LENGTH_SHORT).show();
                }
                if(checkdone){
                    Login_Home();
                }
                Login();

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    private void Login() {

        String email = editEmail_Login.getText().toString().trim();
        String pw = editPw_Login.getText().toString().trim();

        JSONObject body = new JSONObject();
        try{
            body.put("email",email);
            body.put("passwd", pw);
        }catch (Exception e){
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, Utils.BASE_URL + Utils.PATH_LOGIN, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if(success==false){
                                Toast.makeText(MainActivity.this, "JSON 불러오기 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String token = response.getString("token");

                            editor.putString("token",token);
                            if(checkBox_autoLogin.isChecked()){
                                editor.putBoolean("cb", true);
                            } else{
                                editor.putBoolean("cb", false);
                            }
                            editor.apply();

                            Intent i = new Intent(MainActivity.this,FavoriteCheckActivity.class);
                            i.putExtra("token",token);
                            startActivity(i);
//                            finish();
                        }catch (Exception e){
                            Log.i("error", "error" + e);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonObjectRequest);

    }

    private void Login_Home() {

        String email = editEmail_Login.getText().toString().trim();
        String pw = editPw_Login.getText().toString().trim();

        JSONObject body = new JSONObject();
        try{
            body.put("email",email);
            body.put("passwd", pw);
        }catch (Exception e){
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, Utils.BASE_URL + Utils.PATH_LOGIN, body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("AAA",response.toString());
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(MainActivity.this, R.string.json_ad, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String token = response.getString("token");

                                    editor.putString("token",token);
                                    if(checkBox_autoLogin.isChecked()){
                                        editor.putBoolean("cb", true);
                                    } else{
                                        editor.putBoolean("cb", false);
                                    }
                                    editor.apply();

                                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                                    i.putExtra("token",token);
                                    startActivity(i);
                            finish();
                                }catch (Exception e){
                                    Log.i("error", "error" + e);

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        requestQueue.add(jsonObjectRequest);

    }
}
