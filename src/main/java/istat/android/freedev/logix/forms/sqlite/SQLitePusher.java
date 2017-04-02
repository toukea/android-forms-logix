package istat.android.freedev.logix.forms.sqlite;

import istat.android.data.access.sqlite.SQLiteModel;
import istat.android.freedev.forms.Form;
import istat.android.freedev.logix.forms.FormPusher;
import istat.android.freedev.logix.forms.interfaces.FormCallback;

/**
 * Created by istat on 11/01/17.
 */

public class SQLitePusher extends FormPusher {
    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isPending() {
        return false;
    }

    @Override
    protected void onPush(Form form, FormCallback callback) {
        SQLiteModel.Builder builder=new SQLiteModel.Builder();
       // builder.

    }

    @Override
    protected void onCancel() {

    }
}
