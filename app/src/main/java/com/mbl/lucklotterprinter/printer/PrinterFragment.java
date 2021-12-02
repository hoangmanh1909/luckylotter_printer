package com.mbl.lucklotterprinter.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.mbl.lucklotterprinter.service.TimeService;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DateTimeUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.TimerThread;
import com.mbl.lucklotterprinter.utils.Toast;
import com.mbl.lucklotterprinter.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
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
    @BindView(R.id.tv_count_wait_print)
    TextView tv_count_wait_print;

    @BindView(R.id.ll_keno)
    LinearLayout ll_keno;

    PrinterAdapter mAdapter;
    List<OrderModel> mOrderModels;
    boolean IsPrint = true;
    List<DrawModel> mDrawModels;
    SharedPref sharedPref;
    CountDownTimer cdt;
    int productID;
    Date serverTime;

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
        mDrawModels = new ArrayList<>();

        noData.setVisibility(View.GONE);

        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PrinterAdapter(getContext(), mOrderModels) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.itemView.setOnClickListener(v -> {
                    if (IsPrint)
                        new DetailPresenter((ContainerView) getBaseActivity(), mOrderModels.get(position), mDrawModels).pushView();
                    else
                        Toast.showToast(requireContext(), "Đã hết thời gian in vé");
                });
            }
        };
        recycle.setAdapter(mAdapter);

        Intent intent = requireActivity().getIntent();
        productID = intent.getIntExtra(Constants.PRODUCT_ID, 0);
        tv_title.setText(Utils.getProductName(productID));
        if (productID != Constants.PRODUCT_KENO) {
            ll_keno.setVisibility(View.GONE);
            iv_filter.setVisibility(View.VISIBLE);
            iv_filter.setOnClickListener(v -> pickProduct());
        } else {
            String waitPrint = String.format(getResources().getString(R.string.wait_print_keno), "0");
            tv_count_wait_print.setText(waitPrint);
            countWaitPrint();
            ll_keno.setVisibility(View.VISIBLE);
            iv_filter.setVisibility(View.GONE);
        }

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
    public void showTimeNow(String timeNow) {
        serverTime = DateTimeUtils.convertStringToDateDefault(timeNow);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                serverTime = DateUtils.addSeconds(serverTime, 1);
                Log.d("Timer", DateTimeUtils.convertDateToString(serverTime, ""));
            }
        };
        long delay = 1000L;
        Timer timer = new Timer("ServerTimer");
        timer.schedule(timerTask, 0, delay);
        if (mPresenter != null)
            mPresenter.getKenoDraw();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void showKenoDraw(List<DrawModel> drawModels) {
        if (drawModels.size() > 0) {
            mDrawModels.clear();
            mDrawModels.addAll(drawModels);
            findCurrentDraw();
        }
    }

    private void countWaitPrint() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mPresenter != null)
                    mPresenter.countOrderWattingPrint(Constants.PRODUCT_KENO);
            }
        };
        long delay = 15000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timerTask, 0, delay);
    }

    @Override
    public void showCountWaitPrint(int value) {
        try {

            if (requireActivity() != null) {
                @SuppressLint("StringFormatMatches") String waitPrint = String.format(getResources().getString(R.string.wait_print_keno), value);
                tv_count_wait_print.setText(waitPrint);
            }
        } catch (Exception exception) {

        }
    }


    private void findCurrentDraw() {
        //Log.e("TimeService.findCurrentDraw", DateTimeUtils.convertDateToString(TimerThread.timer,DateTimeUtils.DEFAULT_DATETIME_FORMAT));
        Date drawDateTime = null;

        DrawModel currentDrawModel = null;
        for (DrawModel drawModel : mDrawModels) {
            drawDateTime = DateTimeUtils.convertStringToDateDefault(drawModel.getDrawDate() + " " + drawModel.getDrawTime());

            if (DateTimeUtils.compareToDay(serverTime, drawDateTime) < 0
                    && DateTimeUtils.compareToDay(DateUtils.addMinutes(serverTime, 10), drawDateTime) >= 0) {
                currentDrawModel = drawModel;
                break;
            }
        }
        //Log.e("TimeService.drawDateTime", DateTimeUtils.convertDateToString(drawDateTime,DateTimeUtils.DEFAULT_DATETIME_FORMAT));
        //Log.e("TimeService.DrawModel", currentDrawModel.getDrawTime().toString());
        if (currentDrawModel != null) {
            int diffPrintSecond = Integer.parseInt(sharedPref.getString(Constants.KEY_DIFF_PRINT_SECOND, ""));
            assert drawDateTime != null;
            Date fromPrintTime = DateUtils.addSeconds(DateUtils.addMinutes(drawDateTime, -10), diffPrintSecond);
            Date toPrintTime = DateUtils.addSeconds(drawDateTime, -diffPrintSecond);
            String printTime = DateTimeUtils.convertDateToString(fromPrintTime, DateTimeUtils.SIMPLE_TIME_FORMAT) + " - " + DateTimeUtils.convertDateToString(toPrintTime, DateTimeUtils.SIMPLE_TIME_FORMAT);

            tv_draw.setText("#" + currentDrawModel.getDrawCode());
            tv_time.setText(printTime);

            long diff = drawDateTime.getTime() - serverTime.getTime();
            CountdownKeno(diff);
        } else {
            if (cdt != null)
                cdt.cancel();
            tv_draw.setText("#");
            tv_time.setText("Hết thời gian in vé");
        }
    }

    private void CountdownKeno(long diff) {
        cdt = new CountDownTimer(diff, 1000) {
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
                if (mPresenter != null)
                    mPresenter.getDateTimeNow();
            }
        };
        cdt.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cdt != null)
            cdt.cancel();
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
