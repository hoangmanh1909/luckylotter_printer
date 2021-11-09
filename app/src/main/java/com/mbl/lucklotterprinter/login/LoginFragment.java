package com.mbl.lucklotterprinter.login;

import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.core.base.viper.ViewFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.main.MainActivity;
import com.mbl.lucklotterprinter.utils.NumberUtils;
import com.mbl.lucklotterprinter.utils.SharedPref;
import com.mbl.lucklotterprinter.utils.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends ViewFragment<LoginContract.Presenter> implements LoginContract.View {
    @BindView(R.id.edt_mobile_number)
    TextInputEditText edtPhoneNumber;
    @BindView(R.id.edt_password)
    TextInputEditText edtPass;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick({R.id.btn_login})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        String phoneNumber = Objects.requireNonNull(edtPhoneNumber.getText()).toString();
        String passWord = Objects.requireNonNull(edtPass.getText()).toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.showToast(getActivity(), getString(R.string.error_empty_phone));
            return;
        }
        if (!org.apache.commons.lang3.math.NumberUtils.isDigits(phoneNumber) || phoneNumber.contains(".") || phoneNumber.contains(",")) {
            Toast.showToast(getActivity(), getString(R.string.error_warning_phone));
            return;
        }
        String phone = NumberUtils.convertVietNamPhoneNumber(phoneNumber);
        if (phone.length() != 10) {
            Toast.showToast(getActivity(), getString(R.string.error_warning_phone));
            return;
        }
        if (TextUtils.isEmpty(passWord)) {
            Toast.showToast(getActivity(), getString(R.string.error_password_empty));
            return;
        }

        mPresenter.login(phone, passWord);
    }

    @Override
    public void goHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
