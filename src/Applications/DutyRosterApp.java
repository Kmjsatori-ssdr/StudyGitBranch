package Applications;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.*;

import APISet.DutyIntervalSet;
import APIs.APIs;
import IntervalSet.Interval;
import IntervalSet.MyExceptions.BlankException;
import IntervalSet.MyExceptions.LabelNotFoundException;
import IntervalSet.MyExceptions.OverlapException;

public class DutyRosterApp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 271396013041713891L;
	private static JLabel startDateLabel, endDateLabel;
	private static JTextField startDateField;
	private static JTextField endDateField;
	private static JButton setDateButton, addEmployeeButton, removeEmployeeButton;
	private static JButton manualScheduleButton, removeScheduleButton, autoScheduleButton, readFileButton,
			checkScheduleButton;
	private static DefaultListModel<String> employeeListModel;
	private static JList<String> employeeList;
	private static DefaultListModel<String> scheduleListModel;
	private static JList<String> scheduleList;

	final private static LocalDate ZEROPOINT = LocalDate.of(2020, 1, 1);
	private static LocalDate startDay;
	private static LocalDate endDay;
	private static long numStart;
	private static long numEnd;
	private static DutyIntervalSet<Employee> intervalSet = new DutyIntervalSet<Employee>();
	private static Map<Employee, Boolean> employeeMap = new HashMap<Employee, Boolean>();// boolean值为true表示已经加入到值班表中，不能被删除

	// Window Constructor.
	public DutyRosterApp() {
		setTitle("排班管理系统");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 将窗口中心显示

		// 初始化UI组件
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel datePanel = new JPanel();

		JPanel employeePanel = new JPanel(new BorderLayout());
		JPanel employeeButtonPanel = new JPanel(); // 创建一个新的面板用于存放按钮

		JPanel schedulePanel = new JPanel(new BorderLayout());
		JPanel scheduleButtonPanel = new JPanel();

		startDateLabel = new JLabel("开始日期（yyyy-mm-dd):");
		startDateField = new JTextField(10);
		endDateLabel = new JLabel("结束日期(yyyy-mm-dd):");
		endDateField = new JTextField(10);
		setDateButton = new JButton("确认设置日期");

		addEmployeeButton = new JButton("增加员工");
		removeEmployeeButton = new JButton("删除选中员工");

		manualScheduleButton = new JButton("手动排班");
		removeScheduleButton = new JButton("删除指定员工排班");
		autoScheduleButton = new JButton("自动排班");
		readFileButton = new JButton("从文件中读取员工与排班信息");
		checkScheduleButton = new JButton("检查排班");

		datePanel.add(startDateLabel);
		datePanel.add(startDateField);
		datePanel.add(endDateLabel);
		datePanel.add(endDateField);
		datePanel.add(setDateButton);

		employeeListModel = new DefaultListModel<>();
		employeeList = new JList<>(employeeListModel);

		employeeButtonPanel.add(addEmployeeButton);
		employeeButtonPanel.add(removeEmployeeButton);

		employeePanel.add(employeeButtonPanel, BorderLayout.NORTH);
		employeePanel.add(new JScrollPane(employeeList), BorderLayout.CENTER);

		scheduleListModel = new DefaultListModel<>();
		scheduleList = new JList<>(scheduleListModel);

		scheduleButtonPanel.add(manualScheduleButton);
		scheduleButtonPanel.add(removeScheduleButton);
		scheduleButtonPanel.add(autoScheduleButton);
		scheduleButtonPanel.add(readFileButton);

		schedulePanel.add(scheduleButtonPanel, BorderLayout.NORTH);
		schedulePanel.add(new JScrollPane(scheduleList), BorderLayout.CENTER);
		schedulePanel.add(checkScheduleButton, BorderLayout.SOUTH);

		mainPanel.add(datePanel, BorderLayout.NORTH);
		mainPanel.add(employeePanel, BorderLayout.WEST);
		mainPanel.add(schedulePanel, BorderLayout.CENTER);

		add(mainPanel);

		// 事件监听器
		setDateButton.addActionListener(e -> {
			setDate();
		});

		addEmployeeButton.addActionListener(e -> {
			addEmployee();
		});

		removeEmployeeButton.addActionListener(e -> {
			removeEmployee();
		});

		manualScheduleButton.addActionListener(e -> {
			manualSchedule();
		});

		removeScheduleButton.addActionListener(e -> {
			removeSchedule();
		});

		autoScheduleButton.addActionListener(e -> {
			autoSchedule();
		});

		readFileButton.addActionListener(e -> {
			readFile();
		});

		checkScheduleButton.addActionListener(e -> {
			checkSchedule();
		});
	}

	/**
	 * 设置日期。
	 */
	private static void setDate() {
		String startDateStr = startDateField.getText();
		String endDateStr = endDateField.getText();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate startDate = LocalDate.parse(startDateStr, formatter);
			LocalDate endDate = LocalDate.parse(endDateStr, formatter);

			if (!startDate.isBefore(endDate)) {
				throw new IllegalArgumentException();
			}

			// 设置
			startDay = startDate;
			endDay = endDate;
			numStart = ChronoUnit.DAYS.between(ZEROPOINT, startDay);
			numEnd = ChronoUnit.DAYS.between(ZEROPOINT, endDay);
			intervalSet.setStartEndTime(numStart, numEnd);
			JOptionPane.showMessageDialog(null,
					"Date set successfully: " + startDay.toString() + "~" + endDay.toString(), "Success",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (DateTimeParseException e) {
			// 弹出窗口显示异常
			JOptionPane.showMessageDialog(null, "Invalid Date Format, Please use yyyy-MM-dd format for dates.", // 消息内容
					"Error", // 标题
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			// 弹出窗口显示异常
			JOptionPane.showMessageDialog(null, "Invalid Date, start date must be after end date.", // 消息内容
					"Error", // 标题
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 添加一个员工。要求不能有名字，职务和电话号码都完全相同的员工。
	 */
	private static void addEmployee() {
		// 弹出输入对话框
		String name = JOptionPane.showInputDialog(null, "请输入员工的姓名:", // 对话框的消息
				"添加员工", // 对话框的标题
				JOptionPane.PLAIN_MESSAGE);
		if (name == null || name.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "已取消输入。", // 消息内容
					"取消输入", // 标题
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		String duty = JOptionPane.showInputDialog(null, "请输入员工的职务:", // 对话框的消息
				"添加员工", // 对话框的标题
				JOptionPane.PLAIN_MESSAGE);
		if (duty == null || duty.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "已取消输入。", // 消息内容
					"取消输入", // 标题
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		String phoneNumber = JOptionPane.showInputDialog(null, "请输入员工的电话号码:", // 对话框的消息
				"添加员工", // 对话框的标题
				JOptionPane.PLAIN_MESSAGE);
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "已取消输入。", // 消息内容
					"取消输入", // 标题
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		Employee employee = new Employee(name, duty, phoneNumber);
		// 检查员工是否已经存在
		if (employeeMap.containsKey(employee)) {
			JOptionPane.showMessageDialog(null, "该员工已经存在，添加失败！", // 消息内容
					"Warning", // 标题
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 添加员工到employeeMap中，并设置为未排班
		employeeMap.put(employee, false);
		employeeListModel.addElement(employee.toString());
	}

	/**
	 * 删除一个员工。
	 */
	private static void removeEmployee() {
		int selectedIndex = employeeList.getSelectedIndex();
		if (selectedIndex != -1) {
			int choice = JOptionPane.showConfirmDialog(null, "您确定要删除选中的员工吗？", "确认删除", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				// 确认删除，但是先检查一下map中这个员工能不能删？
				String employeeStr = employeeListModel.get(selectedIndex);
				Employee employeeToRemove = strToEmployee(employeeStr);
				if (employeeMap.get(employeeToRemove) == true) {
					JOptionPane.showMessageDialog(null, "该员工已经被安排在排班表中，请先删除排班表中该员工相关信息！", // 消息内容
							"Error", // 标题
							JOptionPane.ERROR_MESSAGE);
				} else {
					employeeListModel.removeElementAt(selectedIndex);
					employeeMap.remove(employeeToRemove);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "请先选中一个员工！");
		}
	}

	/**
	 * 手动安排一个员工的值班。
	 */
	private static void manualSchedule() {
		if (startDay == null || endDay == null) {
			JOptionPane.showMessageDialog(null, "在设置排班前请先设置排班的始末日期！", // 消息内容
					"Warning", // 标题
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int selectedIndex = employeeList.getSelectedIndex();
		if (selectedIndex != -1) {
			String employeeStr = employeeListModel.get(selectedIndex);
			Employee employeeToSchedule = strToEmployee(employeeStr);
			// 输入值班起止时间
			try {
				String startDateStr = JOptionPane.showInputDialog(null, "请输入该员工值班的起始日期：", // 对话框的消息
						"手动排班", // 对话框的标题
						JOptionPane.PLAIN_MESSAGE);
				if (startDateStr == null || startDateStr.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "已取消输入。", // 消息内容
							"取消输入", // 标题
							JOptionPane.PLAIN_MESSAGE);
					return;
				}
				LocalDate startDate = LocalDate.parse(startDateStr);
				String endDateStr = JOptionPane.showInputDialog(null, "请输入该员工值班的结束日期：", // 对话框的消息
						"手动排班", // 对话框的标题
						JOptionPane.PLAIN_MESSAGE);
				if (endDateStr == null || endDateStr.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "已取消输入。", // 消息内容
							"取消输入", // 标题
							JOptionPane.PLAIN_MESSAGE);
					return;
				}
				LocalDate endDate = LocalDate.parse(endDateStr);
				if (startDate.isAfter(endDate)) {
					throw new IllegalArgumentException("Invalid Date, start date must be after end date.");
				}
				// 保存在内部数据结构中
				employeeMap.put(employeeToSchedule, true);
				long start = ChronoUnit.DAYS.between(ZEROPOINT, startDate);
				long end = ChronoUnit.DAYS.between(ZEROPOINT, endDate);
				intervalSet.insert(start, end, employeeToSchedule);
				// 显示在窗口上
				refreshScheduleList();
				JOptionPane.showMessageDialog(null, "已成功设置。", // 消息内容
						"Success", // 标题
						JOptionPane.PLAIN_MESSAGE);
			} catch (DateTimeParseException e) {
				// 弹出窗口显示异常
				JOptionPane.showMessageDialog(null, "Invalid Date Format, Please use yyyy-MM-dd format for dates.", // 消息内容
						"Error", // 标题
						JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException e) {
				// 弹出窗口显示异常
				JOptionPane.showMessageDialog(null, e.getMessage(), // 消息内容
						"Error", // 标题
						JOptionPane.ERROR_MESSAGE);
			} catch (OverlapException e) {
				intervalSet.remove(employeeToSchedule);
				employeeMap.put(employeeToSchedule, false);
				JOptionPane.showMessageDialog(null, "不同员工的值班时间不能重叠！", // 消息内容
						"Error", // 标题
						JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "请先选中一个员工！");
		}

	}

	/**
	 * 删除一个员工的全部排班
	 */
	private static void removeSchedule() {
		int selectedIndex = employeeList.getSelectedIndex();
		if (selectedIndex != -1) {
			String employeeStr = employeeListModel.get(selectedIndex);
			Employee employeeToRemoveSchedule = strToEmployee(employeeStr);
			intervalSet.remove(employeeToRemoveSchedule);
			employeeMap.put(employeeToRemoveSchedule, false);
			refreshScheduleList();
			JOptionPane.showMessageDialog(null, "删除成功!");
		} else {
			JOptionPane.showMessageDialog(null, "请先选中一个员工！");
		}
	}

	/**
	 * 自动安排所有员工的值班。
	 */
	private static void autoSchedule() {
		if (startDay == null || endDay == null) {
			JOptionPane.showMessageDialog(null, "在设置排班前请先设置排班的始末日期！", // 消息内容
					"Warning", // 标题
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int count = employeeMap.keySet().size();
		if (count == 0) {
			JOptionPane.showMessageDialog(null, "员工列表为空，请先添加员工信息！", // 消息内容
					"Warning", // 标题
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 清空已有排班信息
		for (Employee e : intervalSet.labels()) {
			intervalSet.remove(e);
			employeeMap.put(e, false);
		}
		Set<Long> times = new HashSet<Long>();
		times.add(numStart);
		times.add(numEnd);
		Random random = new Random();
		while (times.size() != count + 1) {
			// 注意：因为nextInt(n)生成的是[0, n)范围内的数，所以需要调整范围
			long randomNum = numStart + random.nextLong(numEnd - numStart + 1);
			times.add(randomNum);
		}

		java.util.List<Long> timeList = new ArrayList<Long>(times);
		Collections.sort(timeList);
		int i = 0;
		for (Employee e : employeeMap.keySet()) {
			try {
				intervalSet.insert(timeList.get(i), timeList.get(i + 1), e);
				employeeMap.put(e, true);
				i++;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		refreshScheduleList();
	}

	private static void readFile() {
		// 模拟有限状态自动机
		String filePath = "src/Applications/input.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean readingEmployee = false;
			boolean readingRoster = false;
			String regex = "}\\s*//.*";// 每一行后注释的正则表达式
			StringBuilder currentEmployeeLines = new StringBuilder();
			StringBuilder currentRosterLines = new StringBuilder();
			// 读取文件内容
			while ((line = reader.readLine()) != null) {
				// 忽略空行和注释
				if (line.trim().isEmpty() || line.trim().startsWith("//")) {
					continue;
				} else
				// 读取员工信息
				if (line.startsWith("Employee{")) {
					readingEmployee = true;

					// 跳过开始标记
					continue;
				} else if (readingEmployee) {
					if (line.startsWith("}")) {
						readingEmployee = false;
						// 处理Employee块的结束
					} else {
						// 添加当前行到Employee块
						line = line.replaceAll(regex, "}");
						currentEmployeeLines.append(line);
						currentEmployeeLines.append("\n");
					}
					continue;
				} else
				// 处理Period块
				if (line.startsWith("Period{")) {
					// 假设Period块只有一行
					line = line.replaceAll(regex, "}");
					String[] dates = line.substring("Period{".length(), line.length() - 1).split(",");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					startDateField.setText(dates[0]);
					endDateField.setText(dates[1]);
					startDay = LocalDate.parse(dates[0].trim(), formatter);
					endDay = LocalDate.parse(dates[1].trim(), formatter);
					numStart = ChronoUnit.DAYS.between(ZEROPOINT, startDay);
					numEnd = ChronoUnit.DAYS.between(ZEROPOINT, endDay);
					intervalSet.setStartEndTime(numStart, numEnd);
					continue;
				} else
				// 处理Roster块
				if (line.startsWith("Roster{")) {
					readingRoster = true;
					// 跳过开始标记
					continue;
				} else

				if (readingRoster) {
					if (line.startsWith("}")) {
						readingRoster = false;
						// Roster块结束，不需要额外处理
					} else {
						// 每行一个排班计划条目
						line = line.replaceAll(regex, "}");
						currentRosterLines.append(line);
						currentRosterLines.append("\n");
					}
				} else
					throw new IOException("文件内容格式不正确！");
			}

			// 处理文件内容
			// 添加员工信息
			String EmployeeLines[] = currentEmployeeLines.toString().split("\n");
			String RosterLines[] = currentRosterLines.toString().split("\n");
			for (String e : EmployeeLines) {
				String tempE = e.replace("{", "\n").replace("}", "\n").replace(",", "\n");
				String[] employeeData = tempE.split("\n");// 0名字1职务2电话号码
				Employee employee = new Employee(employeeData[0], employeeData[1], employeeData[2]);
				// 检查员工是否已经存在
				if (!employeeMap.containsKey(employee)) {
					employeeMap.put(employee, false);
					employeeListModel.addElement(employee.toString());
				}
				// 添加员工到employeeMap中，并设置为未排班
			}
			for (String r : RosterLines) {
				String tempR = r.replace("{", "\n").replace("}", "\n").replace(",", "\n");
				String[] rosterData = tempR.split("\n");// 0名字1起始日期2结束日期
				Employee employeeToArrange = null;
				// 寻找要安排的员工
				for (Employee employee : employeeMap.keySet()) {
					if (employee.getName().equals(rosterData[0])) {
						employeeToArrange = employee;
						break;
					}
				}
				if (employeeToArrange == null) {
					throw new IOException("员工 " + rosterData[0] + " 不在值班表中，无法安排值班！");
				}
				LocalDate startDate = LocalDate.parse(rosterData[1]); // 使用try-catch来处理可能的解析错误
				LocalDate endDate = LocalDate.parse(rosterData[2]); // 同样处理可能的解析错误
				// 检查日期是否有效
				if (startDate.isAfter(endDate)) {
					throw new IOException("起始日期不能晚于中止日期！");
				}
				employeeMap.put(employeeToArrange, true);
				long start = ChronoUnit.DAYS.between(ZEROPOINT, startDate);
				long end = ChronoUnit.DAYS.between(ZEROPOINT, endDate);
				intervalSet.insert(start, end, employeeToArrange);
			}
			refreshScheduleList();
			JOptionPane.showMessageDialog(null, "读取完成。");
		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查是否已经安排完毕，没有空闲时间段.
	 */
	private static void checkSchedule() {
		if (startDay == null || endDay == null) {
			JOptionPane.showMessageDialog(null, "请先设置值班日期!");
			return;
		}
		try {
			intervalSet.checkNoBlank();
			JOptionPane.showMessageDialog(null, "值班表已经安排完成!");
		} catch (BlankException e) {
			String message = new String();
			APIs<Employee> apis = new APIs<Employee>();
			double freeTimeRatio = apis.calcFreeTimeRatio(intervalSet, numStart, numEnd);
			message += "仍存在时间无人值班.无人值班比例:" + freeTimeRatio + "\n无人值班时间：";
			java.util.List<Interval> allI = apis.allIntervals(intervalSet);
			if (!allI.isEmpty()) {
				if (numStart < allI.get(0).getStart()) {
					message += startDay + "~" + ZEROPOINT.plusDays(allI.get(0).getStart()) + "\n";
				}
				for (int i = 0; i < allI.size() - 1; i++) {
					if (allI.get(i).getEnd() < allI.get(i + 1).getStart())
						message += ZEROPOINT.plusDays(allI.get(i).getEnd()) + "~"
								+ ZEROPOINT.plusDays(allI.get(i + 1).getStart()) + "\n";
				}
				if (numEnd > allI.get(allI.size() - 1).getEnd()) {
					message += endDay + "~" + ZEROPOINT.plusDays(allI.get(allI.size() - 1).getEnd()) + "\n";
				}
			}
			JOptionPane.showMessageDialog(null, message, // 消息内容
					"Warning", // 标题
					JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	//
	// 其他可复用的内部方法
	//
	//
	/**
	 * 将Employee对象的toString()字符串转换回Employee对象。
	 * 
	 * @param employeeStr Employee对象的toString()结果
	 * @return 对应Employee对象
	 */
	private static Employee strToEmployee(String employeeStr) {
		String[] parts = employeeStr.split(";");
		String name = parts[0].substring(parts[0].indexOf("：") + 1);
		String duty = parts[1].substring(parts[1].indexOf("：") + 1);
		String phoneNumber = parts[2].substring(parts[2].indexOf("：") + 1);
		return new Employee(name, duty, phoneNumber);
	}

	/**
	 * 刷新安排列表。
	 */
	private static void refreshScheduleList() {
		scheduleListModel.clear();
		Set<Employee> labels = intervalSet.labels();

		// 小小写一个方便排序的局部类
		class ScheduleItem {
			private long numStart;
			private long numEnd;
			private Employee employee;

			public ScheduleItem(long numStart, long numEnd, Employee employee) {
				this.numStart = numStart;
				this.numEnd = numEnd;
				this.employee = employee;
			}

			public long getStart() {
				return this.numStart;
			}

			public long getEnd() {
				return this.numEnd;
			}

			public Employee getEmployee() {
				return this.employee;
			}
		}

		java.util.List<ScheduleItem> scheduleList = new ArrayList<ScheduleItem>();
		for (Employee label : labels) {
			try {
				Interval i = intervalSet.intervals(label).get(0);
				long numStartDate = i.getStart();
				long numEndDate = i.getEnd();
				ScheduleItem si = new ScheduleItem(numStartDate, numEndDate, label);
				scheduleList.add(si);
			} catch (LabelNotFoundException e) {
				e.printStackTrace();
			}
		}

		Collections.sort(scheduleList, (s1, s2) -> Long.compare(s1.getStart(), s2.getStart()));
		for (ScheduleItem si : scheduleList) {
			// 可以选择将天数转换回日期格式，或者直接显示天数，视需求而定
			LocalDate startDate = ZEROPOINT.plusDays(si.getStart());
			LocalDate endDate = ZEROPOINT.plusDays(si.getEnd());
			scheduleListModel.addElement("[" + startDate + "~" + endDate + "]：" + si.getEmployee().toString());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			DutyRosterApp frame = new DutyRosterApp();
			frame.setVisible(true);
		});
	}
}
