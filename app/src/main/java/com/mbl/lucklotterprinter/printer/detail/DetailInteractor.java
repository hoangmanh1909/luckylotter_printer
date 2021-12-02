package com.mbl.lucklotterprinter.printer.detail;

import com.core.base.viper.Interactor;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.GetItemByCodeResponse;
import com.mbl.lucklotterprinter.model.response.PrintResponse;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.network.CommonCallback;
import com.mbl.lucklotterprinter.network.NetWorkController;

import java.util.List;

public class DetailInteractor extends Interactor<DetailContract.Presenter>
        implements DetailContract.Interactor {

    public DetailInteractor(DetailContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getDateTimeNow(CommonCallback<BaseResponse> callback) {
        NetWorkController.getDateTimeNow(callback);
    }

    @Override
    public void getItemByCode(String code, CommonCallback<GetItemByCodeResponse> callback) {
        NetWorkController.getItemByOrderCode(code, callback);
    }

    @Override
    public void print(List<ItemModel> itemModels, CommonCallback<PrintResponse> callback) {
        NetWorkController.print(itemModels, callback);
    }

    @Override
    public void postImage(String filePath, String type, CommonCallback<UploadResponse> callback) {
        NetWorkController.postImage(filePath, type, callback);
    }

    @Override
    public void finishOrderKeno(FinishOrderKenoRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.finsishOrderKeno(request, callback);
    }

    @Override
    public void updateImage(OrderImagesRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.updateImage(request, callback);
    }

    @Override
    public void changeToImage(ChangeUpImageRequest request, CommonCallback<SimpleResult> callback) {
        NetWorkController.changeToImage(request, callback);
    }
}
