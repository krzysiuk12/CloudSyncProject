package pl.edu.agh.iosr.tasks.googledrive.factories;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.IOUtils;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;
import pl.edu.agh.iosr.tasks.googledrive.DownloadFileTask;
import pl.edu.agh.iosr.tasks.googledrive.params.DownloadFileTaskParams;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Created by Mateusz Drożdż on 21.05.15.
 */
public class DownloadFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public DownloadFileTask create(final Drive service, CloudPath filePath, final OutputStream outputStream) {
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
                    System.out.println("Downloading file witd Id: " + params.getFilePath().getPath());
                    File file = service.files().get(params.getFilePath().getPath()).execute();
                    progressMonitor.setProgress(new Progress(0.3f));
                    // download
//                todo: set progresslistener: downloader.setProgressListener(FileDownloadProgressListener);
                    System.out.println("File DownloadUrl: " + file.getDownloadUrl());
                    InputStream inputStream = downloadFile(service, file);

                    IOUtils.copy(inputStream, params.getOutputStream());
                    inputStream.close();
                    params.getOutputStream().close();
                    System.out.println("File downloaded.");
                    progressMonitor.setProgress(new Progress(1.0f));
                    return true;

                } catch (IOException e) {
                    logger.error("Problem while downloading file.", e);
                } finally {
//                    params.getOutputStream().close();
                }
                return false;
            }
        });
    }
    private static InputStream downloadFile(Drive service, File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                                .execute();
                return resp.getContent();
            } catch (IOException e) {
                // An error occurred.
                e.printStackTrace();
                return null;
            }
        } else {
            // The file doesn't have any content stored on Drive.
            return null;
        }
    }

}
