package com.mbl.lucklotterprinter.personal;

import com.core.base.viper.interfaces.IInteractor;
import com.core.base.viper.interfaces.IPresenter;
import com.core.base.viper.interfaces.PresentView;

public interface PersonalContract {
    interface Interactor extends IInteractor<PersonalContract.Presenter> {
    }

    interface View extends PresentView<PersonalContract.Presenter> {
    }

    interface Presenter extends IPresenter<PersonalContract.View, PersonalContract.Interactor> {

    }
}
