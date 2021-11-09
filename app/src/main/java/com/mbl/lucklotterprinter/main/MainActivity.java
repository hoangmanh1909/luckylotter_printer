package com.mbl.lucklotterprinter.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.core.base.viper.ViewFragment;
import com.core.base.viper.interfaces.ContainerView;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.base.LKLPrinterActivity;

public class MainActivity extends LKLPrinterActivity {

    @Override
    public ViewFragment onCreateFirstFragment() {
        return (ViewFragment)new MainPresenter((ContainerView)getBaseActivity()).getFragment();
    }
}