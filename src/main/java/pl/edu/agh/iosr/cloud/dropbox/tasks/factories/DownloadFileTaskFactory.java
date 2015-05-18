package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.params.DownloadFileTaskParams;

import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DownloadFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public DownloadFileTask create(final DbxClient dbxClient, CloudPath filePath, OutputStream outputStream) {
        DownloadFileTaskParams downloadFileTaskParams = new DownloadFileTaskParams(filePath, outputStream);
        return getTask(dbxClient, downloadFileTaskParams);
    }

    private DownloadFileTask getTask(final DbxClient dbxClient, final DownloadFileTaskParams params) {
        final ProgressMonitor progressMonitor = new ProgressMonitor();
        return new DownloadFileTask(progressMonitor, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    progressMonitor.setProgress(new Progress(0.1f));
                    DbxEntry.File file = dbxClient.getMetadata(params.getCloudPath().getPath()).asFile();
                    progressMonitor.setProgress(new Progress(0.3f));
                    if(file.numBytes > 0) {
                        dbxClient.getFile(params.getCloudPath().getPath(), null, params.getOutputStream());
                    }
                    progressMonitor.setProgress(new Progress(1.0f));
                    return true;
                } catch(DbxException ex) {
                    logger.error("Problem during file downloading.", ex);
                }
                return false;
            }
        });
    }

}
