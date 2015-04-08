package com.project.clip;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PersonViewHolder>{


    List<String> data;

    RecyclerAdapter(List<String> data){
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_finance_recycler, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {


        personViewHolder.personName.setText(data.get(i));
        //personViewHolder.personAge.setText(persons.get(i).age);
       // personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }









    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.finance_recycler);
            personName = (TextView)itemView.findViewById(R.id.textView_cardView);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
          //  personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);


        }

    }


}