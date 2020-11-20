package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.media.Audio;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.media.AudioManager;

import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.AUDIO_URI;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.EVENT;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.USE_SOUND;
import static id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.broadcastReceiver.TimeBasedAlarmReceiver.VIBRATE;

public class EventBasedAlarmFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.spinnerEvent) Spinner eventSpinner;
    @BindView(R.id.checkBoxVibrate) CheckBox vibrateCheckBox;
    @BindView(R.id.checkBoxSound) CheckBox soundCheckBox;
    @BindView(R.id.audioSpinner) Spinner audioSpinner;

    private EventBasedAlarmViewModel alarmViewModel;
    private List<Audio> audioList;
    private List<String> eventKeys;
    private List<String> eventValues;

    public static EventBasedAlarmFragment newInstance() {
        return new EventBasedAlarmFragment();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        audioList = AudioManager.getAllAudio();
        setEventKeysValues();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event_based_alarm, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> eventAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, this.eventValues);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);

        ArrayAdapter<String> audioAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, audioListToNameList());
        audioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audioSpinner.setAdapter(audioAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        alarmViewModel = ViewModelProviders.of(this).get(EventBasedAlarmViewModel.class);
        Intent intent = getActivity().getIntent();
        updateViewModelWithIntent(intent);
        setUIData();
    }

    @Override
    public void onStop() {
        this.saveStateToViewModel();
        super.onStop();
    }

    // Should implement onItemSelected and onNothingSelected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        int viewId = arg0.getId();
        if (viewId == eventSpinner.getId()) {
            String event = eventValues.get(position);
            Toast.makeText(getContext(), event , Toast.LENGTH_LONG).show();
        } else if (viewId == audioSpinner.getId()) {
            Audio selectedAudio = audioList.get(position);
            Toast.makeText(getContext(), selectedAudio.getName() , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private List<String> audioListToNameList() {
        List<String> lst = new ArrayList<>();
        for (Audio a : this.audioList) {
            lst.add(a.getName());
        }
        return lst;
    }

    private void setEventKeysValues() {
        List<String> keyList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (Map.Entry me : EventBasedAlarm.EVENT_MAP.entrySet()) {
            keyList.add(me.getKey().toString());
            valueList.add(me.getValue().toString());
        }
        this.eventKeys = keyList;
        this.eventValues = valueList;
    }

    private int findAudioPositionByUri(String uri) {
        for (int i = 0 ; i < audioList.size(); i++) {
            Audio a = audioList.get(i);
            if (a.getUri().toString().equals(uri)) {
                return i;
            }
        }
        return 0;
    }

    private void updateViewModelWithIntent(Intent intent) {
        if( intent.getExtras() == null) return;

        try {
            alarmViewModel.setEvent(intent.getStringExtra(EVENT));
            alarmViewModel.setIsVibrate(intent.getBooleanExtra(VIBRATE, false));
            alarmViewModel.setIsUseSound(intent.getBooleanExtra(USE_SOUND, false));
            alarmViewModel.setAudioUri(intent.getStringExtra(AUDIO_URI));
        } catch (Exception e) {
            Log.d("Exception:", e.toString());
            e.printStackTrace();
        }
    }

    private void setUIData() {
        try {
            alarmViewModel.getEvent().observe(this, event -> {
                eventSpinner.setSelection(eventKeys.indexOf(event));
            });
            alarmViewModel.getIsVibrate().observe(this, is -> vibrateCheckBox.setChecked(is));
            alarmViewModel.getIsUseSound().observe(this, is -> soundCheckBox.setChecked(is));
            alarmViewModel.getAudioUri().observe(this, uri -> {
                int selectedAudio = findAudioPositionByUri(uri);
                audioSpinner.setSelection(selectedAudio);
            });
        } catch (NullPointerException e) {
            Log.d("EventAlarmViewModel", e.toString());
            e.printStackTrace();
        }
    }

    private void saveStateToViewModel() {
        int eventPos = eventSpinner.getSelectedItemPosition();
        if (eventPos == -1) eventPos = 0;
        alarmViewModel.setEvent(eventKeys.get(eventPos));
        alarmViewModel.setIsVibrate(vibrateCheckBox.isChecked());
        alarmViewModel.setIsUseSound(soundCheckBox.isChecked());
        int pos = audioSpinner.getSelectedItemPosition();
        if (pos == -1) pos = 0;
        Uri audioUri = audioList.get(pos).getUri();
        alarmViewModel.setAudioUri(audioUri.toString());
    }

    public void saveAlarm() {
        this.saveStateToViewModel();
        EventBasedAlarm alarm = alarmViewModel.createAlarm();
        alarmViewModel.saveAlarm(alarm);
    }
}
