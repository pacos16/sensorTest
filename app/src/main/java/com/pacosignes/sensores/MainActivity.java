package com.pacosignes.sensores;

import androidx.appcompat.app.AppCompatActivity;




import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentController fragmentController=new FragmentController();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,fragmentController).commit();

    }


}
