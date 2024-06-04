package APIs;

import static org.junit.Assert.*;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;

public class APIsTest {
	// Test Strategy:
	// similarity():
	// 对于每个小区间：有标签vs空，单个标签vs两个标签
	// 对于整个大区间：中间有空
	// calcCOnfictRatio():
	// 有冲突，无冲突
	// calcFreeTimeRatio():
	// 空set，从头到尾无空，有空
	@Test
	public void testSimularity() throws Exception {
		APIs<String> apis = new APIs<String>();
		IntervalSet<String> i1 = new CommonIntervalSet<String>();
		IntervalSet<String> i2 = new CommonIntervalSet<String>();
		i1.insert(0, 5, "A");
		i1.insert(20, 25, "A");
		i1.insert(10, 20, "B");
		i1.insert(25, 30, "B");
		i2.insert(20, 35, "A");
		i2.insert(10, 20, "B");
		i2.insert(0, 5, "C");
		// 到此为止即为实验指导书上的那个图
		assertEquals((double) 3 / 7, apis.Similarity(i1, i2), 0.0001);
		i2.insert(25, 30, "B");
		assertEquals((double) 3 / 7, apis.Similarity(i1, i2), 0.0001);
		i1.insert(25, 30, "A");
		assertEquals((double) 4 / 7, apis.Similarity(i1, i2), 0.0001);
	}

	@Test
	public void testCalcConflictRatio() throws Exception {
		APIs<String> apis = new APIs<String>();
		IntervalSet<String> i = new CommonIntervalSet<String>();
		i.insert(0, 5, "A");
		i.insert(5, 15, "B");
		assertEquals((double) 0, apis.calcConflictRatio(i), 0.0001);
		i.insert(5, 10, "A");
		assertEquals((double) 1 / 3, apis.calcConflictRatio(i), 0.0001);
		i.insert(5, 10, "C");
		assertEquals((double) 1 / 3, apis.calcConflictRatio(i), 0.0001);
		i.insert(10, 15, "C");
		assertEquals((double) 2 / 3, apis.calcConflictRatio(i), 0.0001);
	}

	@Test
	public void testCalcFreeTimeRatio() throws Exception {
		APIs<String> apis = new APIs<String>();
		IntervalSet<String> i = new CommonIntervalSet<String>();
		assertEquals((double) 1, apis.calcFreeTimeRatio(i, 0, 15), 0.0001);
		i.insert(0, 5, "A");
		i.insert(10, 15, "B");
		assertEquals((double) 1 / 3, apis.calcFreeTimeRatio(i, 0, 15), 0.0001);
		i.insert(5, 10, "A");
		assertEquals((double) 0, apis.calcFreeTimeRatio(i, 0, 15), 0.0001);
	}
}
