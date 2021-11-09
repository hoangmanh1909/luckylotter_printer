package com.mbl.lucklotterprinter.login;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.request.LoginRequest;
import com.mbl.lucklotterprinter.model.response.LoginResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;

public interface LoginContract {
    interface Interactor extends IInteractor<Presenter> {
        void login(LoginRequest loginRequest, CommonCallback<LoginResponse> callback);
    }

    interface View extends PresentView<Presenter> {
        void goHome();
    }

    interface Presenter extends IPresenter<View, Interactor> {
        void login(String phone, String passWord);
    }
}
