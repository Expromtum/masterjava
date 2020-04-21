package ru.javaops.masterjava.matrix;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {

        class MultiResult {
            private final int col;
            private final int[] cols;

            public MultiResult(int col, int[] cols) {
                this.col = col;
                this.cols = cols;
            }
        }

        final CompletionService<MultiResult> completionService = new ExecutorCompletionService<>(executor);

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        double BT[][] = new double[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                BT[j][i] = matrixB[i][j];
            }
        }

        List<Future<MultiResult>> futures = Stream.iterate(0, col -> col + 1)
                .limit(matrixSize)
                .map(col ->
                        completionService.submit(() -> {
                                    int[] cols = new int[matrixSize];
                                    for (int j = 0; j < matrixSize; j++) {
                                        int sum = 0;
                                        for (int k = 0; k < matrixSize; k++) {
                                            sum += matrixA[col][k] * BT[j][k];
                                        }
                                        cols[j] = sum;
                                    }

                                    return new MultiResult(col, cols);
                                }
                        )
                ).collect(Collectors.toList());

        while (!futures.isEmpty()) {
            Future<MultiResult> future = completionService.take();
            futures.remove(future);

            MultiResult res = future.get();

            if (res == null)
                return new int[matrixSize][matrixSize];

            for (int j = 0; j < res.cols.length; j++) {
                matrixC[res.col][j] = res.cols[j];
            }
        }

        return matrixC;
    }


    // optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiplyOptimized(int[][] matrixA, int[][] matrixB) {
        // TODO: Проверка наличия хотя бы одной строки в массиве

        final int aColumns = matrixA[0].length;
        final int aRows = matrixA.length;
        final int bColumns = matrixB[0].length;
        final int bRows = matrixB.length;

        final int[][] matrixC = new int[aRows][aRows];

        int thatColumn[] = new int[bRows];

        try {
            for (int j = 0; ; j++) {
                for (int k = 0; k < aColumns; k++) {
                    thatColumn[k] = matrixB[k][j];
                }

                for (int i = 0; i < aRows; i++) {
                    int thisRow[] = matrixA[i];
                    int summand = 0;
                    for (int k = 0; k < aColumns; k++) {
                        summand += thisRow[k] * thatColumn[k];
                    }
                    matrixC[i][j] = summand;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
