package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.clock;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class ClockFragment extends Fragment implements WeatherAsyncTask.WeatherCallback,
        LocationListener {
    @BindView(R.id.text_clock) TextView textClock;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private ClockViewModel clockViewModel;
    private WeatherAsyncTask weatherAsyncTask;
    private LocationManager locationManager;
    protected Double lat = null;
    protected Double lon = null;

    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);

        clockViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textClock.setText(s);
            }
        });

        initWeatherAsyncTask(this);

    }

    @Override
    public void onStop() {
        try {
            weatherAsyncTask.cancel(true);
        } catch (Exception e) {
            Log.d("AsyncTask Cancellation", e.toString());
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try {
            weatherAsyncTask.cancel(true);
        } catch (Exception e) {
            Log.d("AsyncTask Cancellation", e.toString());
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void initWeatherAsyncTask(WeatherAsyncTask.WeatherCallback cb) {
        if (checkPermission()) {
            try {
                locationManager = (LocationManager) getAppContext().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                lat = location.getLatitude();
                lon = location.getLongitude();
            } catch (NullPointerException e) {
                Log.d("Init Weather AsyncTask", e.toString());
                e.printStackTrace();
                onDataLoaded("Weather service:\nTurn on device's location service and wait for a moment to access weather service.");
            }

            weatherAsyncTask = new WeatherAsyncTask(cb);
            weatherAsyncTask.execute();
        } else {
            requestPermission();
        }

    }

    @Override
    public void onDataLoaded(String data) {
        clockViewModel.setText(data);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Left empty because provider sudden change to disabled won't change lat long used for weather.
        // Weather only use the lat long obtain when this fragment got RESUMED and won't change until it get stopped/destroyed and get RESUMED again
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Left empty because even if provider is enabled, Location object can still be null and thus can't obtain lat and long.
        // So it's useless to re-check location in this callback.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Deprecated in API Level 29
    }

    private boolean checkPermission() {
        try {
            int result = ContextCompat.checkSelfPermission(getAppContext(), ACCESS_FINE_LOCATION);
            return result == PackageManager.PERMISSION_GRANTED;
        } catch (Exception e) {
            Log.d("WEATHER_CheckPermission", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private void requestPermission() {
        requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted)
                        initWeatherAsyncTask(this);
                    else {
                        String locationDeniedMsg = "Weather service:\nYou need to allow location permission for this app to use weather service.";
                        String locationDeniedDialogMsg = locationDeniedMsg + " You can still use the rest of this app's features without location permission.";
                        onDataLoaded(locationDeniedMsg);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel(locationDeniedDialogMsg,
                                        (dialog, which) -> {
                                            requestPermission();
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, okListener)
                .setNegativeButton(R.string.action_cancel, null)
                .create()
                .show();
    }
}
