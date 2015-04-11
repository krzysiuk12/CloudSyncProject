package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudManagementService;
import pl.edu.agh.iosr.cloud.dropbox.services.DropboxCloudSessionService;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.LoginCloudSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;

import java.util.ArrayList;

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
        dropboxCloudSessionService.logoutUser(sessionId);
        return new ResponseSerializer(ResponseStatus.OK);
    }

}
