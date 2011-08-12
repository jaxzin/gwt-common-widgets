package com.jaxzin.gwt.ui.client.autocompletion;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:40:37 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public interface AutoCompletionMatcherServiceAsync {
    void match(String text, int requestNumber, AsyncCallback callback);
}
