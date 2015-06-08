package pl.edu.agh.iosr.cloud.it;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.agh.iosr.domain.cloud.CloudType;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.session.CloudSession;
import pl.edu.agh.iosr.services.interfaces.ICloudManagementService;

public abstract class AbstractCloudManagementAccessDeniedIT {

    private ICloudManagementService managementService;
    private CloudSession invalidSession;

    @Before
    public void setUp() throws Exception {
        managementService = createManagementService();
        invalidSession = createInvalidSession();
    }

    protected abstract ICloudManagementService createManagementService();
    protected abstract CloudSession createInvalidSession();

    @Ignore
    @Test()
    public void cannotListFiles() throws Exception {
        // when
        //TODO: when exception occurs inside task do not return emptylist or null; raise the exception mate
        //TODO: disgenerify the management service, session service maybe too?
        managementService.listAllDirectoryFiles(invalidSession, new CloudPath("/", CloudType.ONE_DRIVE)).get();
    }
}