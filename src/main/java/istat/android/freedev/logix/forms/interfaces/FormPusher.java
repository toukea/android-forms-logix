package istat.android.freedev.logix.forms.interfaces;

import istat.android.freedev.forms.Form;

/**
 * Created by istat on 11/01/17.
 */

public interface FormPusher {
    public void onPush(Form form);

    public interface PushCallback {

    }
}
