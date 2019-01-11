package com.example.tb990.shortpathalarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    double dest_long;
    double dest_lati;
    double velocity;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            double loadVelo = Double.valueOf(load().toString());
            if (loadVelo > 0) {
                Toast.makeText(getApplicationContext(), "저장된 속력은" + loadVelo + "m/분 입니다.", Toast.LENGTH_LONG).show();
                velocity = loadVelo;

            }
        }catch (Exception e){ }

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("60540fe3-19c2-4b66-9a2e-442a7f53e860");

        TMapPoint tpoint = tMapView.getLocationPoint();
        double Latitude = tpoint.getLatitude();
        double Longitude = tpoint.getLongitude();

        tMapView.setCenterPoint(Latitude, Longitude);
        tMapView.setLocationPoint(Latitude, Latitude);

        linearLayoutTmap.addView( tMapView );

        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, final TMapPoint tMapPoint) {
                AlertDialog.Builder oDialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                oDialog.setMessage("도착지로 설정하시겠습니까?")
                        .setTitle("일반 Dialog")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Log.i("Dialog", "취소");
                                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dest_lati = tMapPoint.getLatitude(); dest_long = tMapPoint.getLongitude();
                                String test ="lon=" + tMapPoint.getLongitude() + "\nlat=" + tMapPoint.getLatitude()+"로 설정되었습니다.";
                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();

                            }
                        })
                        .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                        .show();
            }
        });

    }

    public StringBuilder load(){
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();

        try{
            fis = openFileInput(setVelocity.filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb;
    }

}