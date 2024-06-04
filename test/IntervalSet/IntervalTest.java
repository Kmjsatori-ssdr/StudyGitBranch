package IntervalSet;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalTest {

	// Test Strategy:
	// 直接测试返回值是否符合预期。
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testGetStart() {
		Interval tp =new Interval(0,1);
		assertEquals(0,tp.getStart());
	}
	@Test
	public void testGetEnd() {
		Interval tp =new Interval(0,1);
		assertEquals(1,tp.getEnd());
	}
	@Test
	public void testToString() {
		Interval tp =new Interval(0,1);
		assertEquals("[0,1]",tp.toString());
	}
}
