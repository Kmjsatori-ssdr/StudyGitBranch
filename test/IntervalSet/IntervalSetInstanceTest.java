package IntervalSet;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import IntervalSet.MyExceptions.*;

import org.junit.Test;

/**
 * 测试IntervalSet的实例方法。 闹不好还有人想写一个IntervalSet的其他实现
 * 所有就不直接测试CommonIntervalSet了，反正只需要遵循spec就可以了
 */
public abstract class IntervalSetInstanceTest {
	// Test Strategy:
	// .insert():不存在待加入标签，已经存在待加入标签且和已有时间段没有重叠，和已有时间段有重叠，和已有时间段首尾相接
	// 按照起始时间顺序加入，不按顺序加入
	// .labels():为空或不为空
	// .remove():标签已经在对象中或者不在
	// .intervals():不存在此标签，存在此标签且只有一个与之对应的时间段，存在此标签且有多个与之对应的时间段
	//
	public abstract IntervalSet<String> emptyInstance();

	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testInsert() {
		IntervalSet<String> i = emptyInstance();
		try {
			// 不存在此标签
			i.insert(0, 2, "A");
			assertEquals("{\"A\"=[[0,2]]}", i.toString());
			// 存在且不重叠
			i.insert(3, 5, "A");
			assertEquals("{\"A\"=[[0,2],[3,5]]}", i.toString());
			i.remove("A");
			i.insert(0, 2, "A");
			// 存在且首尾相接
			i.insert(2, 4, "A");
			assertEquals("{\"A\"=[[0,4]]}", i.toString());
			// 存在且有部分重叠
			i.insert(3, 5, "A");
			assertEquals("{\"A\"=[[0,5]]}", i.toString());
			// 不按顺序加入
			i.insert(10, 20, "A");
			i.insert(6, 9, "A");
			assertEquals("{\"A\"=[[0,5],[6,9],[10,20]]}", i.toString());
		} catch (Exception e) {
			fail("抛出了意外的异常" + e);
		}

	}

	@Test
	public void testLabels() {
		IntervalSet<String> i = emptyInstance();
		Set<String> s = new HashSet<String>();
		assertEquals(s, i.labels());
		try {
			i.insert(0, 1, "A");
		} catch (Exception e) {
			fail("抛出了意外的异常" + e);
		}
		s.add("A");
		assertEquals(s, i.labels());
	}

	@Test
	public void testRemove() {
		IntervalSet<String> i = emptyInstance();
		assertEquals(false, i.remove("A"));
		try {
			i.insert(0, 1, "A");
		} catch (Exception e) {
			fail("抛出了意外的异常" + e);
		}
		assertEquals(true, i.remove("A"));
		assertEquals(Collections.emptySet(), i.labels());
	}

	@Test(expected = LabelNotFoundException.class)
	public void testIntervalsThrowException() throws LabelNotFoundException {
		IntervalSet<String> i = emptyInstance();
		i.intervals("A");// 应该抛出LabelNotFoundException
	}

	@Test
	public void testIntervals() {
		IntervalSet<String> i = emptyInstance();
		Interval zero_One = new Interval(0, 1);
		Interval two_Three = new Interval(2, 3);
		List<Interval> s = new ArrayList<Interval>();
		try {
			i.insert(0, 1, "A");
			s.add(zero_One);
			assertEquals(s, i.intervals("A"));
			i.insert(2, 3, "A");
			s.add(two_Three);
			assertEquals(s, i.intervals("A"));
		} catch (Exception e) {
			// 实际上应该永远不发生。。。
			fail("抛出了意外的异常：" + e);
		}
	}
}
