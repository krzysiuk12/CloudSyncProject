/**
 * Created by mateusz on 18.05.15.
 */

(function() {
    var oneDriveLoginFactory = function ($http) {

        var factory = {};
        factory.getAuthenticationUrl = function () {
            return $http.get('/onedrive/authUrl')
        };

        factory.logIn = function (login) {
            return $http.post('/onedrive/login', login);
        }

        return factory;
    };

    angular.module('cloudSyncApp').factory('oneDriveLoginFactory', oneDriveLoginFactory);
}());