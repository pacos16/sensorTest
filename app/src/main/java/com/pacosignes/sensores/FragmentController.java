package com.pacosignes.sensores;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.location.FusedLocationProviderClient;

public class FragmentController extends Fragment implements View.OnClickListener {

    private Button btAccelerometer;
    private Button btGeolocation;
    private Button btGyroscope;
    private Bundle bundle;
    private Location ubicacionActual;
    private FusedLocationProviderClient fusedLocationClient;
    public static final int REQUEST_ACCESS_COURSE_LOCATION = 1;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 2;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btAccelerometer = getActivity().findViewById(R.id.btAccelerometer);
        btAccelerometer.setOnClickListener(this);
        btGyroscope=getActivity().findViewById(R.id.btGyroscope);
        btGyroscope.setOnClickListener(this);
        btGeolocation=getActivity().findViewById(R.id.btLocation);
        btGeolocation.setOnClickListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.controller_screen,container,false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btAccelerometer:
                FragmentAcelerometroColores fragmentAcelerometroColores=new FragmentAcelerometroColores();
                getFragmentManager().beginTransaction().replace(R.id.mainFrame,fragmentAcelerometroColores).commit();
                break;
            case R.id.btGyroscope:
                FragmentFlipChallenge fragmentFlipChallenge=new FragmentFlipChallenge();
                getFragmentManager().beginTransaction().replace(R.id.mainFrame,fragmentFlipChallenge).commit();
                break;
            case R.id.btLocation:

                boolean permiso = true;
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    permiso = false;
                    ActivityCompat.requestPermissions( getActivity(), new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            REQUEST_ACCESS_COURSE_LOCATION);
                }
                if(ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    permiso = false;
                    ActivityCompat.requestPermissions( getActivity(), new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                            REQUEST_ACCESS_FINE_LOCATION);
                }
                if(permiso) {
                    obtenerUltimaUbicacion();
                } else {
                    Log.d(getClass().getSimpleName(), "Sin permisos para obtener la ubicación");
                }
                break;
        }

    }
    public void obtenerUltimaUbicacion() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            ubicacionActual = location;
                            bundle = new Bundle();
                            bundle.putDouble(DialogoMapView.LONGITUD, ubicacionActual.getLongitude());
                            bundle.putDouble(DialogoMapView.LATITUD, ubicacionActual.getLatitude());
                            bundle.putString(DialogoMapView.TEXTO_BOTON, "Aceptar");
                            DialogoMapView dialogoMapView = new DialogoMapView();
                            dialogoMapView.setArguments(bundle);
                            dialogoMapView.show(getActivity().getSupportFragmentManager(), "error_dialog_mapview");

                        }
                    });
        } catch (SecurityException se) {
            Log.d(getClass().getSimpleName(), "Sin permisos para obtener la ubicación");
        }
    }



}
