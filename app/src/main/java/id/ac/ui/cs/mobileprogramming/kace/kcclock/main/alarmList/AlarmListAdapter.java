package id.ac.ui.cs.mobileprogramming.kace.kcclock.main.alarmList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.AlarmDetailActivity;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.Alarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.TimeBasedAlarm;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.ALARM_TYPE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.AUDIO_URI;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.FRIDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.HOUR;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MINUTE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.MONDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.NAME;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SATURDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.SUNDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.THURSDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TIME_BASED_ALARM;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.TUESDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.WEDNESDAY;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.application.App.getAppContext;

public class AlarmListAdapter extends
        RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView alarmName;
        private TextView alarmTime;
        private TextView alarmRecurrence;
        private Switch alarmEnableSwitch;
        private LinearLayout viewHolderContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmTime = itemView.findViewById(R.id.timeText);
            alarmEnableSwitch = itemView.findViewById(R.id.alarmEnableSwitch);
            alarmRecurrence = itemView.findViewById(R.id.recurrenceText);
            alarmName = itemView.findViewById(R.id.nameText);
            viewHolderContainer = itemView.findViewById(R.id.itemContainer);
        }

        public void bind(Alarm a, OnToggleAlarmListener listener) {
            if (a instanceof TimeBasedAlarm) {
                TimeBasedAlarm alarm = (TimeBasedAlarm) a;
                String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

                alarmTime.setText(alarmText);
                alarmEnableSwitch.setChecked(alarm.isEnabled());
                alarmRecurrence.setText(alarm.getRecurrenceStr(getAppContext()));
                alarmName.setText(alarm.getName());

                alarmEnableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    listener.onToggle(alarm, alarmTime, alarmName, alarmRecurrence);
                });
                viewHolderContainer.setOnClickListener((view) -> {
                    Intent intent = new Intent(getAppContext(), AlarmDetailActivity.class);
                    intent.putExtra(ALARM_TYPE, TIME_BASED_ALARM);
                    intent.putExtra("id", alarm.getId());
                    intent.putExtra(HOUR, alarm.getHour());
                    intent.putExtra(MINUTE, alarm.getMinute());
                    intent.putExtra(NAME, alarm.getName());
                    intent.putExtra(VIBRATE, alarm.isVibrate());
                    intent.putExtra(USE_SOUND, alarm.isUseSound());
                    intent.putExtra(SUNDAY, alarm.isOnSunday());
                    intent.putExtra(MONDAY, alarm.isOnMonday());
                    intent.putExtra(TUESDAY, alarm.isOnTuesday());
                    intent.putExtra(WEDNESDAY, alarm.isOnWednesday());
                    intent.putExtra(THURSDAY, alarm.isOnThursday());
                    intent.putExtra(FRIDAY, alarm.isOnFriday());
                    intent.putExtra(SATURDAY, alarm.isOnSaturday());
                    intent.putExtra(AUDIO_URI, alarm.getAudioUri());
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getAppContext().startActivity(intent);
                });
            } else {
                EventBasedAlarm alarm = (EventBasedAlarm) a;

                int eventTextId = EventBasedAlarm.EVENT_MAP.get(alarm.getEvent());
                alarmTime.setText(getAppContext().getString(eventTextId));
                alarmEnableSwitch.setChecked(alarm.isEnabled());
                alarmRecurrence.setVisibility(View.GONE);
                alarmName.setVisibility(View.GONE);

                alarmEnableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    listener.onToggle(alarm, alarmTime, alarmName, alarmRecurrence);
                });
                viewHolderContainer.setOnClickListener((view) -> {
                    Intent intent = new Intent(getAppContext(), AlarmDetailActivity.class);
                    intent.putExtra(ALARM_TYPE, EVENT_BASED_ALARM);
                    intent.putExtra(EVENT, alarm.getEvent());
                    intent.putExtra(VIBRATE, alarm.isVibrate());
                    intent.putExtra(USE_SOUND, alarm.isUseSound());
                    intent.putExtra(AUDIO_URI, alarm.getAudioUri());
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    getAppContext().startActivity(intent);
                });
            }
        }
    }


    private List<Alarm> alarms;
    private OnToggleAlarmListener timeBasedListener;

    public AlarmListAdapter(OnToggleAlarmListener timeBasedListener) {
        this.alarms = new ArrayList<>();
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
        Alarm alarm = alarms.get(position);

        holder.bind(alarm, this.timeBasedListener);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.alarmEnableSwitch.setOnCheckedChangeListener(null);
    }

    public void setTimeBasedAlarms(List<TimeBasedAlarm> alarms) {
        for (TimeBasedAlarm newAlarm : alarms) {
            boolean hasModifyOldAlarm = false;
            for (int i = 0; i < this.alarms.size(); i++) {
                Alarm current = this.alarms.get(i);
                if (current instanceof TimeBasedAlarm) {
                    if (newAlarm.getId() == ((TimeBasedAlarm) current).getId()) {
                        this.alarms.set(i, newAlarm);
                        hasModifyOldAlarm = true;
                        i = this.alarms.size();
                    }
                }
            }
            if (!hasModifyOldAlarm) {
                this.alarms.add(newAlarm);
            }
        }
        notifyDataSetChanged();
    }

    public void setEventBasedAlarms(List<EventBasedAlarm> alarms) {
        for (EventBasedAlarm newAlarm : alarms) {
            boolean hasModifyOldAlarm = false;
            for (int i = 0; i < this.alarms.size(); i++) {
                Alarm current = this.alarms.get(i);
                if (current instanceof EventBasedAlarm) {
                    String currentEvent = ((EventBasedAlarm) current).getEvent();
                    if (newAlarm.getEvent().equals(currentEvent)) {
                        this.alarms.set(i, newAlarm);
                        hasModifyOldAlarm = true;
                        i = this.alarms.size();
                    }
                }
            }
            if (!hasModifyOldAlarm) {
                this.alarms.add(newAlarm);
            }
        }
        notifyDataSetChanged();
    }
}