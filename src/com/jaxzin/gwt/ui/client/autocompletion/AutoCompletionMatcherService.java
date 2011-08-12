package com.jaxzin.gwt.ui.client.autocompletion;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:33:20 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public interface AutoCompletionMatcherService extends RemoteService {
    MatcherResult match(String text, int requestNumber);
}
