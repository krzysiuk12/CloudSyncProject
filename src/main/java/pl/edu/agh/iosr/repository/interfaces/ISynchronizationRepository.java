package pl.edu.agh.iosr.repository.interfaces;

import pl.edu.agh.iosr.domain.account.UserAccount;
import pl.edu.agh.iosr.domain.synchronization.SynchronizationEntry;

import java.util.Collection;

/**
 * Created by ps_krzysztof on 2015-06-01.
 */
public interface ISynchronizationRepository {

    public void addNewSynchronizationRule(UserAccount userAccount, SynchronizationEntry entry);

    public Collection<SynchronizationEntry> getSynchronizationEntries();

    public Collection<SynchronizationEntry> getSynchronizationEntriesForUser(UserAccount userAccount);

}
