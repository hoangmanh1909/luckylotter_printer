package com.mbl.lucklotterprinter.printer;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.request.SearchOrderRequest;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.DrawResponse;
import com.mbl.lucklotterprinter.model.response.SearchOrderResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

public class PrinterInteractor  extends Interactor<PrinterContract.Presenter>
        implements PrinterContract.Interactor {

    public PrinterInteractor(PrinterContract.Presenter presenter) {
        super(presenter);
    }


    @Override
    public void getKenoDraw(String date, int productID, CommonCallback<DrawResponse> callback) {
        NetWorkController.getResult(date, productID, callback);
    }

    @Override
    public void getOrder(SearchOrderRequest searchOrderRequest, CommonCallback<SearchOrderResponse> callback) {
        NetWorkController.searchOrder(searchOrderRequest, callback);
    }

    @Override
    public void countOrderWattingPrint(int productID, int POSID, CommonCallback<BaseResponse> callback) {
        NetWorkController.countOrderWattingPrint(productID, POSID, callback);
    }
}
