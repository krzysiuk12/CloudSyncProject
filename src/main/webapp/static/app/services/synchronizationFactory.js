/**
 * Created by mateusz on 24.05.15.
 */

(function() {
    var synchronizationFactory = function ($http) {

        var factory = {};

        factory.synchronize = function (request) {
            $http.defaults.headers.common['Content-Type'] = 'application/json';
            return $http.post('/synchronization', request);
        };


        return factory;
    };

    angular.module('cloudSyncApp').factory('synchronizationFactory', synchronizationFactory);
}());