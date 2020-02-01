package com.example.digitalcompass.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.digitalcompass.R;
import com.example.digitalcompass.Utils.FormatDate;
import com.example.digitalcompass.api.OpenWeatherAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class FragmentForeCast extends Fragment {
    ImageView imageViewReload;
    TextView textViewCity, textViewUpdateTime, textViewDegree, textViewWindSpeed,
            textViewWindDir, textViewHumidity, textViewPressure,
            textViewStatus, textViewVisibility;
    Calendar calendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fore_cast, container, false);
        init(view);
        listener(view);
        getForecast(21.028456, 105.805377);
        return view;
    }

    private void getForecast(final double lattitude, final double longtitude) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                OpenWeatherAPI.getPathAsGeo(lattitude, longtitude, 1), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArray = new JSONArray();
                            String visibility = "";
                            try {

                                jsonObject = response.getJSONObject("main");
                                textViewDegree.setText(Math.round(Float.parseFloat(jsonObject.getString("temp"))) + " °c");
                                textViewPressure.setText(Math.round(Float.parseFloat(jsonObject.getString("pressure"))) + " mb");
                                textViewHumidity.setText(Math.round(Float.parseFloat(jsonObject.getString("humidity"))) + " %");

                                jsonObject = response.getJSONObject("wind");
                                textViewWindSpeed.setText(Math.round(Float.parseFloat(jsonObject.getString("speed"))) + " km/h");

                                textViewWindDir.setText(Math.round(Float.parseFloat(jsonObject.getString("deg"))) + " °");
                                textViewCity.setText(response.getString("name"));
                                textViewCity.setVisibility(View.VISIBLE);

                                if (response.getString("visibility").length() == 0 || response.getString("visibility") == null) {

                                    textViewVisibility.setText("available");

                                } else {
                                    textViewVisibility.setText(response.getString("visibility"));

                                }
                                jsonArray = response.getJSONArray("weather");
                                Log.d("11111", "onResponse: " + jsonArray.toString());
                                textViewStatus.setText(jsonArray.getJSONObject(0).getString("main"));
                                textViewUpdateTime.setText(FormatDate.formatDate(FormatDate.simpleformat1, calendar) + " Local Time");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("11111", "onErrorResponse: " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", lattitude + "");
                params.put("lon", longtitude + "");

                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void listener(View view) {
        imageViewReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(21.028456, 105.805377);

            }
        });
    }

    private void init(View view) {
        imageViewReload = (ImageView) view.findViewById(R.id.imgReload_Maps);
        imageViewReload.setColorFilter(view.getResources().getColor(R.color.mwhite));
        textViewCity = (TextView) view.findViewById(R.id.textviewCity_Forecast);
        textViewUpdateTime = (TextView) view.findViewById(R.id.textviewTimeupdate_Forecast);
        textViewDegree = (TextView) view.findViewById(R.id.textviewDegree_Forecast);
        textViewWindSpeed = (TextView) view.findViewById(R.id.textviewWindspeed_Forecast);
        textViewWindDir = (TextView) view.findViewById(R.id.textviewWindDir_Forecast);
        textViewHumidity = (TextView) view.findViewById(R.id.textviewHumidity_Forecast);
        textViewStatus = (TextView) view.findViewById(R.id.textviewStatus_Forecast);
        textViewVisibility = (TextView) view.findViewById(R.id.textviewVisibility_Forecast);
        textViewPressure = (TextView) view.findViewById(R.id.textviewPressure_Forecast);
        calendar = Calendar.getInstance();
    }

}
