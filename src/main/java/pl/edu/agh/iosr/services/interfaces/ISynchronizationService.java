package pl.edu.agh.iosr.services.interfaces;

import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.domain.synchronization.SynchronizationEntry;

import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-05-18.
 */
public interface ISynchronizationService {

    public void addSynchronizationRule(String login, SynchronizationEntry entry) throws Exception;

    public void performAutomaticSynchronization() throws Exception;

    public List<ProgressAwareFuture<FileMetadata>> synchronize(SynchronizationEntry synchronizationEntry) throws Exception;

}
