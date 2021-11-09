package com.mbl.lucklotterprinter.printer.detail.outOfNumber;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.OutOfNumberRequest;
import com.mbl.lucklotterprinter.network.CommonCallback;

import java.util.List;

public interface OutOfNumberContract {
    interface Interactor extends IInteractor<OutOfNumberContract.Presenter> {
        void outOfNumber(OutOfNumberRequest request, CommonCallback<SimpleResult> callback);
    }

    interface View extends PresentView<OutOfNumberContract.Presenter> {
    }

    interface Presenter extends IPresenter<OutOfNumberContract.View, OutOfNumberContract.Interactor> {
        List<LineModel> getLineModels();
        void outOfNumber(OutOfNumberRequest request);
        String getOrderCode();
    }
}
