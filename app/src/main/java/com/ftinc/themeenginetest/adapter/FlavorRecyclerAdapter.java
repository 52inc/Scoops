package com.ftinc.themeenginetest.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftinc.scoop.model.Flavor;
import com.ftinc.themeenginetest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.themeenginetest.adapter
 * Created by drew.heavner on 6/8/16.
 */

public class FlavorRecyclerAdapter extends RecyclerView.Adapter<FlavorRecyclerAdapter.FlavorViewHolder> {

    private final LayoutInflater mInflater;
    private final List<Flavor> mItems;

    private OnItemClickListener mItemClickListener;

    public FlavorRecyclerAdapter(Activity activity){
        mInflater = activity.getLayoutInflater();
        mItems = new ArrayList<>();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public FlavorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return FlavorViewHolder.create(mInflater, parent);
    }

    @Override
    public void onBindViewHolder(final FlavorViewHolder holder, final int position) {
        final Flavor item = mItems.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null) mItemClickListener.onItemClicked(v, item, holder.getAdapterPosition());
            }
        });

        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(List<Flavor> flavors) {
        mItems.clear();
        mItems.addAll(flavors);
        notifyDataSetChanged();
    }

    static class FlavorViewHolder extends RecyclerView.ViewHolder{

        static FlavorViewHolder create(LayoutInflater inflater, ViewGroup parent){
            View view = inflater.inflate(R.layout.item_layout_flavor, parent, false);
            return new FlavorViewHolder(view);
        }

        @BindView(R.id.title)
        TextView mTitle;

        FlavorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Flavor flavor){
            mTitle.setText(flavor.getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(View view, Flavor item, int position);
    }

}
