package APISet;

import java.util.List;
import java.util.Set;

import IntervalSet.CommonIntervalSet;
import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.LabelNotFoundException;
import IntervalSet.MyExceptions.PeriodBoundaryViolationException;
import IntervalSetDecorator.PeriodicIntervalSet;

/**
 * 表示了一个课程表。周期长度的默认值为42（即一周中每天有六节课），可以使用setter方法手动重新设置。
 * 
 * @param <L>标签的类型，必须Immutable
 */
public class CourseIntervalSet<L> implements IntervalSet<L> {

	final private PeriodicIntervalSet<L> courseIntervalSet = new PeriodicIntervalSet<L>(new CommonIntervalSet<L>(), 42);
	// Abstract Function:
	// 表示了一个课程表，记录在了courseIntervalSet中。
	// Rep Invariant:
	// 课程不能超出周期长度范围。
	// Safety from Rep:使用private，final，并在必要的地方使用防御性拷贝。

	// checkRep():
	// 已经由装饰器检查。
	@Override
	public void insert(long start, long end, L label) throws Exception {
		courseIntervalSet.insert(start, end, label);
	}

	@Override
	public Set<L> labels() {
		return courseIntervalSet.labels();
	}

	@Override
	public boolean remove(L label) {
		return courseIntervalSet.remove(label);
	}

	@Override
	public List<Interval> intervals(L label) throws LabelNotFoundException {
		return courseIntervalSet.intervals(label);
	}

	// toString()
	@Override
	public String toString() {
		return "周课表长度：" + courseIntervalSet.getCycleLength() + ";\n" + courseIntervalSet.toString();
	}

	/**
	 * 获得当前周课表的长度。
	 * 
	 * @return 当前周课表的长度（即每个循环）
	 */
	public long getCycleLength() {
		return courseIntervalSet.getCycleLength();
	}

	/**
	 * 重新设置当前周课表的长度。
	 * 
	 * @param cycleLength 新的周课表长度
	 * @throws PeriodBoundaryViolationException 如果周期长度修改后原有的时间段超出了新的周期长度
	 */
	public void setCycleLength(long cycleLength) throws Exception {
		courseIntervalSet.setCycleLength(cycleLength);
	}
}
