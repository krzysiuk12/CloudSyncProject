/**
 * Created by mateusz on 25.04.15.
 */
(function() {

    var app = angular.module('cloudSyncApp', ['ngRoute', 'ui.bootstrap']);

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
            .when('/onedrivelogin', {
                controller: 'OneDriveLoginController',
                templateUrl: '/static/app/views/onedrive/oneDriveLogin.html'
            })
            .when('/onedrivecloud', {
                controller: 'OneDriveCloudController',
                templateUrl: '/static/app/views/onedrive/oneDriveCloud.html'
            })
            .when('/synchronization', {
                controller: 'SynchronizationInfoController',
                templateUrl: '/static/app/views/synchronizationInfo.html'
            })
            .otherwise( { redirectTo: '/' });
    });

}());