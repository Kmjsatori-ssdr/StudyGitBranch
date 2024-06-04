package IntervalSetDecorator;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.LabelMultipleIntervalsException;

public class SingleIntervalSetTest {

	// Test Strategy：
	// 同个标签，不同标签
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test(expected = LabelMultipleIntervalsException.class)
	public void testSameLabel() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		sI.insert(0, 1, "A");
		sI.insert(2, 3, "A");
	}

	@Test
	public void testDifferentLabels() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		sI.insert(0, 1, "A");
		sI.insert(2, 3, "B");
	}

}
