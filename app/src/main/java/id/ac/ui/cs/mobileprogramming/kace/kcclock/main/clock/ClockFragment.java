package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.clock;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class ClockFragment extends Fragment implements WeatherAsyncTask.WeatherCallback {
    @BindView(R.id.text_clock) TextView textClock;

    private ClockViewModel clockViewModel;
    private WeatherAsyncTask weatherAsyncTask;

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

        weatherAsyncTask = new WeatherAsyncTask(this);
        weatherAsyncTask.execute();

    }

    @Override
    public void onStop() {
        weatherAsyncTask.cancel(true);
        super.onStop();
    }

    public void onDataLoaded(String data) {
        clockViewModel.setText(data);
    }
}
