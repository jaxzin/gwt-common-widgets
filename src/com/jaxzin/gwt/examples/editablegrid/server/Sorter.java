package com.jaxzin.gwt.examples.editablegrid.server;

/**
 * <pre>
 * Date: Jan 12, 2007
 * Time: 10:15:24 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public interface Sorter {
    void sort(Comparable[] arr, int start, int end);
}
