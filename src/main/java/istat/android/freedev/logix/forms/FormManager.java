package istat.android.freedev.logix.forms;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.istat.freedev.processor.Process;
import com.istat.freedev.processor.Processor;

import istat.android.freedev.forms.Form;
import istat.android.freedev.forms.FormFiller;
import istat.android.freedev.forms.FormFlower;
import istat.android.freedev.forms.FormState;
import istat.android.freedev.forms.FormValidator;
import istat.android.freedev.forms.interfaces.FormValidatorBuilder;
import istat.android.freedev.logix.forms.interfaces.CompletionCallback;
import istat.android.freedev.logix.forms.interfaces.FormCallback;
import istat.android.freedev.logix.forms.interfaces.Puller;
import istat.android.freedev.logix.forms.interfaces.Pusher;
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
    Puller<Form> puller;
    Pusher<Form, Form> pusher;
    CompletionCallback<FormState> checkupCallback;
    FormCallback pushCallback, pullCallback;
    Handler handler = new Handler(Looper.getMainLooper());
    boolean autoCheckupEnable = true;
    SubmitListener submitListener;
    Process<Form, Throwable> pullProcess;
    Process<Form, Throwable> pushProcess;

    FormManager(Activity activity, View managedView) {
        this.activity = activity;
        this.managedView = managedView;
    }

    public FormManager setPusher(Pusher pusher) {
        this.pusher = pusher;
        return this;
    }

    public FormManager setPuller(Puller puller) {
        this.puller = puller;
        return this;
    }

    public View getManagedView() {
        return managedView;
    }

    public Activity getActivity() {
        if (activity != null) {
            return this.activity;
        }
        return getActivity(managedView);
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
            try {
                if (callback != null) {
                    callback.onStart();
                }
                pullProcess = puller.onPull();
                final Process<Form, Throwable> finalProcess = pullProcess;
                pullProcess.runWhen(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            boolean success = finalProcess.isSuccess();
                            callback.onComplete(success);
                            if (success) {
                                callback.onSuccess(finalProcess.getResult());
                            }
                        }
                    }
                });
            } catch (Exception e) {
                if (callback != null) {
                    callback.onFail(e);
                }
            }
        }
    }

    private void refresh() {
        getFormFlower().flowInto(managedView);
    }

    private void push(Form form, final FormCallback callback) {
        if (pusher != null) {
            try {
                if (callback != null) {
                    callback.onStart();
                }
                pushProcess = pusher.onPush(form);
                final Process<Form, Throwable> finalProcess = pushProcess;
                pushProcess.runWhen(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            boolean success = finalProcess.isSuccess();
                            callback.onComplete(success);
                            if (success) {
                                callback.onSuccess(finalProcess.getResult());
                            }
                        }
                    }
                });
            } catch (Exception e) {
                if (callback != null) {
                    callback.onFail(e);
                }
            }
        }
    }

    public FormManager setSubmitListener(SubmitListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }

    public boolean isPulling() {
        return pullProcess != null && pullProcess.isRunning();
    }

    public boolean isPushing() {
        return pushProcess != null && pushProcess.isRunning();
    }

    public boolean cancelPulling() {
        return pullProcess != null && pullProcess.cancel();
    }

    public boolean cancelPushing() {
        return pushProcess != null && pushProcess.cancel();
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
