package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudManagementService;
import pl.edu.agh.iosr.cloud.googledrive.services.GoogleDriveCloudSessionService;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mateusz Drożdż on 18.04.15.
 */
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
            return new ResponseSerializer<String>(url);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return new ResponseSerializer<String>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "login")
    public ResponseSerializer<BasicSession> loginUser(@RequestBody LoginCloudSerializer loginCloudSerializer) {
        System.out.println("GOOGLE LOGIN!!!");
        try {
            BasicSession basicSession = googleDriveCloudSessionService.loginUser(loginCloudSerializer.getLogin(), loginCloudSerializer.getAuthorizationCode());
            return new ResponseSerializer<BasicSession>(basicSession);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<BasicSession>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public ResponseSerializer logout(@RequestHeader("cloudSessionId") String sessionId) {
        System.out.println("GOOGLE - LOGOUT!!!");
        googleDriveCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<CloudPath>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        System.out.println("GOOGLE - LIST ALL FILES!!!");
        try {
            List<CloudPath> files = googleDriveCloudManagementService.listAllDirectoryFiles(sessionId, directory);
            return new ResponseSerializer<List<CloudPath>>(files);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<List<CloudPath>>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
