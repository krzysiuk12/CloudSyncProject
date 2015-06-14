/**
 * Created by mateusz on 18.05.15.
 */
(function() {
    var oneDriveCloudFactory = function ($http, sessionService) {

        var factory = {};
        factory.getAllDirectoryFiles = function (request) {
            console.log(sessionService.getOneDrive().sessionId);
            $http.defaults.headers.common['Content-Type'] = 'application/json';
            $http.defaults.headers.common['cloudSessionId'] = sessionService.getOneDrive().sessionId;
            return $http.post('/onedrive/listFiles', request);
        };

        return factory;
    };

    angular.module('cloudSyncApp').factory('oneDriveCloudFactory', oneDriveCloudFactory);
}());