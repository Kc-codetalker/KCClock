package id.ac.ui.cs.mobileprogramming.kace.kcclock.anim;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class ClockAnimSurfaceView extends GLSurfaceView {
    private final ClockAnimGLRenderer renderer;

    public ClockAnimSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new ClockAnimGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
