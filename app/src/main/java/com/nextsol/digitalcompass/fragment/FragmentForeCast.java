package com.nextsol.digitalcompass.fragment;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nextsol.digitalcompass.R;
import com.nextsol.digitalcompass.Utils.FormatDate;
import com.nextsol.digitalcompass.Utils.GlobalApplication;
import com.nextsol.digitalcompass.Utils.NumderUltils;
import com.nextsol.digitalcompass.adapter.ForeCastAdapter;
import com.nextsol.digitalcompass.api.OpenWeatherAPI;
import com.nextsol.digitalcompass.model.Forecast;
import com.nextsol.digitalcompass.model.IconForeCast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentForeCast extends Fragment {
    ImageView imageViewReload, imageViewIcon;
    TextView textViewCity, textViewUpdateTime, textViewDegree, textViewWindSpeed,
            textViewWindDir, textViewHumidity, textViewPressure,
            textViewStatus, textViewVisibility, textViewFeelslike, textViewminmaxTemp;
    Calendar calendar;
    GlobalApplication globalApplication;
    Location location;
    RecyclerView recyclerViewDays;
    ArrayList<Forecast> listForecast3Hour;
    ArrayList<Forecast> listForeCastDays;
    ForeCastAdapter foreCastAdapter;
    Map<String, Integer> map;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fore_cast, container, false);
        init(view);
        getLocation();
        listener(view);
        getForecast(location.getLatitude(), location.getLongitude());
        getGeoCast5Days(location.getLatitude(), location.getLongitude());
        return view;
    }

    private void getLocation() {
        if (globalApplication.location != null) {
            location = globalApplication.location;


        } else {
            location = new Location("");
            location.setLatitude(21.0291263);
            location.setLongitude(105.8075145);

        }
    }

    private void getForecast(final double lattitude, final double longtitude) {
        textViewUpdateTime.setVisibility(View.INVISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                OpenWeatherAPI.getPathAsGeo(lattitude, longtitude, 1), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Forecast forecast = new Forecast();
                            try {
                                JSONArray jsonArray = response.getJSONArray("weather");

                                forecast.setIcon(map.get(jsonArray.getJSONObject(0).getString("icon")));
                                forecast.setStatus(jsonArray.getJSONObject(0).getString("description"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONObject jsonObject = response.getJSONObject("main");
                                forecast.setTemp(jsonObject.getDouble("temp"));
                                forecast.setMintemp(jsonObject.getDouble("temp_min"));
                                forecast.setMaxtemp(jsonObject.getDouble("temp_max"));
                                forecast.setFeelslike(jsonObject.getDouble("feels_like"));
                                forecast.setHumidity(jsonObject.getDouble("humidity"));
                                forecast.setPressure(jsonObject.getDouble("pressure"));
                                jsonObject = response.getJSONObject("wind");
                                forecast.setSpeedWind(jsonObject.getDouble("speed"));
                                try {
                                    forecast.setDirWind(jsonObject.getDouble("deg"));
                                } catch (Exception e){}
                                forecast.setTimeStamp(response.getLong("dt"));
                                forecast.setCity(response.getString("name"));
                                if (response.getString("visibility").length() == 0 || response.getString("visibility") == null) {

                                    forecast.setVisibility(response.getLong("visibility"));

                                } else {
                                    forecast.setVisibility(-1);

                                }

                                calendar = Calendar.getInstance();
                                textViewUpdateTime.setVisibility(View.VISIBLE);
                                textViewUpdateTime.setText(FormatDate.formatDate(FormatDate.simpleformat1, calendar) + " Local Time");
                                textViewminmaxTemp.setText("Day ↑" + (int) (forecast.getMaxtemp()) + "°C ⋅ Night ↓"
                                        + (int) (forecast.getMintemp()) + "°C");
                                textViewDegree.setText((int) (forecast.getTemp()) + "°C");
                                textViewFeelslike.setText("Feels like " + (int) (forecast.getFeelslike()) + "°C");
                                imageViewIcon.setImageResource(forecast.getIcon());
                                textViewStatus.setText(forecast.getStatus());
                                textViewWindSpeed.setText(forecast.getSpeedWind() + "km/h");
                                textViewWindDir.setText(forecast.getDirWind() + "°" + NumderUltils.getsymbolDirection(forecast.getDirWind()));
                                textViewHumidity.setText(forecast.getHumidity() + "%");
                                textViewPressure.setText((int) (forecast.getPressure()) + "mb");
                                if (forecast.getVisibility() != -1) {
                                    textViewVisibility.setText(forecast.getVisibility() + " (" + forecast.getVisibility() / 1000 + " km)");

                                } else {
                                    textViewVisibility.setText("unavailable");

                                }
                                textViewCity.setText(forecast.getCity());
                                textViewCity.setVisibility(View.VISIBLE);


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

    public void getGeoCast5Days(final double lattitude, final double longtitude) {


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, OpenWeatherAPI.getPathAsGeo5Days(lattitude, longtitude, 1), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("11111", "onResponse: 5 ngay " + response.toString());
                if (response != null) {
                    if (response.length() > 0) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list");

                            String timeStamp;
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArraychild = new JSONArray();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Forecast forecast = new Forecast();
                                JSONObject jsonObjectchild = new JSONObject();
                                jsonObject = (JSONObject) jsonArray.get(i);
                                forecast.setTimeStamp(jsonObject.getLong("dt"));
                                forecast.setTemp(jsonObject.getJSONObject("main").getDouble("temp"));
                                forecast.setHumidity(jsonObject.getJSONObject("main").getDouble("humidity"));
                                forecast.setPressure(jsonObject.getJSONObject("main").getDouble("pressure"));
                                forecast.setStatus(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
                                forecast.setIcon(map.get(jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")));
                                forecast.setSpeedWind(jsonObject.getJSONObject("wind").getDouble("speed"));
                                forecast.setDirWind(jsonObject.getJSONObject("wind").getDouble("deg"));
//                                Log.d("2222", "onResponse: "+forecast.getIcon()+"--"+i);
                                listForecast3Hour.add(forecast);
                                if (i % 8 == 0) {
                                    listForeCastDays.add(forecast);

                                }

//                                Log.d("2222", "onResponse: "+jsonObject.getJSONArray("weather")
//                                        .getJSONObject(0).getString("main")+"\n");

                            }
                            foreCastAdapter = new ForeCastAdapter(getActivity(), R.id.recycleviewDays_ForeCast, listForeCastDays);
                            recyclerViewDays.setAdapter(foreCastAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("11111", "onResponse: Error 5 ngay" + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("lat", lattitude + "");
                map.put("lon", longtitude + "");
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void listener(View view) {
        imageViewReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                getForecast(location.getLatitude(), location.getLongitude());

            }
        });
    }

    private void init(View view) {
        imageViewIcon = (ImageView) view.findViewById(R.id.imageviewIcon_ForeCast);
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
        textViewFeelslike = (TextView) view.findViewById(R.id.textviewFeelslike_Forecast);
        textViewminmaxTemp = (TextView) view.findViewById(R.id.textviewMinmaxTemp_ForeCast);
        calendar = Calendar.getInstance();
        globalApplication = (GlobalApplication) getActivity().getApplicationContext();
        recyclerViewDays = (RecyclerView) view.findViewById(R.id.recycleviewDays_ForeCast);
        listForecast3Hour = new ArrayList<>();
        listForeCastDays = new ArrayList<>();
        foreCastAdapter = new ForeCastAdapter(getActivity(), R.id.recycleviewDays_ForeCast, listForeCastDays);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDays.setHasFixedSize(true);
        recyclerViewDays.setNestedScrollingEnabled(false);
        recyclerViewDays.setLayoutManager(linearLayoutManager);
        recyclerViewDays.setAdapter(foreCastAdapter);
        setDataMap();


    }

    public void setDataMap() {

        IconForeCast iconForeCast=new IconForeCast();
        map=iconForeCast.getMapicon();

    }

}
