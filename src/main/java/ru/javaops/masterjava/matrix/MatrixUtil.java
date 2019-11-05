package ru.javaops.masterjava.matrix;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        class ColumnMultipleResult {
            private final int col;
            private final int[] columnC;

            private ColumnMultipleResult(int col, int[] columnC) {
                this.col = col;
                this.columnC = columnC;
            }
        }

        final CompletionService<ColumnMultipleResult> completionService = new ExecutorCompletionService<>(executor);

        for (int j = 0; j < matrixSize; j++) {
            final int col = j;
            final int[] columnB = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][col];
            }
            completionService.submit(() -> {
                final int[] columnC = new int[matrixSize];

                for (int row = 0; row < matrixSize; row++) {
                    final int[] rowA = matrixA[row];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += rowA[k] * columnB[k];
                    }
                    columnC[row] = sum;
                }
                return new ColumnMultipleResult(col, columnC);
            });
        }

        for (int i = 0; i < matrixSize; i++) {
            ColumnMultipleResult res = completionService.take().get();
            for (int k = 0; k < matrixSize; k++) {
                matrixC[k][res.col] = res.columnC[k];
            }
        }
        return matrixC;
    }

    public static int[][] concurrentMultiplyDarthVader(int[][] matrixA, int[][] matrixB, ExecutorService executor)
            throws InterruptedException, ExecutionException {

        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<Void>> tasks = IntStream.range(0, matrixSize)
                .parallel()
                .mapToObj(i -> new Callable<Void>() {
                    private final int[] tempColumn = new int[matrixSize];

                    @Override
                    public Void call() throws Exception {
                        for (int c = 0; c < matrixSize; c++) {
                            tempColumn[c] = matrixB[c][i];
                        }
                        for (int j = 0; j < matrixSize; j++) {
                            int row[] = matrixA[j];
                            int sum = 0;
                            for (int k = 0; k < matrixSize; k++) {
                                sum += tempColumn[k] * row[k];
                            }
                            matrixC[j][i] = sum;
                        }
                        return null;
                    }
                })
                .collect(Collectors.toList());

        executor.invokeAll(tasks);
        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int aColumns = matrixA.length, aRows = matrixA.length;
        final int bColumns = matrixB.length, bRows = matrixB.length;
        final int[][] matrixC = new int[matrixA.length][matrixB.length];
        int BT[][] = new int[bColumns][bRows];
        for (int i = 0; i < bRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                BT[j][i] = matrixB[i][j];
            }
        }
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aColumns; j++) {
                int summand = 0;
                for (int k = 0; k < bColumns; k++) {
                    summand += matrixA[i][k] * BT[j][k];
                }
                matrixC[i][j] = summand;
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
