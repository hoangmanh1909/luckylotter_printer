package com.mbl.lucklotterprinter.printer.detail;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.PrintCommandModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.SearchOrderRequest;
import com.mbl.lucklotterprinter.model.response.GetItemByCodeResponse;
import com.mbl.lucklotterprinter.model.response.PrintResponse;
import com.mbl.lucklotterprinter.model.response.SearchOrderResponse;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;

import java.util.List;

public interface DetailContract {
    interface Interactor extends IInteractor<DetailContract.Presenter> {
        void getItemByCode(String code, CommonCallback<GetItemByCodeResponse> callback);

        void print(List<ItemModel> itemModels, CommonCallback<PrintResponse> callback);

        void postImage(String filePath, String type, CommonCallback<UploadResponse> callback);

        void finishOrderKeno(FinishOrderKenoRequest request, CommonCallback<SimpleResult> callback);

        void updateImage(OrderImagesRequest request, CommonCallback<SimpleResult> callback);

        void changeToImage(ChangeUpImageRequest request, CommonCallback<SimpleResult> callback);
    }

    interface View extends PresentView<DetailContract.Presenter> {
        void showItem(List<ItemModel> orderModels, String printCode);

        void showPrint(PrintCommandModel printCommandModel);

        void showImage(String file);

        void showSuccessImage();
    }

    interface Presenter extends IPresenter<DetailContract.View, DetailContract.Interactor> {
        void getItemByCode();

        OrderModel getOrderModel();
        List<DrawModel> getDrawModels();

        void print(List<ItemModel> models);

        void postImage(String filePath, String type);

        void finishOrderKeno(FinishOrderKenoRequest request);

        void updateImage(OrderImagesRequest request);

        void changeToImage(String code, String printCode);
    }
}
