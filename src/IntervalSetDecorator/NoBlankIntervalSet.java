package IntervalSetDecorator;

import java.util.List;

import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;

/**
 * 不允许在给定的时间段中有空白的IntervalSet。
 * 
 * @param <L> 标签的类型，必须Immutable
 */
public class NoBlankIntervalSet<L> extends Decorator<L> {
	// constructor
	public NoBlankIntervalSet(IntervalSet<L> intervalSet) {
		super(intervalSet);
	}

	/**
	 * 检查当前IntervalSet中在start到end中间是否有空白的时间段。
	 * 
	 * @throws BlankException 如果时间段中有空白
	 */
	public void checkNoBlank(long start, long end) throws Exception {
		// 原来这一段代码和NonOverlapIntervalSet的checkNoOverlap中有重复
		// 这是否违反了DRY的原则？这样修复是否恰当？
		// 反正已经用一个allIntervals方法解决了。
		List<Interval> intervalList = allIntervals();
		if (intervalList.isEmpty() && start != end) {
			throw new BlankException();
		}
		if (start >= end) {
			return;
		}
		// 把每个标签对应的时间段全部加入一个List中，由于IntervalSet的spec，只需检查新的List中有没有出现空白
		if (start < intervalList.get(0).getStart()) {
			throw new BlankException();
		}
		if (intervalList.get(intervalList.size() - 1).getEnd() < end) {
			throw new BlankException();
		}
		for (int i = 0; i < intervalList.size() - 1; i++) {
			if (intervalList.get(i).getEnd() < intervalList.get(i + 1).getStart()) {
				throw new BlankException();
			}
		}
	}
}
