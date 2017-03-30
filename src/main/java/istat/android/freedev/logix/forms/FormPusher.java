package istat.android.freedev.logix.forms;

import istat.android.freedev.forms.Form;
import istat.android.freedev.logix.forms.interfaces.FormCallback;

/**
 * Created by istat on 11/01/17.
 */

public abstract class FormPusher {
    public final void push(Form form, FormCallback callback) {
        onPush(form, callback);
    }

    public abstract boolean isCompleted();

    public abstract boolean isSuccess();

    public abstract boolean isPending();

    protected abstract void onPush(Form form, FormCallback callback);

    protected abstract void onCancel();

    public final boolean cancel() {
        boolean canceled = true;
        if (canceled) {
            onCancel();
        }
        return canceled;
    }

}
