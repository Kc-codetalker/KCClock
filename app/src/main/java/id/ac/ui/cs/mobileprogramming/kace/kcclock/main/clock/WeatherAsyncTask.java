package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.clock;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class WeatherAsyncTask extends AsyncTask<String, String, Void> {
    WeatherCallback mCb;

    public interface WeatherCallback {
        public void onDataLoaded(String data);
    }

    public WeatherAsyncTask(WeatherCallback cb) {
        mCb = cb;
    }

    protected Void doInBackground(String... str) {
        boolean keepLooping = true;
        while (!isCancelled() && keepLooping) {
            if (isInternetConnected()) {
                getWeatherFromOpenWeather();
            } else {
                Log.d("WEATHER_JOB", "Prevent API call, no internet connection.");
                publishProgress("Make sure your device has internet connection to access weather.");
            }
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                keepLooping = false;
            }
        }
        return null;
    }

    protected void onProgressUpdate(String... value) {
        mCb.onDataLoaded(value[0]);
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void getWeatherFromOpenWeather() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getAppContext());

        double lat = -6.2088;
        double lon = 106.8456;
        String apikey = "1ab1bd8961572600d1b31710b863834b";
        String units = "metric";

        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, apikey, units);

        HashMap<String, List<String>> params = new HashMap<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEATHER_RESPONSE", "Weather request response: " + response.toString());
                        try {
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            String weatherDesc = weather.getString("description");
                            mCb.onDataLoaded("Weather: " + weatherDesc);
                        } catch(JSONException e) {
                            Log.d("Weather Response Error", e.toString());
                            e.printStackTrace();
                            mCb.onDataLoaded("No weather data for this location.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WEATHER_RESPONSE", "Weather request error response: " + error.toString());
                        error.printStackTrace();
                        mCb.onDataLoaded("Make sure your device has internet connection to access weather.");
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
