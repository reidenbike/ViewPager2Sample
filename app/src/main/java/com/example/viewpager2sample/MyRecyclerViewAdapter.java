package com.example.viewpager2sample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int height;
    private int numberOfDisplays;

    //View Types
    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_MAX = 1;
    private static final int VIEW_TYPE_ANTI_BURN = 2;
    private boolean antiburn = false;

    //Data Displays
    private Integer[] dataFieldOptions = {0,9,1,8,2,7,3,6,4,5}; //This would be retrieved from database after user saves which and how many data fields to display
    private int maximizedView = 0;
    private boolean viewMaximized = false;
    private String[] mLabels = {"speed","speed_average","dist","elevation_gain","elevation_loss","alt","grade","power","power_average","power_5s","power_30s","power_5min"};
    private String[] mUnits = {"m\nh","m\nh","m\ni","ft","ft","ft","%","W","W","W","W","W"};
    private String[] mUnitsInline = {"mph","mph","mi","ft","ft","ft","%","W","W","W","W","W"};
    private ArrayList<String> mData;
    private ArrayList<TextView> textViews = new ArrayList<>();

    private ObjectAnimator anim;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, int numberOfDisplays, int height) {
        //this.mInflater = LayoutInflater.from(context);
        this.height = height;
        this.numberOfDisplays = numberOfDisplays;
    }

    @Override
    public int getItemViewType(int position) {
        if (antiburn) {
            return VIEW_TYPE_ANTI_BURN;
        } else if (viewMaximized) {
            return VIEW_TYPE_MAX;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*int rows;
        if (viewMaximized){
            rows = 1;
        } else if (numberOfDisplays > 8) {
            rows = 5;
        } else if (numberOfDisplays > 6) {
            rows = 4;
        } else {
            rows = Math.min(numberOfDisplays, 3);
        }*/

        switch (viewType){
            case VIEW_TYPE_MAX:
                View viewMax = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_item_max, parent, false);

                return new ViewHolderMax(viewMax);

            case VIEW_TYPE_ANTI_BURN:
                View viewAntiBurn = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_antiburn, parent, false);

                return new ViewHolderAntiBurn(viewAntiBurn);

            //VIEW_TYPE_USER_DEFAULT
            default:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_item, parent, false);

                return new ViewHolderDefault(view);
        }
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
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

        lp.height = height / rows;

        holder.itemView.setLayoutParams(lp);

        /*holder.txtLabel.setText(mLabels[dataFieldOptions[position]]);
        holder.txtUnit.setText(mUnits[dataFieldOptions[position]]);
        if (mData != null) {
            holder.txtData.setText(mData.get(dataFieldOptions[position]));
        }

        textViews.add(holder.txtData);*/

        Log.i("TAG","Binding view holder: " + position);


        //Assign the holder:
        switch (holder.getItemViewType()){
            case VIEW_TYPE_DEFAULT:
                ((ViewHolderDefault) holder).bind(position);
                break;
            case VIEW_TYPE_MAX:
                ((ViewHolderMax) holder).bind(position);
                break;
            case VIEW_TYPE_ANTI_BURN:
                ((ViewHolderAntiBurn) holder).bind(position);
                break;
        }
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
    public class ViewHolderDefault extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLabel;
        TextView txtData;
        TextView txtUnit;

        ViewHolderDefault(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.txtLabel);
            txtData = itemView.findViewById(R.id.txtData);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition(), antiburn);
        }

        void bind (int position) {
            txtLabel.setText(mLabels[dataFieldOptions[position]]);
            txtUnit.setText(mUnits[dataFieldOptions[position]]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions[position]));
            }

            textViews.add(txtData);
        }
    }

    public class ViewHolderMax extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLabel;
        TextView txtData;
        TextView txtUnit;

        ViewHolderMax(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.txtLabel);
            txtData = itemView.findViewById(R.id.txtData);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition(), antiburn);
        }

        void bind (int position) {
            txtLabel.setText(mLabels[dataFieldOptions[position]]);
            txtUnit.setText(mUnitsInline[dataFieldOptions[position]]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions[position]));
            }

            textViews.add(txtData);
        }
    }

    public class ViewHolderAntiBurn extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLabel;
        TextView txtData;
        TextView txtUnit;

        LinearLayout antiBurnAnimationGroup;

        ViewHolderAntiBurn(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.antiBurnLabel);
            txtData = itemView.findViewById(R.id.txtAntiBurnMetric);
            txtUnit = itemView.findViewById(R.id.antiBurnMetricUnit);

            antiBurnAnimationGroup = itemView.findViewById(R.id.antiBurnAnimationGroup);

            itemView.setOnClickListener(this);

            anim = ObjectAnimator.ofFloat(
                    antiBurnAnimationGroup,
                    "translationY",
                    0f,
                    (int) (height * 0.53));
            anim.setDuration(60000);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setInterpolator(new LinearInterpolator());
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    Log.i("Anim","Anim Repeat");
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition(), antiburn);
        }

        void bind (int position) {
            txtLabel.setText(mLabels[dataFieldOptions[position]]);
            txtUnit.setText(mUnitsInline[dataFieldOptions[position]]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions[position]));
            }

            textViews.add(txtData);

            anim.start();
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
        void onItemClick(View view, int position, boolean antiBurnMode);
    }

    void updateViewNumber(int numberOfDisplays, int height){
        this.height = height;
        this.numberOfDisplays = numberOfDisplays;
        this.textViews.clear();
        notifyDataSetChanged();
    }

    void maximizeView(boolean antiburn, boolean viewMaximized, int position, int height){
        this.height = height;
        this.antiburn = antiburn;
        this.viewMaximized = viewMaximized;
        if (!antiburn) {
            if (viewMaximized) {
                maximizedView = position;
            }
            if (anim != null) {
                anim.cancel();
            }
        }
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
