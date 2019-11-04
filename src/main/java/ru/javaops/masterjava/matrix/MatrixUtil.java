package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int aColumns = matrixA.length, aRows = matrixA.length;
        final int bColumns = matrixB.length, bRows = matrixB.length;
        final int[][] matrixC = new int[matrixA.length][matrixB.length];
        for (int i=0; i < aRows; i++) {
            for (int j=0; j < bColumns; j++) {
                int finalJ = j;
                int finalI = i;
                executor.execute(() -> {
                    int sum = 0;
                    for (int k = 0; k < matrixB.length; k++) {
                        sum += matrixA[finalI][k] * matrixB[k][finalJ];
                    }
                    matrixC[finalI][finalJ] = sum;
                });
            }
        }
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
