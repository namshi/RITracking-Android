package de.rocketinternet.android.tracking;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author alessandro.balocco
 */
public class RIDeepLinkingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        TextView url = (TextView) findViewById(R.id.text);
        Intent intent = getIntent();
        Uri data = intent.getData();

        url.setText("This is deep linking activity and this is the url: " + data.toString());
    }
}
