package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "dropbox")
public class DropboxController {

    private DropboxCloudSessionService dropboxCloudSessionService;
    private DropboxCloudManagementService dropboxCloudManagementService;

    @Autowired
    public DropboxController(DropboxCloudSessionService dropboxCloudSessionService, DropboxCloudManagementService dropboxCloudManagementService) {
        this.dropboxCloudSessionService = dropboxCloudSessionService;
        this.dropboxCloudManagementService = dropboxCloudManagementService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "authUrl")
    public ResponseSerializer<String> authorizationUrl() {
        try {
            String authorizationUrl = dropboxCloudSessionService.getAuthorizationUrl();
            return new ResponseSerializer<>(authorizationUrl);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "login")
    public ResponseSerializer<BasicSession> loginUser(@RequestBody LoginCloudSerializer loginCloudSerializer) {
        try {
            BasicSession basicSession = dropboxCloudSessionService.loginUser(loginCloudSerializer.getLogin(), loginCloudSerializer.getAuthorizationCode());
            return new ResponseSerializer<>(basicSession);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public ResponseSerializer logout(@RequestHeader("cloudSessionId") String sessionId) {
        dropboxCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<FileMetadata>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        try {
            ProgressAwareFuture<List<FileMetadata>> future = dropboxCloudManagementService.listAllDirectoryFiles(sessionId, directory);
            List<FileMetadata> files = future.get();
            return new ResponseSerializer<>(files);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

    @RequestMapping(method = RequestMethod.POST, value = "delete")
    public ResponseSerializer<Boolean> deleteFile(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath file) {
        try {
            ProgressAwareFuture<Boolean> future = dropboxCloudManagementService.deleteFile(sessionId, file);
            future.get();
            return new ResponseSerializer<>();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
