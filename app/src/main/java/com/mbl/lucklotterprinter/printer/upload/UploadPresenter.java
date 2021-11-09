package com.mbl.lucklotterprinter.printer.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.login.LoginActivity;
import com.mbl.lucklotterprinter.main.MainActivity;
import com.mbl.lucklotterprinter.main.SplashScreenActivity;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesImagesRequest;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.printer.PrinterActivity;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.blankj.utilcode.util.ActivityUtils.finishActivity;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class UploadPresenter extends Presenter<UploadContract.View, UploadContract.Interactor>
        implements UploadContract.Presenter {

    OrderModel orderModel;
    List<ItemModel> itemModelList;
    String printCode;

    public UploadPresenter(ContainerView containerView, OrderModel orderModel, List<ItemModel> itemModels, String printCode) {
        super(containerView);

        this.orderModel = orderModel;
        this.itemModelList = itemModels;
        this.printCode = printCode;
    }

    @Override
    public void start() {

    }

    @Override
    public UploadContract.Interactor onCreateInteractor() {
        return new UploadInteractor(this);
    }

    @Override
    public UploadContract.View onCreateView() {
        return UploadFragment.getInstance();
    }

    @Override
    public OrderModel getOrderModel() {
        return orderModel;
    }

    @Override
    public List<ItemModel> getItemModels() {
        return itemModelList;
    }

    @Override
    public void postImage(String filePath, String type) {
        mView.showProgress();
        CommonCallback<UploadResponse> callback = new CommonCallback<UploadResponse>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<UploadResponse> call, Response<UploadResponse> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showImage(response.body().getFileInfoModels().get(0).getName());
                }
            }

            @Override
            protected void onError(Call<UploadResponse> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };
        mInteractor.postImage(filePath, type, callback);
    }

    @Override
    public void changeToImage(String code) {
        mView.showProgress();
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showChangeToImage();
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };
        ChangeUpImageRequest request = new ChangeUpImageRequest();
        request.setCode(code);
        request.setPrintCode(printCode);
        mInteractor.changeToImage(request, callback);
    }

    @Override
    public void updateImage(OrderImagesRequest request) {
        mView.showProgress();
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    Toast.showToast(getViewContext(), "Cập nhật thành công");

                    new Handler().postDelayed(() -> {
                        back();
                        back();
                    }, 2000);
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };
        mInteractor.updateImage(request, callback);
    }

    @Override
    public void updateImageV1(OrderTablesImagesRequest request) {
        mView.showProgress();
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    Toast.showToast(getViewContext(), "Cập nhật thành công");

                    new Handler().postDelayed(() -> {
                        back();
                        back();
                    }, 2000);
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };
        mInteractor.updateImageV1(request, callback);
    }
}
