package com.example.viewpager2sample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int height;
    private int numberOfDisplays;

    //Data Displays
    private Integer[] dataFieldOptions = {0,9,1,8,2,7,3,6,4,5}; //This would be retrieved from database after user saves which and how many data fields to display
    private int maximizedView = 0;
    private boolean viewMaximized = false;
    private String[] mLabels = {"speed","speed_average","dist","elevation_gain","elevation_loss","alt","grade","power","power_average","power_5s","power_30s","power_5min"};
    private String[] mUnits = {"m\nh","m\nh","m\ni","ft","ft","ft","%","W","W","W","W","W"};
    private ArrayList<String> mData;
    private ArrayList<TextView> textViews = new ArrayList<>();

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, int numberOfDisplays, int height) {
        this.mInflater = LayoutInflater.from(context);
        this.height = height;
        this.numberOfDisplays = numberOfDisplays;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        int rows;
        if (viewMaximized){
            rows = 1;
        } else if (numberOfDisplays > 8) {
            rows = 5;
        } else if (numberOfDisplays > 6) {
            rows = 4;
        } else {
            rows = Math.min(numberOfDisplays, 3);
        }

        lp.height = parent.getMeasuredHeight() / rows;
        view.setLayoutParams(lp);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (viewMaximized){
            position = maximizedView;
        }

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

        int rows;
        if (viewMaximized){
            rows = 1;
        } else if (numberOfDisplays > 8) {
            rows = 5;
        } else if (numberOfDisplays > 6) {
            rows = 4;
        } else {
            rows = Math.min(numberOfDisplays, 3);
        }

        /*if (numberOfDisplays > 4 && numberOfDisplays % 2 != 0){
            lp.height = (int) (position == 0 ? height * 0.4 : height * 0.6 / (rows - 1));
        } else {*/
            lp.height = height / rows;
        //}
        holder.itemView.setLayoutParams(lp);

        holder.txtLabel.setText(mLabels[dataFieldOptions[position]]);
        holder.txtUnit.setText(mUnits[dataFieldOptions[position]]);
        if (mData != null) {
            holder.txtData.setText(mData.get(dataFieldOptions[position]));
        }

        textViews.add(holder.txtData);

        Log.i("TAG","Binding view holder: " + position);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (viewMaximized){
            return 1;
        } else {
            return numberOfDisplays;
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLabel;
        TextView txtData;
        TextView txtUnit;

        ViewHolder(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.txtLabel);
            txtData = itemView.findViewById(R.id.txtData);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(dataFieldOptions[id]);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    void updateViewNumber(int numberOfDisplays, int height){
        this.height = height;
        this.numberOfDisplays = numberOfDisplays;
        this.textViews.clear();
        notifyDataSetChanged();
    }

    void maximizeView(boolean viewMaximized, int position, int height){
        this.height = height;
        this.viewMaximized = viewMaximized;
        maximizedView = position;
        this.textViews.clear();
        notifyDataSetChanged();
    }

    void updateHeight (int height) {
        this.height = height;
        this.textViews.clear();
        notifyDataSetChanged();
    }

    void updateDataDisplays (ArrayList<String> dataValues) {
        mData = dataValues;

        Log.i("MAX",maximizedView + ", " + viewMaximized);

        if (textViews.size() == ((viewMaximized) ? 1 : numberOfDisplays)) {
            if (viewMaximized){
                textViews.get(0).setText(mData.get(dataFieldOptions[maximizedView]));
            } else {
                int i = 0;
                for (Integer dataFieldOptions : dataFieldOptions) {
                    textViews.get(i).setText(mData.get(dataFieldOptions));
                    i++;
                    if (i >= numberOfDisplays) {
                        break;
                    }
                }
            }
        }
        //notifyDataSetChanged();
    }
}
