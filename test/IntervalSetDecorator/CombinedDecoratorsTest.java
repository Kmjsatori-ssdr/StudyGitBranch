package IntervalSetDecorator;

import IntervalSet.MyExceptions.*;

import org.junit.Test;

import IntervalSet.CommonIntervalSet;
import IntervalSet.IntervalSet;

public class CombinedDecoratorsTest {
	// Test Strategy:
	// 两者结合，四者结合
	// （补充，不是四者结合我还真没发现有两个玩意结合起来是有bug的）
	@Test(expected = LabelMultipleIntervalsException.class)
	public void testTwoDecorators1() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		nOSI.insert(0, 10, "A");
		nOSI.insert(20, 30, "A");
	}

	@Test(expected = OverlapException.class)
	public void testTwoDecorators2() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		nOSI.insert(0, 10, "A");
		nOSI.insert(5, 30, "B");
	}

	@Test(expected = LabelMultipleIntervalsException.class)
	public void testFourDecorators1() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		IntervalSet<String> pNOSI = new PeriodicIntervalSet<String>(nOSI, 30);
		IntervalSet<String> nBPNOSI = new NoBlankIntervalSet<String>(pNOSI);
		nBPNOSI.insert(0, 10, "A");
		nBPNOSI.insert(20, 30, "A");
	}

	@Test(expected = OverlapException.class)
	public void testFourDecorators2() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		IntervalSet<String> pNOSI = new PeriodicIntervalSet<String>(nOSI, 30);
		IntervalSet<String> nBPNOSI = new NoBlankIntervalSet<String>(pNOSI);
		nBPNOSI.insert(0, 10, "A");
		nBPNOSI.insert(5, 30, "B");
	}

	@Test(expected = PeriodBoundaryViolationException.class)
	public void testFourDecorators3() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		IntervalSet<String> pNOSI = new PeriodicIntervalSet<String>(nOSI, 30);
		IntervalSet<String> nBPNOSI = new NoBlankIntervalSet<String>(pNOSI);
		nBPNOSI.insert(0, 40, "A");
	}

	@Test(expected = BlankException.class)
	public void testFourDecorators4() throws Exception {
		IntervalSet<String> i = new CommonIntervalSet<String>();
		IntervalSet<String> sI = new SingleIntervalSet<String>(i);
		IntervalSet<String> nOSI = new NonOverlapIntervalSet<String>(sI);
		IntervalSet<String> pNOSI = new PeriodicIntervalSet<String>(nOSI, 30);
		IntervalSet<String> nBPNOSI = new NoBlankIntervalSet<String>(pNOSI);
		nBPNOSI.insert(0, 10, "A");
		nBPNOSI.insert(11, 30, "B");
		((NoBlankIntervalSet<String>) nBPNOSI).checkNoBlank(0, 30);
	}
}
