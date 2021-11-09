package com.mbl.lucklotterprinter.printer.detail.outOfNumber;

import android.content.Context;
import android.os.Handler;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.OutOfNumberRequest;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.utils.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OutOfNumberPresenter  extends Presenter<OutOfNumberContract.View, OutOfNumberContract.Interactor>
        implements OutOfNumberContract.Presenter {

    List<LineModel> mLineModel;
    String mOrderCode;
    public OutOfNumberPresenter(ContainerView containerView, List<LineModel> lineModels,String orderCode) {
        super(containerView);

        this.mLineModel = lineModels;
        this.mOrderCode = orderCode;
    }

    @Override
    public void start() {

    }

    @Override
    public OutOfNumberContract.Interactor onCreateInteractor() {
        return new OutOfNumberInteractor(this);
    }

    @Override
    public OutOfNumberContract.View onCreateView() {
        return OutOfNumberFragment.getInstance();
    }

    @Override
    public List<LineModel> getLineModels() {
        return mLineModel;
    }

    @Override
    public void outOfNumber(OutOfNumberRequest request) {
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
        mInteractor.outOfNumber(request, callback);
    }

    @Override
    public String getOrderCode() {
        return mOrderCode;
    }
}
