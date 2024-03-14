package de.mle.stackoverflow.jmh;

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

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(5)
@State(Scope.Benchmark)
public class JMHTesterStringContainsAndRegex {
	private static final String TEXT = """
			Bifröst (einnig nefnd Ásbrú) er brú í norrænni goðafræði.
			Brú þessi liggur á milli Ásgarðs, þar sem goðin eiga heima,
			og Miðgarðs, þar sem mennirnir eiga heima. Brú þessi er
			útskýring norrænnar goðafræði á regnboga. Heimdallur,
			hinn hvíti ás, gætir brúarinnar. Rauði liturinn í þessari
			brú á að vera eldur og verndar hann Ásgarð frá jötnum.
			Æsir ferðast upp þessa brú daglega til að funda undir skugga Asks Yggdrasils.
			""";
	private static final Matcher MATCHER = Pattern.compile(".*Ágarð.*").matcher(TEXT);

	@Benchmark
	public void stringContains(Blackhole bh) {
		bh.consume(TEXT.contains("Ásgarð"));
	}

	@Benchmark
	public void regex(Blackhole bh) {
		bh.consume(MATCHER.find());
	}

	public static void main(String[] args) throws RunnerException {
		new Runner(new OptionsBuilder().include(".*" + JMHTesterStringContainsAndRegex.class.getSimpleName() + ".*").build()).run();
	}
}
