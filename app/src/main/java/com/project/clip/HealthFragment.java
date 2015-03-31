package com.project.clip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import  android.content.Context;
import java.util.zip.Inflater;

import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class HealthFragment extends Fragment  {

    private static final String TAG_ADD_BLOOD_PRESSURE = "addBloodPressure";
    private static final String TAG_ADD_TEMPERATURE = "addTemperature";
    private static final String TAG_ADD_HEART_RATE = "addRate";
    private static final String TAG_ADD_RESPIRATION_RATE = "addRespirationRate";

    public HealthFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_health, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.bloodPressureIcon);
        addBloodPressureView(imageView);

        return rootView;
    }

    void addBloodPressureView(ImageView imageView)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBloodPressureAlert(R.layout.dialog_bloodpressure);

            }
        });


    }



    void ShowBloodPressureAlert(int layoutID) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setView(layoutID);

       View v = getView();

        EditText dia = (EditText)getView().findViewById(R.id.EditViewDiastolic);

        EditText sys = (EditText)getView().findViewById(R.id.editViewSystolic);


        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                TextView bp = (TextView) getView().findViewById(R.id.textViewBP);
                //bp.setText(dia.getText().toString().trim());



            }
        });

        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();


    }


}