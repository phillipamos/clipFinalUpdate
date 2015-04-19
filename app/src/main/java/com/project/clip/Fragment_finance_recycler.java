package com.project.clip;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Fragment_finance_recycler extends Fragment {


    /*Declare these two public so the adapter can be changed from RecyclerAdapterFinance
      It allows the adapters to be updated and the recycler view refreshed without reloading
      the fragment
     */
    public static RecyclerView recyclerView;
    public static RecyclerAdapterFinance adapter;

    public Fragment_finance_recycler() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get the layoutid passed from
        String getLayout = getArguments().getString("LayoutId");
        View rootView = inflater.inflate(R.layout.recycler_finance, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.textView_title);
        title.setTypeface(MainActivity.robotoRegular);

        //Define the recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerList);


        /*Switch between the possible layouts. Needed since all views use the same layout.
          The switch makes this possible and adjusts the titles of each accordingly
        */
        switch (getLayout) {


            case ("Stocks"):
                //Set the global variable to stocks to let the Recycler Adapter Finance know which view is currently inflated
                RecyclerAdapterFinance.layout = "stocks";
                title.setText("Your Stocks");


                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(llm);
                adapter = new RecyclerAdapterFinance(generatePalettes(DataStrings.TABLE_STOCKS,
                        DataStrings.COLUMN_STOCK_NAME, DataStrings.COLUMN_STOCK_VALUE));
                recyclerView.setAdapter(adapter);
                break;


            case ("Assets"):

                //Set switch variable for delete in RecyclerAdapter
                RecyclerAdapterFinance.layout = "assets";
                title.setText("Your Assets");

                recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerList);
                llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(llm);

                adapter = new RecyclerAdapterFinance(generatePalettes(DataStrings.TABLE_ASSETS,
                        DataStrings.COLUMN_ASSET_NAME, DataStrings.COLUMN_ASSET_VALUE));


                recyclerView.setAdapter(adapter);
                break;

            case ("Account"):

                RecyclerAdapterFinance.layout = "accounts";
                title.setText("Your Accounts");

                recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerList);
                llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(llm);

                adapter = new RecyclerAdapterFinance(generatePalettes(DataStrings.TABLE_ACCOUNTS,
                        DataStrings.COLUMN_ACCOUNT_NAME, DataStrings.COLUMN_ACCOUNT_BALANCE));
                recyclerView.setAdapter(adapter);

                break;

            default:
                break;

        }

        return rootView;


    }

    public static ArrayList<FinanceFragment.Palette> generatePalettes(String table, String columnName, String columnValue) {


        final List<String> name = MainActivity.database.getAllFromDb(table, columnName);
        final List<String> value = MainActivity.database.getAllFromDb(table, columnValue);

        ArrayList<FinanceFragment.Palette> palettes = new ArrayList<>();

        for (int i = 0; i < name.size(); i++) {
            palettes.add(new FinanceFragment.Palette(name.get(i), "$" + value.get(i), Color.parseColor("#FFFFFF"), i));
        }
        return palettes;


    }


}
