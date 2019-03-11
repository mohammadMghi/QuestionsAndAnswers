package com.example.questionandanswer.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ApiService {


    private final Context context;

    public ApiService(Context context){
        this.context=context;
    }

            public void loginUser(String email, String password, final OnLoginResponse onLoginResponse){
                JSONObject requestJsonObject=new JSONObject();
                try {
                    requestJsonObject.put("email",email);
                    requestJsonObject.put("password",password);


                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/user/login",requestJsonObject , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            onLoginResponse.onResponse(response.optString("token"));

                            Log.d("response",response.toString());
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error",error.toString());
                        }
                    }) {
                        /**
                         * Passing some request headers*
                         */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("Content-Type", "application/json");
                            headers.put("csrf-token", "X-XSRF-TOKEN");
                            return headers;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(18000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(context).add(request);
                } catch (JSONException e) {
                    Log.e(TAG, "loginUser: "+e.toString());
                }
            }

    public interface OnLoginResponse{
        void onResponse(String token);
    }
}
