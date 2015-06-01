package pl.edu.agh.iosr.synchronization.service;

import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.synchronization.SynchronizationEntry;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-05-18.
 */
public interface ISynchronizationService {

    public void addSynchronizationRule(String login, SynchronizationEntry entry) throws Exception;

    public void performAutomaticSynchronization() throws Exception;

    public List<ProgressAwareFuture<FileMetadata>> synchronize(SynchronizationEntry synchronizationEntry) throws Exception;

}
