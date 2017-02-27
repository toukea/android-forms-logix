package istat.android.freedev.logix.forms;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormFlower;
import istat.android.freedev.forms.FormState;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.interfaces.FormValidatorBuilder;
import istat.android.freedev.logix.forms.interfaces.FormPuller;
import istat.android.freedev.logix.forms.interfaces.FormPusher;

/**
 * Created by istat on 11/01/17.
 */

public class FormManager {
    public final static String TAG_SUBMIT_BUTTON = "submit";
    Form managedForm = new Form();
    Activity activity;
    View managedView;
    FormFiller formFiller = FormFiller.use(managedForm);
    FormFlower formFlower = FormFlower.use(managedForm);
    FormValidator formValidator;
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
        FormState state = checkUp();
        if (state != null || !state.hasError()) {
//this.puller.u
        }
    }

    public FormState checkUp() {
        if (this.formValidator != null) {
            return this.formValidator.validate(managedForm, managedView);
        }
        return null;
    }

    public void push(Form form, FormPusher.PushCallback callback) {

    }

    boolean started = false;

    public void start() {
        started = true;

    }

    public void setFormValidator(FormValidator formValidator) {
        if (formValidator != null) {
            this.formValidator = formValidator;
        }
    }

    public void setFormValidator(FormValidatorBuilder builder) {
        this.formValidator = builder.create();
    }

    public void stop() {
        started = false;
        prepare();
    }

    private void prepare() {
        if (submitButton == null) {
            submitButton = managedView.findViewWithTag(TAG_SUBMIT_BUTTON);
        }
        addListener();
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
        Form form = getForm();
        if (submitButton != null && this.onSubmitListener != null) {
            this.onSubmitListener.onSubmit(submitButton, form);
        }
    }

    private void addListener() {
        submitButton.setOnClickListener(mOnClickListener);
    }

    public Form getForm() {
        return getForm(true);
    }

    public Form getForm(boolean clearOldValue) {
        Form tmp = formFiller.fillWith(getManagedView());
        if (clearOldValue) {
            managedForm.clear();
        }
        if (tmp != null && !tmp.isEmpty()) {
            managedForm.putAll(tmp);
        }
        return managedForm;
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

    public FormValidator getFormValidator() {
        return formValidator;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == submitButton) {
                proceedSubmit(view);
            }
        }
    };

    private void proceedSubmit(View view) {
        Form formToPush = getForm();
        push(formToPush);
    }
}
