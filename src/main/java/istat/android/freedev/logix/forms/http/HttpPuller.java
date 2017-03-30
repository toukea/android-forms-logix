package istat.android.freedev.logix.forms.http;

import android.os.AsyncTask;
import android.os.Build;

import istat.android.freedev.forms.Form;
import istat.android.freedev.logix.forms.FormPuller;
import istat.android.freedev.logix.forms.interfaces.FormCallback;
import istat.android.network.http.AsyncHttp;
import istat.android.network.http.HttpAsyncQuery;
import istat.android.network.http.HttpQuery;
import istat.android.network.http.HttpQueryError;

/**
 * Created by istat on 11/01/17.
 */

public class HttpPuller extends FormPuller {
    private final HttpQuery http;
    private final String url;
    int method;
    HttpAsyncQuery asyncQuery;


    public HttpPuller(HttpQuery http, String url) {
        this(http, HttpAsyncQuery.TYPE_GET, url);
    }

    public HttpPuller(HttpQuery http, int method, String url) {
        this.http = http;
        this.url = url;
        this.method = method;
    }

    @Override
    protected void onPull(final FormCallback callback) {
        AsyncHttp asyncHttp = AsyncHttp.from(http);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncHttp.useExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
        asyncQuery = asyncHttp.doQuery(this.method, new HttpAsyncQuery.HttpQueryCallback() {
            @Override
            public void onHttpSuccess(HttpAsyncQuery.HttpQueryResponse resp) {
                if (callback != null) {
                    Form form = new Form();
                    Object body = resp.getBody();
                    if (body != null && body instanceof Form) {
                        form = Form.fromObject(resp.getBody());
                    }
                    callback.onSuccess(form);
                }
            }

            @Override
            public void onHttpError(HttpAsyncQuery.HttpQueryResponse resp, HttpQueryError e) {
                if (callback != null) {
                    callback.onFaill(e);
                }
            }

            @Override
            public void onHttpFail(Exception e) {
                if (callback != null) {
                    callback.onFaill(e);
                }
            }

            @Override
            public void onHttComplete(HttpAsyncQuery.HttpQueryResponse resp) {
                if (callback != null) {
                    callback.onComplete(resp.isAccepted());
                }
            }

            @Override
            public void onHttpAborted() {
                if (callback != null) {
                    callback.onAborted();
                }
            }
        }, this.url);
    }

    @Override
    public boolean isCompleted() {
        return asyncQuery != null & asyncQuery.isCompleted();
    }

    @Override
    public boolean isSuccess() {
        return asyncQuery != null & asyncQuery.isSuccess();
    }

    @Override
    public boolean isPending() {
        return asyncQuery != null & asyncQuery.isPending();
    }

    @Override
    protected void onCancel() {
        if (asyncQuery != null) {
            asyncQuery.cancel();
        }
    }
}
