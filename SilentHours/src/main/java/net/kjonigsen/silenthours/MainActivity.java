package net.kjonigsen.silenthours;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initial reset may be needed on app start.
        ActivationManager.resetStateFor(this);
    }

    @Override
    protected void onResume() {
        updateFromConfig();
        super.onResume();
    }

    private void updateFromConfig()
    {
        ApplicationStatus status = ApplicationStatusProvider.getFor(this);
        ActivationManager.setStateFor(this);

        updateFromStatus(status);
    }

    private void updateFromStatus(ApplicationStatus status)
    {
        setText(R.id.text_service_status, getEnabled(status.ServiceEnabled));
        setText(R.id.text_silent_status, getEnabled(status.SilentHoursEnabled));
    }

    private String getEnabled(boolean enabled)
    {
        int resource = enabled ? R.string.enabled : R.string.inactive;
        String text = getText(resource).toString();
        return text;
    }

    private void setText(int control, String value)
    {
        TextView tv = (TextView)findViewById(control);
        tv.setText(value);
    }


    /* settings menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // we know this is settings. Its the only option.

        switch (item.getItemId())
        {

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

        }

        return true;
    }
}
