package com.example.viewpager2sample;

import android.content.Context;
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
    private Integer[] dataFieldOptions = {0,5,1,4,2,3,6,7}; //This would be retrieved from database after user saves which and how many data fields to display
    private int maximizedView = 0;
    private boolean viewMaximized = false;
    private String[] mLabels = {"speed","speed_average","dist","elevation_gain","elevation_loss","alt","grade","power","power_average","power_5s","power_30s","power_5min"};
    private ArrayList<String> mData;

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
        } else {
            rows = (numberOfDisplays > 6) ? 4 : Math.min(numberOfDisplays, 3);
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
        holder.txtLabel.setText(mLabels[dataFieldOptions[position]]);
        if (mData != null) {
            holder.txtData.setText(mData.get(dataFieldOptions[position]));
        }

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

        int rows;
        if (viewMaximized){
            rows = 1;
        } else {
            rows = (numberOfDisplays > 6) ? 4 : Math.min(numberOfDisplays, 3);
        }

        lp.height = height / rows;
        holder.itemView.setLayoutParams(lp);
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

        ViewHolder(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.txtLabel);
            txtData = itemView.findViewById(R.id.txtData);
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
        notifyDataSetChanged();
    }

    void maximizeView(boolean viewMaximized, int position, int height){
        this.height = height;
        this.viewMaximized = viewMaximized;
        maximizedView = position;
        notifyDataSetChanged();
    }

    void updateHeight (int height) {
        this.height = height;
        notifyDataSetChanged();
    }

    void updateDataDisplays (ArrayList<String> dataValues) {
        mData = dataValues;
        notifyDataSetChanged();
    }
}
