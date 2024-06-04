package IntervalSetDecorator;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.*;

public class NonOverlapIntervalSetTest {
	// Test Strategy：
	// 不同标签有重叠，同标签有重叠
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test(expected = OverlapException.class)
	public void testDifferrentLabel() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nOI = new NonOverlapIntervalSet<String>(i);
		nOI.insert(0, 5, "A");
		nOI.insert(1, 6, "B");
	}

	@Test
	public void testSameLabel() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nOI = new NonOverlapIntervalSet<String>(i);
		nOI.insert(0, 5, "A");
		nOI.insert(1, 6, "A");
	}
}
