package com.example.viewpager2sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class FragmentGrid extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private GridLayoutManager manager;
    private RecyclerView recyclerView;
    private LinearLayout editLayout;

    SeekBar viewSeekBar;

    private int numberOfDisplays = 5;
    private int fragNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gridlayout, container, false);

        boolean editMode = false;
        boolean metric = false;
        if (getArguments() != null) {
            fragNumber = getArguments().getInt("fragNumber");
            editMode = getArguments().getBoolean("edit");
            metric = getArguments().getBoolean("metric");
        }
        //Shared Preferences
        SharedPreferences mPrefs = Objects.requireNonNull(getActivity()).getSharedPreferences(Metrics.DISPLAY_PREFS,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(Metrics.DISPLAY + fragNumber, "");

        DisplayObject dataFieldOptions;
        if (!json.equals("")) {
            dataFieldOptions = gson.fromJson(json, DisplayObject.class);
            numberOfDisplays = dataFieldOptions.getNumberDisplays();
        } else {
            if (fragNumber == 1) {
                dataFieldOptions = new DisplayObject(new ArrayList<>(Arrays.asList(
                        Metrics.POWER.getMetricNumber(),
                        Metrics.POWER_AVERAGE.getMetricNumber(),
                        Metrics.POWER_5_SEC.getMetricNumber(),
                        Metrics.POWER_30_SEC.getMetricNumber(),
                        Metrics.POWER_5_MIN.getMetricNumber(),
                        Metrics.SPEED.getMetricNumber(),
                        Metrics.SPEED_AVERAGE.getMetricNumber(),
                        Metrics.DISTANCE.getMetricNumber(),
                        Metrics.ELEVATION_GAIN.getMetricNumber(),
                        Metrics.ALTITUDE.getMetricNumber())), numberOfDisplays);
            } else {
                dataFieldOptions = new DisplayObject(new ArrayList<>(Arrays.asList(
                        Metrics.SPEED.getMetricNumber(),
                        Metrics.SPEED_AVERAGE.getMetricNumber(),
                        Metrics.DISTANCE.getMetricNumber(),
                        Metrics.ELEVATION_GAIN.getMetricNumber(),
                        Metrics.ALTITUDE.getMetricNumber(),
                        Metrics.POWER.getMetricNumber(),
                        Metrics.POWER_AVERAGE.getMetricNumber(),
                        Metrics.POWER_5_SEC.getMetricNumber(),
                        Metrics.POWER_30_SEC.getMetricNumber(),
                        Metrics.POWER_5_MIN.getMetricNumber())), numberOfDisplays);
            }
        }

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.rvNumbers);

        final int numberOfColumns = 2;


        manager = new GridLayoutManager(getActivity(), numberOfColumns) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        setSpanSize(numberOfDisplays);

        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(Objects.requireNonNull(getActivity()), dataFieldOptions, recyclerView.getMeasuredHeight(), metric);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adapter.updateViewNumber(numberOfDisplays, recyclerView.getMeasuredHeight());
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        editLayout = view.findViewById(R.id.editLayout);

        viewSeekBar = view.findViewById(R.id.viewSeekBar);
        viewSeekBar.setMax(10);
        viewSeekBar.setProgress(adapter.getItemCount());
        viewSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //numberOfDisplays = i;

                if (i > 0) {

                    setSpanSize(i);

                    adapter.updateViewNumber(i, recyclerView.getMeasuredHeight());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageButton buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) Objects.requireNonNull(getActivity())).deleteFragment(fragNumber);
            }
        });

        if (editMode){
            editDisplays(true, false, fragNumber);
        }

        return view;
    }

    @Override
    public void onItemClick(View view, int position, boolean antiBurnMode) {
        if (antiBurnMode){
            ((MainActivity) Objects.requireNonNull(getActivity())).enableAntiScreenBurnMode(false);
        } else {
            maximizeView(position);
        }
    }

    private void maximizeView(int position) {
        if (manager.getChildCount() == 1){
            setSpanSize(numberOfDisplays);
            adapter.maximizeView(false,false, position, recyclerView.getMeasuredHeight());
        } else if (numberOfDisplays > 1) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 2;
                }
            });

            adapter.maximizeView(false,true, position, recyclerView.getMeasuredHeight());
        }
    }

    void updateHeight (/*int viewPagerHeight*/) {
        if (recyclerView != null && getContext() != null) {
            adapter.updateHeight(recyclerView.getMeasuredHeight()/*(int) (viewPagerHeight - (viewSeekBar.getMeasuredHeight() + 2 * (getContext().getResources().getDimension(R.dimen.grid_padding))))*/);
        }
    }

    void updateDataDisplays (ArrayList<String> dataValues) {
        if (adapter != null && dataValues != null) {
            adapter.updateDataDisplays(dataValues);
        }
    }

    void updateSpecificMetricDisplays (int metric, String dataValue) {
        if (adapter != null) {
            adapter.updateSpecificMetricDisplays(metric, dataValue);
        }
    }

    void enableAntiBurnMode (boolean antiBurnMode) {
        if (antiBurnMode) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 2;
                }
            });
            adapter.maximizeView(true, true, 0, recyclerView.getMeasuredHeight());
        } else {
            maximizeView(0);
        }
    }

    private void setSpanSize(final int numberOfDisplays) {
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize;
                if (numberOfDisplays == 10) {
                    spanSize = 1;
                } else if (numberOfDisplays == 9) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 8) {
                    spanSize = 1;
                } else if (numberOfDisplays == 7) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 6) {
                    spanSize = 1;
                } else if (numberOfDisplays == 5) {
                    if (position == 0) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 4) {
                    if (position <= 1) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 3) {
                    if (position <= 2) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else if (numberOfDisplays == 2) {
                    if (position <= 3) {
                        spanSize = 2;
                    } else {
                        spanSize = 1;
                    }
                } else {
                    spanSize = 2;
                }

                return spanSize;
            }
        });
    }

    void editDisplays (final boolean enableEdit, boolean save, int fragNumber) {
        if (enableEdit) {
            editLayout.setVisibility(View.VISIBLE);

            if (manager.getChildCount() == 1 && numberOfDisplays > 1){
                setSpanSize(numberOfDisplays);
                adapter.maximizeView(false,false, 0, recyclerView.getMeasuredHeight());
            }
        } else {
            editLayout.setVisibility(View.GONE);
            if (save){
                numberOfDisplays = adapter.getItemCount();
            } else {
                setSpanSize(numberOfDisplays);
                //adapter.updateViewNumber(numberOfDisplays, recyclerView.getMeasuredHeight());
                viewSeekBar.setProgress(numberOfDisplays);

            }
        }

        adapter.editDisplays(enableEdit, fragNumber, save);

        editLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.i("TAG","updating height to " + "Visible = " + visible);
                if (enableEdit ? editLayout.getVisibility() == View.VISIBLE : editLayout.getVisibility() == View.GONE) {
                    updateHeight();
                    editLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    static FragmentGrid newInstance(int fragNumber, boolean editMode, boolean metric) {
        FragmentGrid fragmentGrid = new FragmentGrid();
        /* See this code gets executed immediately on your object construction */
        Bundle args = new Bundle();
        args.putInt("fragNumber", fragNumber);
        args.putBoolean("edit", editMode);
        args.putBoolean("metric", metric);
        fragmentGrid.setArguments(args);
        return fragmentGrid;
    }
}
