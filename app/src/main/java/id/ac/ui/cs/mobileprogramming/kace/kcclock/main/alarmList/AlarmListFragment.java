package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

public class AlarmListFragment extends Fragment implements OnToggleTimeBasedAlarmListener {
    @BindView(R.id.alarmListRecyclerView) RecyclerView alarmRecyclerView;

    private AlarmListViewModel alarmListViewModel;
    private AlarmListAdapter alarmRecyclerViewAdapter;

    public static AlarmListFragment newInstance() {
        return new AlarmListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmRecyclerViewAdapter = new AlarmListAdapter(this);
        alarmListViewModel = ViewModelProviders.of(this).get(AlarmListViewModel.class);
        alarmListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setTimeBasedAlarms(alarms);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        ButterKnife.bind(this, view);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmRecyclerView.setAdapter(alarmRecyclerViewAdapter);

//        addAlarm = view.findViewById(R.id.fragment_listalarms_addAlarm);
//        addAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
//            }
//        });

        return view;
    }

    @Override
    public void onToggle(TimeBasedAlarm alarm) {
        if (alarm.isHasStarted()) {
            alarm.disableAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
        } else {
            alarm.scheduleAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
        }
    }

}
