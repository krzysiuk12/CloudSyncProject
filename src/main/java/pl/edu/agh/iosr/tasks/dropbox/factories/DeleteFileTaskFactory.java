package pl.edu.agh.iosr.tasks.dropbox.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.tasks.Progress;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressMonitor;
import pl.edu.agh.iosr.tasks.dropbox.DeleteFileTask;
import pl.edu.agh.iosr.tasks.dropbox.params.DeleteTaskParams;

import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public DeleteFileTask create(final DbxClient dbxClient, CloudPath filePath) {
        DeleteTaskParams deleteFileTaskParams = new DeleteTaskParams(filePath);
        return getTask(dbxClient, deleteFileTaskParams);
    }

    private DeleteFileTask getTask(final DbxClient dbxClient, final DeleteTaskParams params) {
        final ProgressMonitor progressMonitor = new ProgressMonitor();
        return new DeleteFileTask(progressMonitor, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    progressMonitor.setProgress(new Progress(0.1f));
                    dbxClient.delete(params.getPath().getPath());
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
