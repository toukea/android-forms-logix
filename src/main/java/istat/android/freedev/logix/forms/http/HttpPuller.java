package istat.android.freedev.logix.forms.http;

import android.os.AsyncTask;
import android.os.Build;

import com.istat.freedev.processor.Process;

import istat.android.freedev.forms.Form;
import istat.android.freedev.logix.forms.FormPuller;
import istat.android.freedev.logix.forms.interfaces.FormCallback;
import istat.android.freedev.logix.forms.interfaces.Puller;
import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.HttpQueryError;

/**
 * Created by istat on 11/01/17.
 */

public class HttpPuller<OUT> implements Puller<OUT> {
    @Override
    public Process<OUT, Throwable> onPull() {
        return null;
    }
}
