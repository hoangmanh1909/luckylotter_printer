package com.mbl.lucklotterprinter.printer;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.request.SearchOrderRequest;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.DrawResponse;
import com.mbl.lucklotterprinter.model.response.SearchOrderResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;

import java.util.List;

public interface PrinterContract {
    interface Interactor extends IInteractor<PrinterContract.Presenter> {
        void getKenoDraw(String date, int productID, CommonCallback<DrawResponse> callback);

        void getOrder(SearchOrderRequest searchOrderRequest, CommonCallback<SearchOrderResponse> callback);
        void countOrderWattingPrint(int productID, int POSID, CommonCallback<BaseResponse> callback);
    }

    interface View extends PresentView<PrinterContract.Presenter> {
        void showOrder(List<OrderModel> orderModels);

        void showKenoDraw(List<DrawModel> drawModels);
        void showCountWaitPrint(int value);
    }

    interface Presenter extends IPresenter<PrinterContract.View, PrinterContract.Interactor> {
        void getOrder(int productID);
        void countOrderWattingPrint(int productID);
    }
}
