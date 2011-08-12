package com.jaxzin.gwt.examples.editablegrid.server;

/**
 * <pre>
 * Date: Jan 14, 2007
 * Time: 3:18:34 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class HeapSort implements Sorter {
    public void sort(Comparable[] arr, int start, int end) {
        build_heap(arr);
        int size = arr.length;
        for(int i = arr.length-1; i >= 1; i--) {
            swap(arr, 0, i);
            size--;
            heapify(arr, 0, size);
        }
    }

    private void build_heap(Comparable[] arr) {
        int size = arr.length;
        for(int i = (arr.length-1)/2; i >= 0; i--) {
            heapify(arr, i, size);
        }
    }

    private void heapify(Comparable[] arr, int start, int size) {
        int l = left(start);
        int r = right(start);
        int largest;
        if(l < size && Compare.gt(arr[l], arr[start])) {
            largest = l;
        } else {
            largest = start;
        }
        if(r < size && Compare.gt(arr[r], arr[largest])) {
            largest = r;
        }
        if(largest != start) {
            swap(arr, start, largest);
            heapify(arr, largest, size);
        }
    }

    private void swap(Comparable[] arr, int left, int right) {
        Comparable temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    private int parent(int index) {
        return index/2;
    }

    private int left(int index) {
        return index*2;
    }

    private int right(int index) {
        return left(index) + 1;
    }
}
