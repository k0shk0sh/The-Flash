package com.fastaccess.tfl.apps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.ui.widget.BubbleTextView;
import com.fastaccess.tfl.ui.widget.VHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Kosh on 18/12/15 12:53 AM
 */
public class AppsAdapter extends RecyclerView.Adapter<VHolder> implements Filterable {

    public interface OnAppClick {
        void onAppClick(AppsModel model);
    }

    private OnAppClick onAppClick;

    private List<AppsModel> modelList;

    public AppsAdapter(OnAppClick onAppClick) {
        this.onAppClick = onAppClick;
        this.modelList = new ArrayList<>();
    }

    @Override public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_row_item, parent, false));
    }

    @Override public void onBindViewHolder(VHolder holder, int position) {
        final AppsHolder h = (AppsHolder) holder;
        h.appName.applyFromApplicationInfo(modelList.get(position));
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAppClick.onAppClick(modelList.get(h.getAdapterPosition()));
            }
        });
    }

    @Override public int getItemCount() {
        return modelList.size();
    }

    @Override public Filter getFilter() {
        return null;
    }

    public void setModelList(List<AppsModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public void clear() {
        modelList.clear();
        notifyDataSetChanged();
    }

    static class AppsHolder extends VHolder {
        @Bind(R.id.appName) BubbleTextView appName;

        public AppsHolder(View itemView) {
            super(itemView);
        }
    }
}
