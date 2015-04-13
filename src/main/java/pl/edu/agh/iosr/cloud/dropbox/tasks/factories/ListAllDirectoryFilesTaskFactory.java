package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.CloudPathType;
import pl.edu.agh.iosr.cloud.dropbox.tasks.DropboxCallable;
import pl.edu.agh.iosr.cloud.dropbox.tasks.ListAllDirectoryFilesTask;
import pl.edu.agh.iosr.cloud.dropbox.tasks.params.ListAllDirectoryFilesTaskParams;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-04-13.
 */
public class ListAllDirectoryFilesTaskFactory {

    private Logger logger = Logger.getLogger(this.getClass());

    public ListAllDirectoryFilesTask create(final DbxClient dbxClient, CloudPath directory) {
        ListAllDirectoryFilesTaskParams listAllDirectoryFilesTaskParams = new ListAllDirectoryFilesTaskParams(directory);
        DropboxCallable<List<CloudPath>> callable = getTask(dbxClient, listAllDirectoryFilesTaskParams);
        return new ListAllDirectoryFilesTask(callable);
    }

    private DropboxCallable<List<CloudPath>> getTask(final DbxClient dbxClient, final ListAllDirectoryFilesTaskParams params) {
        return new DropboxCallable<List<CloudPath>>() {
            @Override
            public List<CloudPath> call() throws Exception {
                try {
                    setProgress(0.1f);
                    List<CloudPath> paths = getResult(dbxClient, params.getDirectory().getPath());
                    setProgress(1.0f);
                    return paths;
                } catch(DbxException ex) {
                    logger.error("Problem during files listing.", ex);
                }
                return new LinkedList<CloudPath>();
            }
        };
    }

    private List<CloudPath> getResult(final DbxClient dbxClient, String directory) throws DbxException {
        List<CloudPath> paths = new ArrayList<CloudPath>();
        DbxEntry.WithChildren directoryEntries = dbxClient.getMetadataWithChildren(directory);
        if(directoryEntries == null || directoryEntries.children == null || directoryEntries.children.isEmpty()) {
            return paths;
        }
        for(DbxEntry entry : directoryEntries.children) {
            CloudPath path = new CloudPath();
            path.setFileName(entry.name);
            path.setPath(entry.path);
            if(entry.isFile()) {
                path.setType(CloudPathType.SIMPLE_FILE);
                path.setSize(((DbxEntry.File)entry).numBytes);
                path.setLastModificationDate(((DbxEntry.File)entry).lastModified);
                path.setExtension(path.getPath().substring(path.getPath().lastIndexOf(".") + 1));
            } else {
                path.setType(CloudPathType.DIRECTORY);
            }
            paths.add(path);
        }
        return paths;
    }

}
