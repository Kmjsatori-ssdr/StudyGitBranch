package IntervalSetDecorator;

import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;

/**
 * 每个标签只允许对应一个时间段的IntervalSet。
 * 
 * @param <L> 标签的类型，必须Immutable
 */
public class SingleIntervalSet<L> extends Decorator<L> {
	// constructor
	public SingleIntervalSet(IntervalSet<L> intervalSet) {
		super(intervalSet);
	}

	private void checkSingleInterval() throws Exception {
		for (L label : intervalSet.labels()) {
			if (intervalSet.intervals(label).size() != 1) {
				throw new LabelMultipleIntervalsException();
			}
		}
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		super.insert(start, end, label);
		checkSingleInterval();
	}
}
