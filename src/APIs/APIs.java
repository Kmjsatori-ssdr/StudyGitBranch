package APIs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import IntervalSet.Interval;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.LabelNotFoundException;

/**
 * 
 * @param <L>对应IntervalSet的标签类型。Immutable
 */
public class APIs<L> {
	/**
	 * 计算两个IntervalSet对象的相似度。
	 * 
	 * @param s1 IntervalSet对象
	 * @param s2 IntervalSet对象
	 * @return 相似度
	 */
	public double Similarity(IntervalSet<L> s1, IntervalSet<L> s2) {

		Set<L> labelSet1 = s1.labels();
		Set<L> labelSet2 = s2.labels();
		List<Interval> ls1 = allIntervals(s1);// 已经排好序
		List<Interval> ls2 = allIntervals(s2);
		long startTime = Math.min(ls1.get(0).getStart(), ls2.get(0).getStart());
		long endTime = startTime;
		// 按开始时间排序完成并不意味按结束时间也已经排序完成
		for (Interval i1 : ls1) {
			if (i1.getEnd() > endTime)
				endTime = i1.getEnd();
		}
		for (Interval i2 : ls2) {
			if (i2.getEnd() > endTime)
				endTime = i2.getEnd();
		}
		List<Interval> splitedIntervals1 = splitIntervals(ls1);
		long coincideLengthCount = 0;
		// 对s1中每一个子区间，去看看s2对应的区间里标签集和是不是完全一样的一样
		try {
			for (Interval i : splitedIntervals1) {
				Set<L> labelsOfI1 = new HashSet<L>();// 1中小区间i对应的所有标签集合
				Set<L> labelsOfI2 = new HashSet<L>();// 2中小区间i对应的所有标签集合
				// 寻找s1中i对应的标签
				for (Interval i1 : ls1) {
					if (isSubInterval(i1, i)) {
						for (L lables1 : labelSet1) {
							// 找到所有标签
							if (s1.intervals(lables1).contains(i1))
								labelsOfI1.add(lables1);
						}
						break;
					}
				}
				// 寻找s2中i对应的标签
				for (Interval i2 : ls2) {
					if (isSubInterval(i2, i)) {
						for (L lables2 : labelSet2) {
							// 找到所有标签
							if (s2.intervals(lables2).contains(i2))
								labelsOfI2.add(lables2);
						}
						break;
					}
				}
				if (labelsOfI1.equals(labelsOfI2)) {
					coincideLengthCount += i.getEnd() - i.getStart();
					// System.out.println(i.getStart() + " " + i.getEnd());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();// 不可能发生
		}
		// System.out.println(coincideLengthCount + " " + (endTime - startTime));
		if (endTime - startTime == 0)// 防止除以0
			return 0;
		else
			return (double) coincideLengthCount / (endTime - startTime);
	}

	/**
	 * 计算Interval对象中的时间冲突比例。
	 * 
	 * @param set IntervalSet对象
	 * @return 冲突比例
	 */
	public double calcConflictRatio(IntervalSet<L> set) {
		Set<L> labelSet = set.labels();
		List<Interval> list = allIntervals(set);// 已经排好序
		List<Interval> splitedIntervals = splitIntervals(list);
		long startTime = list.get(0).getStart();
		long endTime = startTime;
		// 按开始时间排序完成并不意味按结束时间也已经排序完成
		for (Interval i : list) {
			if (i.getEnd() > endTime)
				endTime = i.getEnd();
		}
		long conflictLengthCount = 0;
		try {
			for (Interval i : splitedIntervals) {
				Set<L> labelsOfI = new HashSet<L>();// 1中小区间i对应的所有标签集合
				// 寻找s中i对应的标签
				for (Interval i1 : list) {
					if (isSubInterval(i1, i)) {
						for (L lable : labelSet) {
							// 找到所有标签
							if (set.intervals(lable).contains(i1)) {
								labelsOfI.add(lable);
								// System.out.println(labelsOfI + " " + i);
							}
						}
					}
				}
				if (labelsOfI.size() >= 2) {
					conflictLengthCount += i.getEnd() - i.getStart();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();// 不可能发生
		}
		if (endTime - startTime == 0)// 防止除以0
			return 0;
		else
			return conflictLengthCount / (double) (endTime - startTime);
	}

	/**
	 * 计算一个 IntervalSet象中的空闲时间比例。
	 * 
	 * @param set IntervalSet对象
	 * @return 空闲比例
	 */
	public double calcFreeTimeRatio(IntervalSet<L> set, long startTime, long endTime) {
		// 最简单的一个先写好了
		List<Interval> list = allIntervals(set);// 已经排好序
		if (list.size() == 0)
			return 1;
		// 按开始时间排序完成并不意味按结束时间也已经排序完成
		for (Interval i : list) {
			if (i.getEnd() > endTime)
				endTime = i.getEnd();
		}
		// 将list中有重叠的合并，可以复用之前写好的CommonIntervalSet.mergeIntervals()：
		List<Interval> mergedList = new ArrayList<Interval>();
		Interval toMerge = list.get(0);
		for (int i = 0; i < list.size(); i++) {
			Interval current = list.get(i);
			// 如果当前区间的开始小于或等于待合并区间的结束，则合并
			if (current.getStart() <= toMerge.getEnd()) {
				toMerge = new Interval(Math.min(toMerge.getStart(), current.getStart()),
						Math.max(toMerge.getEnd(), current.getEnd()));

			} else {
				mergedList.add(toMerge);
				toMerge = current;
			}
		}
		mergedList.add(toMerge);
		// mergeIntervals()结束
		long busyTime = 0;
		for (Interval i : mergedList) {
			busyTime += i.getEnd() - i.getStart();
		}
		return (endTime - startTime == 0) ? 1 : (1 - (double) busyTime / (endTime - startTime));
	}

	/**
	 * 将所有标签对应的每一个Interval加入到一个List中并排序。
	 * 
	 * @param set IntervalSet对象
	 * @return 一个按Interval开始时间排序的IntervalList
	 */
	public List<Interval> allIntervals(IntervalSet<L> set) {
		Set<L> labels = set.labels();
		List<Interval> intervalList = new ArrayList<Interval>();
		for (L label : labels) {
			// 把每个标签对应的时间段全部加入一个List中
			try {
				intervalList.addAll(set.intervals(label));
			} catch (LabelNotFoundException e) {
				// 其实不可能发生
				e.printStackTrace();
			}
		}
		Collections.sort(intervalList, Comparator.comparingLong(Interval::getStart));
		return intervalList;
	};

	/**
	 * 将一个Interval List中所有出现重叠的区间进行拆分
	 * 例：[0,4],[1,5]->[0,1],[1,4],[4,5]，如果一个小区间有三重重叠也是同理同理
	 * 
	 * @param list 一个Interval的List
	 * @return
	 */
	private List<Interval> splitIntervals(List<Interval> list) {
		// 使用迭代的方法，往结果数组中逐个加入区间，每加入一个区间检查和区间原有的是否有重叠
		// 每一次迭代后都应该重新排序
		if (list.size() <= 1)
			return list;
		List<Interval> result = new ArrayList<Interval>();
		Set<Long> edges = new HashSet<Long>();
		for (Interval i : list) {
			edges.add(i.getStart());
			edges.add(i.getEnd());
		}
		List<Long> edgesList = new ArrayList<Long>(edges);
		Collections.sort(edgesList);
		for (int i = 0; i < edgesList.size() - 1; i++) {
			Interval newInterval = new Interval(edgesList.get(i), edgesList.get(i + 1));
			for (Interval superI : list) {
				if (isSubInterval(superI, newInterval)) {
					result.add(newInterval);
					break;
				}
			}
		}
		return result;
	};

	private boolean isSubInterval(Interval superI, Interval subI) {
		return subI.getStart() >= superI.getStart() && subI.getEnd() <= superI.getEnd();
	}
}
