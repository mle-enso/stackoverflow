package de.mle.stackoverflow.jmh;

import lombok.val;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(5)
@State(Scope.Benchmark)
public class JMHTesterFilterAndMap {
	public static final int PIVOT_ELEMENT = 200;
	private final List<Integer> numbers = List.of(10_000, 1_0000, 100, 100_000, 10);

	@Benchmark
	public void filterFirst(Blackhole bh) {
		val first = numbers.stream()
				.filter(i -> i < PIVOT_ELEMENT)
				.sorted(Comparator.naturalOrder())
				.findFirst();
		bh.consume(first);
	}

	@Benchmark
	public void sortFirst(Blackhole bh) {
		val first = numbers.stream()
				.sorted(Comparator.naturalOrder())
				.filter(i -> i < PIVOT_ELEMENT)
				.findFirst();
		bh.consume(first);
	}

	public static void main(String[] args) throws RunnerException {
		new Runner(new OptionsBuilder().include(".*" + JMHTesterFilterAndMap.class.getSimpleName() + ".*").build()).run();
	}
}