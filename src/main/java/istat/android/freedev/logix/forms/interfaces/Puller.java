package istat.android.freedev.logix.forms.interfaces;

import com.istat.freedev.processor.Process;

/**
 * Created by Istat Toukea on 23/08/2017.
 */

public interface Puller<OUT> {
    Process<OUT, Throwable> onPull();
}
