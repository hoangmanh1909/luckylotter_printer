package com.mbl.lucklotterprinter.printer.detail;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.adapter.RecyclerBaseAdapter;
import com.core.widget.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.NumberUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class RowAdapter extends RecyclerBaseAdapter {

    Activity activity;

    public RowAdapter(Context context, List items) {
        super(context, items);
        activity = (Activity) context;
    }

    @Override
    public RowAdapter.HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RowAdapter.HolderView(inflateView(parent, R.layout.item_row));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((RowAdapter.HolderView) holder, position);
    }

    public class HolderView extends BaseViewHolder {

        @BindView(R.id.recycle)
        RecyclerView recycle;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_amount)
        TextView tv_amount;
        @BindView(R.id.divider)
        View divider;

        public HolderView(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(final Object model, int position) {
            LineModel lineModel = (LineModel) model;

            tv_title.setText(lineModel.getTitle());

            if (lineModel.getProductID() == Constants.PRODUCT_MEGA || lineModel.getProductID() == Constants.PRODUCT_POWER) {
                tv_amount.setVisibility(View.GONE);
                if(lineModel.getTitle().equals("F"))
                    divider.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                tv_amount.setVisibility(View.VISIBLE);
                tv_amount.setText(NumberUtils.formatPriceNumber(lineModel.getAmount()));
            }

            List<String> listNumber=  Arrays.asList(StringUtils.trim(lineModel.getLine()).split(","));

            recycle.setLayoutManager(new FlexboxLayoutManager(mContext));
            NumberAdapter adapter = new NumberAdapter(mContext,listNumber);
            recycle.setAdapter(adapter);
        }

    }
}
