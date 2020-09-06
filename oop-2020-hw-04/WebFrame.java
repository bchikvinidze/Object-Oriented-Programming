import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class WebFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollpane;
    private JButton singleButton;
    private JButton concurrentButton;
    private JButton stopButton;
    private JLabel running;
    private JLabel completed;
    private JLabel elapsed;
    private JTextField field;
    private JProgressBar progressBar;

    private int runningThreadCnt;
    private int completedThreadCnt;
    private int rowCnt;
    private String elapsedTime;
    private Launcher launcher;
    private Semaphore sem;
    private int startTime;
    private String urls;
    private Semaphore stopSem; //This semaphore is used to coordinate action between new worker fetching versus laucher and rest of the workers interruption

    private ArrayList<WebWorker> workers;

    /* Reads file line by line and updates tableModel with new entries*/
    private void readURLs() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(urls));
            String line = reader.readLine();
            Vector<String> data;
            while (line != null) {
                data =  new Vector<String>();
                data.add(line);
                model.insertRow(0, data);
                line = reader.readLine();
                rowCnt++;
            }
            reader.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    /* Updates table's status column with information about
    * downloaded URL */
    public void updateTable(String info, int index){
        model.setValueAt(info, index, 1);
    }

    /* Sets up view of table and created a new model responsible for URLs */
    private void setupTableView(){
        model = new DefaultTableModel(new String[] { "url", "status"}, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        rowCnt = 0;
        readURLs();
        scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600,300));
        add(scrollpane);
    }

    /* Sets up view of southern part of frame */
    private void setupSouth(){
        singleButton = new JButton("Single Thread Fetch");
        concurrentButton = new JButton("Concurrent Fetch");
        stopButton =  new JButton("Stop");
        field = new JTextField();
        field.setMaximumSize(new Dimension(75, 25));
        resetStatus();
        running = new JLabel("Running: " + runningThreadCnt);
        completed =  new JLabel("Completed: " + completedThreadCnt);
        elapsed =  new JLabel("Elapsed: " + elapsedTime);
        progressBar =  new JProgressBar();
    }

    /* adds various elements to the JFrame */
    private void addToJFrame(){
        add(singleButton);
        add(concurrentButton);
        add(field);
        add(running);
        add(completed);
        add(elapsed);
        add(progressBar);
        add(stopButton);
    }

    /* During the program, Thread interruption is not available, so
    * while fetching threads are active, "stop" button becomes unusable,
    * and while "stop" is doing its job, thread fetching buttons are
    * unavailable. this flip is controlled by the boolean flag*/
    private void changeButtonsState(boolean flag){
        singleButton.setEnabled(!flag);
        concurrentButton.setEnabled(!flag);
        stopButton.setEnabled(flag);
    }

    /* Program's status is represented by what labels show to
    * the user: running thread count, already completed thread count and
    * elapsed time of the program. */
    private void resetStatus(){
        runningThreadCnt = 0;
        completedThreadCnt = 0;
        elapsedTime = "";
    }

    /* updates southern region of JFrame, namely: labels and progress bar,
    * resets important private variables that describe the state of the program*/
    private void updateSouth(){
        resetStatus();
        running.setText("Running: " + runningThreadCnt);
        completed.setText("Completed: " + completedThreadCnt);
        elapsed.setText("Elapsed: " + elapsedTime);
        int rows = model.getRowCount();
        progressBar.setValue(0);
        progressBar.setMaximum(rows);
    }

    /* These methods make semaphore accessable to other classes.*/
    public Semaphore getSem(){
        return sem;
    }

    /* Launcher thread class: simple class that is used to fetch
    * new worker threads. Saves workers in an arraylist ro access them
    * when need be (when interrupted) */
    private class Launcher extends Thread {
        @Override
        public void run(){
            stopSem = new Semaphore(1);
            workers = new ArrayList<WebWorker>();
            modifyRunningLabel(true, false, false);
            for(int i = 0; i < rowCnt; i++){
                nextWorker(i);
            }
            modifyRunningLabel(false, false, false);
        }

        /* This method is responsible for handling worker creation and interruption handling
         * of the Launcher thread*/
        private void nextWorker(int i){
            if (isInterrupted()) {
                modifyRunningLabel(false, true, false);
                return;
            }
            try {
                stopSem.acquire();
            } catch (InterruptedException e) {e.printStackTrace();}
            WebWorker worker = new WebWorker((String) model.getValueAt(i, 0), i, WebFrame.this);
            workers.add(i, worker);
            worker.start();
            stopSem.release();
        }
    }

    /* Interrupts all workers in the passed array */
    public void interruptWorkers(){
        for(int i=0; i<workers.size(); i++)
            workers.get(i).interrupt();
    }

    /* Each time currently executing thread count (including launcher and worker threads)
    * changes, "running" label should be updated. This function makes sure to do so
    * without data race. Also, if all threads finished working, changes the state of program*/
    public synchronized void modifyRunningLabel(boolean increase, boolean terminate, boolean worker){
        if(increase)
            runningThreadCnt++;
        else {
            runningThreadCnt--;
            if(!terminate && worker) progressBar.setValue(++completedThreadCnt);
        }
        running.setText("Running: " + runningThreadCnt);
        completed.setText("Completed: " + completedThreadCnt);
        if(completedThreadCnt == rowCnt || runningThreadCnt == 0){
            elapsed.setText("Elapsed: " +  Integer.toString( ((int) System.currentTimeMillis() - startTime)/1000 ) );
            changeButtonsState(false);
        }
    }

    /* a new process is created which results in change of button's state (reversing),
    * updating labels on southern part of JFrame and creating a launcher thread.*/
    private void newProcess(){
        startTime = (int) System.currentTimeMillis();
        changeButtonsState(true);
        updateSouth();
        launcher = new Launcher();
        launcher.start();
    }

    /* Adds Listeners to three buttons: single thread, concurrent threads and stop.
    * each directs program to behave differently: single thread limits worker
    * thread count to one, concurrent threads button limits worker thead count to
    * whatever's written in the field area and stop button behavior is self explanatory.*/
    private void addListeners(){
        singleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sem = new Semaphore(1);
                newProcess();
            }
        });

        concurrentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sem = new Semaphore(Integer.parseInt(field.getText()));
                newProcess();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    stopSem.acquire();
                } catch (InterruptedException e) {e.printStackTrace();}
                launcher.interrupt();
                interruptWorkers();
                stopSem.release();
                changeButtonsState(false);
            }
        });
    }

    /* Constructor that setps up the frame including: table, southern part,
    * adds listeners and does default stuff for JFrame*/
    public WebFrame(String file){
        super("WebLoader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        urls = file;
        setupTableView();
        setupSouth();
        addToJFrame();
        addListeners();
        setVisible(true);
        pack();
    }

    /* main method for running commands in terminal */
    public static void main(String[] args){
        WebFrame fr = new WebFrame("links2.txt");
    }
}