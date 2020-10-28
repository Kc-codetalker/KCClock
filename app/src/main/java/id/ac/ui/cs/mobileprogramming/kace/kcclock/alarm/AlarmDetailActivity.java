package id.ac.ui.cs.mobileprogramming.kace.kcclock.alarm;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class AlarmDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adjustActivityTheme();

        setContentView(R.layout.activity_alarm_detail);
        try
        {
            this.getSupportActionBar().setTitle(R.string.appbar_alarm_detail);
        }
        catch (NullPointerException e){}
    }

    private void adjustActivityTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.LightTheme);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme);
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonTimeBased:
                if (checked) {
                    setSectionVisibility(R.id.sectionEventBased, View.GONE);
                    setSectionVisibility(R.id.sectionTimeBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonEventBased, false);
                    setRadioButtonText(R.id.radioButtonTimeBased, true);
                }
                break;
            case R.id.radioButtonEventBased:
                if (checked) {
                    setSectionVisibility(R.id.sectionTimeBased, View.GONE);
                    setSectionVisibility(R.id.sectionEventBased, View.VISIBLE);
                    setRadioButtonText(R.id.radioButtonTimeBased, false);
                    setRadioButtonText(R.id.radioButtonEventBased, true);
                }
                break;
        }
    }

    private void setSectionVisibility(int id, int value) {
        View sectionLayout = findViewById(id);
        sectionLayout.setVisibility(value);
    }

    private void setRadioButtonText(int id, boolean isActive) {
        RadioButton radio = findViewById(id);

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        if (isActive)
            theme.resolveAttribute(R.attr.activeTextColor, typedValue, true);
        else
            theme.resolveAttribute(R.attr.inactiveTextColor, typedValue, true);
        @ColorInt int color = typedValue.data;

        radio.setTextColor(color);
    }
}
