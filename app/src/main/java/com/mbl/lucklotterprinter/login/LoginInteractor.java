package com.mbl.lucklotterprinter.login;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.request.LoginRequest;
import com.mbl.lucklotterprinter.model.response.LoginResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

public class LoginInteractor extends Interactor<LoginContract.Presenter>
        implements LoginContract.Interactor {

    public LoginInteractor(LoginContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void login(LoginRequest loginRequest, CommonCallback<LoginResponse> callback) {
        NetWorkController.login(loginRequest, callback);
    }
}
