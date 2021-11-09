package com.mbl.lucklotterprinter.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.ProductModel;
import com.mbl.lucklotterprinter.printer.detail.DetailPresenter;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DateTimeUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class PrinterFragment extends ViewFragment<PrinterContract.Presenter> implements PrinterContract.View, SwipeRefreshLayout.OnRefreshListener {
    public final String TAG = "PrinterFragment";
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.no_data)
    LinearLayout noData;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_filter)
    ImageView iv_filter;
    @BindView(R.id.tv_draw)
    TextView tv_draw;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_countdown)
    TextView tv_countdown;

    PrinterAdapter mAdapter;
    List<OrderModel> mOrderModels;

    SharedPref sharedPref;

    private Date d1 = null;
    private Date d2 = null;
    private Date date;
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    int productID;

    public static PrinterFragment getInstance() {
        return new PrinterFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_printer;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        sharedPref = new SharedPref(requireActivity());
        mOrderModels = new ArrayList<>();

        noData.setVisibility(View.GONE);

        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PrinterAdapter(getContext(), mOrderModels) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.itemView.setOnClickListener(v -> {
                    new DetailPresenter((ContainerView) getBaseActivity(), mOrderModels.get(position)).pushView();
                });
            }
        };
        recycle.setAdapter(mAdapter);

        Intent intent = requireActivity().getIntent();
        productID = intent.getIntExtra(Constants.PRODUCT_ID, 0);
        tv_title.setText(Utils.getProductName(productID));
        if (productID != Constants.PRODUCT_KENO) {
            iv_filter.setVisibility(View.VISIBLE);
            iv_filter.setOnClickListener(v -> pickProduct());
        } else
            iv_filter.setVisibility(View.GONE);

        iv_back.setOnClickListener(v -> mPresenter.back());
    }

    @Override
    public void onDisplay() {
        super.onDisplay();

        if (mPresenter != null) {
            mPresenter.getOrder(productID);
        }
    }

    @Override
    public void showOrder(List<OrderModel> orderModels) {

        if (orderModels.size() > 0)
            noData.setVisibility(View.GONE);
        else
            noData.setVisibility(View.VISIBLE);

        mAdapter.setAllItemsEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mOrderModels.clear();
        mOrderModels.addAll(orderModels);
        mAdapter.setItems(mOrderModels);
    }

    @Override
    public void showKenoDraw(List<DrawModel> drawModels) {
        String serverTime = sharedPref.getString(Constants.KEY_DATE_TIME_NOW,"");
        Date date1 = DateTimeUtils.convertStringToDateDefault(serverTime);

        try {
            String dateStart = format1.format(date);
            d1 = format1.parse(dateStart);
            d2 = format1.parse(serverTime);

            assert d2 != null;
            long diff = d2.getTime() - d1.getTime();
            CountdownKeno(diff);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void CountdownKeno(long diff) {
        CountDownTimer cdt = new CountDownTimer(diff, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                tv_countdown.setText(StringUtils.leftPad(String.valueOf(minutes), 2, '0') + ":" + StringUtils.leftPad(String.valueOf(seconds), 2, '0'));
            }

            @Override
            public void onFinish() {
                cancel();
                CountdownKeno(60 * 10 * 1000);
            }
        };
        cdt.start();
    }

    @Override
    public void onRefresh() {
        mAdapter.setAllItemsEnabled(false);
        mPresenter.getOrder(productID);
    }

    private void pickProduct() {
        List<ProductModel> productModels = new ArrayList<>();

        ProductModel productModel = new ProductModel();

        productModel.setId(0);
        productModel.setName(Utils.getProductName(0));
        productModels.add(productModel);

        productModel = new ProductModel();
        productModel.setId(Constants.PRODUCT_POWER);
        productModel.setName(Utils.getProductName(Constants.PRODUCT_POWER));
        productModels.add(productModel);

        productModel = new ProductModel();
        productModel.setId(Constants.PRODUCT_MEGA);
        productModel.setName(Utils.getProductName(Constants.PRODUCT_MEGA));
        productModels.add(productModel);

        productModel = new ProductModel();
        productModel.setId(Constants.PRODUCT_MAX3D);
        productModel.setName(Utils.getProductName(Constants.PRODUCT_MAX3D));
        productModels.add(productModel);

        productModel = new ProductModel();
        productModel.setId(Constants.PRODUCT_MAX3D_PLUS);
        productModel.setName(Utils.getProductName(Constants.PRODUCT_MAX3D_PLUS));
        productModels.add(productModel);

        productModel = new ProductModel();
        productModel.setId(Constants.PRODUCT_MAX3D_PRO);
        productModel.setName(Utils.getProductName(Constants.PRODUCT_MAX3D_PRO));
        productModels.add(productModel);

        PopupMenu popupMenu = new PopupMenu(requireActivity(), iv_filter, Gravity.END);

        for (ProductModel productModel1 : productModels) {
            popupMenu.getMenu().add(1, productModel1.getId(), 0, productModel1.getName());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String value = item.getTitle().toString();
                tv_title.setText(value);
                productID = item.getItemId();
                mPresenter.getOrder(productID);
                return true;
            }
        });

        popupMenu.show();
    }
}
