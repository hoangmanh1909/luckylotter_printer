package com.mbl.lucklotterprinter.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mbl.lucklotterprinter.BuildConfig;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.LoginRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesImagesRequest;
import com.mbl.lucklotterprinter.model.request.OutOfNumberRequest;
import com.mbl.lucklotterprinter.model.request.SearchOrderRequest;
import com.mbl.lucklotterprinter.model.response.BaseResponse;
import com.mbl.lucklotterprinter.model.response.DrawResponse;
import com.mbl.lucklotterprinter.model.response.GetItemByCodeResponse;
import com.mbl.lucklotterprinter.model.response.LoginResponse;
import com.mbl.lucklotterprinter.model.response.ParamsResponse;
import com.mbl.lucklotterprinter.model.response.PrintResponse;
import com.mbl.lucklotterprinter.model.response.SearchOrderResponse;
import com.mbl.lucklotterprinter.model.response.UploadResponse;
import com.mbl.lucklotterprinter.utils.Utils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkController {
    private NetWorkController() {
    }

    private static volatile LuckyLotterApi apiBuilder;


    public static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    private static LuckyLotterApi getAPIBuilder() {
        if (apiBuilder == null) {
            // mPrivateKeySignature = Constants.PRIVATE_KEY;
            // mApiKey = Constants.API_KEY;
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(Utils.getUnsafeOkHttpClient(120, 120, BuildConfig.API_KEY))
                    .build();
            apiBuilder = retrofit.create(LuckyLotterApi.class);
        }
        return apiBuilder;
    }

    public static void login(LoginRequest request, CommonCallback<LoginResponse> callback) {
        Call<LoginResponse> call = getAPIBuilder().login(request.getMobileNumber(), request.getPassword(), request.getDeviceCode());
        call.enqueue(callback);
    }

    public static void searchOrder(SearchOrderRequest searchOrderRequest, CommonCallback<SearchOrderResponse> callback) {
        Call<SearchOrderResponse> call = getAPIBuilder().searchOrder(searchOrderRequest);
        call.enqueue(callback);
    }

    public static void getItemByOrderCode(String orderCode, CommonCallback<GetItemByCodeResponse> callback) {
        Call<GetItemByCodeResponse> call = getAPIBuilder().getByOrderCode(orderCode);
        call.enqueue(callback);
    }

    public static void print(List<ItemModel> request, CommonCallback<PrintResponse> callback) {
        Call<PrintResponse> call = getAPIBuilder().print(request);
        call.enqueue(callback);
    }

    public static void postImage(String filePath, String type, CommonCallback<UploadResponse> callback) {
        File file = new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("ticket", "file_ticket.jpg", reqFile);
        //MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
        RequestBody typeBody = RequestBody.create(MediaType.parse("text/plain"), type);
        Call<UploadResponse> call = getAPIBuilder().postImage(body, typeBody);
        call.enqueue(callback);
    }

    public static void finsishOrderKeno(FinishOrderKenoRequest request, CommonCallback<SimpleResult> callback) {
        Call<SimpleResult> call = getAPIBuilder().finishOrderKeno(request);
        call.enqueue(callback);
    }

    public static void updateImage(OrderImagesRequest request, CommonCallback<SimpleResult> callback) {
        Call<SimpleResult> call = getAPIBuilder().updateImage(request);
        call.enqueue(callback);
    }

    public static void updateImageV1(OrderTablesImagesRequest request, CommonCallback<SimpleResult> callback) {
        Call<SimpleResult> call = getAPIBuilder().updateImageV1(request);
        call.enqueue(callback);
    }

    public static void changeToImage(ChangeUpImageRequest request, CommonCallback<SimpleResult> callback) {
        Call<SimpleResult> call = getAPIBuilder().changeToUpImage(request);
        call.enqueue(callback);
    }

    public static void outOfNumber(OutOfNumberRequest overTicket, CommonCallback<SimpleResult> callback) {
        Call<SimpleResult> call = getAPIBuilder().outOfNumber(overTicket);
        call.enqueue(callback);
    }

    public static void getDateTimeNow(CommonCallback<BaseResponse> callback) {
        Call<BaseResponse> call = getAPIBuilder().getDateTimeNow();
        call.enqueue(callback);
    }

    public static void getResult(String date, int productID, CommonCallback<DrawResponse> callback) {
        Call<DrawResponse> call = getAPIBuilder().getResult(date, productID);
        call.enqueue(callback);
    }

    public static void getAllConfig(CommonCallback<ParamsResponse> callback) {
        Call<ParamsResponse> call = getAPIBuilder().getAllConfig();
        call.enqueue(callback);
    }

    public static void countOrderWattingPrint(int productID, int POSID, CommonCallback<BaseResponse> callback) {
        Call<BaseResponse> call = getAPIBuilder().countOrderWattingPrint(productID, POSID);
        call.enqueue(callback);
    }
}
