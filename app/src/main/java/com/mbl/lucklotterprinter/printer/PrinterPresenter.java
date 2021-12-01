package com.mbl.lucklotterprinter.printer;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;
import com.google.gson.reflect.TypeToken;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.request.SearchOrderRequest;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.DrawResponse;
import com.mbl.lucklotterprinter.model.response.SearchOrderResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;
import com.mbl.lucklotterprinter.service.TimeService;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DateTimeUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PrinterPresenter extends Presenter<PrinterContract.View, PrinterContract.Interactor>
        implements PrinterContract.Presenter {

    SharedPref sharedPref;
    EmployeeModel employeeModel;
    int mProductID;

    public PrinterPresenter(ContainerView containerView) {
        super(containerView);

        Intent intent = ((Activity) mContainerView).getIntent();
        sharedPref = new SharedPref((Activity) mContainerView);

        employeeModel = sharedPref.getEmployeeModel();
        mProductID = intent.getIntExtra(Constants.PRODUCT_ID, 0);
    }

    @Override
    public void getOrder(int productID) {
        mView.showProgress();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        Date date = new Date();

        SearchOrderRequest searchOrderRequest = new SearchOrderRequest();
        searchOrderRequest.setPointOfSaleID(employeeModel.getPointOfSaleID());
        searchOrderRequest.setProductID(productID);
        searchOrderRequest.setFromDate(formatter.format(yesterday));
        searchOrderRequest.setToDate(formatter.format(date));

        mInteractor.getOrder(searchOrderRequest,new CommonCallback<SearchOrderResponse>((Activity)mContainerView){
            @Override
            protected void onSuccess(Call<SearchOrderResponse> call, Response<SearchOrderResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    mView.showOrder(response.body().getOrderModels());
                } else {
                    mView.showOrder(new ArrayList<>());
                }
            }

            @Override
            protected void onError(Call<SearchOrderResponse> call, String message) {
                super.onError(call, message);
                mView.showOrder(new ArrayList<>());
            }
        });
    }

    @Override
    public void countOrderWattingPrint(int productID) {
        mInteractor.countOrderWattingPrint(productID,employeeModel.getPointOfSaleID(),new CommonCallback<BaseResponse>((Activity)mContainerView){
            @Override
            protected void onSuccess(Call<BaseResponse> call, Response<BaseResponse> response) {
                super.onSuccess(call, response);

                if ("00".equals(response.body().getErrorCode())) {
                    int countWait = (int)Float.parseFloat(response.body().getValue().toString());
                    Log.e("TimeService", DateTimeUtils.convertDateToString(TimeService.dateTimer,DateTimeUtils.DEFAULT_DATETIME_FORMAT));
                    Log.e("TimeKEY_DATE_TIME_NOW", sharedPref.getString(Constants.KEY_DATE_TIME_NOW,""));
                    mView.showCountWaitPrint(countWait);
                }
            }

            @Override
            protected void onError(Call<BaseResponse> call, String message) {
                super.onError(call, message);
            }
        });
    }

    @Override
    public void start() {
        if(mProductID == Constants.PRODUCT_KENO){
            String currentDate = DateTimeUtils.getCurrentDateString();
            mInteractor.getKenoDraw(currentDate,Constants.PRODUCT_KENO, new CommonCallback<DrawResponse>((Activity)mContainerView){
                @Override
                protected void onSuccess(Call<DrawResponse> call, Response<DrawResponse> response) {
                    super.onSuccess(call, response);

                    if ("00".equals(response.body().getErrorCode())) {
                        mView.showKenoDraw(response.body().getDrawModels());
                    } else {
                        mView.showOrder(new ArrayList<>());
                    }
                }

                @Override
                protected void onError(Call<DrawResponse> call, String message) {
                    super.onError(call, message);
                }
            });
        }
    }

    @Override
    public PrinterContract.Interactor onCreateInteractor() {
        return new PrinterInteractor(this);
    }

    @Override
    public PrinterContract.View onCreateView() {
        return PrinterFragment.getInstance();
    }
}
