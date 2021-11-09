package com.mbl.lucklotterprinter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.mbl.lucklotterprinter.R;

import butterknife.ButterKnife;

public class ProcessFragDialog extends Dialog {

    public ProcessFragDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = View.inflate(context, R.layout.dialog_progress, null);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
