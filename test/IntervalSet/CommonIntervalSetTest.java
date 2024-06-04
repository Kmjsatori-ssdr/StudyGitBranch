package IntervalSet;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 对CommonIntervalSet的测试。
 * 
 * 这个类用CommonIntervalSet跑一遍IntervalSetInstanceTest, 并补上了这个类特有的一些方法如toString()
 * 
 * 对实例方法spec的测试已经在IntervalSetInstanceTest中.
 */
public class CommonIntervalSetTest extends IntervalSetInstanceTest {

	@Override
	public IntervalSet<String> emptyInstance() {
		return new CommonIntervalSet<String>();
	}

	// Test Strategy:
	// 等价类划分：
	// 对象为空，对象内有一个标签，对象内有多个标签
	// 一个标签对应一个时间段，一个标签对应多个时间段
	@Test
	public void testToString() {
		IntervalSet<String> i = emptyInstance();
		assertEquals("{}", i.toString());
		try {
			i.insert(0, 1, "A");
			assertEquals("{\"A\"=[[0,1]]}", i.toString());
			i.insert(2, 3, "A");
			assertEquals("{\"A\"=[[0,1],[2,3]]}", i.toString());
			i.insert(0, 1, "B");
			assertEquals("{\"A\"=[[0,1],[2,3]],\"B\"=[[0,1]]}", i.toString());
		} catch (Exception e) {
			fail("抛出了意外的异常" + e);
		}
	}
}
