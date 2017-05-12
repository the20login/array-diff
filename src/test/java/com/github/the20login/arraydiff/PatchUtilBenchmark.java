package com.github.the20login.arraydiff;

import com.github.the20login.arraydiff.patch.Patch;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 5, jvmArgs = {"-XX:+AggressiveOpts"})
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public class PatchUtilBenchmark {
    private static final int SIZE = 1_000_000;
    private static final int INSERTS = 10_000;
    private static final int REMOVES = 10_000;
    private int[] initial;
    private int[] target;
    private Patch patch;

    @Setup
    public void init() {
        Random rand = new Random(666);
        initial = rand.ints(SIZE).toArray();
        List<Integer> list = IntStream.of(initial).boxed().collect(Collectors.toList());
        for (int i = 0; i < INSERTS; i++) {
            list.add(rand.nextInt(SIZE), rand.nextInt());
        }
        for (int i = 0; i < REMOVES; i++) {
            list.remove(rand.nextInt(SIZE));
        }
        target = list.stream().mapToInt(i -> i).toArray();
        patch = PatchUtil.createPatch(initial, target);
    }

    @Benchmark
    public Patch createPatch() {
        return PatchUtil.createPatch(initial, target);
    }

    @Benchmark
    public int[] applyPatch() {
        return patch.apply(initial);
    }
}
