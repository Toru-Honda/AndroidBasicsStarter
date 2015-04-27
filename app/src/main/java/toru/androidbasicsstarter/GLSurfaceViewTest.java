package toru.androidbasicsstarter;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class GLSurfaceViewTest extends Activity {
    GLSurfaceView glView;

    static class SimpleRenderer implements GLSurfaceView.Renderer {
        Random random = new Random();
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(this.toString(), "surface created.");
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.d(this.toString(), "surface changed: " + width + "x" + height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        glView = new GLSurfaceView(this);
        glView.setRenderer(new SimpleRenderer());
        setContentView(glView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }
}
