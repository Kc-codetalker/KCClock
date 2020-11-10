package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class TimeBasedAlarmFragment extends Fragment {

    private TimeBasedAlarmViewModel mViewModel;

    public static TimeBasedAlarmFragment newInstance() {
        return new TimeBasedAlarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_based_alarm, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TimeBasedAlarmViewModel.class);
        // TODO: Use the ViewModel
    }

}
