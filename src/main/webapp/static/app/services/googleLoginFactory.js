/**
 * Created by mateusz on 25.04.15.
 */
(function() {
    var googleLoginFactory = function ($http) {

        var factory = {};
        factory.getAuthenticationUrl = function () {
            return $http.get('/google/login')
        };

        factory.logIn = function (login) {
            return $http.post('/google/login', login);
        }

        return factory;
    };

    angular.module('cloudSyncApp').factory('googleLoginFactory', googleLoginFactory);
}());