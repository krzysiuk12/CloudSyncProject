/**
 * Created by mateusz on 25.04.15.
 */
(function () {

    var GoogleLoginController = function ($scope, $log, $location, googleLoginFactory, sessionService) {
        $scope.authenticationUrl = '';
        $scope.authenticationCode = '';
        $scope.userName = 'Janek';

        $scope.response = {};


        function init() {
            console.log("GoogleLoginController: init()");
            googleLoginFactory.getAuthenticationUrl()
                .success(function (response) {
                    $scope.authenticationUrl = response.result;
                })
                .error(function (data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        }

        init();

        $scope.logIn = function (hash) {
            postData = {
                "login" : $scope.userName,
                "authorizationCode" : $scope.authenticationCode
            };
            googleLoginFactory.logIn(postData)
                .success(function (response) {
                    $scope.response = response;
                    sessionService.setGoogle({sessionId : response.result.sessionId});
                    console.log('googleSessionId: ' + response.result.sessionId);
                    console.log("Google loggedIn: " + postData);
                    console.log("go to "+hash);
                    $location.path(hash);
                })
                .error(function (data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        };
    };

    angular.module('cloudSyncApp').controller('GoogleLoginController', GoogleLoginController);

}());