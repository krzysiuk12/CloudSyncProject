package pl.edu.agh.iosr.repository.implementation;

import org.springframework.stereotype.Repository;
import pl.edu.agh.iosr.domain.account.UserAccount;
import pl.edu.agh.iosr.domain.synchronization.SynchronizationEntry;
import pl.edu.agh.iosr.repository.interfaces.ISynchronizationRepository;

import java.util.*;

/**
 * Created by ps_krzysztof on 2015-06-01.
 */
@Repository
public class SynchronizationRepository implements ISynchronizationRepository {

    private Map<UserAccount, List<SynchronizationEntry>> synchronizationEntryMap = new HashMap<>();

    @Override
    public void addNewSynchronizationRule(UserAccount userAccount, SynchronizationEntry entry) {
        if(synchronizationEntryMap.containsKey(userAccount)) {
            synchronizationEntryMap.get(userAccount).add(entry);
        } else {
            synchronizationEntryMap.put(userAccount, Arrays.asList(entry));
        }
    }

    @Override
    public Collection<SynchronizationEntry> getSynchronizationEntries() {
        Collection<SynchronizationEntry> resultEntries = new ArrayList<>();
        for(UserAccount userAccount : synchronizationEntryMap.keySet()) {
            resultEntries.addAll(synchronizationEntryMap.get(userAccount));
        }
        return resultEntries;
    }

    @Override
    public Collection<SynchronizationEntry> getSynchronizationEntriesForUser(UserAccount userAccount) {
        return synchronizationEntryMap.get(userAccount);
    }

}
