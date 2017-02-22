package istat.android.freedev.logix.forms;


import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by istat on 11/01/17.
 */

public class FormLogix {

    public static FormManager from(Activity activity) {
        return null;
    }

    public static FormManager from(FragmentManager fm) {
        return null;
    }

    public static FormManager from(View view) {
        return null;
    }

    public FormManager createManager() {
        return null;
    }

    public FormManager startManaging() {
        FormManager manager = createManager();
        manager.start();
        return manager;
    }
}
