package com.example.viewpager2sample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static android.content.Context.MODE_PRIVATE;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int height;
    private int numberOfDisplays;
    private Context context;

    //View Types
    private static final int VIEW_TYPE_DEFAULT = 0;
    private static final int VIEW_TYPE_MAX = 1;
    private static final int VIEW_TYPE_ANTI_BURN = 2;
    private static final int VIEW_TYPE_EDIT = 3;
    private boolean antiburn = false;
    private boolean editDisplays = false;

    //Data Displays
    private DisplayObject dataFieldOptions; //This would be retrieved from database after user saves which and how many data fields to display
    private int maximizedView = 0;
    private boolean viewMaximized = false;
    private String[] mLabels = {"Speed","Average Speed","Distance","Elevation Gain","Elevation Loss","Altitude","Percent Grade","Power","Average Power","5s Power","30s Power","5 min Power"};
    private String[] mUnits = {"m\nh","m\nh","m\ni","ft","ft","ft","%","W","W","W","W","W"};
    private String[] mUnitsInline = {"mph","mph","mi","ft","ft","ft","%","W","W","W","W","W"};
    private ArrayList<String> mData;
    private ArrayList<TextView> textViews = new ArrayList<>();

    private ObjectAnimator anim;

    //Shared Preferences
    private String displayPrefs = "display_config";

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, DisplayObject dataFieldOptions, int height) {
        //this.mInflater = LayoutInflater.from(context);

        this.context = context;

        this.dataFieldOptions = dataFieldOptions;

        this.height = height;
        this.numberOfDisplays = dataFieldOptions.getNumberDisplays();

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (editDisplays){
            return VIEW_TYPE_EDIT;
        } else if (antiburn) {
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

            case VIEW_TYPE_EDIT:
                View viewEdit = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_item_edit, parent, false);

                return new ViewHolderEdit(viewEdit);

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
            case VIEW_TYPE_EDIT:
                ((ViewHolderEdit) holder).bind(position);
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
            txtLabel.setText(mLabels[dataFieldOptions.getMetric(position)]);
            txtUnit.setText(mUnits[dataFieldOptions.getMetric(position)]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions.getMetric(position)));
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
            txtLabel.setText(mLabels[dataFieldOptions.getMetric(position)]);
            txtUnit.setText(mUnitsInline[dataFieldOptions.getMetric(position)]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions.getMetric(position)));
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
            txtLabel.setText(mLabels[dataFieldOptions.getMetric(position)]);
            txtUnit.setText(mUnitsInline[dataFieldOptions.getMetric(position)]);
            if (mData != null) {
                txtData.setText(mData.get(dataFieldOptions.getMetric(position)));
            }

            textViews.add(txtData);

            anim.start();
        }
    }

    public class ViewHolderEdit extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLabel;

        ViewHolderEdit(View itemView) {
            super(itemView);
            txtLabel = itemView.findViewById(R.id.txtLabel);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            displayMetricSelection(getBindingAdapterPosition(),txtLabel);
        }

        void bind (int position) {
            txtLabel.setText(mLabels[dataFieldOptions.getMetric(position)]);
        }
    }

    private void displayMetricSelection(final int position, final TextView txtLabel) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Select Display Metric");
        b.setItems(mLabels, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                setDisplayMetrics(position,which,txtLabel);
            }

        });

        b.show();
    }

    private void setDisplayMetrics(int position, int metric, TextView txtLabel){
        dataFieldOptions.setMetric(position, metric);
        txtLabel.setText(mLabels[metric]);
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(dataFieldOptions.getMetric(id));
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

        if (textViews.size() == ((viewMaximized) ? 1 : numberOfDisplays)) {
            if (viewMaximized){ // 7 == Power Metric. This will change in dataFieldOptions object to check if metric is independent of GPS recording points or not
                if (dataFieldOptions.getMetric(maximizedView) != 7) {
                    textViews.get(0).setText(mData.get(dataFieldOptions.getMetric(maximizedView)));
                }
            } else {
                for (int i = 0; i < numberOfDisplays; i++) {
                    if (dataFieldOptions.getMetric(i) != 7) {
                        textViews.get(i).setText(mData.get(dataFieldOptions.getMetric(i)));
                    }
                }
            }
        }
    }

    void updateSpecificMetricDisplays (int metric, String dataValue){
        if (textViews.size() == ((viewMaximized) ? 1 : numberOfDisplays)) {
            if (viewMaximized){
                if (dataFieldOptions.getMetric(maximizedView) == metric) {
                    textViews.get(0).setText(dataValue);
                }
            } else {
                for (int i = 0; i < numberOfDisplays; i++) {
                    if (dataFieldOptions.getMetric(i) == metric) {
                        textViews.get(i).setText(dataValue);
                    }
                }
            }
        }
    }

    void editDisplays (boolean enableEdit, int fragNumber, boolean save) {

        if (!enableEdit){
            dataFieldOptions.setNumberDisplays(numberOfDisplays);

            if (save) {
                SharedPreferences mPrefs = context.getSharedPreferences(displayPrefs, MODE_PRIVATE);

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(dataFieldOptions);
                prefsEditor.putString("display_" + fragNumber, json);
                prefsEditor.apply();
            }
        }

        editDisplays = enableEdit;
        this.textViews.clear();
    }
}
