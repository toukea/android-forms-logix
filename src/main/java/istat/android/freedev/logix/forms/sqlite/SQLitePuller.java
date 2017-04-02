package istat.android.freedev.logix.forms.sqlite;

import android.database.Cursor;

import org.json.JSONException;

import java.util.List;

import istat.android.data.access.sqlite.SQLite;
import istat.android.data.access.sqlite.SQLiteModel;
import istat.android.data.access.sqlite.SQLiteSelect;
import istat.android.data.access.sqlite.utils.SQLiteAsyncExecutor;
import istat.android.data.access.sqlite.utils.SQLiteThread;
import istat.android.freedev.forms.Form;
import istat.android.freedev.logix.forms.FormPuller;
import istat.android.freedev.logix.forms.interfaces.FormCallback;
import istat.android.freedev.logix.forms.utils.Toolkit;

/**
 * Created by istat on 11/01/17.
 */

public class SQLitePuller extends FormPuller {
    SQLiteSelect selection;

    public SQLitePuller(SQLiteSelect selection) {
        this.selection = selection;
    }

    @Override
    protected void onPull(FormCallback callback) {
        Cursor c = selection.limit(1).getCursor();
        try {
            Form form = Toolkit.cursorToForm(c);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFail(e);
        }
    }

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
    protected void onCancel() {

    }
}
