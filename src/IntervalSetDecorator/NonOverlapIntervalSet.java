package IntervalSetDecorator;

import java.util.List;

import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;

/**
 * 不允许不同标签的区间中有重叠的IntervalSet。
 * 
 * @param <L> 标签的类型，必须Immutable
 */
public class NonOverlapIntervalSet<L> extends Decorator<L> {
	// constructor
	public NonOverlapIntervalSet(IntervalSet<L> intervalSet) {
		super(intervalSet);
	}

	private void checkNoOverlap() throws Exception {
		List<Interval> intervalList = allIntervals();
		// 把每个标签对应的时间段全部加入一个List中，由于IntervalSet的spec，只需检查新的List中有没有出现重叠。
		for (int i = 0; i < intervalList.size() - 1; i++) {
			if (intervalList.get(i).getEnd() > intervalList.get(i + 1).getStart()) {
				throw new OverlapException();
			}
		}
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		super.insert(start, end, label);
		checkNoOverlap();
	}
}
