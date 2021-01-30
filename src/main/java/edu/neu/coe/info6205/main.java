package edu.neu.coe.info6205;

import edu.neu.coe.info6205.sort.simple.InsertionSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(3);
        list.add(4);
        System.out.println(list);
        Integer[] xs = list.toArray(new Integer[0]);
        int from = 0;
        int to = 4;
        for (int i = from + 1; i < to; i++){
            for (int j = i; j > from && (((Comparable) xs[j-1]).compareTo(xs[j]) > 0); j--){
                Integer t = xs[j-1];
                xs[j-1] = xs[j];
                xs[j] = t;
            }
        }
        System.out.println(Arrays.toString(xs));
    }
}
