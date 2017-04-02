package istat.android.freedev.logix.forms.utils;

import android.database.Cursor;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import istat.android.freedev.forms.Form;

/**
 * Created by istat on 31/03/17.
 */

public class Toolkit {
    public static JSONObject cursorToJson(Cursor c, boolean autoClose) throws JSONException {
        JSONObject json = new JSONObject();
        String[] columnNames = c.getColumnNames();
        while (c.moveToNext()) {
            for (int i = 0; i < columnNames.length; i++) {
                String name = columnNames[i];
                String value = c.getString(i);
                json.put(name, value);
            }
        }
        if (autoClose) {
            c.close();
        }
        return json;
    }

    public static JSONObject cursorToJson(Cursor c) throws JSONException {
        return cursorToJson(c, true);
    }

    public static Form cursorToForm(Cursor c) throws JSONException {
        return cursorToForm(c, true);
    }

    public static Form cursorToForm(Cursor c, boolean autoClose) throws JSONException {
        Form form = new Form();
        String[] columnNames = c.getColumnNames();
        while (c.moveToNext()) {
            for (int i = 0; i < columnNames.length; i++) {
                String name = columnNames[i];
                String value = c.getString(i);
                form.put(name, value);
            }
        }
        if (autoClose) {
            c.close();
        }
        return form;
    }
}
