package id.ac.ui.cs.mobileprogramming.kace.kcclock.anim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.Square;
import id.ac.ui.cs.mobileprogramming.kace.kcclock.anim.shapes.Triangle;

public class ClockAnimGLRenderer implements GLSurfaceView.Renderer {
    private Triangle mTriangle;
    private Square mSquare;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(46/255f, 40/255f, 66/255f, 1.0f);

        // initialize a triangle
        mTriangle = new Triangle();
        // initialize a square
        mSquare = new Square();
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        mSquare.draw();
        mTriangle.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
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
}
