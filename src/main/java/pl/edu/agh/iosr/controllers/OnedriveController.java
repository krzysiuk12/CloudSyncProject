package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.files.FileMetadata;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.common.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudSessionService;
import pl.edu.agh.iosr.cloud.onedrive.sessionswtf.OnedriveCloudSession;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;

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
        System.out.println("ONEDRIVE AUTHORIZATION URL");
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
        System.out.println("HERE I AM - LOGIN!!!");
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
        System.out.println("HERE I AM - LOGOUT!!!");
        onedriveCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<FileMetadata>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        System.out.println("HERE I AM - LIST ALL FILES!!!");
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
