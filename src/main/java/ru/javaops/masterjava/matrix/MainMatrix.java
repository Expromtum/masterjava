package ru.javaops.masterjava.matrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * gkislin
 * 03.07.2016
 */
public class MainMatrix {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = 10;

    private final static ExecutorService executor = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);

        double optimizedSingleThreadSum = 0.;
        double singleThreadSum = 0.;
        double concurrentThreadSum = 0.;
        int count = 1;

        while (count < 6) {
            System.out.println("Pass " + count);

            long start = System.currentTimeMillis();
            final int[][] matrixC = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
            double duration = (System.currentTimeMillis() - start) / 1000.;

            out("Single thread time, sec: %.3f", duration);

            singleThreadSum += duration;

            //---------------------------------------------------------------------
            start = System.currentTimeMillis();
            final int[][] optimizedMatrixC = MatrixUtil.singleThreadMultiplyOptimized(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;

            out("Optimized single thread time, sec: %.3f", duration);

            optimizedSingleThreadSum += duration;

            if (!MatrixUtil.compare(matrixC, optimizedMatrixC)) {
                System.err.println("Comparison optimized failed");

                break;
            }
            //---------------------------------------------------------------------

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;

            out("Concurrent thread time, sec: %.3f", duration);

            concurrentThreadSum += duration;

            if (!MatrixUtil.compare(matrixC, concurrentMatrixC)) {
                System.err.println("Comparison failed");

                System.err.println("matrixC:");
                out(matrixC);

                System.err.println("");
                System.err.println("concurrentMatrixC:");

                out(concurrentMatrixC);
                break;
            }
            count++;
        }
        executor.shutdown();

        out("\nAverage optimized single thread time, sec: %.3f", optimizedSingleThreadSum / 5.);
        out("\nAverage single thread time, sec: %.3f", singleThreadSum / 5.);
        out("Average concurrent thread time, sec: %.3f", concurrentThreadSum / 5.);
    }

    private static void out (int[][] matrix ) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("\n");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print( matrix[i][j] + " ");
            }}
    }

    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }
}
