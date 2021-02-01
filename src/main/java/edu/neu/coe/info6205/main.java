package edu.neu.coe.info6205;

import edu.neu.coe.info6205.sort.simple.InsertionSort;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Destination;
import tech.tablesaw.io.csv.CsvWriter;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class main {

    public static final int RANDOM = 0;
    public static final int ORDERED = 1;
    public static final int PART_ORDERED = 2;
    public static final int REVERSE_ORDERED = 3;
    private static final List<String> categories = new ArrayList<>();
    private static final List<Double> results = new ArrayList<>();
    private static final List<Double> Y = new ArrayList<>();
    private static final List<Integer> X = new ArrayList<>();
    private static final List<Integer> N = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        for (int n = 256; n <= 8192; n *= 2){
            sort(n);
        }
        plotChart();
    }

    public static List<Integer> getArrayList(int n, int type){
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i < n; i++){
            array.add(i);
        }
        switch (type){
            case RANDOM:
                categories.add("Random");
                Collections.shuffle(array);
                break;
            case ORDERED:
                categories.add("Ordered");
                break;
            case PART_ORDERED:
                categories.add("Partially_Ordered");
                List<Integer> array2 = array.subList(n/2, n);
                Collections.shuffle(array2);
                break;
            case REVERSE_ORDERED:
                categories.add("Reverse_Ordered");
                Collections.reverse(array);
                break;
        }
        return array;
    }

    public static void sort(int n){
        InsertionSort is = new InsertionSort();
        for (int i = 0; i < 4; i++){
            List<Integer> list = getArrayList(n, i);
            Integer[] xs = list.toArray(new Integer[0]);
            Benchmark<Integer[]> timer = new Benchmark_Timer<>("InsertionSort experiments", b->{
                is.sort(xs);
            }, null);
            double time = timer.run(xs, 1000);
            N.add(n);
            results.add(time);
            //log2(T(N))
            Y.add(Math.log(time)/Math.log(2));
            X.add((int) (Math.log(n)/Math.log(2)));
        }
    }

    public static void plotChart() throws IOException {
        Table table = createTable(X, Y, categories);
        CsvWriter writer = new CsvWriter();
        File file = new File("Results.csv");
        Destination destination = new Destination(file);
        writer.write(table, destination);
        Plot.show(LinePlot.create("log-log plot", table, "log2(N)", "log2(T(N))", "categories"));
        System.out.println(table.print());
    }

    public static Table createTable(List<Integer> x, List<Double> y, List<String> categories){
        Integer [] n = new Integer[N.size()];
        IntColumn number = IntColumn.create("N", N.toArray(n));
        Integer [] x_column = new Integer[x.size()];
        Double [] result = new Double[results.size()];
        DoubleColumn t = DoubleColumn.create("T(N)", results.toArray(result));
        IntColumn logN = IntColumn.create("log2(N)", x.toArray(x_column));
        Double [] y_column = new Double[y.size()];
        DoubleColumn logT = DoubleColumn.create("log2(T(N))", y.toArray(y_column));
        String [] groups = new String[categories.size()];
        StringColumn group = StringColumn.create("categories", categories.toArray(groups));
        Table table = Table.create(number, t, logN, logT, group);
        return table;
    }
}
