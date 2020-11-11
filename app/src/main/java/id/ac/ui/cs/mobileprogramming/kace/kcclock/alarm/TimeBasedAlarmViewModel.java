package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimeBasedAlarmViewModel extends ViewModel {
    private MutableLiveData<Integer> hour = new MutableLiveData<>();
    private MutableLiveData<Integer> minute = new MutableLiveData<>();
    private MutableLiveData<String> alarmName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isVibrate = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUseSound = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingSun = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingMon = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingTue = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingWed = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingThu = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingFri = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRingSat = new MutableLiveData<>();

    public MutableLiveData<Integer> getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour.setValue(hour);
    }

    public MutableLiveData<Integer> getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute.setValue(minute);
    }

    public MutableLiveData<String> getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName.setValue(alarmName);
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

    public MutableLiveData<Boolean> getIsRingSun() {
        return isRingSun;
    }

    public void setIsRingSun(Boolean isRingSun) {
        this.isRingSun.setValue(isRingSun);
    }

    public MutableLiveData<Boolean> getIsRingMon() {
        return isRingMon;
    }

    public void setIsRingMon(Boolean isRingMon) {
        this.isRingMon.setValue(isRingMon);
    }

    public MutableLiveData<Boolean> getIsRingTue() {
        return isRingTue;
    }

    public void setIsRingTue(Boolean isRingTue) {
        this.isRingTue.setValue(isRingTue);
    }

    public MutableLiveData<Boolean> getIsRingWed() {
        return isRingWed;
    }

    public void setIsRingWed(Boolean isRingWed) {
        this.isRingWed.setValue(isRingWed);
    }

    public MutableLiveData<Boolean> getIsRingThu() {
        return isRingThu;
    }

    public void setIsRingThu(Boolean isRingThu) {
        this.isRingThu.setValue(isRingThu);
    }

    public MutableLiveData<Boolean> getIsRingFri() {
        return isRingFri;
    }

    public void setIsRingFri(Boolean isRingFri) {
        this.isRingFri.setValue(isRingFri);
    }

    public MutableLiveData<Boolean> getIsRingSat() {
        return isRingSat;
    }

    public void setIsRingSat(Boolean isRingSat) {
        this.isRingSat.setValue(isRingSat);
    }
}
