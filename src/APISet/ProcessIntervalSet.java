package APISet;

import java.util.List;
import java.util.Set;

import IntervalSet.CommonIntervalSet;
import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.LabelNotFoundException;
import IntervalSetDecorator.NonOverlapIntervalSet;

/**
 * 表示了一个单核CPU在各个时段内执行哪个进程的时间表
 * 
 * @param <L>标签的类型，必须Immutable
 */
public class ProcessIntervalSet<L> implements IntervalSet<L> {

	final private NonOverlapIntervalSet<L> processIntervalSet = new NonOverlapIntervalSet<L>(
			new CommonIntervalSet<L>());
	// Abstract Function:
	// 表示了一个单核CPU在各个时段内执行哪个进程的时间表，记录在了processIntervalSet中。
	// Rep Invariant:
	// 每段时间只能执行一个进程。
	// Safety from Rep:使用private，final，并在必要的地方使用防御性拷贝。

	// checkRep():
	// 已经由装饰器检查。

	@Override
	public void insert(long start, long end, L label) throws Exception {
		processIntervalSet.insert(start, end, label);
	}

	@Override
	public Set<L> labels() {
		return processIntervalSet.labels();
	}

	@Override
	public boolean remove(L label) {
		return processIntervalSet.remove(label);
	}

	@Override
	public List<Interval> intervals(L label) throws LabelNotFoundException {
		return processIntervalSet.intervals(label);
	}

	// toString()
	@Override
	public String toString() {
		return processIntervalSet.toString();
	}
}
