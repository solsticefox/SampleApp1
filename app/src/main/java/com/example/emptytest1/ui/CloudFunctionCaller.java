package com.example.emptytest1.ui;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.emptytest1.ui.anotherscreen.SecondScreenViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CloudFunctionCaller {
    private static final String TAG = "CloudFunctionCaller";
    private final Context context;

    private int numSum;

    public int getSum() {
        return numSum;
    }

    public CloudFunctionCaller(Context context) {
        this.context = context;
    }

    public void callCloudFunctionAndStoreResult(SecondScreenViewModel vm) {
        // Replace with your function URL
        String url = "https://processnumbersfromdatabase-b7medtspva-uc.a.run.app";

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Make a GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response
                            boolean isPrime = response.getBoolean("isPrime");
                            int sum = response.getInt("sum");

                            // Store or use the result as needed
                            Log.d(TAG, "Sum: " + sum + ", Is Prime: " + isPrime);
                            vm.isPrime.setValue(isPrime);
                            numSum = sum;
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error calling Cloud Function", error);
                    }
                }
        );

        // Add the request to the request queue
        queue.add(jsonObjectRequest);
    }
}

