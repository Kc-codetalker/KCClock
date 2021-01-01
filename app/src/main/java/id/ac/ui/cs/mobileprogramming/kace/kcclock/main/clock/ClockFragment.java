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

public class ClockFragment extends Fragment implements WeatherAsyncTask.WeatherCallback,
        LocationListener {
    @BindView(R.id.text_clock) TextView textClock;

    private static final int PERMISSION_REQUEST_CODE = 200;

    private ClockViewModel clockViewModel;
    private WeatherAsyncTask weatherAsyncTask;
    private LocationManager locationManager;

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

    private void initWeatherAsyncTask(WeatherAsyncTask.WeatherCallback cb) {
        if (checkPermission()) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            weatherAsyncTask = new WeatherAsyncTask(cb);
            weatherAsyncTask.execute();
        } else {
            requestPermission();
        }

    }

    public void onDataLoaded(String data) {
        clockViewModel.setText(data);
    }

    @Override
    public void onLocationChanged(Location location) {
//        txtLat = (TextView) findViewById(R.id.textview1);
//        textClock.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("Latitude","status");
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
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
                        String locationDeniedMsg = "Weather feature:\nYou need to allow location permission for this app to use weather feature.";
                        String locationDeniedDialogMsg = locationDeniedMsg + " You can still use the rest of this app's features without location permission.";
                        onDataLoaded(locationDeniedMsg);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel(locationDeniedDialogMsg,
                                        (dialog, which) -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                            }
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
