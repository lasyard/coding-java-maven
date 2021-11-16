package io.github.lasyard.spark.app;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public final class SparkApp {
    private SparkApp() {
    }

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("SparkApp");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        List<Integer> integers = Arrays.asList(1, 7, 2, 8, 5, 3, 6);
        JavaRDD<Integer> rdd = sc.parallelize(integers);
        List<Integer> results = rdd
            .filter(x -> x % 2 == 0)
            .sortBy(x -> x, true, 0)
            .map(x -> x / 2)
            .collect();
        System.out.println(results);
    }
}
