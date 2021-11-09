package com.mbl.lucklotterprinter.home;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.ParamsResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

public class HomeInteractor extends Interactor<HomeContract.Presenter>
        implements HomeContract.Interactor{

    public HomeInteractor(HomeContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getDateTimeNow(CommonCallback<BaseResponse> callback) {
        NetWorkController.getDateTimeNow(callback);
    }

    @Override
    public void getParams(CommonCallback<ParamsResponse> callback) {
        NetWorkController.getAllConfig(callback);
    }
}
