package com.mbl.lucklotterprinter.printer.upload;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesImagesRequest;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;

import java.util.List;

public interface UploadContract {
    interface Interactor extends IInteractor<UploadContract.Presenter> {
        void postImage(String filePath, String type, CommonCallback<UploadResponse> callback);

        void changeToImage(ChangeUpImageRequest request, CommonCallback<SimpleResult> callback);

        void updateImage(OrderImagesRequest request, CommonCallback<SimpleResult> callback);

        void updateImageV1(OrderTablesImagesRequest request, CommonCallback<SimpleResult> callback);
    }

    interface View extends PresentView<UploadContract.Presenter> {
        void showImage(String file);

        void showChangeToImage();
    }

    interface Presenter extends IPresenter<UploadContract.View, UploadContract.Interactor> {
        OrderModel getOrderModel();

        List<ItemModel> getItemModels();

        void postImage(String filePath, String type);

        void changeToImage(String code);

        void updateImage(OrderImagesRequest request);

        void updateImageV1(OrderTablesImagesRequest request);
    }
}
