package com.mbl.lucklotterprinter.login;

import android.app.Activity;
import android.content.Context;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.request.LoginRequest;
import com.mbl.lucklotterprinter.model.response.LoginResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.utils.SharedPref;

import retrofit2.Call;
import retrofit2.Response;

public class LoginPresenter extends Presenter<LoginContract.View, LoginContract.Interactor>
        implements LoginContract.Presenter {

    public LoginPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {

    }

    @Override
    public LoginContract.Interactor onCreateInteractor() {
        return new LoginInteractor(this);
    }

    @Override
    public LoginContract.View onCreateView() {
        return LoginFragment.getInstance();
    }

    @Override
    public void login(String phone, String passWord) {
        mView.showProgress();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobileNumber(phone);
        loginRequest.setPassword(passWord);
        mInteractor.login(loginRequest,new CommonCallback<LoginResponse>((Activity)mContainerView){
            @Override
            protected void onSuccess(Call<LoginResponse> call, Response<LoginResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())){
                    SharedPref sharedPref = new SharedPref((Context) mContainerView);
                    sharedPref.saveEmployee(response.body().getEmployeeModel());
                    mView.goHome();
                }
                else {
                    mView.showAlertDialog(response.body().getMessage());
                }
            }

            @Override
            protected void onError(Call<LoginResponse> call, String message) {
                super.onError(call, message);
            }
        });
    }
}
