/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
/**
 * Created by mateusz on 25.04.15.
 */
(function() {
    var dropboxSessionFactory = function ($http) {

        var factory = {};

        factory.getAuthorizationUrl = function () {
            console.log("Dropbox Session Factory - authenticationUrl");
            return $http.get('/dropbox/authUrl')
        };

        factory.login = function (login) {
            console.log("Dropbox Session Factory - login");
            return $http.post('/dropbox/login', login);
        }

        factory.logout = function() {
            console.log("Dropbox Session Factory - logout");
            return $http.post('/dropbox/logout');
        }

        return factory;
    };

    angular.module('cloudSyncApp').factory('dropboxSessionFactory', dropboxSessionFactory);
}());