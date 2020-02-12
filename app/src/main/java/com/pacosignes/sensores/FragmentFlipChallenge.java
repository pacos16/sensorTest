package com.pacosignes.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FragmentFlipChallenge extends Fragment implements SensorEventListener {

    private Sensor gyroscope;
    private SensorManager sensorManager;

    private Button btStart;
    private TextView tvVueltas;
    private TextView tvVelAngMed;
    private TextView tvVelAngMax;
    private TextView tvNumMuestras;
    private ArrayList<Float> muestras;
    private float velMedia;
    private float velMaxima;
    private int vueltas;
    private Handler handler;
    private SensorEventListener listener;
    private Executor executor;
    private SoundPool soundPool;
    private int sound1;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        gyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        tvVueltas=getActivity().findViewById(R.id.tvNumVueltas);
        tvVelAngMed=getActivity().findViewById(R.id.tvVelAngMed);
        tvVelAngMax=getActivity().findViewById(R.id.tvVelAngMax);
        tvNumMuestras=getActivity().findViewById(R.id.tvNumMuestras);
        btStart=getActivity().findViewById(R.id.btStart);
        btStart.setEnabled(false);
        listener=this;
        handler=new Handler();
        executor= Executors.newSingleThreadExecutor();
        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        sound1=soundPool.load(getContext(),R.raw.beep01,1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                btStart.setEnabled(true);
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                muestras=new ArrayList<>();
                tvVelAngMax.setText("");
                tvVelAngMed.setText("");
                tvNumMuestras.setText("");
                tvVueltas.setText("");

                Runnable task=new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            soundPool.play(sound1,1,1,1,0,1);
                            Thread.sleep(500);
                            soundPool.autoPause();
                            Thread.sleep(500);
                            soundPool.play(sound1,1,1,1,0,1);
                            Thread.sleep(500);
                            soundPool.autoPause();
                            Thread.sleep(500);
                            soundPool.play(sound1,1,1,1,0,1);
                            sensorManager.registerListener(listener,gyroscope,SensorManager.SENSOR_DELAY_FASTEST);
                            Thread.sleep(1000);
                            sensorManager.unregisterListener(listener);
                            soundPool.autoPause();
                            int denominador=muestras.size();
                            float suma=0;
                            velMaxima=0;
                            for (float num:muestras
                            ) {
                                if(num!=0) suma+=num;
                                else denominador--;
                                if(num>velMaxima) velMaxima=num;
                            }
                            velMedia=suma/denominador;
                            vueltas= (int)Math.floor(velMedia/6.28f);
                            handler.post(new CalcAndSet());
                        }catch (InterruptedException ie){

                        }
                    }
                };

                executor.execute(task);
            }
        });
    }

    public class CalcAndSet implements Runnable{

        @Override
        public void run() {

            tvNumMuestras.setText(String.valueOf(muestras.size()));
            tvVelAngMax.setText(String.valueOf(velMaxima));
            tvVelAngMed.setText(String.valueOf(velMedia));
            tvVueltas.setText(String.valueOf(vueltas));
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flip_info_layout,container,false);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("info gyro",event.values[0]+" "+event.values[1]+" "+event.values[2]);
       muestras.add(Math.abs(event.values[1]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
