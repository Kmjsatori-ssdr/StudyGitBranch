package IntervalSetDecorator;

import java.util.List;

import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;

/**
 * 具有周期性的IntervalSet。若周期为cycleLength>0，存储的每一个时间段都必须在[0,cycleLength]之中。
 * 
 * @param <L> 标签类型，immutable
 */
public class PeriodicIntervalSet<L> extends Decorator<L> {
	// 谁能告诉我这个玩意怎么写？
	private long cycleLength;// 周期长度为cycle，每一个时间段从都应该在0到cycleLength之中，必须大于0。

	// constructor
	public PeriodicIntervalSet(IntervalSet<L> intervalSet, long cycleLength) {
		super(intervalSet);
		this.cycleLength = cycleLength;
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		if (start < 0 || end < 0 || start > cycleLength || end > cycleLength) {
			throw new PeriodBoundaryViolationException();
		}
		super.insert(start, end, label);
		
	}

	/**
	 * 返回周期长度。
	 * 
	 * @return 周期长度
	 */
	public long getCycleLength() {
		return this.cycleLength;
	}

	/**
	 * 设置周期长度。
	 * 
	 * @param cycleLength 新的周期长度
	 * @throws PeriodBoundaryViolationException 如果周期长度修改后原有的时间段超出了周期长度。
	 */
	public void setCycleLength(long cycleLength) throws Exception {
		this.cycleLength = cycleLength;
		checkPeriodBoundary();
	}

	private void checkPeriodBoundary() throws Exception {
		List<Interval> intervalList = allIntervals();
		for(Interval i:intervalList) {
			if (i.getStart() < 0 || i.getEnd() < 0 || i.getStart() > cycleLength || i.getEnd() > cycleLength) {
				throw new PeriodBoundaryViolationException();
			}
		}
	}
}
