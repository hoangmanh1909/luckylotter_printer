package com.mbl.lucklotterprinter.home;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.viper.ViewFragment;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.HomeModel;
import com.mbl.lucklotterprinter.printer.PrinterActivity;
import com.mbl.lucklotterprinter.service.BluetoothServices;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends ViewFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    HomeAdapter adapter;
    List<HomeModel> homeModels = new ArrayList<>();

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        this.itemHome();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new HomeAdapter(getContext(), homeModels) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.itemView.setOnClickListener(v -> {
                    if (BluetoothServices.mState == BluetoothServices.STATE_CONNECTED) {
                        HomeModel homeModel = homeModels.get(position);
                        Intent intent = new Intent(getActivity(), PrinterActivity.class);
                        intent.putExtra(Constants.PRODUCT_ID, homeModel.getProductID());
                        startActivity(intent);
                    } else {
                        if (BluetoothServices.mState == BluetoothServices.STATE_CONNECTING)
                            Toast.showToast(getContext(), "Đang tiến hành kết nối Bluetooth");
                        if (BluetoothServices.mState == BluetoothServices.STATE_FAIL)
                            Toast.showToast(getContext(), "Kết nối Bluetooth thất bại");
                        else
                            Toast.showToast(getContext(), "Không có kết nối Bluetooth");
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private List<HomeModel> itemHome() {
        Resources res = getResources();


        HomeModel homeModel = new HomeModel();

        homeModel.setLogo(R.drawable.home_keno);
        homeModel.setProductID(Constants.PRODUCT_KENO);
        homeModel.setTitle(res.getString(R.string.printer_keno));
        homeModels.add(homeModel);

        homeModel = new HomeModel();
        homeModel.setLogo(R.drawable.logo_lucky_lotter);
        homeModel.setProductID(Constants.PRODUCT_NORMAL);
        homeModel.setTitle(res.getString(R.string.printer_normal));
        homeModels.add(homeModel);

//        homeModel = new HomeModel();
//        homeModel.setLogo(R.drawable.home_power);
//        homeModel.setProductID(Constants.PRODUCT_POWER);
//        homeModel.setTitle(res.getString(R.string.printer_power));
//        homeModels.add(homeModel);
//
//        homeModel = new HomeModel();
//        homeModel.setLogo(R.drawable.logomax3dpro);
//        homeModel.setProductID(Constants.PRODUCT_MAX3D_PRO);
//        homeModel.setTitle(res.getString(R.string.printer_max3d_pro));
//        homeModels.add(homeModel);
//
//        homeModel = new HomeModel();
//        homeModel.setLogo(R.drawable.home_max3d);
//        homeModel.setProductID(Constants.PRODUCT_MAX3D);
//        homeModel.setTitle(res.getString(R.string.printer_max3d));
//        homeModels.add(homeModel);
//
//        homeModel = new HomeModel();
//        homeModel.setLogo(R.drawable.home_max3d);
//        homeModel.setProductID(Constants.PRODUCT_MAX3D);
//        homeModel.setTitle(res.getString(R.string.printer_max3d_plus));
//        homeModels.add(homeModel);

        return homeModels;
    }
}
