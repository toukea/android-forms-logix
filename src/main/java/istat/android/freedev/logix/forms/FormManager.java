package istat.android.freedev.logix.forms;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormFlower;
import istat.android.freedev.forms.FormState;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.interfaces.FormValidatorBuilder;
import istat.android.freedev.logix.forms.interfaces.CompletionCallback;
import istat.android.freedev.logix.forms.interfaces.FormCallback;
import istat.android.freedev.logix.forms.interfaces.SubmitListener;

/**
 * Created by istat on 11/01/17.
 */

public class FormManager {
    public final static String TAG_SUBMIT_BUTTON = "submit";
    final Form managedForm = new Form();
    Activity activity;
    final View managedView;
    final FormFiller formFiller = FormFiller.using(managedForm);
    final FormFlower formFlower = FormFlower.using(managedForm);
    FormValidator formValidator;
    View submitButton;
    OnSubmitListener onSubmitListener;
    FormPuller puller;
    FormPusher pusher;
    CompletionCallback<FormState> checkupCallback;
    FormCallback pushCallback, pullCallback;
    Handler handler = new Handler(Looper.getMainLooper());
    boolean autoCheckupEnable = true;
    SubmitListener submitListener;

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

    public Thread checkUpAsync() {
        return checkUpAsync(null);
    }

    public Thread checkUpAsync(final CompletionCallback<FormState> callback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                final FormState state = checkUp();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onComplete(state);
                        }
                        checkupCallback.onComplete(state);
                    }
                });

            }
        };
        thread.start();
        return thread;
    }

    public FormState checkUp() {
        if (this.formValidator != null) {
            return this.formValidator.validate(managedForm, managedView);
        }
        return null;
    }

    boolean started = false;

    public void start() {
        started = true;
        prepare();
    }

    public FormManager setFormValidator(FormValidator formValidator) {
        if (formValidator != null) {
            this.formValidator = formValidator;
        }
        return this;
    }

    public FormManager setFormValidator(FormValidatorBuilder builder) {
        this.formValidator = builder.create();
        return this;
    }

    public FormManager setFormValidator(FormValidator formValidator, FormValidator.ValidationListener listener) {
        setFormValidator(formValidator);
        this.formValidator.setValidationListener(listener);
        return this;
    }

    public FormManager setFormValidator(FormValidatorBuilder builder, FormValidator.ValidationListener listener) {
        setFormValidator(builder);
        this.formValidator.setValidationListener(listener);
        return this;
    }

    public void stop() {
        started = false;
    }

    private void prepare() {
        if (managedView != null) {
            if (submitButton == null) {
                submitButton = managedView.findViewWithTag(TAG_SUBMIT_BUTTON);
            }
            addListener();
        }
    }

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
        Form form = getForm();
        if (submitButton != null && this.onSubmitListener != null) {
            this.onSubmitListener.onSubmit(submitButton, form);
        }
    }

    private void addListener() {
        if (submitButton != null) {
            submitButton.setOnClickListener(mOnClickListener);
        }
    }

    public Form getForm() {
        return getForm(true);
    }

    public <T> T getFormAs(Class<T> cLass) throws IllegalAccessException, InstantiationException {
        Form form = getForm();
        if (form != null) {
            return form.as(cLass);
        }
        return null;
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

    public FormManager useForm(Form form) {
        return useForm(form, true);
    }

    public FormManager useForm(Form form, boolean clearOld) {
        if (clearOld) {
            managedForm.clear();
        }
        this.managedForm.putAll(form);
        formFlower.flowInto(getManagedView());
        return this;
    }

    public interface OnSubmitListener {
        void onSubmit(View v, Form form);
    }

    public Context getContext() {
        if (managedView != null) {
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


    public FormManager setCheckupCallback(CompletionCallback<FormState> checkupCallback) {
        this.checkupCallback = checkupCallback;
        return this;
    }

    public FormManager setPullCallback(FormCallback pullCallback) {
        this.pullCallback = pullCallback;
        return this;
    }

    public FormManager setPushCallback(FormCallback pushCallback) {
        this.pushCallback = pushCallback;
        return this;
    }

    public FormManager setFormFieldNames(String... names) {
        getFormFiller().setFieldToFill(names);
        return this;
    }

    public FormManager setAutoCheckupEnable(boolean autoCheckupEnable) {
        this.autoCheckupEnable = autoCheckupEnable;
        return this;
    }

    public void pull() {
        pull(pullCallback);
    }

    public void push() {
        Form form = getForm(true);
        push(pushCallback);
    }

    public void push(FormCallback callback) {
        if (autoCheckupEnable) {
            checkUpAsync(new CompletionCallback<FormState>() {
                @Override
                public void onComplete(FormState state) {
                    push(state.getForm(), pushCallback);
                }
            });
        } else {
            push(getForm(true), pushCallback);
        }
    }

    private void proceedSubmit(View view) {
        if (submitListener != null) {
            submitListener.onSubmit(view);
        }
        push(pushCallback);
    }

    public void pull(final FormCallback callback) {
        if (puller != null) {
            puller.pull(new FormCallback() {
                @Override
                public void onStart() {
                    if (callback != null) {
                        callback.onStart();
                    }
                }

                @Override
                public void onComplete(boolean state) {
                    if (callback != null) {
                        callback.onComplete(state);
                    }
                }

                @Override
                public void onSuccess(Form form) {
                    if (form != null) {
                        managedForm.putAll(form);
                        refresh();
                    }
                    if (callback != null) {
                        callback.onSuccess(form);
                    }
                }

                @Override
                public void onFail(Throwable error) {
                    if (callback != null) {
                        callback.onFail(error);
                    }
                }

                @Override
                public void onAborted() {
                    if (callback != null) {
                        callback.onAborted();
                    }
                }
            });
        }
    }

    private void refresh() {
        getFormFlower().flowInto(managedView);
    }

    private void push(Form form, FormCallback callback) {
        if (pusher != null) {
            pusher.push(form, callback);
        }
    }

    public FormManager setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }
}
