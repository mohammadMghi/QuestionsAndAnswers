package com.example.questionandanswer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.questionandanswer.JARcustom;
import com.example.questionandanswer.view.NewQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ApiService {


    private final Context context;

    public ApiService(Context context) {
        this.context = context;
    }

    public void loginUser(String email, String password, final OnLoginResponse onLoginResponse) {
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("email", email);
            requestJsonObject.put("password", password);

            //TODO :: login
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/user/login", requestJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.has("token") && !response.isNull("token")) {
                        onLoginResponse.onResponse(response.optString("token"));
                    } else if ((response.has("error") && !response.isNull("error"))) {
                        try {
                            if (response.getString("error") == "invalid_credentials") {
                                onLoginResponse.onResponseError(0);
                            } else if (response.getString("error") == "could_not_create_token") {
                                onLoginResponse.onResponseError(1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("response", response.toString());
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
                    if(error.toString().equals("com.android.volley.AuthFailureError")){
                        Toast.makeText(context, "username or password is wrong", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "please check your intent connection", Toast.LENGTH_SHORT).show();
                    }
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
            request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(request);
        } catch (JSONException e) {
            Log.e(TAG, "loginUser: " + e.toString());
        }
    }

    public void RegisterUser(String name, String email, String password, final OnRegisterResponse onRegisterResponse) {
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("email", email);
            requestJsonObject.put("password", password);
            requestJsonObject.put("name", name);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/user/register", requestJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    onRegisterResponse.onResponse(response.optString("token"));

                    Log.d("response", response.toString());
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
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
            request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(request);
        } catch (JSONException e) {
            Log.e(TAG, "loginUser: " + e.toString());
        }
    }

    public void getUserInfo(final String token, final OnRecivedInfo onRecivedInfo) {


        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "http://192.168.1.4:8000/api/users", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = new User();
                try {
                    user.setEmail(response.getString("email"));
                    user.setName(response.getString("name"));
                    user.setId(response.getInt("id"));
                    if (response.has("img_profile") && !response.isNull("img_profile"))
                        user.setURL_prifle(response.getString("img_profile"));
                    else
                        user.setURL_prifle("null");

                    onRecivedInfo.onRecivied(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

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
                headers.put("Authorization", token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }


    public void sendQuestion(int id_user, String title, String content, Bitmap bitmap, final OnRecivedStatusQuestion onRecivedStatusQuestion) {
        JSONObject requestJsonObject = new JSONObject();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                requestJsonObject.put("img", encodedImage);
            } else {
                requestJsonObject.put("img", "null");
            }
            requestJsonObject.put("id_user", id_user);
            requestJsonObject.put("title", title);
            requestJsonObject.put("content", content);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/newQuestion", requestJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getBoolean("state")) {
                            onRecivedStatusQuestion.Status(true);

                        } else {
                            onRecivedStatusQuestion.Status(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("response", response.toString());
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
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
            request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(request);
        } catch (JSONException e) {
            Log.e(TAG, "loginUser: " + e.toString());
        }
    }


    public void getMainQuestions(final OnRecievedMainQuestion onRecievedMainQuestion) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "http://192.168.1.4:8000/api/mainQuestion", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                ArrayList<Question> questions = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Question question = new Question();
                        JSONObject jsonObject = response.getJSONObject(i);
                        question.setId(jsonObject.getInt("id"));
                        question.setTitle(jsonObject.getString("title"));
                        question.setContent(jsonObject.getString("content"));
                        if (jsonObject.has("relation") && !jsonObject.isNull("relation")) {
                            JSONObject relation = jsonObject.getJSONObject("relation");
                            question.setUrlImageQuestion(relation.getString("img_question"));
                        }
                        questions.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                onRecievedMainQuestion.Recivied(questions);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

    /*public void getImageOfQuestion(int idQuestion, final OnReceivedIMGQuestion onRecievedMainQuestion) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_question",idQuestion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JARcustom request = new JARcustom(Request.Method.POST, "http://192.168.1.4:8000/api/mainImgQuestion", jsonObject , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Question question = new Question();
                try {
                    JSONObject jsonArray = response.getJSONObject(0);
                    question.setUrlImageQuestion(jsonArray.getString("img_question"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onRecievedMainQuestion.Received(question);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }*/

    public void sendAnswer(int idQuestion, int idUser, String content, final PostCommand postCommand) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_question", idQuestion);
            jsonObject.put("id_user", idUser);
            jsonObject.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/getCommend", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int stateCommand = response.getInt("state");
                    if (stateCommand == 1) {
                        postCommand.Status(1);
                    } else {
                        postCommand.Status(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void getAnwers(int idQuestion, final OnReceivedAnswers onReceivedAnswers) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_question", idQuestion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JARcustom jsonObjectRequest = new JARcustom(Request.Method.POST, "http://192.168.1.4:8000/api/answers", jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Answers> answers = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Answers answer = new Answers();
                        JSONObject jsonObject = response.getJSONObject(i);
                        answer.setContent(jsonObject.getString("content"));
                        JSONObject objectUsers = jsonObject.getJSONObject("users");
                        JSONObject relation = jsonObject.getJSONObject("relation");
                        answer.setUsername(objectUsers.getString("name"));
                        if (relation.has("img_question") && !relation.isNull("img_question")) {
                            answer.setUrlProfile(relation.getString("img_question"));
                        } else {
                            answer.setUrlProfile("null");
                        }
                        answers.add(answer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                onReceivedAnswers.Revivied(answers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void uploadImageProfile(int id_user,  Bitmap bitmap, final OnRecieviedImg onRecieviedImg) {
        JSONObject requestJsonObject = new JSONObject();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                requestJsonObject.put("img_profile", encodedImage);
            }
            requestJsonObject.put("id_user", id_user);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.4:8000/api/profileImage", requestJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getBoolean("state")) {
                            onRecieviedImg.onRecieved(true);

                        } else {
                            onRecieviedImg.onRecieved(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("response", response.toString());
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", error.toString());
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
            request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(request);
        } catch (JSONException e) {
            Log.e(TAG, "loginUser: " + e.toString());
        }
    }





    public interface OnLoginResponse {
        void onResponse(String token);
        void onResponseError(int state);
    }

    public interface OnRecieviedImg{
        void onRecieved(boolean state);
    }
    public interface OnRegisterResponse {
        void onResponse(String token);
    }
    public interface OnRecivedInfo {
        void onRecivied(User user);
    }

    public interface OnRecivedStatusQuestion {
        void Status(boolean state);
    }

    public interface OnRecievedMainQuestion {
        void Recivied(ArrayList<Question> question);
    }

    public interface OnReceivedIMGQuestion {
        void Received(Question question);
    }

    public interface PostCommand {
        void Status(int state);
    }

    public interface OnReceivedAnswers {
        void Revivied(ArrayList<Answers> answers);
    }
}