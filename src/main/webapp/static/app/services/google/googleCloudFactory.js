/**
 * Created by mateusz on 26.04.15.
 */
(function() {
    var googleCloudFactory = function ($http, googleSession) {

        var factory = {};
        factory.getAllDirectoryFiles = function (request) {
            console.log(googleSession.sessionId);
            $http.defaults.headers.common['Content-Type'] = 'application/json';
            $http.defaults.headers.common['cloudSessionId'] = googleSession.sessionId;
            return $http.post('/google/listFiles', request);
        };

        return factory;
    };

    angular.module('cloudSyncApp').factory('googleCloudFactory', googleCloudFactory);
}());