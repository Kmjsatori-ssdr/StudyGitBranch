package Applications;

/**
 * 表示一个进程。Immutable。
 */
public class Process {
	final private String processID;
	final private String processName;
	final private long minTime;
	final private long maxTime;

	// Abstract Function: 表示了一个课程，field表示了这门课的基础信息
	// processID,processName,minTime,maxTime分别表示进程ID，进程名，最短执行时间，最长执行时间。
	// Rep Invariant:field中元素不是null
	// Safety from Rep:使用private，final。
	// constructor
	public Process(String processID, String processName, long minTime, long maxTime) {
		this.processID = processID;
		this.processName = processName;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	/**
	 * 获取课程ID。
	 * 
	 * @return 课程ID
	 */
	public String getProcessID() {
		return this.processID;
	}

	/**
	 * 获取课程名字。
	 * 
	 * @return 课程名字
	 */
	public String getProcessName() {
		return this.processName;
	}

	/**
	 * 获取任课教师姓名。
	 * 
	 * @return 任课教师姓名
	 */
	public long getMinTime() {
		return this.minTime;
	}

	/**
	 * 获取上课地点。
	 * 
	 * @return 上课地点
	 */
	public long getMaxTime() {
		return this.maxTime;
	}

	@Override
	public String toString() {
		String s = new String();
		s = "进程：" + this.processID + ";进程名：" + this.processName + ";最短执行时间：" + this.minTime + ";最长执行时间：" + this.maxTime
				+ ";";
		return s;
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Process))
			return false;
		else {
			Process thatProcess = (Process) thatObject;
			return this.processID.equals(thatProcess.processID) && this.processName.equals(thatProcess.processName)
					&& this.minTime == thatProcess.minTime && this.maxTime == thatProcess.maxTime;
		}
	}

	@Override
	public int hashCode() {
		return this.processID.hashCode() + this.processName.hashCode() + (int) (this.minTime * this.maxTime);
	}
}
