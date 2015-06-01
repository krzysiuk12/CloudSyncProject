package pl.edu.agh.iosr.synchronization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.accounts.UserAccount;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.session.UserSession;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressMonitor;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;
import pl.edu.agh.iosr.cloud.onedrive.sessionswtf.OnedriveCloudSession;
import pl.edu.agh.iosr.execution.IExecutionService;
import pl.edu.agh.iosr.repository.ICloudSessionRepository;
import pl.edu.agh.iosr.repository.ISynchronizationRepository;
import pl.edu.agh.iosr.synchronization.SynchronizationEntry;
import pl.edu.agh.iosr.synchronization.SynchronizationSingleCloudEntry;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collection;
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
    private ICloudSessionRepository cloudSessionRepository;
    private ISynchronizationRepository synchronizationRepository;
    private IExecutionService executionService;

    @Autowired
    public SynchronizationService(DropboxCloudManagementService dropboxCloudManagementService, GoogleDriveCloudManagementService googleDriveCloudManagementService, OnedriveCloudManagementService onedriveCloudManagementService, ICloudSessionRepository cloudSessionRepository, ISynchronizationRepository synchronizationRepository, IExecutionService executionService) {
        this.dropboxCloudManagementService = dropboxCloudManagementService;
        this.googleDriveCloudManagementService = googleDriveCloudManagementService;
        this.onedriveCloudManagementService = onedriveCloudManagementService;
        this.cloudSessionRepository = cloudSessionRepository;
        this.synchronizationRepository = synchronizationRepository;
        this.executionService = executionService;
    }

    @Override
    public void addSynchronizationRule(String login, SynchronizationEntry entry) throws Exception {
        synchronizationRepository.addNewSynchronizationRule(new UserAccount(login), entry);
    }

    @Override
    public void performAutomaticSynchronization() throws Exception {
        Collection<SynchronizationEntry> entries = synchronizationRepository.getSynchronizationEntries();
        for(SynchronizationEntry entry : entries) {
            synchronize(entry);
        }
    }

    @Override
    public List<ProgressAwareFuture<FileMetadata>> synchronize(SynchronizationEntry synchronizationEntry) throws Exception {

        final UserSession userSession = cloudSessionRepository.getUserSessionByLogin(synchronizationEntry.getLogin());

        List<ProgressAwareFuture<FileMetadata>> uploadFutures = new ArrayList<>();
        for(SynchronizationSingleCloudEntry entry : synchronizationEntry.getDestination()) {
            uploadFutures.add(synchronize(userSession, synchronizationEntry.getSource(), entry));
        }

        return uploadFutures;

    }

    private ProgressAwareFuture<FileMetadata> synchronize(UserSession userSession, SynchronizationSingleCloudEntry from, SynchronizationSingleCloudEntry to) throws Exception {
        final PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);
        getDownloadProgressAwareFuture(userSession, from.getCloudPath(), outputStream, from.getCloudPath().getType());
        return getUpdateProgressAwareFuture(userSession, to.getCloudPath(), "test", inputStream, to.getCloudPath().getType());
    }

    private ICloudManagementService getCloudManagementService(CloudType type) {
        switch(type) {
            case DROPBOX: return dropboxCloudManagementService;
            case GOOGLE_DRIVE: return googleDriveCloudManagementService;
            case ONE_DRIVE: return onedriveCloudManagementService;
            default: return null;
        }
    }

    private ProgressAwareFuture<Boolean> getDownloadProgressAwareFuture(UserSession userSession, CloudPath path, OutputStream outputStream, CloudType type) throws Exception {
        switch (type) {
            case DROPBOX: return dropboxCloudManagementService.downloadFile((DropboxCloudSession) userSession.getDropboxCloudSession(), path, outputStream);
            case GOOGLE_DRIVE: return googleDriveCloudManagementService.downloadFile((GoogleDriveCloudSession) userSession.getGoogleDriveCloudSession(), path, outputStream);
            case ONE_DRIVE: return onedriveCloudManagementService.downloadFile((OnedriveCloudSession) userSession.getOnedriveCloudSession(), path, outputStream);
            default: return null;
        }
    }

    private ProgressAwareFuture<FileMetadata> getUpdateProgressAwareFuture(UserSession userSession, CloudPath path, String fileName, InputStream inputStream, CloudType type) throws Exception {
        switch (type) {
            case DROPBOX: return dropboxCloudManagementService.uploadFile((DropboxCloudSession) userSession.getDropboxCloudSession(), path, fileName, inputStream);
            case GOOGLE_DRIVE: return googleDriveCloudManagementService.uploadFile((GoogleDriveCloudSession) userSession.getGoogleDriveCloudSession(), path, fileName, inputStream);
            case ONE_DRIVE: return onedriveCloudManagementService.uploadFile((OnedriveCloudSession) userSession.getOnedriveCloudSession(), path, fileName, inputStream);
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
