package id.ac.ui.cs.mobileprogramming.kace.kcclock.anim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.ClockBackground;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.ClockBackgroundSmall;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.LongClockHand;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.ShortClockHand;

public class ClockAnimGLRenderer implements GLSurfaceView.Renderer {
    private ShortClockHand mShortClockHand;
    private LongClockHand mLongClockHand;
    private ClockBackground mClockBackground;
    private ClockBackgroundSmall mClockBackgroundSmall;

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(46/255f, 40/255f, 66/255f, 1.0f);

        // initialize a triangle
        mShortClockHand = new ShortClockHand();
        // initialize a square
        mLongClockHand = new LongClockHand();

        mClockBackground = new ClockBackground();
        mClockBackgroundSmall = new ClockBackgroundSmall();
    }

    public void onDrawFrame(GL10 unused) {

        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw shape
        mClockBackground.draw(vPMatrix);
        mClockBackgroundSmall.draw(vPMatrix);
        mShortClockHand.draw(rotationMatrix(vPMatrix, -0.0075f, 48000L));
        mLongClockHand.draw(rotationMatrix(vPMatrix, -0.0900f, 4000L));
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private static float[] rotationMatrix(float[] vPMatrix, float rotateSpeed, long rotateTime) {
        float[] scratch = new float[16];
        float[] rotationMatrix = new float[16];

        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % rotateTime;
        float angle = (rotateSpeed) * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
        return scratch;
    }
}
