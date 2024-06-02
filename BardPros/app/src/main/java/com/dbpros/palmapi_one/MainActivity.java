package com.dbpros.palmapi_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private EditText editText;
    private  String stringAPIKey = "YOURAPIKEY";

    private  String stringURLEndPoint = "YOURSTRINGURLENDPOINT="+stringAPIKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
    }
        public void buttonPaLMAPI(View view) {

            String stringInputText = "hi";
            if (!editText.getText().toString().isEmpty()) {
                stringInputText = editText.getText().toString();
            }

            JSONObject jsonObject = new JSONObject();

            JSONObject jsonObjectText = new JSONObject();
            try {
                jsonObjectText.put("text", stringInputText);
                jsonObject.put("prompt", jsonObjectText);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringURLEndPoint, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String stringOutput = response.getJSONArray("candidates").getJSONObject(0).getString("output");
                        textView.setText(stringOutput);
                    }
                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    textView.setText("Error");

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> mapHeader = new HashMap<>();
                    mapHeader.put("Content-Type","application/json");
                    return super.getHeaders();
                }
            };

            int intTimeoutPeriod =60000;//60 seconds
            RetryPolicy retryPolicy= new DefaultRetryPolicy(intTimeoutPeriod,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(retryPolicy);
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        }





    }
