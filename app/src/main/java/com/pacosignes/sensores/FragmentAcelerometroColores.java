package com.pacosignes.sensores;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentAcelerometroColores extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private LinearLayout colorBase;
    private TextView tvX;
    private TextView tvY;
    private TextView tvZ;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        colorBase=getActivity().findViewById(R.id.colorBase);
        tvX=getActivity().findViewById(R.id.tvX);
        tvY=getActivity().findViewById(R.id.tvY);
        tvZ=getActivity().findViewById(R.id.tvZ);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.color_layout,container,false);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float converter=3.26f;
        int x=Math.abs((int)(event.values[0]*converter));
        int y=Math.abs((int)(event.values[1]*converter));
        int z=Math.abs((int)(event.values[2]*converter));
        tvX.setText(String.valueOf(x));
        tvY.setText(String.valueOf(y));
        tvZ.setText(String.valueOf(z));
        colorBase.setBackgroundColor(Color.argb(255,x,y,z));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}
