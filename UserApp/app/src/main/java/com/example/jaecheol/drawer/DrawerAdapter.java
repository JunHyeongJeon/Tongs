package com.example.jaecheol.drawer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jaecheol.tongs.R;


/**
 * Created by JaeCheol on 15. 5. 7..
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>   {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

//    private String name;        //String Resource for header View Name
//    private int profile;        //int Resource for header view profile picture
//    private String email;       //String Resource for header view email


    private Bitmap barcode;
    private int currentNum;
    static private Handler mHandler=null;

    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them



    public DrawerAdapter(String Titles[], int Icons[], int _currentNum, Bitmap _barcode, Handler handler){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
//        name = Name;
//        email = Email;
//        profile = Profile;                     //here we assign those passed values to the values we declared here
        currentNum = _currentNum;
        barcode = _barcode;
        mHandler = handler;
        //in adapter

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;
        ImageView imageView;
        TextView currentNumText;
//        ImageView profile;
        TextView Name;
//        TextView email;

        RelativeLayout drawerTopLayout;



        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            itemView.setOnClickListener(this);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{
                drawerTopLayout = (RelativeLayout) itemView.findViewById(R.id.id_drawerTop);
                currentNumText = (TextView) itemView.findViewById(R.id.id_currentNumText);
                itemView.findViewById(R.id.id_plusButton).setOnClickListener(mClickListener);
                itemView.findViewById(R.id.id_minusButton).setOnClickListener(mClickListener);

//                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
//                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
//                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

        @Override
        public void onClick(View view)   {
            int pos = getPosition();
            switch (pos)    {
                case 0 :
                    Log.d("HELLO", "BARCODE PAGE");
                    sendHandler(21);
                    break;
                case 1 :
                    Log.d("HELLO", "COUPON PAGE");
                    sendHandler(22);
                    break;
            }
        }


        Button.OnClickListener mClickListener = new View.OnClickListener()  {

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.id_plusButton :
                        sendHandler(11);
                        break;
                    case R.id.id_minusButton :
                        sendHandler(12);
                        break;
                }
            }
        };

        public void sendHandler(int msgId)
        {
            if(mHandler == null)
                return;

            mHandler.obtainMessage(msgId).sendToTarget();
        }
    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
        }
        else{
            Drawable drawableBarcode = new BitmapDrawable(barcode);

            holder.drawerTopLayout.setBackground(drawableBarcode);
            holder.currentNumText.setText("현재 인원수 " + currentNum + "명");
//            holder.drawerTopLayout.setBackgroundResource(barcode);         // Similarly we set the resources for header view
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}