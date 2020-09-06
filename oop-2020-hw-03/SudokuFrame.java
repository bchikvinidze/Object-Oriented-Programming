import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuFrame extends JFrame {
	private JPanel textPanel;
	private JTextArea puzzleTxt;
	private JTextArea resultsTxt;
	private JPanel lowerPanel;
	private JButton button;
	private JCheckBox checkBox;

	/*
	 * Adding two text Areas on the center and eastern
	* positions of the panel. Center area is editable. This is
	* the part which is used for user input.
	*/
	private void addTextAreas(){
		textPanel = new JPanel(new GridLayout(1, 2));
		puzzleTxt = new JTextArea(15,20);
		resultsTxt = new JTextArea(15,20);
		resultsTxt.setEnabled(false);
		puzzleTxt.setBorder(new TitledBorder("Puzzle"));
		resultsTxt.setBorder(new TitledBorder("Solution"));
		add(puzzleTxt, BorderLayout.CENTER);
		add(resultsTxt, BorderLayout.EAST);
	}

	/*
	* Sets up southers part of the panel, containing a button
	* and a checkBox.
	*/
	private void setupSouth(){
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		button = new JButton("Check");
		button.setSize(new Dimension(5, 20));
		checkBox = new JCheckBox("Auto Check");
		checkBox.setSelected(true);
		lowerPanel.add(button);
		lowerPanel.add(checkBox);
	}

	/*
	* sets up actionListeners for the button and checkBox.
	* clicking button solves input on center textArea.
	* if checkBox is turned on, listener makes sure that on
	* every change on the input textArea results in same effect as
	* clicking the check button.
	*/
	private void setupListeners(){
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solve(puzzleTxt, resultsTxt);
			}
		});

		puzzleTxt.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(checkBox.isSelected())
					solve(puzzleTxt, resultsTxt);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(checkBox.isSelected())
					solve(puzzleTxt, resultsTxt);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
	}

	/* Constructor for Sudoku class */
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4,4));
		addTextAreas();
		setupSouth();
		add(lowerPanel, BorderLayout.SOUTH);
		setupListeners();
		// Could do this:
		// setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/*
	* takes as input JTextAreas, extracts text
	* and tries to solve the input text as Sudoku.
	* Results are displayed on another passed JTextArea
	*/
	private void solve(JTextArea puzzleTxt, JTextArea resultsTxt){
		try {
			String txt = puzzleTxt.getText();
			Sudoku sudo = new Sudoku(txt);
			int solCnt = sudo.solve();
			String sol = sudo.getSolutionText();
			String toPrint = sol + "solutions:" + solCnt + "\n" + "elapsed:" + sudo.getElapsed() + "ms\n";
			resultsTxt.setText(toPrint);
		} catch (Exception ex){
			resultsTxt.setText("Parsing Problem");
		}
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
