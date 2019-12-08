package matrix;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(2)
@Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
public class MatrixBenchmark {
    // Matrix size
    private static final int MATRIX_SIZE = 1000;

    @Param({"3", "4", "10", "12"})
    private int threadNumber;

    private static int[][] matrixA;
    private static int[][] matrixB;

    private ExecutorService executor;

    @Setup
    public void setUp() {
        matrixA = MatrixUtil.create(MATRIX_SIZE);
        matrixB = MatrixUtil.create(MATRIX_SIZE);
    }

    @Setup
    public void setup() {
        executor = Executors.newFixedThreadPool(threadNumber);
    }

    @TearDown
    public void tearDown() {
        executor.shutdown();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MatrixBenchmark.class.getSimpleName())
                .threads(1)
                .forks(1)
                .timeout(TimeValue.minutes(5))
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public int[][] singleThreadMultiplyOpt() throws Exception {
        return MatrixUtil.singleThreadMultiply(matrixA, matrixB);
    }

    @Benchmark
    public int[][] concurrentMultiply() throws Exception {
        return MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiplyDarthVader() throws Exception {
        return MatrixUtil.concurrentMultiplyDarthVader(matrixA, matrixB, executor);
    }
}
