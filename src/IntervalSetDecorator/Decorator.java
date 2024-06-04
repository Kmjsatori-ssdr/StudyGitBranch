package IntervalSetDecorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;
import IntervalSet.Interval;

public class Decorator<L> implements IntervalSet<L> {

	protected IntervalSet<L> intervalSet;

	// constructor
	public Decorator() {
		this.intervalSet = IntervalSet.empty();
		// 。。这对吗，不敢写， 我泛型呢
	}

	public Decorator(IntervalSet<L> intervalSet) {
		this.intervalSet = intervalSet;
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		intervalSet.insert(start, end, label);
	}

	@Override
	public Set<L> labels() {
		return intervalSet.labels();
	}

	@Override
	public boolean remove(L label) {
		return intervalSet.remove(label);
	}

	@Override
	public List<Interval> intervals(L label) throws LabelNotFoundException {
		return intervalSet.intervals(label);
	}

	@Override
	public String toString() {
		return intervalSet.toString();
	}

	/**
	 * 将所有标签对应的每一个Interval加入到一个List中并排序。 也就NonOverlap和NoBlank两个需要但是就写了算了。
	 * 
	 * @return 一个按Interval开始时间排序的IntervalList
	 */
	protected List<Interval> allIntervals() throws Exception {
		Set<L> labels = this.intervalSet.labels();
		List<Interval> intervalList = new ArrayList<Interval>();
		for (L label : labels) {
			// 把每个标签对应的时间段全部加入一个List中
			intervalList.addAll(intervalSet.intervals(label));
		}
		Collections.sort(intervalList, Comparator.comparingLong(Interval::getStart));
		return intervalList;
	};

}
