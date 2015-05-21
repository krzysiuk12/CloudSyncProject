package pl.edu.agh.iosr.cloud.googledrive.tasks.factories;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.googledrive.tasks.DownloadFileTask;
import pl.edu.agh.iosr.cloud.googledrive.tasks.params.DownloadFileTaskParams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class DownloadFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public DownloadFileTask create(final Drive service, CloudPath filePath, OutputStream outputStream) {
        DownloadFileTaskParams downloadFileTaskParams = new DownloadFileTaskParams(filePath, outputStream);
        return getTask(service, downloadFileTaskParams);

    }

    private DownloadFileTask getTask(final Drive service, final DownloadFileTaskParams params) {
        final ProgressMonitor progressMonitor = new ProgressMonitor();
        return new DownloadFileTask(progressMonitor, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    progressMonitor.setProgress(new Progress(0.1f));
                    // get requesting file metadata
                    File file = service.files().get(params.getFilePath().getPath()).execute();
                    progressMonitor.setProgress(new Progress(0.3f));
                    // download
                    MediaHttpDownloader downloader = new MediaHttpDownloader(new NetHttpTransport(), service.getRequestFactory().getInitializer());
//                todo: set progresslistener: downloader.setProgressListener(FileDownloadProgressListener);
                    downloader.download(new GenericUrl(file.getDownloadUrl()), params.getOutputStream());
                    progressMonitor.setProgress(new Progress(1.0f));
                    return true;

                } catch (IOException e) {
                    logger.error("Problem while downloading file.", e);
                }
                return false;
            }
        });
    }


}
