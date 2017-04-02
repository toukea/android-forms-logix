package istat.android.freedev.logix.forms.interfaces;

import istat.android.freedev.forms.Form;

/**
 * Created by istat on 30/03/17.
 */

public interface FormCallback {
    void onStart();

    void onComplete(boolean state);

    void onSuccess(Form form);

    void onFail(Throwable error);

    void onAborted();
}
