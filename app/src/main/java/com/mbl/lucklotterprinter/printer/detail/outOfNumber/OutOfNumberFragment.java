package com.mbl.lucklotterprinter.printer.detail.outOfNumber;

import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.base.viper.ViewFragment;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mbl.lucklotterprinter.R;
import com.mbl.lucklotterprinter.model.LineModel;
import com.mbl.lucklotterprinter.model.request.OutOfNumberModel;
import com.mbl.lucklotterprinter.model.request.OutOfNumberRequest;
import com.mbl.lucklotterprinter.printer.PrinterContract;
import com.mbl.lucklotterprinter.utils.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OutOfNumberFragment extends ViewFragment<OutOfNumberContract.Presenter> implements OutOfNumberContract.View {
    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    OutOfNumberAdapter adapter;
    List<LineModel> mLineModel = new ArrayList<>();
    String mOrderCode;

    public static OutOfNumberFragment getInstance() {
        return new OutOfNumberFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_out_of_number;
    }

    @Override
    public void initLayout() {
        super.initLayout();

        if (mPresenter != null) {
            mLineModel = mPresenter.getLineModels();
            mOrderCode = mPresenter.getOrderCode();

            adapter = new OutOfNumberAdapter(getViewContext(), mLineModel) {
                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);

                    ((HolderView) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                            LineModel reward = (LineModel) adapter.getItem(position);
                            reward.setSelected(isCheck);
                        }
                    });
                    ((HolderView) holder).checkBox.setChecked(((LineModel) adapter.getItem(position)).isSelected());
                }
            };
            recyclerView.setLayoutManager(new FlexboxLayoutManager(getViewContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    @OnClick({R.id.btn_ok, R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                ok();
                break;
            case R.id.iv_back:
                mPresenter.back();
                break;
        }
    }

    private void ok() {
        List<LineModel> lineModels = adapter.getItemSelected();
        if (!lineModels.isEmpty()) {
            OutOfNumberRequest request = new OutOfNumberRequest();

            List<OutOfNumberModel> outOfNumberModelList = new ArrayList<>();

            for (LineModel lineModel : lineModels) {
                OutOfNumberModel model = new OutOfNumberModel();
                model.setLineNo(lineModel.getTitle());
                model.setOrderItemId(String.valueOf(lineModel.getId()));
                outOfNumberModelList.add(model);
            }

            request.setOrderCode(mOrderCode);
            request.setOutOfNumberModelList(outOfNumberModelList);
            mPresenter.outOfNumber(request);
        } else {
            Toast.showToast(getActivity(), "Bạn chưa chọn bộ số");
        }
    }
}
