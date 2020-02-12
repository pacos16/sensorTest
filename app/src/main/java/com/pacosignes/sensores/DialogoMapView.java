package com.pacosignes.sensores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class DialogoMapView extends DialogFragment implements DialogInterface.OnClickListener, OnMapReadyCallback {
    //Claves para el Bundle
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String TEXTO_BOTON = "button";

    private TextView tvLatitud;
    private TextView tvLongitud;
    private MapView mapView;
    private GoogleMap gmap;
    private double latitud;
    private double longitud;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_mapview, null);
        builder.setView(layout);
        tvLatitud = layout.findViewById(R.id.tvLatitud);
        tvLongitud = layout.findViewById(R.id.tvLongitud);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = layout.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.onResume();
        mapView.getMapAsync(this);

        latitud = 0;
        longitud = 0;
        String boton = "Aceptar";
        if(arguments != null) {
            latitud = arguments.getDouble(LATITUD, 0);
            longitud = arguments.getDouble(LONGITUD, 0);
            boton = arguments.getString(TEXTO_BOTON, "Aceptar");
        }
        builder.setTitle("Ubicación: " + String.format("%.2f, %.2f", latitud, longitud));
        tvLatitud.setText(String.format("%.2f",latitud));
        tvLongitud.setText(String.format("%.2f",longitud));
        builder.setPositiveButton(boton, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.i("Diálogos", "Aceptar");
        dialogInterface.dismiss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(latitud, longitud);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
}
