package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.iosr.cloud.common.files.CoolCloudPath;
import pl.edu.agh.iosr.cloud.common.files.CoolFileMetadata;
import pl.edu.agh.iosr.cloud.common.session.BasicSession;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudManagementService;
import pl.edu.agh.iosr.cloud.onedrive.services.OnedriveCloudSessionService;
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
    public ResponseSerializer<List<CoolFileMetadata>> listAllDirectoryFiles(@RequestHeader("cloudSessionId") String sessionId) {
        System.out.println("HERE I AM - LIST ALL FILES!!!");
        try {
            List<CoolFileMetadata> paths = onedriveCloudManagementService.listAllDirectoryFiles(sessionId, new CoolCloudPath("/"));
            return new ResponseSerializer<>(paths);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
    }

}
