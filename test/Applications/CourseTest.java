package Applications;

import static org.junit.Assert.*;

import org.junit.Test;

public class CourseTest {

	// Test Strategy:
		// 直接测试返回值。
		@Test
		public void test() {
			Course c= new Course("123456", "软件构造", "王忠杰&徐汉川","正心209");
			assertEquals("123456",c.getCourseID());
			assertEquals("软件构造",c.getCourseName());
			assertEquals("王忠杰&徐汉川",c.getTeacherName());
			assertEquals("正心209",c.getLocation());
			assertEquals("课程ID：123456;课程名：软件构造;教师名：王忠杰&徐汉川;上课地点：正心209;",c.toString());
		}

}
