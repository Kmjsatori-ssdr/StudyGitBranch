package IntervalSetDecorator;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;
import IntervalSet.MyExceptions.BlankException;

/**
 * 注：在进行多个Decorator的组合时，请确保这个NoBlank在装饰器链的最后一个，否则可能无法正常访问checkNoBlank方法。
 */
public class NoBlankIntervalSetTest {

	// Test Strategy：
	// 无空白，开头有空白，中间有空白，结尾有空白
	@Test(expected = AssertionError.class)
	public void testAssertionsEnabled() {
		assert false;
	}

	@Test
	public void testNoBlank() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nBI = new NoBlankIntervalSet<String>(i);
		nBI.insert(0, 5, "A");
		nBI.insert(5, 10, "B");
		((NoBlankIntervalSet<String>) nBI).checkNoBlank(0, 10);
	}

	@Test(expected = BlankException.class)
	public void testFrontBlank() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nBI = new NoBlankIntervalSet<String>(i);
		nBI.insert(1, 5, "A");
		nBI.insert(5, 10, "B");
		((NoBlankIntervalSet<String>) nBI).checkNoBlank(0, 10);
	}

	@Test(expected = BlankException.class)
	public void testMidBlank() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nBI = new NoBlankIntervalSet<String>(i);
		nBI.insert(0, 5, "A");
		nBI.insert(6, 10, "B");
		((NoBlankIntervalSet<String>) nBI).checkNoBlank(0, 10);
	}

	@Test(expected = BlankException.class)
	public void testBackBlank() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> nBI = new NoBlankIntervalSet<String>(i);
		nBI.insert(0, 5, "A");
		nBI.insert(5, 9, "B");
		((NoBlankIntervalSet<String>) nBI).checkNoBlank(0, 10);
	}
}
