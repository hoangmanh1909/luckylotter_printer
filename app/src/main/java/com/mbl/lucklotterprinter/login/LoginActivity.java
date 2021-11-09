package com.mbl.lucklotterprinter.login;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.base.LKLPrinterActivity;

public class LoginActivity extends LKLPrinterActivity {
    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new LoginPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}
