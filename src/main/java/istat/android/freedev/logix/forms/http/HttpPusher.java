package istat.android.freedev.logix.forms.http;


import com.istat.freedev.processor.Process;

import istat.android.freedev.logix.forms.interfaces.Pusher;

/**
 * Created by istat on 11/01/17.
 */

public class HttpPusher<IN,OUT> implements Pusher<IN,OUT>{
    @Override
    public Process<OUT, Throwable> onPush(IN input) {
        return null;
    }
}
