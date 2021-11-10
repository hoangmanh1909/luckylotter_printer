package com.mbl.lucklotterprinter.home;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.model.ParamsModel;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.ParamsResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.service.BluetoothServices;
import com.mbl.lucklotterprinter.service.TimeService;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.SharedPref;

import java.sql.Time;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomePresenter extends Presenter<HomeContract.View, HomeContract.Interactor>
        implements HomeContract.Presenter {
    SharedPref sharedPref;
    Activity activity;

    public HomePresenter(ContainerView containerView) {
        super(containerView);

    }

    @Override
    public void start() {
        activity = (Activity) mContainerView;
        sharedPref = new SharedPref(activity);

        getParams();
        getDateTimeNow();
    }

    private void getDateTimeNow(){
        mView.showProgress();
        mInteractor.getDateTimeNow(new CommonCallback<BaseResponse>(activity) {
            @Override
            protected void onSuccess(Call<BaseResponse> call, Response<BaseResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    sharedPref.putString(Constants.KEY_DATE_TIME_NOW, String.valueOf(response.body().getValue()));

                    TimeService timeService = new TimeService();
                    Intent intent = activity.getIntent();
                    intent.putExtra(Constants.KEY_DATE_TIME_NOW,"");
                    timeService.onStartCommand(intent, 0, 0);
//                    Intent intent = new Intent(getViewContext(), TimeService.class);
//                    activity.startActivity(intent);
                }
            }

            @Override
            protected void onError(Call<BaseResponse> call, String message) {
                super.onError(call, message);
            }
        });
    }

    private void getParams() {
        mView.showProgress();
        mInteractor.getParams(new CommonCallback<ParamsResponse>((Activity) mContainerView) {
            @Override
            protected void onSuccess(Call<ParamsResponse> call, Response<ParamsResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    List<ParamsModel> paramsResponseList = response.body().getParamsModels();
                    for (ParamsModel paramsModel : paramsResponseList) {
                        if (paramsModel.getParameter().equals("DIFF_PRINT_SECOND")) {
                            sharedPref.putString(Constants.KEY_DIFF_PRINT_SECOND, paramsModel.getValue());
                        }
                    }
                }
            }

            @Override
            protected void onError(Call<ParamsResponse> call, String message) {
                super.onError(call, message);
            }
        });
    }

    @Override
    public HomeContract.Interactor onCreateInteractor() {
        return new HomeInteractor(this);
    }

    @Override
    public HomeContract.View onCreateView() {
        return HomeFragment.getInstance();
    }
}
