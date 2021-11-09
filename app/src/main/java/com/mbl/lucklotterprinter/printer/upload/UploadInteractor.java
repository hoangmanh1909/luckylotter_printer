package com.mbl.lucklotterprinter.printer.upload;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesImagesRequest;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

public class UploadInteractor extends Interactor<UploadContract.Presenter>
        implements UploadContract.Interactor {

    public UploadInteractor(UploadContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void postImage(String filePath, String type, CommonCallback<UploadResponse> callback) {
        NetWorkController.postImage(filePath, type, callback);
    }

    @Override
    public void updateImage(OrderImagesRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.updateImage(request, callback);
    }

    @Override
    public void updateImageV1(OrderTablesImagesRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.updateImageV1(request, callback);
    }

    @Override
    public void changeToImage(ChangeUpImageRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.changeToImage(request, callback);
    }
}
