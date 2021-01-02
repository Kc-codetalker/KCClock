package id.ac.ui.cs.mobileprogramming.kace.kcclock.anim;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.R;

public class ClockAnimationActivity extends AppCompatActivity {

    private ClockAnimSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        glView = new ClockAnimSurfaceView(this);
        setContentView(glView);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.appbar_activity_animation);
        }
    }
}
