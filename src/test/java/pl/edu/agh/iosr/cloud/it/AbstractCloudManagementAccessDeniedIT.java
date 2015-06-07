package pl.edu.agh.iosr.cloud.it;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.agh.iosr.cloud.common.CloudType;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudSessionService;
import pl.edu.agh.iosr.cloud.common.session.CloudSession;
import pl.edu.agh.iosr.cloud.common.session.CloudSessionStatus;
import pl.edu.agh.iosr.cloud.dropbox.session.DropboxCloudSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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