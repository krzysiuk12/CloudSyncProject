package pl.edu.agh.iosr.cloud.dropbox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.interfaces.ICloudManagementService;
import pl.edu.agh.iosr.cloud.common.tasks.CloudTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
@Service
public class DropboxCloudManagementService implements ICloudManagementService {

    private DropboxCloudSessionService cloudSessionService;

    @Autowired
    public DropboxCloudManagementService(DropboxCloudSessionService cloudSessionService) {
        this.cloudSessionService = cloudSessionService;
    }

    @Override
    public CloudTask<List<CloudPath>> listAllDirectoryFiles(String sessionId, CloudPath cloudDirectory) {
        return null;
    }

    @Override
    public CloudTask<CloudPath> downloadFile(String sessionId, CloudPath cloudPath, OutputStream outputStream) {
        return null;
    }

    @Override
    public CloudTask<CloudPath> uploadFile(String sessionId, CloudPath cloudPath, InputStream fileInputStream) {
        return null;
    }

    @Override
    public CloudTask<Boolean> deleteFile(String sessionId, CloudPath cloudPath) {
        return null;
    }

}
