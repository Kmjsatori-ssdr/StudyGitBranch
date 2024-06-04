package Applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.*;
import javax.swing.*;

import APISet.ProcessIntervalSet;
import APIs.APIs;
import IntervalSet.Interval;
import IntervalSet.MyExceptions.LabelNotFoundException;

public class ProcessScheduleApp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5126037668879317333L;
	private static JLabel processIDLabel, processNameLabel, minTimeLabel, maxTimeLabel;
	private static JButton addProcessButton, randomScheduleButton, shortestFirstButton, showScheduleButton;
	private static JTextField processIDField, processNameField, minTimeField, maxTimeField;
	private static DefaultListModel<String> scheduleDisplayModel;
	private static JList<String> scheduleDisplay;

	private static Map<Process, Long> processMap = new HashMap<>();
	private static ProcessIntervalSet<Process> intervalSet = new ProcessIntervalSet<>();
	private static long currentTime = 0;

	public ProcessScheduleApp() {
		setTitle("进程调度管理系统");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 将窗口中心显示
		setLayout(new BorderLayout());

		JPanel controlPanel = new JPanel();
		JPanel inputPanel = new JPanel(new GridLayout(4, 2));

		processIDLabel = new JLabel("进程ID:");
		processNameLabel = new JLabel("进程名字:");
		minTimeLabel = new JLabel("最小执行时间:");
		maxTimeLabel = new JLabel("最大执行时间:");

		processIDField = new JTextField(10);
		processNameField = new JTextField(10);
		minTimeField = new JTextField(10);
		maxTimeField = new JTextField(10);

		inputPanel.add(processIDLabel);
		inputPanel.add(processIDField);
		inputPanel.add(processNameLabel);
		inputPanel.add(processNameField);
		inputPanel.add(minTimeLabel);
		inputPanel.add(minTimeField);
		inputPanel.add(maxTimeLabel);
		inputPanel.add(maxTimeField);

		addProcessButton = new JButton("增加进程");
		randomScheduleButton = new JButton("随机调度");
		shortestFirstButton = new JButton("最短进程优先调度");
		showScheduleButton = new JButton("显示调度情况");

		controlPanel.add(addProcessButton);
		controlPanel.add(randomScheduleButton);
		controlPanel.add(shortestFirstButton);
		controlPanel.add(showScheduleButton);

		scheduleDisplayModel = new DefaultListModel<>();
		scheduleDisplay = new JList<String>(scheduleDisplayModel);
		JScrollPane scrollPane = new JScrollPane(scheduleDisplay);

		add(inputPanel, BorderLayout.NORTH);
		add(controlPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);

		addProcessButton.addActionListener(e -> {
			addProcess();
		});

		randomScheduleButton.addActionListener(e -> {
			randomSchedule();
		});

		shortestFirstButton.addActionListener(e -> {
			shortestFirstSchedule();
		});

		showScheduleButton.addActionListener(e -> {
			showSchedule();
		});
	}

	private static void addProcess() {
		String ID = processIDField.getText();
		String name = processNameField.getText();
		String minTime = minTimeField.getText();
		String maxTime = maxTimeField.getText();

		try {
			long numMinTime = Long.parseLong(minTime);
			long numMaxTime = Long.parseLong(maxTime);
			if (numMinTime >= numMaxTime) {
				throw new IllegalArgumentException();
			}
			Process newProcess = new Process(ID, name, numMinTime, numMaxTime);
			processMap.put(newProcess, 0L);
			scheduleDisplayModel.addElement(newProcess.toString());
			JOptionPane.showMessageDialog(null, "进程添加成功！");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "输入的时间格式不正确，请输入数字！");
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(null, "最短时间应小于最长时间！");
		}
	}

	private static void randomSchedule() {
		clearSchedule();
		List<Process> processList = new ArrayList<Process>(processMap.keySet());
		Random random = new Random();
		while (!isFinished()) {
			// 随机选择一个进程，或者选择不执行任何进程
			int randomIndex = random.nextInt(processList.size() + 1);
			Process chosenProcess = randomIndex == processList.size() ? null : processList.get(randomIndex);
			if (chosenProcess != null) {
				// 随机选择一个执行时间片段（不超过进程剩余时间）
				long executionTime = random.nextLong(chosenProcess.getMaxTime() - processMap.get(chosenProcess) + 1);
				processMap.put(chosenProcess, processMap.get(chosenProcess) + executionTime);
				try {
					intervalSet.insert(currentTime, currentTime + executionTime, chosenProcess);
				} catch (Exception e) {
					e.printStackTrace();
				}
				currentTime += executionTime;
			} else {
				long freeTime = random.nextLong(10);
				currentTime += freeTime;
			}
		}
		JOptionPane.showMessageDialog(null, "随机调度完成。", // 消息内容
				"调度完成", // 标题
				JOptionPane.PLAIN_MESSAGE);
	}

	private static void shortestFirstSchedule() {
		clearSchedule();
		List<Process> processList = new ArrayList<Process>(processMap.keySet());
		Collections.sort(processList,
				(p1, p2) -> (int) ((p1.getMaxTime() - processMap.get(p1)) - (p2.getMaxTime() - processMap.get(p2))));
		for (int i = 0; i < processList.size(); i++) {
			Process chosenProcess = processList.get(i);
			long executionTime = chosenProcess.getMaxTime() - processMap.get(chosenProcess);
			processMap.put(chosenProcess, processMap.get(chosenProcess) + executionTime);
			try {
				intervalSet.insert(currentTime, currentTime + executionTime, chosenProcess);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentTime += executionTime;
		}
		JOptionPane.showMessageDialog(null, "最短优先调度完成。", // 消息内容
				"调度完成", // 标题
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * 显示进程执行安排情况。
	 */
	private static void showSchedule() {
		String message = new String();
		message += "当前时间：" + currentTime + "\n";
		APIs<Process> apis = new APIs<Process>();
		List<Interval> allIntervals = apis.allIntervals(intervalSet);
		for (int i = 0; i < allIntervals.size(); i++) {
			Process currentProcess = null;
			for (Process p : intervalSet.labels()) {
				try {
					if (intervalSet.intervals(p).contains(allIntervals.get(i))) {
						currentProcess = p;
						message += allIntervals.get(i) + ":" + currentProcess + "\n";
					}
				} catch (LabelNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		JOptionPane.showMessageDialog(null, message, // 消息内容
				"调度结果", // 标题
				JOptionPane.PLAIN_MESSAGE);
	}

	// 其他可复用的方法
	/**
	 * 判断processMap里的进程是否已经全部执行完毕。
	 * 
	 * @return true已完成，false未完成
	 */
	private static boolean isFinished() {
		for (Process p : processMap.keySet()) {
			if (p.getMinTime() > processMap.get(p))
				return false;
		}
		return true;
	}

	private static void clearSchedule() {
		currentTime = 0;
		for (Process p : intervalSet.labels()) {
			intervalSet.remove(p);
		}
		for (Process p : processMap.keySet()) {
			processMap.put(p, (long) 0);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			ProcessScheduleApp app = new ProcessScheduleApp();
			app.setVisible(true);
		});
	}
}