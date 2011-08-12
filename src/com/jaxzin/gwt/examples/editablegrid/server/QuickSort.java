package com.jaxzin.gwt.examples.editablegrid.server;

import java.util.*;

/**
 * <pre>
 * Date: Jan 12, 2007
 * Time: 10:16:57 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class QuickSort implements Sorter {
    public void sort(Comparable[] arr, int start, int end) {
        int low = start;
        int high = end;
        if(low >= high) {
            return;
        } else if(low == high - 1) {
            if(Compare.gt(arr[low], arr[high])) {
                swap(arr, low, high);
            }
            return;
        }

        /*
         * Pick a pivot and move it out of way
         */
        int pivotIndex = (low + high) / 2;
        Comparable pivot = arr[pivotIndex];
        swap(arr, pivotIndex, high);

        while(low < high) {
            /*
             * Search forward from low until an element is found that
             * is greater than the pivot or low >= high
             */
            while(Compare.le(arr[low], pivot) && low < high) {
                low++;
            }

            /*
             * Search backward from high until an element is found that
             *  is less than pivot or low >= high
             */
            while(Compare.le(pivot, arr[high]) && low < high) {
                high--;
            }

            if(low < high) {
                swap(arr, low, high);
            }
        }

        /*
         * Put the median in the "center" of the list
         */
        arr[end] = arr[high];
        arr[high] = pivot;

        /*
         * Recursive calls, elements arr[start] to arr[low-1] are less than or
         * equal to pivot, elements arr[high]+1 to arr[end] are greater than pivot
         */
        sort(arr, start, low-1);
        sort(arr, high+1, end);
        
    }

    private void swap(Comparable[] arr, int low, int high) {
        Comparable temp = arr[low];
        arr[low] = arr[high];
        arr[high] = temp;
    }

    public static void main(String[] args) {
        final int size = 100000;
        Comparable[] arr = init(size);
        Comparable[] arr2 = (Comparable[]) arr.clone();
        Comparable[] arr3 = (Comparable[]) arr.clone();
        Comparable[] arr4 = (Comparable[]) arr.clone();
        List list2 = Arrays.asList(arr3);
        Sorter sorter = new QuickSort();
        Sorter heapsort = new HeapSort();


        long start = System.currentTimeMillis();
        Arrays.sort(arr2);
        System.out.println("Arrays.sort took "+(System.currentTimeMillis()-start)+" ms.");
        if(!verify(arr2))
            System.out.println("Arrays.sort failed!");
        start = System.currentTimeMillis();
        Collections.sort(list2);
        System.out.println("Collections.sort took "+(System.currentTimeMillis()-start)+" ms.");
        if(!verify(arr3))
            System.out.println("Collections.sort failed!");
        start = System.currentTimeMillis();
        sorter.sort(arr, 0, arr.length-1);
        System.out.println("QuickSort took "+(System.currentTimeMillis()-start)+" ms.");
        if(!verify(arr))
            System.out.println("QuickSort failed!");
        start = System.currentTimeMillis();
        heapsort.sort(arr4, 0, arr.length-1);
        System.out.println("HeapSort took "+(System.currentTimeMillis()-start)+" ms.");
        if(!verify(arr4))
            System.out.println("HeapSort failed!");

    }

    private static boolean verify(final Comparable[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if(Compare.lt(arr[i], arr[i-1])) {
                System.out.println("arr["+i+"]="+arr[i] +" > arr["+(i-1)+"]="+arr[i-1]);
                return false;
            }
        }
        return true;
    }

    private static Comparable[] init(final int size) {
        final Random rand = new Random();
        final Comparable[] arr = new Comparable[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Integer(rand.nextInt());
        }
        return arr;
    }
}
