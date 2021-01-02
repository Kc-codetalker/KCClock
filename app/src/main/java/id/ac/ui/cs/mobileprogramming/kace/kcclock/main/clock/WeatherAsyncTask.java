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


import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class WeatherAsyncTask extends AsyncTask<Double, String, Void> {
    WeatherCallback mCb;

    public interface WeatherCallback {
        public void onDataLoaded(String data);
    }

    public WeatherAsyncTask(WeatherCallback cb) {
        mCb = cb;
    }

    protected Void doInBackground(Double... coords) {
        boolean keepLooping = true;
        while (!isCancelled() && keepLooping) {
            if (isInternetConnected()) {
                getWeatherFromOpenWeather();
            } else {
                Log.d("WEATHER_JOB", "Prevent API call, no internet connection.");
                publishProgress("Weather service:\nMake sure your device has internet connection to access weather.");
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
        RequestQueue queue = Volley.newRequestQueue(getAppContext());

        Double lat = ((ClockFragment)mCb).lat; // e.g. -7.5088
        Double lon = ((ClockFragment)mCb).lon; // e.g. 115.8456
        String api_key = ((ClockFragment) mCb).getString(R.string.open_weather_api_key);
        String units = "metric";

        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, api_key, units);

        HashMap<String, List<String>> params = new HashMap<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEATHER_RESPONSE", "Weather request response: " + response.toString());
                        try {
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            JSONObject coords = response.getJSONObject("coord");
                            JSONObject main = response.getJSONObject("main");
                            String locationName = response.getString("name");

                            String weatherDesc = weather.getString("description");
                            double lat = coords.getDouble("lat");
                            double lon = coords.getDouble("lon");
                            double temp = main.getDouble("temp");
                            double pressure = main.getDouble("pressure");
                            double humidity = main.getDouble("humidity");

                            String result = String.format(
                                    "Weather: %s\nLocation: %s\nLatitude: %.2f\nLongitude: %.2f\n"
                                            + "Temperature: %.2f\u00B0C\nPressure: %.2f hPa\nHumidity: %.2f%%"
                                            + "\nUnits system: %s",
                                    weatherDesc,
                                    locationName, lat, lon,
                                    temp, pressure, humidity,
                                    units
                            );
                            mCb.onDataLoaded(result);
                        } catch(JSONException e) {
                            Log.d("Weather Response Error", e.toString());
                            e.printStackTrace();
                            mCb.onDataLoaded("Weather service:\nNo weather data for current location.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WEATHER_RESPONSE", "Weather request error response: " + error.toString());
                        error.printStackTrace();
                        mCb.onDataLoaded("Weather service:\nCannot find weather for current location. Make sure your device's internet connection and location service is enabled.");
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }
}
