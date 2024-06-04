package IntervalSet;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import IntervalSet.MyExceptions.*;

public class CommonIntervalSet<L> implements IntervalSet<L> {
	final private Set<L> labels = new HashSet<L>();
	final private Map<L, List<Interval>> intervalMap = new HashMap<L, List<Interval>>();

	// Abstract Function: 表示了一组在时间轴上分布的“时间段”，每个时间段附着一个特定的标签
	// 其中labels表示所有标签的集合，intervalMap表示标签所对应的所有时间段的map。
	// Rep Invariant:每个标签至少关联了一个时间段，每个标签关联的多个时间段的下标按起始时间排序，
	// 多个时间段不能有重合或首尾相接。
	// Safety from Rep:使用private，final，并在必要的地方使用防御性拷贝。

	// checkRep()
	private void checkRep() {
		for (L label : labels()) {
			assert intervalMap.containsKey(label);
		}
		for (L label : intervalMap.keySet()) {
			List<Interval> list = intervalMap.get(label);
			assert list.size() != 0;
			for (int i = 0; i < list.size() - 1; i++) {
				assert list.get(i).getEnd() < list.get(i + 1).getStart();
			}
		}

	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		// 自己作的，写这种spec，导致唯独这个方法的实现比较复杂
		if (!(start < end))
			throw new IllegalArgumentException();
		else {
			if (labels.contains(label)) {
				Interval tp = new Interval(start, end);
				List<Interval> list = intervalMap.get(label);
				list.add(tp);
				List<Interval> merged = mergeIntervals(list);
				intervalMap.put(label, merged);
			} else {
				// 一个新标签被加入的情况
				labels.add(label);
				Interval tp = new Interval(start, end);
				List<Interval> list = new ArrayList<Interval>();
				list.add(tp);
				intervalMap.put(label, list);
			}
		}
		checkRep();
	}

	@Override
	public Set<L> labels() {
		return new HashSet<L>(this.labels);// 防御性拷贝
	}

	@Override
	public boolean remove(L label) {
		if (this.labels().contains(label)) {
			this.labels.remove(label);
			this.intervalMap.remove(label);
			checkRep();
			return true;
		} else
			return false;
	}

	@Override
	public List<Interval> intervals(L label) throws LabelNotFoundException {
		if (this.labels().contains(label)) {
			List<Interval> l = new ArrayList<Interval>(intervalMap.get(label));
			return l;
		} else
			throw new LabelNotFoundException(label + "is not found the interval set");
	}

	// toString()
	@Override
	public String toString() {
		if (labels.size() == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (L label : labels) {
			sb.append("\"" + label.toString() + "\"=[");
			for (Interval timePeriod : intervalMap.get(label)) {
				sb.append(timePeriod.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);// 移除一个多余的逗号
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);// 移除一个多余的逗号
		sb.append("}");
		return new String(sb);
	}

	/**
	 * 将一个Intervals List中重复和首尾相接的时间段合并。 将会修改传入的list，最后list将是合并重复和首尾相接后的，仍然按开始时间升序排序。
	 * 
	 */
	private List<Interval> mergeIntervals(List<Interval> list) {
		// 贪心算法
		if (list.size() <= 1)
			// 如果list只有少于1个时间段那就没有合并的需要了
			return list;
		Collections.sort(list, Comparator.comparingLong(Interval::getStart));
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
		// 忘记加入最后一个待合并的区间曾经导致了一个巨大bug↓
		mergedList.add(toMerge);
		return mergedList;
	}
}