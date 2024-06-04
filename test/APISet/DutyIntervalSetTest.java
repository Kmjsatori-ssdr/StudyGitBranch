package APISet;

import static org.junit.Assert.*;

import org.junit.Test;

import IntervalSet.MyExceptions.BlankException;

public class DutyIntervalSetTest {

	// Test Strategy:
	// getters and setters：直接测试返回值。
	// checkNoBlank()：有空白，无空白
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testGettersSetters() {
		DutyIntervalSet<String> dis = new DutyIntervalSet<String>();
		dis.setStartEndTime(0, 50);
		assertEquals(0, dis.getStartTime());
		assertEquals(50, dis.getEndTime());
	}

	@Test
	public void testCheckNoBlank1() throws Exception {
		DutyIntervalSet<String> dis = new DutyIntervalSet<String>();
		dis.setStartEndTime(0, 50);
		dis.insert(0, 25, "A");
		dis.insert(25, 50, "B");
		dis.checkNoBlank();// 不该抛出异常
	}

	@Test(expected = BlankException.class)
	public void testCheckNoBlank2() throws Exception {
		DutyIntervalSet<String> dis = new DutyIntervalSet<String>();
		dis.setStartEndTime(0, 50);
		dis.insert(0, 25, "A");
		dis.insert(26, 50, "B");
		dis.checkNoBlank();// 该抛出异常
	}
	@Test
	public void testToString() {
		DutyIntervalSet<String> dis = new DutyIntervalSet<String>();
		dis.setStartEndTime(0, 50);
		assertEquals("必须有人值班的时间：[0,50];\n{}", dis.toString());
	}
}
