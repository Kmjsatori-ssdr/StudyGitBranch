package APISet;

import java.util.List;
import java.util.Set;

import IntervalSet.CommonIntervalSet;
import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;
import IntervalSetDecorator.NoBlankIntervalSet;
import IntervalSetDecorator.NonOverlapIntervalSet;
import IntervalSetDecorator.SingleIntervalSet;

/**
 * 表示了一组从开始时间到结束时间的排班表，记录了需要排班的时间段内每一天的安排情况。
 * 
 * @param <L>标签的类型，必须Immutable
 */
public class DutyIntervalSet<L> implements IntervalSet<L> {
	final private NoBlankIntervalSet<L> dutyIntervalSet = new NoBlankIntervalSet<L>(
			new NonOverlapIntervalSet<L>(new SingleIntervalSet<L>(new CommonIntervalSet<L>())));
	private long startTime;
	private long endTime;

	// Abstract Function:
	// 表示了一组从开始时间到结束时间的排班表，记录了需要排班的时间段内每一天的安排情况并记录在了dutyIntervalSet中。
	// Rep Invariant:
	// 1.startTime>endTime
	// 2.同一天不能连续安排人
	// 3.一个人如果安排多天只能是连续多天
	// 4一天只能安排一个人
	// 5.startTime到endTime之间不能无人值班；
	// Safety from Rep:使用private，final，并在必要的地方使用防御性拷贝。

	// checkRep():
	private void checkRep() {
		assert startTime < endTime;
		// 其他RI已经由装饰器检查。
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		dutyIntervalSet.insert(start, end, label);
	}

	@Override
	public Set<L> labels() {
		return dutyIntervalSet.labels();
	}

	@Override
	public boolean remove(L label) {
		boolean bool = dutyIntervalSet.remove(label);
		return bool;
	}

	@Override
	public List<Interval> intervals(L label) throws LabelNotFoundException {
		return dutyIntervalSet.intervals(label);
	}

	// toString()
	@Override
	public String toString() {
		return "必须有人值班的时间：[" + this.startTime + "," + this.endTime + "];\n" + dutyIntervalSet.toString();
	}

	/**
	 * 全部插入完成后，调用此方法检查从开始时间到结束时间是否有某天无人值班。 使用之前必须通过setter方法初始化起始时间和结束时间。
	 * 
	 * @throws BlankException 确实存在某天无人值班
	 */
	public void checkNoBlank() throws Exception {
		dutyIntervalSet.checkNoBlank(startTime, endTime);
	}

	/**
	 * 返回开始时间。
	 * 
	 * @return 开始时间
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * 返回结束时间。
	 * 
	 * @return 结束时间
	 */
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * 设置startTime和endTime的值。必须满足startTime<endTime。
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public void setStartEndTime(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		checkRep();
	}
}
