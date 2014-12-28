package util.test;

import java.util.Map;

import util.test.eval.StatisticsUtil;

public class PerformanceResult {

	public long overallDurationInNs;
	public long sumInNs;
	public Map<Double, Long> quantiles;
	public long avgDurInNs;
	public long confidenceWidthInNs;

	public PerformanceResult() {}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("overallDurationInNs: ");
		stringBuilder.append(this.overallDurationInNs);
		stringBuilder.append("\n");

		stringBuilder.append("sumInNs: ");
		stringBuilder.append(this.sumInNs);
		stringBuilder.append("\n");

		stringBuilder.append("avgDurInNs: ");
		stringBuilder.append(this.avgDurInNs);
		stringBuilder.append("\n");

		stringBuilder.append("confidenceWidthInNs: ");
		stringBuilder.append(this.confidenceWidthInNs);
		stringBuilder.append("\n");

		stringBuilder.append(StatisticsUtil.getQuantilesString(this.quantiles));

		return stringBuilder.toString();
	}
}
