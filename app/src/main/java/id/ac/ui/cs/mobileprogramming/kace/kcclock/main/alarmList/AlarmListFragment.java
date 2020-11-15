package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

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

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class AlarmListFragment extends Fragment {

    private AlarmListViewModel alarmListViewModel;

    public static AlarmListFragment newInstance() {
        return new AlarmListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmListViewModel = ViewModelProviders.of(this).get(AlarmListViewModel.class);

        final TextView textView = getView().findViewById(R.id.text_alarm);
        alarmListViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}
