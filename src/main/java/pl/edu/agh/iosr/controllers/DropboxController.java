package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.files.CloudPath;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Krzysztof Kicinger on 2015-04-11.
 */
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

    @RequestMapping(method = RequestMethod.POST, value = "login")
    public ResponseSerializer<BasicSession> loginUser(@RequestBody LoginCloudSerializer loginCloudSerializer) {
        System.out.println("HERE I AM - LOGIN!!!");
        try {
            BasicSession basicSession = dropboxCloudSessionService.loginUser(loginCloudSerializer.getLogin(), loginCloudSerializer.getAuthorizationCode());
            return new ResponseSerializer<BasicSession>(basicSession);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<BasicSession>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public ResponseSerializer logout(@RequestHeader("cloudSessionId") String sessionId) {
        System.out.println("HERE I AM - LOGOUT!!!");
        dropboxCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "listFiles")
    public ResponseSerializer<List<CloudPath>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId, @RequestBody CloudPath directory) {
        System.out.println("HERE I AM - LIST ALL FILES!!!");
        try {
            List<CloudPath> paths = dropboxCloudManagementService.listAllDirectoryFiles(sessionId, directory);
            return new ResponseSerializer<List<CloudPath>>(paths);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<List<CloudPath>>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
