package com.jaxzin.gwt.ui.client.autocompletion;

import java.util.List;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:35:21 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public interface MatcherResult {
    List getMatches();

    /**
     * Including the original request number in the response
     * will allow the async callback to ignore out-of-date results.
     * @return the original request number
     */
    int getRequestNumber();

    boolean isComplete();
}
