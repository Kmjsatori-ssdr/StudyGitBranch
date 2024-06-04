package APISet;

import static org.junit.Assert.*;

import org.junit.Test;

import IntervalSet.MyExceptions.OverlapException;

public class ProcessIntervalSetTest {

	// Test Strategy：
	// 有重叠，无重叠
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testNoOverlap() throws Exception {
		ProcessIntervalSet<String> pis = new ProcessIntervalSet<String>();
		pis.insert(0, 10, "A");
		pis.insert(20, 30, "A");
		pis.insert(10, 20, "B");
	}

	@Test(expected = OverlapException.class)
	public void testOverlap() throws Exception {
		ProcessIntervalSet<String> pis = new ProcessIntervalSet<String>();
		pis.insert(0, 10, "A");
		pis.insert(20, 30, "A");
		pis.insert(10, 21, "B");// 应该抛出异常
	}
}
