/**
 * Created by mateusz on 25.04.15.
 */
(function() {

    var app = angular.module('cloudSyncApp', ['ngRoute']);

    app.config(function($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'LoginsController',
                templateUrl: '/static/app/views/logins.html'
            })
            .when('/googlelogin', {
                controller: 'GoogleLoginController',
                templateUrl: '/static/app/views/google/googleLogin.html'
            })
            .when('/googlecloud', {
                controller: 'GoogleCloudController',
                templateUrl: '/static/app/views/google/googleCloud.html'
            })
            .when('/dropboxsession', {
                controller: 'DropboxSessionController',
                templateUrl: '/static/app/views/dropbox/dropboxSession.html'
            })
            .when('/dropboxmanagement', {
                controller: 'DropboxCloudManagementController',
                templateUrl: '/static/app/views/dropbox/dropboxCloudManagement.html'
            })
            .otherwise( { redirectTo: '/' });
    });

}());