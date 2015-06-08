package pl.edu.agh.iosr.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.domain.account.UserAccount;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.services.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.domain.cloud.session.UserSession;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.domain.cloud.session.DropboxCloudSession;
import pl.edu.agh.iosr.domain.cloud.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.domain.cloud.session.OnedriveCloudSession;
import pl.edu.agh.iosr.repository.interfaces.ICloudSessionRepository;
import pl.edu.agh.iosr.repository.interfaces.ISynchronizationRepository;
import pl.edu.agh.iosr.services.interfaces.IExecutionService;
import pl.edu.agh.iosr.services.interfaces.ISynchronizationService;
import pl.edu.agh.iosr.domain.synchronization.SynchronizationEntry;
import pl.edu.agh.iosr.domain.synchronization.SynchronizationSingleCloudEntry;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        PipedOutputStream outputStream = new PipedOutputStream();
        getDownloadProgressAwareFuture(userSession, from.getCloudPath(), outputStream, from.getCloudPath().getType());
        PipedInputStream inputStream = null;
        try {
            inputStream = new PipedInputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getUpdateProgressAwareFuture(userSession, to.getCloudPath(), getUploadedFileName(from.getCloudPath().getPath()), inputStream, to.getCloudPath().getType());
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

    private String getUploadedFileName(String downloadedFilePath) {
        return downloadedFilePath.substring(downloadedFilePath.lastIndexOf("/"), downloadedFilePath.length());
    }

}
