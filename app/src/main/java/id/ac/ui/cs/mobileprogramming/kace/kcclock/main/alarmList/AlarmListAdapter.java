package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class AlarmListAdapter extends
        RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView alarmName;
        private TextView alarmTime;
        private TextView alarmRecurrence;
        private Switch alarmEnableSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmTime = itemView.findViewById(R.id.timeText);
            alarmEnableSwitch = itemView.findViewById(R.id.alarmEnableSwitch);
            alarmRecurrence = itemView.findViewById(R.id.recurrenceText);
            alarmName = itemView.findViewById(R.id.nameText);
        }

        public void bind(TimeBasedAlarm alarm, OnToggleTimeBasedAlarmListener listener) {
            String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

            alarmTime.setText(alarmText);
            alarmEnableSwitch.setChecked(alarm.isHasStarted());
            alarmRecurrence.setText(alarm.getRecurrenceStr(getAppContext()));
            alarmName.setText(alarm.getName());

            alarmEnableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onToggle(alarm));
        }
    }

    private List<TimeBasedAlarm> timeBasedAlarms;
    private OnToggleTimeBasedAlarmListener timeBasedListener;

    public AlarmListAdapter(OnToggleTimeBasedAlarmListener timeBasedListener) {
        this.timeBasedAlarms = new ArrayList<>();
        this.timeBasedListener = timeBasedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View alarmItem = inflater.inflate(R.layout.item_time_based_alarm, parent, false);

        return new ViewHolder(alarmItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeBasedAlarm alarm = timeBasedAlarms.get(position);

        holder.bind(alarm, this.timeBasedListener);
    }

    @Override
    public int getItemCount() {
        return timeBasedAlarms.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.alarmEnableSwitch.setOnCheckedChangeListener(null);
    }

    public void setTimeBasedAlarms(List<TimeBasedAlarm> alarms) {
        this.timeBasedAlarms = alarms;
        notifyDataSetChanged();
    }
}