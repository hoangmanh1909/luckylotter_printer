package com.mbl.lucklotterprinter.printer.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.ImageModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesImagesRequest;
import com.mbl.lucklotterprinter.model.request.OrderTablesItemImages;
import com.mbl.lucklotterprinter.printer.detail.DetailFragment;
import com.mbl.lucklotterprinter.utils.BitmapUtils;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DialogHelper;
import com.mbl.lucklotterprinter.utils.DrawViewUtils;
import com.mbl.lucklotterprinter.utils.MediaUltis;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Toast;
import com.mbl.lucklotterprinter.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UploadFragment extends ViewFragment<UploadContract.Presenter> implements UploadContract.View {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_fullName)
    TextView tv_fullName;
    @BindView(R.id.tv_pid_number)
    TextView tv_pid_number;
    @BindView(R.id.btn_print)
    Button btn_print;
    @BindView(R.id.recycle)
    RecyclerView recycle;

    UploadAdapter adapter;
    String ticketType = Constants.TICKET_NO_AMOUNT;

    OrderModel mOrderModel = new OrderModel();
    List<ItemModel> itemModelList = new ArrayList<>();
    boolean IsBefore = true;
    List<LineModel> mLineModels = new ArrayList<>();
    ItemModel mItem;
    int mPosition;
    int mCountImage;
    EmployeeModel employeeModel;
    String systematic = "";

    public static UploadFragment getInstance() {
        return new UploadFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_upload;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        mCountImage = 0;
        SharedPref sharedPref = new SharedPref(requireActivity());
        employeeModel = sharedPref.getEmployeeModel();

        if (mPresenter != null) {
            mOrderModel = mPresenter.getOrderModel();
            itemModelList = mPresenter.getItemModels();

            tv_title.setText("#" + mOrderModel.getOrderCode());
            tv_fullName.setText(mOrderModel.getFullName());
            tv_pid_number.setText(mOrderModel.getpIDNumber());

            switch (mOrderModel.getProductID()) {
                case Constants.PRODUCT_MEGA:
                case Constants.PRODUCT_POWER:
                    ticketType = Constants.TICKET_NO_AMOUNT;
                    break;
                case Constants.PRODUCT_MAX4D:
                case Constants.PRODUCT_MAX3D:
                case Constants.PRODUCT_MAX3D_PLUS:
                case Constants.PRODUCT_MAX3D_PRO:
                case Constants.PRODUCT_KENO:
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
            }

            recycle.setLayoutManager(new FlexboxLayoutManager(requireContext()));
            adapter = new UploadAdapter(requireContext(), itemModelList) {
                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);

                    ((HolderView) holder).image_before.setOnClickListener(v -> {
                        IsBefore = true;
                        capturePermission(itemModelList.get(position));
                        mItem = itemModelList.get(position);
                        mPosition = position;
                    });

                    ((HolderView) holder).image_after.setOnClickListener(v -> {
                        IsBefore = false;
                        capturePermission(itemModelList.get(position));
                        mItem = itemModelList.get(position);
                        mPosition = position;
                    });
                }
            };
            recycle.setAdapter(adapter);

            mItem = itemModelList.get(0);
            mPosition = 0;

            if (mItem.getProductID() == Constants.PRODUCT_KENO) {
                systematic = " - Bậc " + mItem.getSystemA();
            } else if (mItem.getProductID() == Constants.PRODUCT_MAX3D_PRO) {
                if (mItem.getItemType() == 2)
                    systematic = " - Bao bộ số";
                else if (mItem.getItemType() > 2) {
                    systematic = " - Bao " + mItem.getItemType();
                }
            } else {
                if (mItem.getSystemA() > 6)
                    systematic = " - Bao " + mItem.getSystemA();
                if (mItem.getSystemA() == 5)
                    systematic = " - Bao 5";
            }

            capturePermission(itemModelList.get(0));
        }
    }

    @OnClick({R.id.btn_print, R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                ok();
                break;
            case R.id.iv_back:
                mPresenter.back();
                break;
        }
    }

    void ok() {
        boolean IsCheckImage = true;
        if (mOrderModel.getProductID() == Constants.PRODUCT_MAX3D) {
            for (ItemModel itemModel : itemModelList) {
                if (TextUtils.isEmpty(itemModel.getImgBefore())) {
                    IsCheckImage = false;
                    break;
                }
            }
        } else {
            for (ItemModel itemModel : itemModelList) {
                if (TextUtils.isEmpty(itemModel.getImgBefore()) || TextUtils.isEmpty(itemModel.getImgAfter())) {
                    IsCheckImage = false;
                    break;
                }
            }
        }
        if (IsCheckImage) {
            mPresenter.changeToImage(mOrderModel.getOrderCode());
        } else {
            Toast.showToast(getViewContext(), "Bạn chưa cập nhật đủ ảnh!");
        }
    }

    private List<LineModel> getLineModel(ItemModel item) {
        LineModel lineModel = new LineModel();
        mLineModels = new ArrayList<>();
        if (!TextUtils.isEmpty(item.getLineA())) {
            lineModel.setAmount(item.getPriceA());
            lineModel.setTitle("A");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineA())));
            else
                lineModel.setLine(item.getLineA());
            lineModel.setType(ticketType);
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeA());

            if (item.getItemType() != null) {
                lineModel.setItemType(item.getItemType());
            }
            mLineModels.add(lineModel);
        }

        if (!TextUtils.isEmpty(item.getLineB())) {
            lineModel = new LineModel();
            lineModel.setAmount(item.getPriceB());
            lineModel.setTitle("B");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineB())));
            else
                lineModel.setLine(item.getLineB());
            lineModel.setType(ticketType);
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeB());
            if (item.getItemType() != null) {
                lineModel.setItemType(item.getItemType());
            }
            mLineModels.add(lineModel);
        }

        if (!TextUtils.isEmpty(item.getLineC())) {
            lineModel = new LineModel();
            lineModel.setAmount(item.getPriceC());
            lineModel.setTitle("C");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineC())));
            else
                lineModel.setLine(item.getLineC());
            lineModel.setType(ticketType);
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeC());

            if (item.getItemType() != null) {
                lineModel.setItemType(item.getItemType());
            }

            mLineModels.add(lineModel);
        }

        if (!TextUtils.isEmpty(item.getLineD())) {
            lineModel = new LineModel();
            lineModel.setAmount(item.getPriceD());
            lineModel.setTitle("D");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineD())));
            else
                lineModel.setLine(item.getLineD());
            lineModel.setType(ticketType);
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setItemType(item.getItemType());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeD());
            mLineModels.add(lineModel);
        }

        if (!TextUtils.isEmpty(item.getLineE())) {
            lineModel = new LineModel();
            lineModel.setAmount(item.getPriceE());
            lineModel.setTitle("E");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineE())));
            else
                lineModel.setLine(item.getLineE());
            lineModel.setType(ticketType);
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeE());
            if (item.getItemType() != null)
                lineModel.setItemType(item.getItemType());
            mLineModels.add(lineModel);
        }

        if (!TextUtils.isEmpty(item.getLineF())) {
            lineModel = new LineModel();
            lineModel.setAmount(item.getPriceF());
            lineModel.setTitle("F");
            lineModel.setProductID(item.getProductID());
            if (item.getItemType() == 2 && item.getProductID() == Constants.PRODUCT_KENO)
                lineModel.setLine(Utils.getKenoPlus(Integer.parseInt(item.getLineF())));
            else
                lineModel.setLine(item.getLineF());
            lineModel.setDrawCode(item.getDrawCode());
            lineModel.setDrawDate(item.getDrawDate());
            lineModel.setType(ticketType);
            lineModel.setId(item.getOrderItemID());
            lineModel.setSystematic(item.getSystemTypeF());
            if (item.getItemType() != null)
                lineModel.setItemType(item.getItemType());

            mLineModels.add(lineModel);
        }
        return mLineModels;
    }

    private void capturePermission(ItemModel itemModel) {
        CharSequence info;
        boolean IsAmount = ticketType.equals(Constants.TICKET_SHOW_AMOUNT);
        CharSequence infoAfter = Utils.getInfoImageAfter(requireActivity(), mOrderModel.getFullName(), mOrderModel.getpIDNumber(), mOrderModel.getMobileNumber());
        List<LineModel> mLineModels = getLineModel(itemModel);
        if (IsBefore) {
            String draw = "Kỳ #" + itemModel.getDrawCode() + ", Ngày quay:" + itemModel.getDrawDate();
            info = Utils.getInfoImageBefore(requireActivity(), mLineModels, Utils.getProductName(mOrderModel.getProductID()) + systematic, draw, IsAmount);
        } else {
            info = infoAfter;
        }

        PermissionUtils.permission(PermissionConstants.CAMERA)
                .rationale(DialogHelper::showRationaleDialog)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        MediaUltis.captureImage(UploadFragment.this, info, "#" + mOrderModel.getOrderCode(), !IsBefore);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                    }
                })
                .request();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                attemptSendMedia(data.getData().getPath());
            }
        }
    }

    @SuppressLint("CheckResult")
    private void attemptSendMedia(String path_media) {
        File file = new File(path_media);
        Uri picUri = Uri.fromFile(new File(path_media));

        Observable.fromCallable(() -> {
            Uri uri = Uri.fromFile(new File(path_media));
//            return BitmapUtils.processingBitmap(uri, getViewContext());
            DrawViewUtils drawViewUtils = new DrawViewUtils(getContext());
            if (IsBefore) {
                return drawViewUtils.processingBitmapBefore(uri,
                        Utils.getProductName(mItem.getProductID()) + systematic,
                        mLineModels,
                        mItem.getDrawDate(),
                        mItem.getDrawCode(),
                        mOrderModel.getOrderCode());
            } else {
                return drawViewUtils.processingBitmapAfter(uri, mOrderModel.getFullName(), mOrderModel.getpIDNumber(), mOrderModel.getMobileNumber());
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map(bitmap ->
                        BitmapUtils.saveImage(bitmap, file.getParent(), "lkl" + file.getName(), Bitmap.CompressFormat.JPEG, 50)
                )
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                isSavedImage -> {
                    String path = file.getParent() + File.separator + "lkl" + file.getName();
                    String type;
                    if (IsBefore)
                        type = Constants.IMAGE_BEFORE;
                    else
                        type = Constants.IMAGE_AFTER;
                    if (IsBefore) {
                        ((ItemModel) adapter.getItem(mPosition)).setImgBeforeLocal(path);
                    } else
                        ((ItemModel) adapter.getItem(mPosition)).setImgAfterLocal(path);
                    adapter.notifyDataSetChanged();

                    mPresenter.postImage(path, type);
                    if (file.exists())
                        file.delete();
                },
                onError -> Logger.e("error save image")
        );
    }

    @Override
    public void showImage(String file) {
        if (IsBefore) {
            itemModelList.get(mPosition).setImgBefore(file);
            if (mOrderModel.getProductID() == Constants.PRODUCT_MAX3D) {
                if (mPosition < (itemModelList.size() - 1)) {
                    mPosition++;
                    IsBefore = true;
                    mItem = itemModelList.get(mPosition);
                    capturePermission(mItem);
                }
            } else {
                IsBefore = false;
                capturePermission(mItem);
            }

        } else {
            itemModelList.get(mPosition).setImgAfter(file);
            if (mPosition < (itemModelList.size() - 1)) {
                mPosition++;
                IsBefore = true;
                mItem = itemModelList.get(mPosition);
                capturePermission(mItem);
            }
        }
        mCountImage++;
    }

    @Override
    public void showChangeToImage() {
        if (mOrderModel.getTicketCategory() == 2) {
            OrderTablesImagesRequest request = new OrderTablesImagesRequest();

            List<OrderTablesItemImages> itemImages = new ArrayList<>();
            List<String> drawCode = new ArrayList<>();

            for (ItemModel itemModel : itemModelList) {
                drawCode.add(itemModel.getDrawCode());

                OrderTablesItemImages item = new OrderTablesItemImages();
                item.setOrderItemID(itemModel.getOrderItemID().toString());
                item.setAfterImages(itemModel.getImgAfter());
                item.setBeforeImages(itemModel.getImgBefore());
                itemImages.add(item);
            }

            request.setDrawCode(drawCode);
            request.setItems(itemImages);
            request.setOrderCode(mOrderModel.getOrderCode());
            request.setPointOfSaleID(employeeModel.getPointOfSaleID());
            mPresenter.updateImageV1(request);

        } else {
            OrderImagesRequest request = new OrderImagesRequest();
            List<String> drawCode = new ArrayList<>();
            List<ImageModel> imageBefore = new ArrayList<>();
            List<ImageModel> imageAfter = new ArrayList<>();

            for (ItemModel itemModel : itemModelList) {
                drawCode.add(itemModel.getDrawCode());
                ImageModel imageModel = new ImageModel();
                imageModel.setFileName(itemModel.getImgBefore());
                imageBefore.add(imageModel);

                imageModel = new ImageModel();
                imageModel.setFileName(itemModel.getImgAfter());
                imageAfter.add(imageModel);
            }

            request.setDrawCode(drawCode);
            request.setImageBefore(imageBefore);
            request.setImageAfter(imageAfter);
            request.setOrderCode(mOrderModel.getOrderCode());
            request.setPointOfSaleID(employeeModel.getPointOfSaleID());
            mPresenter.updateImage(request);
        }
    }
}
