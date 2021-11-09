package com.mbl.lucklotterprinter.printer.detail.outOfNumber;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.OutOfNumberRequest;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

public class OutOfNumberInteractor  extends Interactor<OutOfNumberContract.Presenter>
        implements OutOfNumberContract.Interactor {

    public OutOfNumberInteractor(OutOfNumberContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void outOfNumber(OutOfNumberRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.outOfNumber(request, callback);
    }
}
