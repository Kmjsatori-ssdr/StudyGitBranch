package Applications;

import static org.junit.Assert.*;

import org.junit.Test;

public class EmployeeTest {
	// Test Strategy:
	// 直接测试返回值。
	@Test
	public void test() {
		Employee e = new Employee("张三", "保安", "114514");
		assertEquals("张三",e.getName());
		assertEquals("保安",e.getDuty());
		assertEquals("114514",e.getPhoneNumber());
		assertEquals("姓名：张三;职务：保安;电话号码：114514;",e.toString());
	}

}
