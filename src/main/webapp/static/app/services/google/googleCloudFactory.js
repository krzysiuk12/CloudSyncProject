/**
 * Created by mateusz on 26.04.15.
 */
(function() {
    var googleCloudFactory = function ($http, sessionService) {

        var factory = {};
        factory.getAllDirectoryFiles = function (request) {
            console.log('Google Cloud Factory - listFiles request for sessionId: ' + sessionService.getGoogle().sessionId);
            $http.defaults.headers.common['Content-Type'] = 'application/json';
            //$http.defaults.headers.common['cloudSessionId'] = googleSession.sessionId;
            $http.defaults.headers.common['cloudSessionId'] = sessionService.getGoogle().sessionId;
            return $http.post('/google/listFiles', request);
        };

        return factory;
    };

    angular.module('cloudSyncApp').factory('googleCloudFactory', googleCloudFactory);
}());