package Applications;

/**
 * 表示一个员工。Immutable。
 */
public class Employee {
	final private String name;
	final private String duty;
	final private String phoneNumber;

	// Abstract Function: 表示了一个员工。field表示这个员工的属性
	// name，duty，phoneNumber分别表示姓名，职务，电话号码。
	// Rep Invariant:field中元素不是null
	// Safety from Rep:使用private，final。
	// constructor
	public Employee(String name, String duty, String phoneNumber) {
		this.name = name;
		this.duty = duty;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 获取员工的姓名。
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 获取员工的职务。
	 * 
	 * @return 职务
	 */
	public String getDuty() {
		return this.duty;
	}

	/**
	 * 获取员工的电话号码。
	 * 
	 * @return 电话号码
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@Override
	public String toString() {
		String s = new String();
		s = "姓名：" + this.name + ";职务：" + this.duty + ";电话号码：" + this.phoneNumber + ";";
		return s;
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Employee))
			return false;
		else {
			Employee thatEmployee = (Employee) thatObject;
			return this.name.equals(thatEmployee.name) && this.duty.equals(thatEmployee.duty)
					&& this.phoneNumber.equals(thatEmployee.phoneNumber);
		}
	}

	@Override
	public int hashCode() {
		return (this.name.hashCode() + this.duty.hashCode() + this.phoneNumber.hashCode());
	}
}
