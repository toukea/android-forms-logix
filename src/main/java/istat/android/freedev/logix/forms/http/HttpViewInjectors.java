package istat.android.freedev.logix.forms.http;

import android.view.View;

import istat.android.freedev.forms.FormFiller;

/**
 * Created by istat on 10/01/17.
 */

public class HttpViewInjectors<T, VIEW extends View> extends FormFiller.ViewValueExtractor<T, VIEW> {
    public HttpViewInjectors(Class<T> valueType, Class<VIEW> viewType) {
        super(valueType, viewType);
    }

    public HttpViewInjectors(Class<T> valueType, Class<VIEW> viewType, String... acceptedField) {
        super(valueType, viewType, acceptedField);
    }

    @Override
    public T getValue(VIEW y) {
        return null;
    }
}
