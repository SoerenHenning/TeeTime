package teetime.stage.util;

/**
 * @since 2.x
 *
 * @author Robin Mohr
 *
 */
public final class QuicksortProblem {

	private int low;
	private int high;
	private final int[] numbers;

	/**
	 * An implementation of a quicksort problem.
	 *
	 * @param low
	 *            Pointer to the lower bound of indices to be compared in the array
	 * @param high
	 *            Pointer to the upper bound of indices to be compared in the array
	 * @param numbers
	 *            Array to be sorted
	 */
	public QuicksortProblem(final int low, final int high, final int[] numbers) {
		this.low = low;
		this.high = high;
		this.numbers = numbers;
	}

	public int getLow() {
		return this.low;
	}

	public int getHigh() {
		return this.high;
	}

	public void setHigh(final int high) {
		this.high = high;
	}

	public void setLow(final int low) {
		this.low = low;
	}

	public int[] getNumbers() {
		return this.numbers;
	}
}
