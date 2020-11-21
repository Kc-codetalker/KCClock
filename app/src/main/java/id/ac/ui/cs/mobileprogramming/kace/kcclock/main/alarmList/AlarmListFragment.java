package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.AlarmDetailActivity;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.db.TimeBasedAlarm;

public class AlarmListFragment extends Fragment implements OnToggleAlarmListener {
    @BindView(R.id.alarmListRecyclerView) RecyclerView alarmRecyclerView;
    @BindView(R.id.addAlarmFab) FloatingActionButton fab;

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
        alarmListViewModel.getTimeBasedAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setTimeBasedAlarms(alarms);
            }
        });
        alarmListViewModel.getEventBasedAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerViewAdapter.setEventBasedAlarms(alarms);
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

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AlarmDetailActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onToggle(TimeBasedAlarm alarm, TextView alarmTime, TextView alarmName,
                         TextView alarmRecurrence) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        if (alarm.isEnabled()) {
            alarm.disableAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
            theme.resolveAttribute(R.attr.inactiveTextColor, typedValue, true);
        } else {
            alarm.scheduleAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
            theme.resolveAttribute(R.attr.activeTextColor, typedValue, true);
        }
        @ColorInt int color = typedValue.data;

        alarmTime.setTextColor(color);
        alarmName.setTextColor(color);
        alarmRecurrence.setTextColor(color);
    }

    @Override
    public void onToggle(EventBasedAlarm alarm, TextView alarmEvent, TextView alarmName,
                         TextView alarmRecurrence) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        if (alarm.isEnabled()) {
            alarm.disableAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
            theme.resolveAttribute(R.attr.inactiveTextColor, typedValue, true);
        } else {
            alarm.enableAlarm(getContext());
            alarmListViewModel.updateAlarm(alarm);
            theme.resolveAttribute(R.attr.activeTextColor, typedValue, true);
        }
        @ColorInt int color = typedValue.data;

        alarmEvent.setTextColor(color);
        alarmName.setTextColor(color);
        alarmRecurrence.setTextColor(color);
    }

}
