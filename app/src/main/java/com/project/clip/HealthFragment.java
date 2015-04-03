package com.project.clip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
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




        //blood Pressure image
        ImageView imageView = (ImageView) rootView.findViewById(R.id.bloodPressureIcon);
        addBloodPressureView(imageView);

        //heart rate image
        ImageView imageViewHeartRate = (ImageView) rootView.findViewById(R.id.HeartRateIcon);
        addHeartRate(imageViewHeartRate);


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
    void addHeartRate(ImageView imageView)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHeartRate(R.layout.diaglog_heartrate);

            }
        });
    }


   //blood pressure pop up
void ShowBloodPressureAlert(int layoutID) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setView(layoutID);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Dialog f = (Dialog)dialog;
                EditText dia = (EditText)f.findViewById(R.id.EditViewDiastolic);

                EditText sys = (EditText)f.findViewById(R.id.editViewSystolic);
                TextView bp = (TextView) getView().findViewById(R.id.textViewBP);
                Editable  sufo = dia.getText();
                bp.setText(dia.getText().toString().trim() + "/" + sys.getText().toString().trim());
     }
        });

        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();


    }
  void ShowHeartRate(int LayoutId)
  {
      AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
      alert.setView(LayoutId);
      alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              Dialog f = (Dialog)dialog;

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