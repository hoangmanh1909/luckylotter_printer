package com.mbl.lucklotterprinter.personal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;

import com.core.base.viper.ViewFragment;
import com.mbl.lucklotterprinter.BuildConfig;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.login.LoginActivity;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.service.BluetoothServices;
import com.mbl.lucklotterprinter.utils.Constants;
import com.mbl.lucklotterprinter.utils.DialogUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;

import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalFragment extends ViewFragment<PersonalContract.Presenter> implements PersonalContract.View {
    @BindView(R.id.tv_bluetooth)
    TextView tv_bluetooth;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.id_version)
    TextView id_version;

    SharedPref sharedPref;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH = 1;
    BluetoothDevice[] btArray;

    public static PersonalFragment getInstance() {
        return new PersonalFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        sharedPref = new SharedPref(requireActivity());
        String bluetooth = sharedPref.getString(Constants.BLUETOOTH_NAME, "");

        EmployeeModel employeeModel = sharedPref.getEmployeeModel();
        tv_email.setText(employeeModel.getEmailAddress());
        tv_mobile.setText(employeeModel.getMobileNumber());
        tv_name.setText(employeeModel.getFullName());
        id_version.setText("v." + BuildConfig.VERSION_NAME);
        if (!TextUtils.isEmpty(bluetooth))
            tv_bluetooth.setText(bluetooth);
    }

    @OnClick({R.id.rl_logout, R.id.ll_bluetooth})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_logout:
                DialogUtils.showOptionAction(
                        getContext(),
                        "Bạn có chắc chắn muốn đăng xuất?",
                        "Đồng ý",
                        "Hủy",
                        (dialog, which) -> {
                            sharedPref.clear();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }, (dialog, which) -> {
                            dialog.dismiss();
                        });

                break;
            case R.id.ll_bluetooth:
                pickBluetooth();
                break;
        }
    }

    private void pickBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        String[] stringsBluetooth = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;

        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                stringsBluetooth[index] = device.getName();
                index++;
            }
        }

        if (stringsBluetooth != null) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), tv_bluetooth, Gravity.END);

            for (String blutooth : stringsBluetooth) {
                popupMenu.getMenu().add(1, 0, 0, blutooth);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String value = item.getTitle().toString();
                    tv_bluetooth.setText(value);

                    SharedPref sharedPref = new SharedPref(getActivity());

                    String devide = sharedPref.getString(Constants.BLUETOOTH_NAME, "");
                    if (!devide.equals(value)) {
                        sharedPref.putString(Constants.BLUETOOTH_NAME, value);
                        registerBluetoothService(value);
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    }

    public void registerBluetoothService(String name) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice mDevice = null;
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getActivity().startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            for (BluetoothDevice device : btArray) {
                if (device.getName().equals(name)) {
                    mDevice = device;
                    break;
                }
            }
            if (mDevice != null) {
                BluetoothServices bluetoothServices = new BluetoothServices();
                Intent intent = getActivity().getIntent();

                intent.putExtra("bluetooth_device", mDevice.getAddress());

                bluetoothServices.onStartCommand(intent, 0, 0);
            }
        }
    }
}
