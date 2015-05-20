package pl.edu.agh.iosr.synchronization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-18.
 */
@Service
public abstract class SynchronizationService implements ISynchronizationService {

    private DropboxCloudManagementService dropboxCloudManagementService;
    private GoogleDriveCloudManagementService googleDriveCloudManagementService;
    private OnedriveCloudManagementService onedriveCloudManagementService;

    @Autowired
    public SynchronizationService(DropboxCloudManagementService dropboxCloudManagementService, GoogleDriveCloudManagementService googleDriveCloudManagementService, OnedriveCloudManagementService onedriveCloudManagementService) {
        this.dropboxCloudManagementService = dropboxCloudManagementService;
        this.googleDriveCloudManagementService = googleDriveCloudManagementService;
        this.onedriveCloudManagementService = onedriveCloudManagementService;
    }

    @Override
    public Boolean copy(String sourceSessionId, CloudPath sourceCloudFile, String destinationSessionId, CloudPath destinationCloudPath) throws Exception {
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);
        getCloudManagementService(sourceCloudFile.getType()).downloadFile(sourceSessionId, sourceCloudFile, outputStream);
        getCloudManagementService(destinationCloudPath.getType()).uploadFile(destinationSessionId, destinationCloudPath, "SomeTest", null, inputStream);
        return true;
    }

    private ICloudManagementService getCloudManagementService(CloudType type) {
        switch(type) {
            case DROPBOX: return dropboxCloudManagementService;
            case GOOGLE_DRIVE: return googleDriveCloudManagementService;
            case ONE_DRIVE: return onedriveCloudManagementService;
            default: return null;
        }
    }

    private static class JoinedCloudTask<T, U, W> extends CloudTask<W> {

        private CloudTask<T> firstTask;
        private CloudTask<U> secondTask;

        private JoinedCloudTask(ProgressMonitor progressMonitor, Callable<W> callable, CloudTask<T> firstTask, CloudTask<U> secondTask) {
            super(progressMonitor, callable);
            this.firstTask = firstTask;
            this.secondTask = secondTask;
        }

    }

}
