package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.session.BasicSession;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.services.implementation.OnedriveCloudManagementService;
import pl.edu.agh.iosr.services.implementation.OnedriveCloudSessionService;
import pl.edu.agh.iosr.domain.cloud.session.OnedriveCloudSession;
import pl.edu.agh.iosr.controllers.serializers.common.ErrorMessages;
import pl.edu.agh.iosr.controllers.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.controllers.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.controllers.serializers.common.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "onedrive")
public class OnedriveController {

    private final OnedriveCloudSessionService onedriveCloudSessionService;
    private final OnedriveCloudManagementService onedriveCloudManagementService;

    @Autowired
    public OnedriveController(OnedriveCloudSessionService onedriveCloudSessionService, OnedriveCloudManagementService onedriveCloudManagementService) {
        this.onedriveCloudSessionService = onedriveCloudSessionService;
        this.onedriveCloudManagementService = onedriveCloudManagementService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "authUrl")
    public ResponseSerializer<String> getAuthorizationUrl() {
        try {
            String url = onedriveCloudSessionService.getAuthorizationUrl();
            return  new ResponseSerializer<>(url);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "login")
    public ResponseSerializer<BasicSession> loginUser(@RequestBody LoginCloudSerializer loginCloudSerializer) {
        try {
            BasicSession basicSession = onedriveCloudSessionService.loginUser(loginCloudSerializer.getLogin(), loginCloudSerializer.getAuthorizationCode());
            return new ResponseSerializer<>(basicSession);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public ResponseSerializer logout(@RequestHeader("cloudSessionId") String sessionId) {
        onedriveCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<FileMetadata>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        try {
            OnedriveCloudSession session = onedriveCloudSessionService.getSession(sessionId);
            ProgressAwareFuture<List<FileMetadata>> future = onedriveCloudManagementService.listAllDirectoryFiles(session, directory);
            List<FileMetadata> files = future.get();
            return new ResponseSerializer<>(files);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
