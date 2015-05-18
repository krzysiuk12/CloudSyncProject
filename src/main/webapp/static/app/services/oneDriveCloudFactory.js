/**
 * Created by mateusz on 18.05.15.
 */
(function() {
    var oneDriveCloudFactory = function ($http, oneDriveSession) {

        var factory = {};
        factory.getAllDirectoryFiles = function (request) {
            console.log(oneDriveSession.sessionId);
            $http.defaults.headers.common['Content-Type'] = 'application/json';
            $http.defaults.headers.common['cloudSessionId'] = oneDriveSession.sessionId;
            return $http.post('/onedrive/listFiles', request);
        };

        return factory;
    };

    angular.module('cloudSyncApp').factory('oneDriveCloudFactory', oneDriveCloudFactory);
}());