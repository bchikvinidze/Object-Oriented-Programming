import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class WebWorker extends Thread {
    private String urlString;
    private int rowNum;
    private WebFrame frame;
    private Semaphore sem;
    private StringBuilder contents;

    private String elapsed;
    private int bytesDownloaded;
    private boolean success;
    private String timeComplete;
    private boolean interrupted;

 	public void download() {
        InputStream input = null;
        contents = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);
            connection.connect();
            input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                bytesDownloaded += len;
                contents.append(array, 0, len);
                Thread.sleep(100);
            }
            success = true;
        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {}
        catch (InterruptedException exception) {
            frame.modifyRunningLabel(false, true, true);
            sem.release();
            interrupted = true;
        } catch (IOException ignored) {}
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {}
        }
    }

    /* gets the result of the worker's labor and updates table accordingly.*/
    private void assignResult(){
        String result;
        if(interrupted){
            result = "interrupted";
        } else if(success){
            result = timeComplete + " " + elapsed + " " + bytesDownloaded + " " + "bytes";
        } else {
            result = "err";
        }
        frame.updateTable(result, rowNum);
    }

    /* Calculates important metrics for display: time of completion,
    * time elapsed and bytes downloaded, after downloading url content. */
    private void work(){
        long startTime = (int) System.currentTimeMillis();
        download();
        long endTime = (int) System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateObj = new Date();
        timeComplete = df.format(dateObj);
        elapsed = Integer.toString((int) (endTime - startTime));
        assignResult();
    }

    /* Runs the thread: first waits for semaphore to become available
    * and acquires it, updates display, does it's job (works)
    * again signals the display about finishing work, releases semaphore
    * and finishes.*/
    @Override
    public void run(){
        try {
            sem.acquire();
            frame.modifyRunningLabel(true, false, true);
            work();
            if(!interrupted) frame.modifyRunningLabel(false, false,true);
            sem.release();
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    /* Constructor for WebWorker class. */
    public WebWorker(String url, int rowNum, WebFrame frame){
        urlString = url;
        bytesDownloaded = 0;
        this.rowNum = rowNum;
        this.frame = frame;
        sem = frame.getSem();
    }
}
