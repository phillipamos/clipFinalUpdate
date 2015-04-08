package com.project.clip;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceRecycler extends Fragment implements View.OnClickListener {






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_finance_recycler, container, false);

        final List<String> accountNames = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS,
                DataStrings.COLUMN_ACCOUNT_NAME);


        RecyclerView rv = (RecyclerView)rootView.findViewById(R.id.finance_recycler);

        RecyclerAdapter adapter = new RecyclerAdapter(accountNames);
        rv.setAdapter(adapter);





        return rootView;
    }


    @Override
    public void onClick(View v) {

    }
}
