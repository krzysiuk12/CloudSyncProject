/**
 * Created by mateusz on 18.05.15.
 */

(function () {

    var OneDriveLoginController = function ($scope, $log, $location, oneDriveLoginFactory, sessionService) {
        $scope.authenticationUrl = '';
        $scope.authenticationCode = '';
        $scope.userName = 'Janek';

        $scope.response = {};

        function init() {
            console.log("OneDriveLoginController: init()");
            oneDriveLoginFactory.getAuthenticationUrl()
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
            oneDriveLoginFactory.logIn(postData)
                .success(function (response) {
                    $scope.response = response;
                    sessionService.setOneDrive({sessionId : response.result.sessionId});
                    console.log('OneDriveSessionId : ' + sessionService.getOneDrive().sessionId);
                    console.log(('OneDrive loggedIn: ' + postData));
                    console.log("go to "+hash);
                    $location.path(hash);
                })
                .error(function (data, status, headers, config) {
                    $log.log(data.error + ' ' + status);
                });
        };
    };

    angular.module('cloudSyncApp').controller('OneDriveLoginController', OneDriveLoginController);

}());