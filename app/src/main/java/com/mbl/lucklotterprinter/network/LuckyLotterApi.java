package com.mbl.lucklotterprinter.network;

import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.SimpleResult;
import com.mbl.lucklotterprinter.model.request.BaseRequest;
import com.mbl.lucklotterprinter.model.request.ChangeUpImageRequest;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
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

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LuckyLotterApi {
    @FormUrlEncoded
    @POST("api/UserProfile/Login")
    Call<LoginResponse> login(@Field("MobileNumber") String mobileNumber,
                              @Field("Password") String password,
                              @Field("DeviceCode") String deviceCode);

    @POST("api/Orders/GetOrderByPointOfSale")
    Call<SearchOrderResponse> searchOrder(@Body SearchOrderRequest searchOrderRequest);

    @GET("api/Orders/GetByOrderCodeV1")
    Call<GetItemByCodeResponse> getByOrderCode(@Query("OrderCode") String orderCode);

    @POST("api/Orders/Print")
    Call<PrintResponse> print(@Body List<ItemModel> itemModels);

    @Multipart
    @POST("api/Handle/UploadImage")
    Call<UploadResponse> postImage(@Part MultipartBody.Part image,
                                   @Part("Type") RequestBody type);

    @POST("api/Orders/UpdateKenoImageV1")
    Call<SimpleResult> finishOrderKeno(@Body FinishOrderKenoRequest request);

    @POST("api/Orders/UpdateImage")
    Call<SimpleResult> updateImage(@Body OrderImagesRequest request);

    //Dung cho mega/power khi mua theo bảng
    @POST("api/Orders/UpdateImageV1")
    Call<SimpleResult> updateImageV1(@Body OrderTablesImagesRequest request);

    @POST("api/Orders/ChangeToUpImage")
    Call<SimpleResult> changeToUpImage(@Body ChangeUpImageRequest request);

    @POST("api/Orders/OutOfNumber")
    Call<SimpleResult> outOfNumber(@Body OutOfNumberRequest overTicket);

    @GET("api/Result/GetDateTimeNow")
    Call<BaseResponse> getDateTimeNow();

    @GET("api/Result/GetResult")
    Call<DrawResponse> getResult(@Query("date") String date,
                                 @Query("ProductID") int product);
    @GET("api/Dictionary/GetAllConfig")
    Call<ParamsResponse> getAllConfig();

}
