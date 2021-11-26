package com.mbl.lucklotterprinter.printer.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.number.CompactNotation;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.core.base.log.Logger;
import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.dialog.ProcessFragDialog;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.ImageModel;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.PrintCommandModel;
import com.mbl.lucklotterprinter.model.request.FinishOrderKenoRequest;
import com.mbl.lucklotterprinter.model.request.OrderImagesRequest;
import com.mbl.lucklotterprinter.printer.PrinterFragment;
import com.mbl.lucklotterprinter.printer.detail.outOfNumber.OutOfNumberPresenter;
import com.mbl.lucklotterprinter.printer.upload.UploadPresenter;
import com.mbl.lucklotterprinter.service.BluetoothServices;
import com.mbl.lucklotterprinter.service.TimeService;
import com.mbl.lucklotterprinter.utils.BitmapUtils;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DateTimeUtils;
import com.mbl.lucklotterprinter.utils.DialogHelper;
import com.mbl.lucklotterprinter.utils.DrawViewUtils;
import com.mbl.lucklotterprinter.utils.MediaUltis;
import com.mbl.lucklotterprinter.utils.NumberUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Toast;
import com.mbl.lucklotterprinter.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends ViewFragment<DetailContract.Presenter> implements DetailContract.View {
    @BindView(R.id.tv_bluetooth)
    TextView tvBluetooth;
    @BindView(R.id.iv_type_ticket)
    ImageView img_logo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_game)
    TextView tv_game;
    @BindView(R.id.tv_fullName)
    TextView tv_fullName;
    @BindView(R.id.tv_pid_number)
    TextView tv_pid_number;
    @BindView(R.id.tv_mobile_number)
    TextView tv_mobile_number;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_quantity)
    TextView tv_quantity;
    @BindView(R.id.tv_system)
    TextView tvSystem;
    @BindView(R.id.tv_draw_code)
    TextView tv_draw_code;
    @BindView(R.id.tv_draw)
    TextView tv_draw;
    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_capture)
    Button btnCapture;
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.rl_keno)
    RelativeLayout rl_keno;
    @BindView(R.id.btn_printed)
    Button btnPrinted;

    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.ll_image_after)
    LinearLayout ll_image_after;
    @BindView(R.id.ll_image)
    LinearLayout ll_image;

    @BindView(R.id.image_before)
    public SimpleDraweeView image_before;
    @BindView(R.id.image_after)
    public SimpleDraweeView image_after;

    boolean IsConnectedBluetooth = false;
    String deviceBluetooth = "";

    List<ItemModel> mItemModels;
    List<DrawModel> mDrawModels;

    List<LineModel> mLineModels;
    RowAdapter rowAdapter;

    ProcessFragDialog processFragDialog;
    long SymbolSpecial = Constants.SymbolSpecial;
    long SymbolBase = Constants.SymbolBase;
    long SymbolNumber = Constants.SymbolNumber;

    boolean IsPrint = false;
    String ticketType = Constants.TICKET_NO_AMOUNT;
    boolean IsBefore = true;
    OrderModel mOrderModel;
    String mFileBefore;
    String mFileAfter;
    String mPrintCode;
    String systematic = "";

    EmployeeModel employeeModel;
    boolean IsKenoPlus = false;
    SharedPref sharedPref;
    CountDownTimer cdt;
    int diffPrintSecond = 0;

    public static DetailFragment getInstance() {
        return new DetailFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_printer_detail;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        sharedPref = new SharedPref(requireActivity());
        mItemModels = new ArrayList<>();
        mLineModels = new ArrayList<>();
        mDrawModels = new ArrayList<>();

        SharedPref sharedPref = new SharedPref(getActivity());
        deviceBluetooth = sharedPref.getString(Constants.BLUETOOTH_NAME, "");
        employeeModel = sharedPref.getEmployeeModel();

        if (BluetoothServices.mState == BluetoothServices.STATE_CONNECTED) {
            IsConnectedBluetooth = true;
            tvBluetooth.setText("Đã kết nối " + deviceBluetooth);
        } else if (BluetoothServices.mState == BluetoothServices.STATE_CONNECTING)
            tvBluetooth.setText("Đang kết nối " + deviceBluetooth);
        else
            tvBluetooth.setText("Kết nối thất bại " + deviceBluetooth);

        if (mPresenter != null) {
            mOrderModel = mPresenter.getOrderModel();

            switch (mOrderModel.getProductID()) {
                case Constants.PRODUCT_MEGA:
                    img_logo.setImageResource(R.drawable.home_mega);
                    break;
                case Constants.PRODUCT_MAX4D:
                    img_logo.setImageResource(R.drawable.home_max4d);
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
                case Constants.PRODUCT_MAX3D:
                    img_logo.setImageResource(R.drawable.home_max3d);
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
                case Constants.PRODUCT_MAX3D_PLUS:
                    img_logo.setImageResource(R.drawable.home_max3d_plus);
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
                case Constants.PRODUCT_MAX3D_PRO:
                    img_logo.setImageResource(R.drawable.logomax3dpro);
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
                case Constants.PRODUCT_POWER:
                    img_logo.setImageResource(R.drawable.home_power);
                    break;
                case Constants.PRODUCT_KENO:
                    img_logo.setImageResource(R.drawable.home_keno);
                    ticketType = Constants.TICKET_SHOW_AMOUNT;
                    break;
            }

            tv_game.setText(Utils.getProductName(mOrderModel.getProductID()));
            tv_fullName.setText(mOrderModel.getFullName());
            tv_pid_number.setText(mOrderModel.getpIDNumber());
            tv_amount.setText(NumberUtils.formatPriceNumber(mOrderModel.getAmount()) + "/" + mOrderModel.getQuantity());
            tv_mobile_number.setText(mOrderModel.getMobileNumber());
            tv_quantity.setText(String.valueOf(mOrderModel.getQuantity()));
            tvTitle.setText("In vé #" + mOrderModel.getOrderCode());

            rl_keno.setVisibility(View.GONE);
            if (mOrderModel.getProductID() == Constants.PRODUCT_KENO || mOrderModel.getProductID() == Constants.PRODUCT_MAX3D) {
                ll_image_after.setVisibility(View.GONE);
                if (mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
                    btnReject.setEnabled(false);
                    rl_keno.setVisibility(View.VISIBLE);
                    btnPrinted.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void showItem(List<ItemModel> orderModels, String printCode) {
        mItemModels.addAll(orderModels);
        mPrintCode = printCode;
        IsKenoPlus = false;

        if (orderModels.get(0).getProductID() == Constants.PRODUCT_KENO) {
            mDrawModels = mPresenter.getDrawModels();
            findCurrentDraw();
            if (orderModels.get(0).getItemType() == 2) {
                IsKenoPlus = true;
                systematic = "Chẵn lẻ - Lớn nhỏ";
            } else {
                systematic = "Bậc " + orderModels.get(0).getSystemA();
            }
        } else if (orderModels.get(0).getProductID() == Constants.PRODUCT_MAX3D_PRO) {
            if (orderModels.get(0).getItemType() == 2)
                systematic = "Bao bộ số";
            else if (orderModels.get(0).getItemType() > 2) {
                systematic = "Bao " + orderModels.get(0).getItemType();
            }
        } else {
            if (orderModels.get(0).getSystemA() > 6)
                systematic = "Bao " + orderModels.get(0).getSystemA();
            if (orderModels.get(0).getSystemA() == 5)
                systematic = "Bao 5";
        }
        tvSystem.setText(systematic);

        String drawCode = "";
        if (orderModels.size() > 1) {
            drawCode = "#" + orderModels.get(0).getDrawCode() + " - " + "#" + orderModels.get(orderModels.size() - 1).getDrawCode() + "/" + NumberUtils.formatPriceNumber(mOrderModel.getPrice());
        } else
            drawCode = "#" + orderModels.get(0).getDrawCode() + "/" + NumberUtils.formatPriceNumber(mOrderModel.getPrice());
        tv_draw_code.setText(drawCode);

        if (mOrderModel.getTicketCategory() == 2) {
            for (ItemModel item : orderModels) {
                addLine(item);
            }
        } else {
            ItemModel item = orderModels.get(0);
            addLine(item);
        }
        if (orderModels.size() > 1 && mOrderModel.getProductID() != Constants.PRODUCT_KENO) {
            btnCapture.setEnabled(false);
            ll_image.setVisibility(View.GONE);
            btnOk.setText("Tiếp tục");
        }

        rowAdapter = new RowAdapter(requireContext(), mLineModels);
        recycle.setLayoutManager(new FlexboxLayoutManager(requireContext()));
        recycle.setAdapter(rowAdapter);
    }

    public void addLine(ItemModel item) {
        try {
            LineModel lineModel = new LineModel();
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
                if (item.getItemType() != null)
                    lineModel.setItemType(item.getItemType());
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
        } catch (Exception e) {
            Log.e("addLine", e.getMessage());
        }
    }

    @OnClick({R.id.btn_print, R.id.btn_capture, R.id.btn_ok, R.id.image_before, R.id.image_after, R.id.iv_back, R.id.btn_reject, R.id.btn_printed})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                printTicket();
                btnPrinted.setEnabled(false);
                break;
            case R.id.btn_printed:
                printed();
                break;
            case R.id.btn_capture:
            case R.id.image_before:
                if (IsPrint) {
                    capturePermission(false);
                    IsBefore = true;
                }
                break;
            case R.id.btn_ok:
                if (mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
                    ok();
                } else {
                    if (mItemModels.size() > 1) {
                        new UploadPresenter((ContainerView) requireActivity(), mOrderModel, mItemModels, mPrintCode).pushView();
                    } else {
                        ok1();
                    }
                }
                break;
            case R.id.image_after:
                if (IsPrint) {
                    capturePermission(true);
                    IsBefore = false;
                }
                break;
            case R.id.iv_back:
                mPresenter.back();
                break;
            case R.id.btn_reject:
                reject();
                break;
        }
    }

    private void printTicket() {
        if (mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
            DrawModel currentDrawModel = null;
            for (DrawModel drawModel : mDrawModels) {
                if (drawModel.getDrawCode().equals(mItemModels.get(0).getDrawCode())) {
                    currentDrawModel = drawModel;
                    break;
                }
            }
            if (currentDrawModel == null) {
                Toast.showToast(getViewContext(), "Đơn hàng hết thời gian mở bán");
                return;
            }

            Date drawDateTime = DateTimeUtils.convertStringToDateDefault(currentDrawModel.getDrawDate() + " " + currentDrawModel.getDrawTime());
            Date serverTime = TimeService.date;

            if (DateTimeUtils.compareToDay(serverTime, DateUtils.addMinutes(DateUtils.addSeconds(drawDateTime, diffPrintSecond), -10)) < 0) {
                Toast.showToast(getViewContext(), "Đơn hàng chưa đến thời gian mở bán");
                return;
            }
            if (DateTimeUtils.compareToDay(serverTime, DateUtils.addSeconds(drawDateTime, -diffPrintSecond)) > 0) {
                Toast.showToast(getViewContext(), "Đơn hàng hết thời gian mở bán");
                return;
            }
        }
        mPresenter.print(mItemModels);
    }

    private void printed() {
        btnPrint.setEnabled(false);
        btnReject.setEnabled(false);
        IsPrint = true;
        btnPrint.setEnabled(false);
        if (mItemModels.size() == 1 || mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
            btnCapture.setEnabled(true);
        }

        btnReject.setEnabled(false);
        if (mItemModels.size() > 0)
            btnOk.setEnabled(true);
        if (mOrderModel.getProductID() == Constants.PRODUCT_KENO || (mItemModels.size() == 1 && mOrderModel.getProductID() == Constants.PRODUCT_MAX3D)) {
            capturePermission(false);
        }
    }

    private void reject() {
        new OutOfNumberPresenter((ContainerView) requireActivity(), mLineModels, mOrderModel.getOrderCode()).pushView();
    }

    private void ok() {
        if (!TextUtils.isEmpty(mFileBefore)) {
            FinishOrderKenoRequest request = new FinishOrderKenoRequest();
            request.setKenoImg(mFileBefore);
            request.setOrderCode(mOrderModel.getOrderCode());
            request.setPrintCode(mItemModels.get(0).getPrintCode());
            request.setPosid(employeeModel.getPointOfSaleID());
            mPresenter.finishOrderKeno(request);
        } else {
            Toast.showToast(getViewContext(), "Bạn chưa cập nhật ảnh!");
        }
    }

    private void ok1() {
        if (!TextUtils.isEmpty(mFileBefore)) {
            if (!TextUtils.isEmpty(mFileAfter) || mOrderModel.getProductID() == Constants.PRODUCT_MAX3D) {
                mPresenter.changeToImage(mOrderModel.getOrderCode(), mPrintCode);
            } else {
                Toast.showToast(getViewContext(), "Bạn chưa cập nhật ảnh mặt sau!");
            }
        } else {
            Toast.showToast(getViewContext(), "Bạn chưa cập nhật ảnh mặt trước!");
        }
    }

    @Override
    public void showPrint(PrintCommandModel printCommandModel) {
        processFragDialog = new ProcessFragDialog(requireContext());
        processFragDialog.show();
        IsPrint = true;
        btnPrint.setEnabled(false);
        if (mItemModels.size() == 1 || mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
            btnCapture.setEnabled(true);
        }

        btnReject.setEnabled(false);
        if (mItemModels.size() > 0)
            btnOk.setEnabled(true);
        if (IsKenoPlus)
            new Thread(() -> printKenoPlus(printCommandModel)).start();
        else
            new Thread(() -> print(printCommandModel)).start();
    }

    @Override
    public void showImage(String file) {
        if (IsBefore) {
            mFileBefore = file;
            if (mOrderModel.getProductID() != Constants.PRODUCT_KENO
                    && mOrderModel.getProductID() != Constants.PRODUCT_MAX3D) {
                IsBefore = false;
                capturePermission(true);
            }
        } else {
            mFileAfter = file;
        }
    }

    @Override
    public void showSuccessImage() {
        OrderImagesRequest request = new OrderImagesRequest();
        List<String> drawCode = new ArrayList<>();
        List<ImageModel> imageBefore = new ArrayList<>();
        List<ImageModel> imageAfter = new ArrayList<>();

        drawCode.add(mItemModels.get(0).getDrawCode());
        ImageModel imageModel = new ImageModel();
        imageModel.setFileName(mFileBefore);
        imageBefore.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setFileName(mFileAfter);
        imageAfter.add(imageModel);

        request.setDrawCode(drawCode);
        request.setImageBefore(imageBefore);
        request.setImageAfter(imageAfter);
        request.setOrderCode(mOrderModel.getOrderCode());
        request.setPointOfSaleID(employeeModel.getPointOfSaleID());

        mPresenter.updateImage(request);
    }

    private void printKenoPlus(PrintCommandModel printCommandModel) {

        String[] printCommand = printCommandModel.getCommandText().split(",");

        for (String message : printCommand) {
            BluetoothServices.sendData(message);
            if (Constants.POSKeyArray.contains(message)) {
                try {
                    Thread.sleep(SymbolSpecial);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (StringUtils.isNumeric(message)) {
                    try {
                        Thread.sleep(SymbolSpecial);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(SymbolBase);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        processFragDialog.dismiss();
        capturePermission(false);
    }

    private void print(PrintCommandModel printCommandModel) {

        String[] printCommand = printCommandModel.getCommandText().split(",");

        for (String message : printCommand) {
            BluetoothServices.sendData(message);
            if (Constants.POSKeyArray.contains(message)) {
                try {
                    Thread.sleep(SymbolSpecial);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (StringUtils.isNumeric(message)) {
                    try {
                        Thread.sleep(SymbolNumber);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(SymbolBase);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        processFragDialog.dismiss();
        if (mOrderModel.getProductID() == Constants.PRODUCT_KENO || (mItemModels.size() == 1 && mOrderModel.getProductID() == Constants.PRODUCT_MAX3D)) {
            capturePermission(false);
        }
//        else {
//            new UploadPresenter((ContainerView) requireActivity(), mOrderModel, mItemModels, mPrintCode).pushView();
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                attemptSendMedia(data.getData().getPath());
            }
        }
    }

    @SuppressLint("CheckResult")
    private void attemptSendMedia(String path_media) {
        File file = new File(path_media);
        Uri picUri = Uri.fromFile(new File(path_media));
        if (IsBefore)
            image_before.setImageURI(picUri);
        else
            image_after.setImageURI(picUri);

        Observable.fromCallable(() -> {
            Uri uri = Uri.fromFile(new File(path_media));
//            return BitmapUtils.processingBitmap(uri, getViewContext());
            DrawViewUtils drawViewUtils = new DrawViewUtils(getContext());
            if (IsBefore) {
                String drawCode = "";
                String drawDate = "";
                if (!TextUtils.isEmpty(systematic))
                    systematic = " - " + systematic;
                if (mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
                    if (mItemModels.size() > 1) {
                        drawCode = mItemModels.get(0).getDrawCode() + ";" + mItemModels.get(mItemModels.size() - 1).getDrawCode();
                        drawDate = mItemModels.get(0).getDrawDate() + ";" + mItemModels.get(mItemModels.size() - 1).getDrawDate();
                    } else {
                        drawCode = mItemModels.get(0).getDrawCode();
                        drawDate = mItemModels.get(0).getDrawDate();
                    }
                } else {
                    drawCode = mItemModels.get(0).getDrawCode();
                    drawDate = mItemModels.get(0).getDrawDate();
                }
                return drawViewUtils.processingBitmapBefore(uri,
                        Utils.getProductName(mOrderModel.getProductID()) + systematic,
                        mLineModels,
                        drawDate,
                        drawCode,
                        mOrderModel.getOrderCode());
            } else {
                return drawViewUtils.processingBitmapAfter(uri, mOrderModel.getFullName(), mOrderModel.getpIDNumber(), mOrderModel.getMobileNumber());
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map(bitmap ->
                        BitmapUtils.saveImage(bitmap, file.getParent(), "lkl_" + file.getName(), Bitmap.CompressFormat.JPEG, 50)
                )
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                isSavedImage -> {
                    String path = file.getParent() + File.separator + "lkl_" + file.getName();
                    String type;
                    if (IsBefore)
                        type = Constants.IMAGE_BEFORE;
                    else
                        type = Constants.IMAGE_AFTER;

                    mPresenter.postImage(path, type);
                    if (file.exists())
                        file.delete();
                },
                onError -> Logger.e("error save image")
        );
    }

    private void capturePermission(boolean IsAfter) {
        CharSequence info;
        boolean IsAmount = ticketType.equals(Constants.TICKET_SHOW_AMOUNT);
        CharSequence infoAfter = Utils.getInfoImageAfter(requireActivity(), mOrderModel.getFullName(), mOrderModel.getpIDNumber(), mOrderModel.getMobileNumber());

        if (!IsAfter) {
            List<LineModel> lineModels = new ArrayList<>();
            String draw = "";
//            for (LineModel lineModel : mLineModels) {
//                LineModel lineModel1 = new LineModel();
//                String[] arrNumber = lineModel.getLine().split(",");
//                List<String> stringList = new ArrayList<>();
//                for (String s : arrNumber) {
//                    stringList.add(StringUtils.leftPad(StringUtils.trim(s), 2, '0'));
//                }
//                lineModel1.setLine(lineModel.getTitle() + ": " + StringUtils.join(stringList, " "));
//                lineModel1.setAmount(lineModel.getAmount());
//                lineModel1.setType(ticketType);
//                lineModels.add(lineModel1);
//            }
            String drawFist = mItemModels.get(0).getDrawCode();
            if (mOrderModel.getProductID() == Constants.PRODUCT_KENO) {
                if (mItemModels.size() > 1) {
                    String drawLast = mItemModels.get(mItemModels.size() - 1).getDrawCode();
                    draw = "Kỳ từ #" + drawFist + " đến #" + drawLast + ", Ngày quay:" + mItemModels.get(0).getDrawDate();
                } else {
                    draw = "Kỳ #" + drawFist + ", Ngày quay:" + mItemModels.get(0).getDrawDate();
                }
            } else {
                if (mItemModels.size() > 1) {
                    String drawLast = mItemModels.get(mItemModels.size() - 1).getDrawCode();
                    String drawLastDate = mItemModels.get(mItemModels.size() - 1).getDrawDate();
                    draw = "Kỳ từ #" + drawFist + " đến #" + drawLast + ", Ngày quay:" + mItemModels.get(0).getDrawDate() + "-" + drawLastDate;
                } else {
                    draw = "Kỳ #" + drawFist + ", Ngày quay:" + mItemModels.get(0).getDrawDate();
                }
            }
            if (!TextUtils.isEmpty(systematic))
                systematic = " - " + systematic;
            info = Utils.getInfoImageBefore(requireActivity(), mLineModels, Utils.getProductName(mOrderModel.getProductID()) + systematic, draw, IsAmount);
        } else {
            info = infoAfter;
        }

        PermissionUtils.permission(PermissionConstants.CAMERA)
                .rationale(DialogHelper::showRationaleDialog)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        MediaUltis.captureImage(DetailFragment.this, info, "#" + mOrderModel.getOrderCode(), IsAfter);
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

    private void findCurrentDraw() {
        Date serverTime = TimeService.date;
        Date drawDateTime = null;

        DrawModel currentDrawModel = null;
        for (DrawModel drawModel : mDrawModels) {
            drawDateTime = DateTimeUtils.convertStringToDateDefault(drawModel.getDrawDate() + " " + drawModel.getDrawTime());

            if (DateTimeUtils.compareToDay(serverTime, drawDateTime) < 0
                    && DateTimeUtils.compareToDay(DateUtils.addMinutes(serverTime, 10), drawDateTime) >= 0) {
                if (drawModel.getDrawCode().equals(mItemModels.get(0).getDrawCode())) {
                    currentDrawModel = drawModel;
                }
                break;
            }
        }

        if (currentDrawModel != null) {
            diffPrintSecond = Integer.parseInt(sharedPref.getString(Constants.KEY_DIFF_PRINT_SECOND, ""));
            assert drawDateTime != null;
            Date fromPrintTime = DateUtils.addSeconds(DateUtils.addMinutes(drawDateTime, -10), diffPrintSecond);
            Date toPrintTime = DateUtils.addSeconds(drawDateTime, -diffPrintSecond);
            String printTime = DateTimeUtils.convertDateToString(fromPrintTime, DateTimeUtils.SIMPLE_TIME_FORMAT) + " - " + DateTimeUtils.convertDateToString(toPrintTime, DateTimeUtils.SIMPLE_TIME_FORMAT);
            tv_draw.setText("#" + currentDrawModel.getDrawCode());
            tv_time.setText(printTime);
            long diff = drawDateTime.getTime() - serverTime.getTime();
            CountdownKeno(diff);
        }
    }

    private void CountdownKeno(long diff) {
        cdt = new CountDownTimer(diff, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                btnPrint.setText("IN VÉ " + StringUtils.leftPad(String.valueOf(minutes), 2, '0') + ":" + StringUtils.leftPad(String.valueOf(seconds), 2, '0'));
            }

            @Override
            public void onFinish() {
                cancel();
            }
        };
        cdt.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cdt != null)
            cdt.cancel();
    }
}
