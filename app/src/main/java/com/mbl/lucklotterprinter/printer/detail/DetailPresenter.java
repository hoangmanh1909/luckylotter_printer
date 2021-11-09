package com.mbl.lucklotterprinter.printer.detail;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.telephony.mbms.FileInfo;
import android.text.TextUtils;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.response.GetItemByCodeResponse;
import com.mbl.lucklotterprinter.model.response.PrintResponse;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Toast;
import com.mbl.lucklotterprinter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DetailPresenter extends Presenter<DetailContract.View, DetailContract.Interactor>
        implements DetailContract.Presenter {

    OrderModel orderModel;

    public DetailPresenter(ContainerView containerView, OrderModel orderModel) {
        super(containerView);

        this.orderModel = orderModel;
    }

    @Override
    public void getItemByCode() {
        mView.showProgress();
        mInteractor.getItemByCode(orderModel.getOrderCode(), new CommonCallback<GetItemByCodeResponse>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<GetItemByCodeResponse> call, Response<GetItemByCodeResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showItem(response.body().getItemModels(), response.body().getPrintCode());
                } else {
                    mView.showItem(new ArrayList<>(), "");
                }
            }

            @Override
            protected void onError(Call<GetItemByCodeResponse> call, String message) {
                super.onError(call, message);
                mView.showItem(new ArrayList<>(), "");
            }
        });
    }

    @Override
    public OrderModel getOrderModel() {
        return orderModel;
    }

    @Override
    public void print(List<ItemModel> models) {
        mView.showProgress();
        List<ItemModel> itemModels = new ArrayList<>();
        ItemModel item1 = models.get(0);

        if (item1.getItemType() == 2 && item1.getProductID() == Constants.PRODUCT_KENO) {
            for (ItemModel itemModel : models) {

                ItemModel item = new ItemModel();

                item.setSystemTypeA(itemModel.getSystemTypeA());
                item.setSystemTypeB(itemModel.getSystemTypeB());
                item.setSystemTypeC(itemModel.getSystemTypeC());
                item.setSystemTypeD(itemModel.getSystemTypeD());
                item.setSystemTypeE(itemModel.getSystemTypeE());
                item.setSystemTypeF(itemModel.getSystemTypeF());
                item.setProductID(itemModel.getProductID());
                item.setProductName(itemModel.getProductName());
                item.setSystemA(itemModel.getSystemA());
                item.setSystemB(itemModel.getSystemB());
                item.setSystemC(itemModel.getSystemC());
                item.setSystemD(itemModel.getSystemD());
                item.setSystemE(itemModel.getSystemE());
                item.setSystemF(itemModel.getSystemF());
                item.setStatus(itemModel.getStatus());
                item.setReasonReject(itemModel.getReasonReject());
                item.setReasonError(itemModel.getReasonError());
                item.setPrintCode(itemModel.getPrintCode());
                item.setPidNumber(itemModel.getPidNumber());
                item.setOrderItemID(itemModel.getOrderItemID());
                item.setMobileNumber(item.getMobileNumber());
                item.setFullName(itemModel.getFullName());
                item.setEmailAddress(itemModel.getEmailAddress());
                item.setDrawDate(itemModel.getDrawDate());
                item.setDrawCode(itemModel.getDrawCode());
                item.setImgBefore(itemModel.getImgBefore());
                item.setImgAfter(itemModel.getImgAfter());
                item.setPriceA(itemModel.getPriceA());
                item.setPriceB(itemModel.getPriceB());
                item.setPriceC(itemModel.getPriceC());
                item.setPriceD(itemModel.getPriceC());
                item.setPriceE(itemModel.getPriceE());
                item.setPriceF(itemModel.getPriceF());
                item.setPrice(itemModel.getPrice());
                item.setItemType(itemModel.getItemType());

                itemModel.setLineA(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineA())));
                if (!TextUtils.isEmpty(itemModel.getLineB()))
                    item.setLineB(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineB())));
                if (!TextUtils.isEmpty(itemModel.getLineC()))
                    item.setLineC(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineC())));
                if (!TextUtils.isEmpty(itemModel.getLineD()))
                    item.setLineD(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineD())));
                if (!TextUtils.isEmpty(itemModel.getLineE()))
                    item.setLineE(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineE())));
                if (!TextUtils.isEmpty(itemModel.getLineF()))
                    item.setLineF(Utils.getCodePrintKeno(Integer.parseInt(itemModel.getLineF())));
                itemModels.add(item);
            }
        } else {
            for (ItemModel itemModel : models) {
                ItemModel item = new ItemModel();

                item.setSystemTypeA(itemModel.getSystemTypeA());
                item.setSystemTypeB(itemModel.getSystemTypeB());
                item.setSystemTypeC(itemModel.getSystemTypeC());
                item.setSystemTypeD(itemModel.getSystemTypeD());
                item.setSystemTypeE(itemModel.getSystemTypeE());
                item.setSystemTypeF(itemModel.getSystemTypeF());
                item.setProductID(itemModel.getProductID());
                item.setProductName(itemModel.getProductName());
                item.setSystemA(itemModel.getSystemA());
                item.setSystemB(itemModel.getSystemB());
                item.setSystemC(itemModel.getSystemC());
                item.setSystemD(itemModel.getSystemD());
                item.setSystemE(itemModel.getSystemE());
                item.setSystemF(itemModel.getSystemF());
                item.setStatus(itemModel.getStatus());
                item.setReasonReject(itemModel.getReasonReject());
                item.setReasonError(itemModel.getReasonError());
                item.setPrintCode(itemModel.getPrintCode());
                item.setPidNumber(itemModel.getPidNumber());
                item.setOrderItemID(itemModel.getOrderItemID());
                item.setMobileNumber(item.getMobileNumber());
                item.setFullName(itemModel.getFullName());
                item.setEmailAddress(itemModel.getEmailAddress());
                item.setDrawDate(itemModel.getDrawDate());
                item.setDrawCode(itemModel.getDrawCode());
                item.setImgBefore(itemModel.getImgBefore());
                item.setImgAfter(itemModel.getImgAfter());
                item.setPriceA(itemModel.getPriceA());
                item.setPriceB(itemModel.getPriceB());
                item.setPriceC(itemModel.getPriceC());
                item.setPriceD(itemModel.getPriceC());
                item.setPriceE(itemModel.getPriceE());
                item.setPriceF(itemModel.getPriceF());
                item.setPrice(itemModel.getPrice());
                item.setItemType(itemModel.getItemType());

                if (!TextUtils.isEmpty(itemModel.getLineA()))
                    item.setLineA(itemModel.getLineA().replaceAll(",", "-"));
                if (!TextUtils.isEmpty(itemModel.getLineB()))
                    item.setLineB(itemModel.getLineB().replaceAll(",", "-"));
                if (!TextUtils.isEmpty(itemModel.getLineC()))
                    item.setLineC(itemModel.getLineC().replaceAll(",", "-"));
                if (!TextUtils.isEmpty(itemModel.getLineD()))
                    item.setLineD(itemModel.getLineD().replaceAll(",", "-"));
                if (!TextUtils.isEmpty(itemModel.getLineE()))
                    item.setLineE(itemModel.getLineE().replaceAll(",", "-"));
                if (!TextUtils.isEmpty(itemModel.getLineF()))
                    item.setLineF(itemModel.getLineF().replaceAll(",", "-"));

                itemModels.add(item);
            }
        }
        mInteractor.print(itemModels, new CommonCallback<PrintResponse>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<PrintResponse> call, Response<PrintResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showPrint(response.body().getPrintCommandModel());
                } else {
                    Toast.showToast(getViewContext(), response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<PrintResponse> call, String message) {
                super.onError(call, message);
            }
        });
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
    public void finishOrderKeno(FinishOrderKenoRequest request) {
        mView.showProgress();
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    Toast.showToast(getViewContext(), "Cập nhật thành công");
                    back();
                }
            }

            @Override
            protected void onError(Call<SimpleResult> call, String message) {
                super.onError(call, message);
                mView.hideProgress();
            }
        };
        mInteractor.finishOrderKeno(request, callback);
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
    public void changeToImage(String code, String printCode) {
        mView.showProgress();
        CommonCallback<SimpleResult> callback = new CommonCallback<SimpleResult>((Context) mContainerView) {
            @Override
            protected void onSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                super.onSuccess(call, response);
                mView.hideProgress();

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showSuccessImage();
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
    public void start() {
        getItemByCode();
    }

    @Override
    public DetailContract.Interactor onCreateInteractor() {
        return new DetailInteractor(this);
    }

    @Override
    public DetailContract.View onCreateView() {
        return DetailFragment.getInstance();
    }
}
