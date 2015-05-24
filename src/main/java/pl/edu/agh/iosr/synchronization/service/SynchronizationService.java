package pl.edu.agh.iosr.synchronization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.Progress;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;
import pl.edu.agh.iosr.execution.IExecutionService;
import pl.edu.agh.iosr.synchronization.SynchronizationEntry;
import pl.edu.agh.iosr.synchronization.SynchronizationSingleCloudEntry;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Krzysztof Kicinger on 2015-05-18.
 */
@Service
public class SynchronizationService implements ISynchronizationService {

    private DropboxCloudManagementService dropboxCloudManagementService;
    private GoogleDriveCloudManagementService googleDriveCloudManagementService;
    private OnedriveCloudManagementService onedriveCloudManagementService;
    private IExecutionService executionService;

    @Autowired
    public SynchronizationService(DropboxCloudManagementService dropboxCloudManagementService, GoogleDriveCloudManagementService googleDriveCloudManagementService, OnedriveCloudManagementService onedriveCloudManagementService, IExecutionService executionService) {
        this.dropboxCloudManagementService = dropboxCloudManagementService;
        this.googleDriveCloudManagementService = googleDriveCloudManagementService;
        this.onedriveCloudManagementService = onedriveCloudManagementService;
        this.executionService = executionService;
    }

    @Override
    public List<FileMetadata> synchronize(SynchronizationEntry synchronizationEntry) throws Exception {
        ICloudManagementService sourceManagementService = getCloudManagementService(synchronizationEntry.getSource().getCloudPath().getType());

        final PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        final CloudTask<Boolean> downloadTask = sourceManagementService.downloadFile(synchronizationEntry.getSource().getSessionId(), synchronizationEntry.getSource().getCloudPath(), outputStream);
        final List<CloudTask<FileMetadata>> uploadTasks = new ArrayList<>();
        for(SynchronizationSingleCloudEntry entry : synchronizationEntry.getDestination()) {
            ICloudManagementService destinationCloudManagementService = getCloudManagementService(entry.getCloudPath().getType());
            uploadTasks.add(destinationCloudManagementService.uploadFile(entry.getSessionId(), entry.getCloudPath(), "pupa", inputStream));
        }

        final ProgressMonitor progressMonitor = new ProgressMonitor();
        CloudTask<List<FileMetadata>> mergedTask = new SynchronizationCloudTask<>(progressMonitor, new Callable<List<FileMetadata>>() {
            @Override
            public List<FileMetadata> call() throws Exception {
                progressMonitor.setProgress(new Progress(0.0));
                List<FileMetadata> fileMetadataList = new ArrayList<>();
                progressMonitor.setProgress(new Progress(0.3));
                if(downloadTask.get()) {
                    outputStream.close();
                    for(CloudTask<FileMetadata> task : uploadTasks) {
                        fileMetadataList.add(task.get());
                    }
                    progressMonitor.setProgress(new Progress(1.0));
                    return fileMetadataList;
                } else {
                    return Collections.emptyList();
                }
            }
        }, downloadTask, uploadTasks);

        executionService.execute(downloadTask);
//        for(CloudTask<FileMetadata> task : uploadTasks) {
//            executionService.execute(task);
//        }
//        executionService.execute(mergedTask);

//        return mergedTask.get();
        return null;
    }

    private ICloudManagementService getCloudManagementService(CloudType type) {
        switch(type) {
            case DROPBOX: return dropboxCloudManagementService;
            case GOOGLE_DRIVE: return googleDriveCloudManagementService;
            case ONE_DRIVE: return onedriveCloudManagementService;
            default: return null;
        }
    }

    private static class SynchronizationCloudTask<T, U, W> extends CloudTask<W> {

        private final CloudTask<T> source;
        private final List<CloudTask<U>> destinations;

        SynchronizationCloudTask(ProgressMonitor progressMonitor, Callable<W> callable, CloudTask<T> source, List<CloudTask<U>> destinations) {
            super(progressMonitor, callable);
            this.source = source;
            this.destinations = destinations;
        }

        public CloudTask<T> getSource() {
            return source;
        }

        public List<CloudTask<U>> getDestinations() {
            return destinations;
        }
    }

}
