package com.mbl.lucklotterprinter.printer.detail;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.adapter.RecyclerBaseAdapter;
import com.core.widget.BaseViewHolder;
import com.mbl.lucklotterprinter.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;

public class NumberAdapter extends RecyclerBaseAdapter {

    Activity activity;

    public NumberAdapter(Context context, List items) {
        super(context, items);
        activity = (Activity) context;
    }

    @Override
    public NumberAdapter.HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NumberAdapter.HolderView(inflateView(parent, R.layout.item_number));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((NumberAdapter.HolderView) holder, position);
    }

    public class HolderView extends BaseViewHolder {

        @BindView(R.id.tv_number)
        public TextView tvNumber;

        public HolderView(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(final Object model, int position) {
            tvNumber.setText(StringUtils.leftPad(StringUtils.trim(model.toString()), 2, '0'));
        }

    }
}
