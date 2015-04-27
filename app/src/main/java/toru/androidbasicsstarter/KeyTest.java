package toru.androidbasicsstarter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class KeyTest extends Activity {
    StringBuilder builder = new StringBuilder();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("Press keys (if you have some)!");
        textView.setOnKeyListener(new View.OnKeyListener() {
                                      @Override
                                      public boolean onKey(View v, int keyCode, KeyEvent event) { //lambda exercise. implements OnKeyListener interface.
                                          builder.setLength(0);
                                          switch (event.getAction()) {
                                              case KeyEvent.ACTION_DOWN:
                                                  builder.append("down, ");
                                                  break;
                                              case KeyEvent.ACTION_UP:
                                                  builder.append("up, ");
                                                  break;
                                          }
                                          builder.append(event.getKeyCode());
                                          builder.append(", ");
                                          builder.append((char) event.getUnicodeChar());
                                          String text = builder.toString();
                                          Log.d("KeyTest", text);
                                          textView.setText(text);

                                          if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                              return false;
                                          } else {
                                              return true;
                                          }
                                      }
                                  }
        );
        textView.setFocusableInTouchMode(true);
        setContentView(textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_key_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
