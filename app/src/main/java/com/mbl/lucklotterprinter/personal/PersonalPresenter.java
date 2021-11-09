package com.mbl.lucklotterprinter.personal;

import com.core.base.viper.Presenter;
import com.core.base.viper.interfaces.ContainerView;

public class PersonalPresenter extends Presenter<PersonalContract.View, PersonalContract.Interactor>
        implements PersonalContract.Presenter {

    public PersonalPresenter(ContainerView containerView) {
        super(containerView);
    }

    @Override
    public void start() {

    }

    @Override
    public PersonalContract.Interactor onCreateInteractor() {
        return null;
    }

    @Override
    public PersonalContract.View onCreateView() {
        return PersonalFragment.getInstance();
    }
}
