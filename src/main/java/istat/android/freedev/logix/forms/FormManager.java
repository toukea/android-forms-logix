package istat.android.freedev.logix.forms;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormFlower;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.interfaces.FormValidable;
import istat.android.freedev.forms.interfaces.FormValidatorBuilder;
import istat.android.freedev.logix.forms.interfaces.FormPuller;
import istat.android.freedev.logix.forms.interfaces.FormPusher;

/**
 * Created by istat on 11/01/17.
 */

public class FormManager {
    Form managedForm = new Form();
    Activity activity;
    View managedView;
    FormFiller formFiller = FormFiller.use(managedForm);
    FormFlower formFlower = FormFlower.use(managedForm);
    FormValidable formValidator;
    View submitButton;
    OnSubmitListener onSubmitListener;
    FormPuller puller;
    FormPusher pusher;

    FormManager(View managedView) {
        this.managedView = managedView;
    }

    public FormManager setPusher(FormPusher pusher) {
        this.pusher = pusher;
        return this;
    }

    public FormManager setPuller(FormPuller puller) {
        this.puller = puller;
        return this;
    }

    public View getManagedView() {
        return managedView;
    }

    public Activity getActivity() {
        return activity;
    }

    public FormFiller getFormFiller() {
        return formFiller;
    }

    public FormFlower getFormFlower() {
        return formFlower;
    }

    public void setSubmitButton(View submitButton) {
        this.submitButton = submitButton;
    }

    public void pull() {
        pull(null);
    }

    public void push(Form form) {
        push(form, null);
    }

    public void pull(FormPuller.PullCallback callback) {

    }

    public void push(Form form, FormPusher.PushCallback callback) {

    }

    boolean started = false;

    public void start() {
        started = true;

    }

    public void setFormValidator(FormValidator formValidator) {
        this.formValidator = formValidator;
    }

    public void setFormValidator(FormValidable formValidator) {
        this.formValidator = formValidator;
    }

    public void setFormValidator(FormValidatorBuilder builder) {
        this.formValidator = builder.create(managedForm, managedView);
    }

    public void stop() {
        started = false;
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
        Form form = getForm();
        if (submitButton != null && this.onSubmitListener != null) {
            this.onSubmitListener.onSubmit(submitButton, form);
        }
    }

    private void addListener() {

    }

    private Form getForm() {
        return formFiller.fillWith(getManagedView());
    }

    private void setForm(Form form) {
        formFlower.flowInto(getManagedView());
    }

    public static interface OnSubmitListener {
        public void onSubmit(View v, Form form);
    }

    private FormPusher.PushCallback mPushCallback = new FormPusher.PushCallback() {
    };
    FormPuller.PullCallback mPullCallback = new FormPuller.PullCallback() {
    };

    public Context getContext() {
        if (managedForm != null) {
            return managedView.getContext();
        } else {
            return null;
        }
    }
}
