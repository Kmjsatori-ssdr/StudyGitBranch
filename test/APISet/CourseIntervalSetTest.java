package APISet;

import static org.junit.Assert.*;

import org.junit.Test;

import IntervalSet.MyExceptions.PeriodBoundaryViolationException;

public class CourseIntervalSetTest {

	// Test Strategy:
	// .getCycleLength()：直接检测返回值
	// .setCycleLength()：检测周期长度变更后会不变引发异常
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testGetAndSetCycleLength() throws Exception {
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>();
		assertEquals(42, cis.getCycleLength());
		cis.setCycleLength(49);
		assertEquals(49, cis.getCycleLength());
	}

	@Test
	public void testSetCycleLengthThrowException1() throws Exception {
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>();
		cis.insert(0, 40, "A");// 不应该抛出异常
		cis.setCycleLength(49);// 不应该抛出异常
	}

	@Test(expected = PeriodBoundaryViolationException.class)
	public void testSetCycleLengthThrowException2() throws Exception {
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>();
		cis.insert(0, 48, "A");// 应该抛出异常
	}

	@Test(expected = PeriodBoundaryViolationException.class)
	public void testSetCycleLengthThrowException3() throws Exception {
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>();
		cis.insert(10, 40, "A");// 不应该抛出异常
		cis.setCycleLength(35);// 应该抛出异常
	}
	@Test
	public void testToString() {
		CourseIntervalSet<String> cis = new CourseIntervalSet<String>();
		assertEquals("周课表长度：42;\n{}", cis.toString());
	}
}
