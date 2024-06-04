package IntervalSetDecorator;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.PeriodBoundaryViolationException;

public class PeriodicIntervalSetTest {

	// Test Strategy：
	// 小于0，大于周期长度，正常
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test(expected = PeriodBoundaryViolationException.class)
	public void testSmaller() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> pI = new PeriodicIntervalSet<String>(i,7);
		pI.insert(-1, 7, "A");
	}

	@Test(expected = PeriodBoundaryViolationException.class)
	public void testBigger() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> pI = new PeriodicIntervalSet<String>(i,7);
		pI.insert(0, 10, "A");
	}

	@Test
	public void testNormal() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> pI = new PeriodicIntervalSet<String>(i,7);
		pI.insert(0, 7, "A");
	}

}
