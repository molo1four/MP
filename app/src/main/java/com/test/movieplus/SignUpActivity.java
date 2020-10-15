package com.test.movieplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.test.movieplus.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    TextView txtSignUp;
    EditText editEmail;
    EditText editPw;
    EditText editPw2;
    Button btnDo_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtSignUp = findViewById(R.id.txtSignUp);
        editEmail = findViewById(R.id.editEmail);
        editPw = findViewById(R.id.editPw);
        editPw2 = findViewById(R.id.editPw2);

        btnDo_SignUp = findViewById(R.id.btnDo_SignuUp);

        btnDo_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String pw = editPw.getText().toString().trim();
                String pw2 = editPw2.getText().toString().trim();

                if(email.isEmpty()||pw.isEmpty()||pw2.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "이메일, 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.contains("@") == false){
                    Toast.makeText(SignUpActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pw.equals(pw2) == false){
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pw.length() < 4 && pw2.length() < 4){
                    Toast.makeText(SignUpActivity.this,"비밀번호는 4자 이상이여야 합니다.",Toast.LENGTH_SHORT).show();
                }
                
                SignUp();
            }
        });

    }

    private void SignUp() {

        String email = editEmail.getText().toString().trim();
        String pw = editPw.getText().toString().trim();

        JSONObject body = new JSONObject();
        try{
            body.put("email",email);
            body.put("passwd", pw);
        }catch (Exception e){
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(SignUpActivity.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Utils.BASE_URL + Utils.PATH_SIGNUP, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(SignUpActivity.this, "JSON 불러오기 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } catch (Exception e) {
                            Log.i("error", "error" + e);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Log.i("plzerror", ""+error);
            }
        }){
            // 발리 에러 시 헤더에 추가할 부분
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);

    }
}