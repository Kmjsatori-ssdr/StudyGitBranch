package IntervalSet;

/**
 * 表示一个区间段。 Immutable.
 */
public class Interval {
	private final long start;
	private final long end;
	// Abstract Function: 表示一个“区间段”。
	// Rep Invariant:end>start
	// Safety from Rep:使用private和final。

	// checkRep()
	private void checkRep() {
		assert start < end;
	}
	// constructor

	public Interval(long start, long end) {
		this.start = start;
		this.end = end;
		checkRep();
	}

	/**
	 * 返回开始时间。
	 * 
	 * @return 开始时间
	 */
	public long getStart() {
		return this.start;
	}

	/**
	 * 返回结束时间。
	 * 
	 * @return 结束时间
	 */
	public long getEnd() {
		return this.end;
	}

	// toString()
	@Override
	public String toString() {
		String s = "[" + start + "," + end + "]";
		return s;
	}

	// equals()
	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Interval))
			return false;
		Interval thatInterval = (Interval) thatObject;
		if (thatInterval.start == this.start && thatInterval.end == this.end)
			return true;
		else
			return false;
	}

	// hashCode()
	@Override
	public int hashCode() {
		// 溢出？这也是设计的一部分。
		return (int) this.start * 114 + (int) this.end * 514;
	}
}