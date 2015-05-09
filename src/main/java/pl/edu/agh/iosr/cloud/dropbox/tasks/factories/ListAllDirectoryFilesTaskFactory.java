package pl.edu.agh.iosr.cloud.dropbox.tasks.factories;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.files.FileType;
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
        DropboxCallable<List<FileMetadata>> callable = getTask(dbxClient, listAllDirectoryFilesTaskParams);
        return new ListAllDirectoryFilesTask(callable);
    }

    private DropboxCallable<List<FileMetadata>> getTask(final DbxClient dbxClient, final ListAllDirectoryFilesTaskParams params) {
        return new DropboxCallable<List<FileMetadata>>() {
            @Override
            public List<FileMetadata> call() throws Exception {
                try {
                    setProgress(0.1f);
                    List<FileMetadata> paths = getResult(dbxClient, params.getDirectory().getPath());
                    setProgress(1.0f);
                    return paths;
                } catch(DbxException ex) {
                    logger.error("Problem during files listing.", ex);
                }
                return new LinkedList<>();
            }
        };
    }

    private List<FileMetadata> getResult(final DbxClient dbxClient, String directory) throws DbxException {
        List<FileMetadata> paths = new ArrayList<>();
        DbxEntry.WithChildren directoryEntries = dbxClient.getMetadataWithChildren(directory);
        if(directoryEntries == null || directoryEntries.children == null || directoryEntries.children.isEmpty()) {
            return paths;
        }
        for(DbxEntry entry : directoryEntries.children) {
            FileMetadata.Builder fileBuilder = FileMetadata.newBuilder();

            fileBuilder.setFileName(entry.name);
            fileBuilder.setPath(new CloudPath(entry.path));
            if(entry.isFile()) {
                fileBuilder.setType(FileType.SIMPLE_FILE);
                fileBuilder.setSize(((DbxEntry.File) entry).numBytes);
                fileBuilder.setLastModificationTime(new DateTime(((DbxEntry.File) entry).lastModified));
                fileBuilder.setExtension(entry.path.substring(entry.path.lastIndexOf(".") + 1));
            } else {
                fileBuilder.setType(FileType.DIRECTORY);
            }
            paths.add(fileBuilder.build());
        }
        return paths;
    }

}
