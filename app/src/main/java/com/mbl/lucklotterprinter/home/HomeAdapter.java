package com.mbl.lucklotterprinter.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.adapter.RecyclerBaseAdapter;
import com.core.widget.BaseViewHolder;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.HomeModel;

import java.util.List;

import butterknife.BindView;

public class HomeAdapter  extends RecyclerBaseAdapter {
    Activity activity;

    public HomeAdapter(Context context, List<HomeModel> mHomeList) {
        super(context, mHomeList);

        activity = (Activity) context;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeAdapter.ViewHolder(inflateView(parent, R.layout.item_home));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((BaseViewHolder) holder, position);
    }

    public class ViewHolder extends BaseViewHolder{
        @BindView(R.id.img_logo)
        ImageView imgLogo;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Object model, int position) {
            HomeModel homeModel = (HomeModel)model;
            imgLogo.setImageResource(homeModel.getLogo());
            tvTitle.setText(homeModel.getTitle());
        }
    }
}
