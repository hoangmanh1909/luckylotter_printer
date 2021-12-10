package com.mbl.lucklotterprinter.printer.detail;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.base.LKLPrinterActivity;
import com.mbl.lucklotterprinter.printer.PrinterPresenter;

public class DetailActivity extends LKLPrinterActivity {
    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new DetailPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}
