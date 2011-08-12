package com.jaxzin.gwt.ui.client.autocompletion;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;
import java.util.ArrayList;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:38:49 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class MatcherResultBean implements MatcherResult, IsSerializable {
    private List matches;
    private int requestNumber;
    private boolean complete;
    private static final int MAX_RESULT_COUNT = 4;

    public MatcherResultBean() {
    }

    public MatcherResultBean(final List matches, final int requestNumber) {
        // Make a defensive copy - so determineComplete is accurate
        this.matches = new ArrayList(matches);
        this.requestNumber = requestNumber;
        determineComplete();
    }

    public List getMatches() {
        // Make a defensive copy - so determineComplete is accurate
        return new ArrayList(matches);
    }

    public void setMatches(final List matches) {
        // Make a defensive copy - so determineComplete is accurate
        this.matches = new ArrayList(matches);
        determineComplete();
    }

    private void determineComplete() {
        complete = matches.size() <= MAX_RESULT_COUNT;
        if(!complete) {
            while(matches.size() > MAX_RESULT_COUNT) {
                matches.remove(matches.size() - 1);
            }
        }
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(final int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public boolean isComplete() {
        return complete;
    }
}
