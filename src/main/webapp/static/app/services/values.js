/**
 * Created by mateusz on 26.04.15.
 */

angular.module('cloudSyncApp').value('appSettings', {
    title: 'cloudSyncApp',
    version: '1.0'
});

angular.module('cloudSyncApp').value('googleSession', {
    id: '',
    sessionId: ''
});

angular.module('cloudSyncApp').value('dropboxSession', {
    id: 'DropboxId',
    sessionId: 'DropboxSessionId'
});

angular.module('cloudSyncApp').value('oneDriveSession', {
    id: '',
    sessionId: ''
});

angular.module('cloudSyncApp').value('synchroConf', {
    "source" : {},
    "destination" : [],
    "type" : "FORWARD_PROPAGATION"
});

angular.module('cloudSyncApp').constant('initialDirectoryPath', '/');