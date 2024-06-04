package Applications;

/**
 * 表示一个课程。Immutable。
 */
public class Course {
	final private String courseID;
	final private String courseName;
	final private String teacherName;
	final private String location;

	// Abstract Function: 表示了一个课程，field表示了这门课的基础信息
	// courseID,courseName,teacherName,location分别表示课程ID，课程名，任课教师姓名和上课地点。
	// Rep Invariant:field中元素不是null
	// Safety from Rep:使用private，final。
	// constructor
	public Course(String courseID, String courseName, String teacherName, String location) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.teacherName = teacherName;
		this.location = location;
	}

	/**
	 * 获取课程ID。
	 * 
	 * @return 课程ID
	 */
	public String getCourseID() {
		return this.courseID;
	}

	/**
	 * 获取课程名字。
	 * 
	 * @return 课程名字
	 */
	public String getCourseName() {
		return this.courseName;
	}

	/**
	 * 获取任课教师姓名。
	 * 
	 * @return 任课教师姓名
	 */
	public String getTeacherName() {
		return this.teacherName;
	}

	/**
	 * 获取上课地点。
	 * 
	 * @return 上课地点
	 */
	public String getLocation() {
		return this.location;
	}

	@Override
	public String toString() {
		String s = new String();
		s = "课程ID：" + this.courseID + ";课程名：" + this.courseName + ";教师名：" + this.teacherName + ";上课地点：" + this.location
				+ ";";
		return s;
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Course))
			return false;
		else {
			Course thatCourse = (Course) thatObject;
			return this.courseID.equals(thatCourse.courseID) && this.courseName.equals(thatCourse.courseName)
					&& this.teacherName.equals(thatCourse.teacherName) && this.location.equals(thatCourse.location);
		}
	}

	@Override
	public int hashCode() {
		return this.courseID.hashCode() + this.courseName.hashCode() + this.teacherName.hashCode()
				+ this.location.hashCode();
	}
}
