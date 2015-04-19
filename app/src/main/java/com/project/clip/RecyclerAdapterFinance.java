package com.project.clip;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapterFinance extends RecyclerView.Adapter<FinanceFragment.PaletteViewHolder> {


    //private  List<FinanceFragment.RecyclerStocks> stocks;
    private List<FinanceFragment.Palette> palettes;

    public RecyclerAdapterFinance(List<FinanceFragment.Palette> palettes) {
        this.palettes = new ArrayList<FinanceFragment.Palette>();
        this.palettes.addAll(palettes);
    }

    //This variable is set in fragment_finance_recycler and is used to switch the delete function
    public static String layout;

    //This variable is an object used to get the current item since Recycler Views do not have onItemClickListeners
    public static FinanceFragment.Palette currentItem;


    @Override
    public FinanceFragment.PaletteViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_stocks, viewGroup, false);




        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View recycler_view = inflater.inflate(R.layout.recycler_finance,null);
        //View to inflate the Card View Onclick alert dialog
        final View dialog_view = inflater.inflate(R.layout.dialog_recycler_accounts, null);
        //Set the relative layout of the cardview clickable
        RelativeLayout rl = (RelativeLayout) itemView.findViewById(R.id.cardBackground);
        //Initialize the alert dialog needed for delete
        final AlertDialog.Builder alert = new AlertDialog.Builder(viewGroup.getContext());
        alert.setView(dialog_view);

        //Define the delete button located inside the alert dialog
        final Button delete = (Button) dialog_view.findViewById(R.id.button_delete);


    //Set the onClick for the relative layout that is on top of the card view
    rl.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {




            //Inflate recylerview layout to get position clicked
            RecyclerView recyclerView = (RecyclerView)recycler_view.findViewById(R.id.recyclerList);
            final int position = recyclerView.getChildPosition(itemView);




            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    ((ViewGroup) dialog_view.getParent()).removeView(dialog_view);
                }
            });


            //Required lists for deleting from database
            final List<String> accounts = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS, DataStrings.COLUMN_ACCOUNT_NAME);
            final List<String> accountid = MainActivity.database.getAllFromDb(DataStrings.TABLE_ACCOUNTS, DataStrings.COLUMN_ACCOUNT_ID);
            final List<String> assetid = MainActivity.database.getAllFromDb(DataStrings.TABLE_ASSETS, DataStrings.COLUMN_ASSET_ID);
            final List<String> stockid = MainActivity.database.getAllFromDb(DataStrings.TABLE_STOCKS, DataStrings.COLUMN_STOCK_ID);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Since Recycler View does not have OnItemSelectedListener we call the current object and get its position


                    //Let the user know the item has been deleted
                    //Toast toast = Toast.makeText(viewGroup.getContext(), Integer.toString(position), Toast.LENGTH_LONG);
                    //toast.show();



                  /*Switch on layouts - This allows the the database to know which table to delete from.
                    layout parameter is a public variable defined in this class and set in the onCreateView
                    of the Fragment finance recycler
                  */
                    switch (layout) {


                        case ("accounts"):
                         /*Deletes the row of TABLE_ACCOUNTS with the ID at the current position(card view clicked).
                           The id of the item to be deleted is stored at the current position in accountid
                         */
                            MainActivity.database.deleteRow(DataStrings.TABLE_ACCOUNTS, "_id", accountid.get(position));

                            //Reset the adapter to update the cardview without having to reload the fragment
                            Fragment_finance_recycler.recyclerView.setAdapter(new RecyclerAdapterFinance(Fragment_finance_recycler.generatePalettes(DataStrings.TABLE_ACCOUNTS,
                                    DataStrings.COLUMN_ACCOUNT_NAME, DataStrings.COLUMN_ACCOUNT_BALANCE)));


                            break;


                        case ("assets"):
                         /*Deletes the row of TABLE_ASSETS with the ID at the current position(card view clicked).
                           The id of the item to be deleted is stored at the current position in assetid
                         */
                            MainActivity.database.deleteRow(DataStrings.TABLE_ASSETS, "_id", assetid.get(position));

                            //Reset the adapter to update the cardview without having to reload the fragment
                            Fragment_finance_recycler.recyclerView.setAdapter(new RecyclerAdapterFinance(Fragment_finance_recycler.generatePalettes(DataStrings.TABLE_ASSETS,
                                    DataStrings.COLUMN_ASSET_NAME, DataStrings.COLUMN_ASSET_VALUE)));
                            break;

                        case ("stocks"):
                            /*Deletes the row of TABLE_ASSETS with the ID at the current position(card view clicked).
                              The id of the item to be deleted is stored at the current position in assetid
                            */
                            MainActivity.database.deleteRow(DataStrings.TABLE_STOCKS, "_id", stockid.get(position));
                            //Reset the adapter to update the cardview without having to reload the fragment
                            Fragment_finance_recycler.recyclerView.setAdapter(new RecyclerAdapterFinance(Fragment_finance_recycler.generatePalettes(DataStrings.TABLE_STOCKS,
                                    DataStrings.COLUMN_STOCK_NAME, DataStrings.COLUMN_STOCK_VALUE)));
                            break;
                        default:
                            break;
                    }


                }
            });
            alert.show();

        }

    });








    return new FinanceFragment.PaletteViewHolder(itemView);
}




    @Override
    public void onBindViewHolder(FinanceFragment.PaletteViewHolder paletteViewHolder, int i) {
        FinanceFragment.Palette palette = palettes.get(i);
        paletteViewHolder.titleText.setText(palette.getName());
        paletteViewHolder.contentText.setText(palette.getHexValue());
        paletteViewHolder.card.setBackgroundColor(palette.getIntValue());

        paletteViewHolder.position = palette.getPosition();

        RecyclerAdapterFinance.currentItem = palettes.get(i);

        /*If this viewHolder is a stock then load the associated webview for it*/
        if (paletteViewHolder.isStocks) {
            //Get column of stock names to append onto the URL
            List<String> stockNames = MainActivity.database.getAllFromDb(DataStrings.TABLE_STOCKS, DataStrings.COLUMN_STOCK_NAME);
            //Get the stock name at the current position i
            String currentStock = stockNames.get(i);
            //Set the webview that is located in the paletteViewHolder class
            paletteViewHolder.stocksWebView.loadUrl("http://finance.yahoo.com/q?s=" + currentStock);

            //Auto load the URL - Do not prompt for a default application to handle the task
            paletteViewHolder.stocksWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

            });

        }


    }


    @Override
    public int getItemCount() {
        return palettes.size();
    }



}