package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DeleteFileTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DropboxCallable;
import pl.edu.agh.iosr.cloud.dropbox.tasks.params.DeleteFileTaskParams;

/**
 * Created by Krzysztof Kicinger on 2015-05-10.
 */
public class DeleteFileTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public DeleteFileTask create(final DbxClient dbxClient, CloudPath filePath) {
        DeleteFileTaskParams deleteFileTaskParams = new DeleteFileTaskParams(filePath);
        DropboxCallable<Boolean> callable = getTask(dbxClient, deleteFileTaskParams);
        return new DeleteFileTask(callable);
    }

    private DropboxCallable<Boolean> getTask(final DbxClient dbxClient, final DeleteFileTaskParams params) {
        return new DropboxCallable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    setProgress(0.1f);
                    dbxClient.delete(params.getPath().getPath());
                    setProgress(1.0f);
                    return true;
                } catch(DbxException ex) {
                    logger.error("Problem during file downloading.", ex);
                }
                return false;
            }
        };
    }

}
