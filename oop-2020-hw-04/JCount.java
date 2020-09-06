// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JCount extends JPanel {
	private static final int SLEEP_TIME = 100;
	private static final int ITERATIONS = 10000;
	private static final int DEFAULT_COUNTER = 100000000;

	private JTextField field;
	private JLabel label;
	private JButton start, stop;
	private int limit;
	private int curVal;
	private WorkerThread countingThread;
	private boolean workerStopped; //this flag is used only for testing purposes

	/* creates GUI and shows it: 4 JCounts */
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/* simulates start button being clicked */
	public void startClick(){
		start.doClick();
	}

	/* simulates stop button being clicked */
	public void stopClick(){
		stop.doClick();
	}

	/* For testing purposes, gets the value of label */
	public String labelVal(){
		return label.getText();
	}

	/* For testing purposes, returns status of worker thread */
	public boolean workerAlive(){
		return workerStopped;
	}

	/* increments label value by 1 */
	private void updateState(String arg){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setText(arg);
			}
		});
	}

	/* WorkerThread class does the UI update: changes the
	* label's value, is responsible for counting up and stops
	* working if interrupted */
	private class WorkerThread extends Thread{
		@Override
		public void run(){
			workerStopped = true;
			while(true){
				try {
					if(curVal > limit) break;
					if(curVal % ITERATIONS == 0)
						sleep(SLEEP_TIME);
					updateState(Integer.toString(curVal++));
				} catch (InterruptedException e) {
					workerStopped = false; // once more: this flag is used only for testing purposes
					break; // must stop loop if interrupted
				}
			}
			workerStopped = false;
			try {
				countingThread.join(); // finish up with this thread
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	/* Constructor for the class. */
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		field = new JTextField();
		field.setText(Integer.toString(DEFAULT_COUNTER));
		label = new JLabel("0");
		start = new JButton("Start");
		stop = new JButton("Stop");
		limit = DEFAULT_COUNTER;
		curVal = 0;
		add(field);
		add(label);
		add(start);
		add(stop);
		add(Box.createRigidArea(new Dimension(0,40)));
		addListeners();
	}

	/* Adds listeners to start and stop buttons. Start button makes
	* a new counter thread (if it already exists, first interrupts it)
	* and starts counting from zero up, as well as displaying
	* result on screen on certain time intervals. Stop button is
	* self explanatory: the worker thread is interrupted. */
	private void addListeners(){
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				limit = Integer.parseInt(field.getText());
				if(countingThread != null) countingThread.interrupt();
				label.setText("0");
				curVal = 0;
				countingThread = new WorkerThread();
				countingThread.start();
			}
		});

		/* Stops the thread on click*/
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(countingThread != null) countingThread.interrupt();
			}
		});
	}

	/* Main thread to execute the program */
	static public void main(String[] args)  {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}

