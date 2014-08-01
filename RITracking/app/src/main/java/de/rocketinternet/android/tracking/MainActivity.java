package de.rocketinternet.android.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This activity will be removed, is it used at the moment only for testing purposes
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Actual class is: " + getClass().getName(), Toast.LENGTH_LONG).show();
    }
}
