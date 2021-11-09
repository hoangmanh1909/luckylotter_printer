package com.mbl.lucklotterprinter.home;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.ParamsResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;

public interface HomeContract {
    interface Interactor extends IInteractor<HomeContract.Presenter> {
        void getDateTimeNow(CommonCallback<BaseResponse> callback);

        void getParams(CommonCallback<ParamsResponse> callback);
    }

    interface View extends PresentView<HomeContract.Presenter> {
    }

    interface Presenter extends IPresenter<HomeContract.View, HomeContract.Interactor> {

    }
}
