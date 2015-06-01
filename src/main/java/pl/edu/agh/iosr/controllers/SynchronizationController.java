package pl.edu.agh.iosr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.iosr.exceptions.ErrorMessages;
import pl.edu.agh.iosr.serializers.common.ResponseSerializer;
import pl.edu.agh.iosr.serializers.common.ResponseStatus;
import pl.edu.agh.iosr.synchronization.SynchronizationEntry;
import pl.edu.agh.iosr.synchronization.service.ISynchronizationService;

import java.util.ArrayList;

/**
 * Created by Krzysztof Kicinger on 2015-05-21.
 */
@RestController
@RequestMapping(value = "synchronization")
public class SynchronizationController {

    private ISynchronizationService synchronizationService;

    @Autowired
    public SynchronizationController(ISynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseSerializer synchronize(@RequestBody SynchronizationEntry synchronizationEntry) {
        try {
            synchronizationService.synchronize(synchronizationEntry);
            return new ResponseSerializer();
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseSerializer<>(ResponseStatus.UNKNOWN_SERVER_ERROR, new ArrayList<ErrorMessages>());
        }
    }

}
