package com.fastaccess.tfl.ui.main.dock;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.ui.widget.FastBitmapDrawable;
import com.fastaccess.tfl.ui.widget.FontTextView;
import com.fastaccess.tfl.ui.widget.VHolder;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Kosh on 19/12/15 10:11 AM
 */
public class SelectAppAdapter extends RecyclerView.Adapter<VHolder> {
    public interface OnAppClicked {
        void onClick(AppsModel model);
    }

    private SparseBooleanArray selectedCheckbox = new SparseBooleanArray();
    private List<AppsModel> modelList;
    private OnAppClicked onAppClicked;
    private boolean isFolder;

    public SelectAppAdapter(List<AppsModel> modelList, OnAppClicked onAppClicked, boolean isFolder) {
        this.modelList = modelList;
        this.onAppClicked = onAppClicked;
        this.isFolder = isFolder;
    }

    @Override public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!isFolder) {
            return new AppsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_pack_layout, parent, false));
        }
        return new FolderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_select_layout, parent, false));
    }

    @Override public void onBindViewHolder(VHolder holder, int position) {
        if (!isFolder) {
            AppsHolder h = (AppsHolder) holder;
            final AppsModel model = modelList.get(position);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onAppClicked.onClick(model);
                }
            });
            h.icon.setImageDrawable(new FastBitmapDrawable(model.getBitmap()));
            h.title.setText(model.getAppName());
        } else {
            final FolderHolder h = (FolderHolder) holder;
            final AppsModel model = modelList.get(position);
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int p = h.getAdapterPosition();
                    select(p, !isItemChecked(p));
                    h.radio.setChecked(!isItemChecked(p));
                    onAppClicked.onClick(model);
                }
            });
            h.icon.setImageDrawable(new FastBitmapDrawable(model.getBitmap()));
            h.title.setText(model.getAppName());
            h.radio.setChecked(isItemChecked(position));
        }
    }

    @Override public int getItemCount() {
        return modelList.size();
    }

    private boolean isItemChecked(int position) {
        return selectedCheckbox.get(position);
    }

    private void select(int position, boolean checked) {
        selectedCheckbox.append(position, checked);
        notifyItemChanged(position);
    }

    private void clearSelection() {
        selectedCheckbox.clear();
    }

    public void add(List<AppsModel> data) {
        modelList.clear();
        modelList.addAll(data);
        notifyDataSetChanged();
    }

    public void reset() {
        modelList.clear();
        notifyDataSetChanged();
    }

    static class AppsHolder extends VHolder {
        @Bind(R.id.icon) ImageView icon;
        @Bind(R.id.title) FontTextView title;
        @Bind(R.id.radio) RadioButton radio;

        public AppsHolder(View itemView) {
            super(itemView);
        }
    }

    static class FolderHolder extends VHolder {
        @Bind(R.id.icon) ImageView icon;
        @Bind(R.id.title) FontTextView title;
        @Bind(R.id.radio) CheckBox radio;

        public FolderHolder(View itemView) {
            super(itemView);
        }
    }
}
