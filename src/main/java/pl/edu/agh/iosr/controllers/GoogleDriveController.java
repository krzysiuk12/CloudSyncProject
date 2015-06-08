package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.domain.cloud.files.CloudPath;
import pl.edu.agh.iosr.domain.cloud.files.FileMetadata;
import pl.edu.agh.iosr.domain.cloud.session.BasicSession;
import pl.edu.agh.iosr.domain.cloud.tasks.ProgressAwareFuture;
import pl.edu.agh.iosr.services.implementation.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.services.implementation.GoogleDriveCloudSessionService;
import pl.edu.agh.iosr.domain.cloud.session.GoogleDriveCloudSession;
import pl.edu.agh.iosr.controllers.serializers.common.ErrorMessages;
import pl.edu.agh.iosr.controllers.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.controllers.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.controllers.serializers.common.ResponseStatus;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "google")
public class GoogleDriveController {
    @Autowired
    private GoogleDriveCloudSessionService googleDriveCloudSessionService;
    @Autowired
    private GoogleDriveCloudManagementService googleDriveCloudManagementService;

    @RequestMapping(method = RequestMethod.GET, value = "authUrl")
    public ResponseSerializer<String> getAuthorizationUrl() {
        System.out.println("GOOGLE AUTHORIZATION URL");
        try {
            String url = googleDriveCloudSessionService.getAuthorizationUrl();
            return new ResponseSerializer<>(url);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "login")
    public ResponseSerializer<BasicSession> loginUser(@RequestBody LoginCloudSerializer loginCloudSerializer) {
        System.out.println("GOOGLE LOGIN!!!");
        try {
            BasicSession basicSession = googleDriveCloudSessionService.loginUser(loginCloudSerializer.getLogin(), loginCloudSerializer.getAuthorizationCode());
            return new ResponseSerializer<>(basicSession);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public ResponseSerializer logout(@RequestHeader("cloudSessionId") String sessionId) {
        System.out.println("GOOGLE - LOGOUT!!!");
        googleDriveCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<FileMetadata>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        System.out.println("GOOGLE - LIST ALL FILES!!!");
        try {
            GoogleDriveCloudSession session = googleDriveCloudSessionService.getSession(sessionId);
            ProgressAwareFuture<List<FileMetadata>> future = googleDriveCloudManagementService.listAllDirectoryFiles(session, directory);
            List<FileMetadata> files = future.get();
            return new ResponseSerializer<>(files);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
