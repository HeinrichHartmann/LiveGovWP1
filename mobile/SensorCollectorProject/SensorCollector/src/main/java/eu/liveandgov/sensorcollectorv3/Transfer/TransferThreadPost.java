package eu.liveandgov.sensorcollectorv3.Transfer;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import eu.liveandgov.sensorcollectorv3.Configuration.SensorCollectionOptions;
import eu.liveandgov.sensorcollectorv3.Connector.ConnectorThread;
import eu.liveandgov.sensorcollectorv3.GlobalContext;
import eu.liveandgov.sensorcollectorv3.Persistence.Persistor;

/**
 * Transfer sensor.log file to server.
 *
 * Use:
 * * .setup()      - setup instance
 * * .doTransfer() - trigger sample transfer
 *
 * Created by hartmann on 8/30/13.
 */
public class TransferThreadPost implements Runnable, TransferManager {
    public static String LOG_TAG = "TransferThreadPost";

    private Thread thread;

    private static final String uploadUrl = SensorCollectionOptions.UPLOAD_URL;

    private Persistor persistor;

    private File stageFile;

    public TransferThreadPost(Persistor persistor, File stageFile) {
        this.stageFile = stageFile;
        this.persistor = persistor;
        thread = new Thread(this);
    };


    public void doTransfer(){
        start();
    }

    public boolean isTransferring() {
        return thread.isAlive();
    }

    public void start(){
        if (thread.isAlive()) { Log.i(LOG_TAG, "Already running."); return; }
        if (thread.getState() == Thread.State.TERMINATED) thread = new Thread(this);
        thread.start();
    }

    public void run() {
        boolean success;

        // get stage file
        success = persistor.exportSamples(stageFile);
        if (!success) { Log.i(LOG_TAG,"Staging failed");  return; }

        // transfer staged File
        success = transferFile(stageFile);
        if (!success) { Log.i(LOG_TAG,"Transfer failed");  return; }

        // delete local copy
        success = stageFile.delete();
        if (!success) { Log.i(LOG_TAG,"Deletion failed"); return; }

        // terminate
        Log.i(LOG_TAG, "Transfer finished successfully");
    }

    public boolean transferFile(File file){
        try {
            HttpClient      httpclient =    new DefaultHttpClient();
            HttpPost        httppost =      new HttpPost(uploadUrl);
            MultipartEntity mEntity =       new MultipartEntity();

            ContentBody fileBody = new FileBody(file);
            mEntity.addPart("upfile", fileBody);
            httppost.setEntity(mEntity);

            httppost.addHeader("CHECKSUM", String.valueOf(file.length()));
            httppost.addHeader("ID", GlobalContext.androidId );

            HttpResponse response = httpclient.execute(httppost);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Log.i(LOG_TAG, "Upload failed");
                return false;
            }

        } catch (HttpHostConnectException e) {
            Log.i(LOG_TAG, "Connection Refused");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getStatus() {
        return isTransferring() ? "transferring" : "stopped";
    }
}

