package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.AppRepository;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm.db.EventBasedAlarm;

public class EventBasedAlarmViewModel extends AndroidViewModel {
    private MutableLiveData<String> event = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVibrate = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUseSound = new MutableLiveData<>();
    private MutableLiveData<String> audioUri = new MutableLiveData<>();

    AppRepository repo;

    public MutableLiveData<String> getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event.setValue(event);
    }

    public MutableLiveData<Boolean> getIsVibrate() {
        return isVibrate;
    }

    public void setIsVibrate(Boolean isVibrate) {
        this.isVibrate.setValue(isVibrate);
    }

    public MutableLiveData<Boolean> getIsUseSound() {
        return isUseSound;
    }

    public void setIsUseSound(Boolean isUseSound) {
        this.isUseSound.setValue(isUseSound);
    }

    public MutableLiveData<String> getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri.setValue(audioUri);
    }

    public EventBasedAlarmViewModel(@NonNull Application application) {
        super(application);

        repo = new AppRepository(application);
    }

    public EventBasedAlarm createAlarm() {

        EventBasedAlarm alarm = new EventBasedAlarm(event.getValue(), true,
                isVibrate.getValue(), isUseSound.getValue(), audioUri.getValue());
        return alarm;
    }

    public void saveAlarm(EventBasedAlarm alarm) {
        repo.insert(alarm);
    }
}
