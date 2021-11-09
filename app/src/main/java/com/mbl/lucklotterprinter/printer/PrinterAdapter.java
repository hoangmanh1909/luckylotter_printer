package com.mbl.lucklotterprinter.printer;

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
import com.mbl.lucklotterprinter.delegate.RecyclerViewItemEnabler;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.NumberUtils;

import java.util.List;

import butterknife.BindView;

public class PrinterAdapter extends RecyclerBaseAdapter implements RecyclerViewItemEnabler {

    Activity activity;
    List mItems;
    boolean mAllEnabled;

    public PrinterAdapter(Context context, List items) {
        super(context, items);

        activity = (Activity) context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public PrinterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrinterAdapter.ViewHolder(inflateView(parent, R.layout.item_printer));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder((PrinterAdapter.ViewHolder) holder, position);
    }

    public void setAllItemsEnabled(boolean enable) {
        mAllEnabled = enable;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public boolean isAllItemsEnabled() {
        return mAllEnabled;
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.img_logo)
        ImageView img_logo;
        @BindView(R.id.tv_code)
        TextView tv_code;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_fullName)
        TextView tv_fullName;
        @BindView(R.id.tv_mobile)
        TextView tv_mobile;
        @BindView(R.id.tv_amount)
        TextView tv_amount;
        @BindView(R.id.tv_quantity)
        TextView tv_quantity;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Object model, int position) {
            OrderModel orderModel = (OrderModel) model;

            switch (orderModel.getProductID()){
                case Constants.PRODUCT_MEGA:
                    img_logo.setImageResource(R.drawable.home_mega);
                    break;
                case Constants.PRODUCT_MAX4D:
                    img_logo.setImageResource(R.drawable.home_max4d);
                    break;
                case Constants.PRODUCT_MAX3D:
                    img_logo.setImageResource(R.drawable.home_max3d);
                    break;
                case Constants.PRODUCT_MAX3D_PLUS:
                    img_logo.setImageResource(R.drawable.home_max3d_plus);
                    break;
                case Constants.PRODUCT_MAX3D_PRO:
                    img_logo.setImageResource(R.drawable.logomax3dpro);
                    break;
                case Constants.PRODUCT_POWER:
                    img_logo.setImageResource(R.drawable.home_power);
                    break;
                case Constants.PRODUCT_KENO:
                    img_logo.setImageResource(R.drawable.home_keno);
                    break;
            }

            tv_code.setText("#" + orderModel.getOrderCode());
            tv_date.setText(orderModel.getOrderDate());
            tv_fullName.setText(orderModel.getFullName() + "/" + orderModel.getpIDNumber());
            tv_mobile.setText(orderModel.getMobileNumber());
            tv_amount.setText(NumberUtils.formatPriceNumber(orderModel.getPrice()));
            tv_quantity.setText(orderModel.getQuantity() + " kỳ");
        }
    }
}
