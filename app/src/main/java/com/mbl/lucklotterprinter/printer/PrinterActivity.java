package com.mbl.lucklotterprinter.printer;

import android.app.Activity;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.base.LKLPrinterActivity;

public class PrinterActivity extends LKLPrinterActivity {
    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new PrinterPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}
