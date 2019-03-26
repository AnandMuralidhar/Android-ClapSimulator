package com.anand.assignmentclapapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    Sensor m_proximitySensor;
    SensorManager m_sensorManager;
    MediaPlayer m_mediaSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        m_proximitySensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Start clapping!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_sensorManager.registerListener(eventListener, m_proximitySensor, 2*900*900);
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_sensorManager.unregisterListener(eventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (m_mediaSound != null) {
            m_mediaSound.release();
            m_mediaSound = null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private SensorEventListener eventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            float distance = event.values[0];
            if(distance < 5f)
            {
                m_mediaSound = MediaPlayer.create(getBaseContext(), R.raw.clappingsoundclip);
                m_mediaSound.start();
            }
            else {
                if(m_mediaSound != null) {
                    m_mediaSound.stop();
                }
            }
        }
    };

}
