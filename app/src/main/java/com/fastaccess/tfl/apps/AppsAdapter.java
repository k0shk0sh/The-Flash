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

        void onLongClick(AppsModel model, int position, View v);
    }

    private OnAppClick onAppClick;
    private List<AppsModel> modelList;
    private List<AppsModel> searchableList;

    public AppsAdapter(OnAppClick onAppClick) {
        this.onAppClick = onAppClick;
        this.modelList = new ArrayList<>();
    }

    @Override public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_item, parent, false));
    }

    @Override public void onBindViewHolder(VHolder holder, int position) {
        final AppsHolder h = (AppsHolder) holder;
        final AppsModel model = modelList.get(position);
        h.appName.applyFromApplicationInfo(model);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onAppClick.onAppClick(model);
            }
        });
        h.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                onAppClick.onLongClick(model, h.getAdapterPosition(), v);
                return true;
            }
        });
    }

    @Override public int getItemCount() {
        return modelList.size();
    }

    @Override public Filter getFilter() {
        return appsFilter;
    }

    public void remove(int position) {
        modelList.remove(position);
        notifyItemRemoved(position);
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

    private Filter appsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults oReturn = new FilterResults();
            final List<AppsModel> results = new ArrayList<>();
            if (searchableList == null) {
                searchableList = modelList;
            }
            if (charSequence != null) {
                if (searchableList != null && searchableList.size() > 0) {
                    for (final AppsModel appInfo : searchableList) {
                        if (appInfo.getAppName().toLowerCase().contains(charSequence.toString())) {
                            results.add(appInfo);
                        }
                    }
                }
                oReturn.values = results;
                oReturn.count = results.size();
            }
            return oReturn;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            modelList = (List<AppsModel>) filterResults.values;
            notifyDataSetChanged();
        }
    };
}
