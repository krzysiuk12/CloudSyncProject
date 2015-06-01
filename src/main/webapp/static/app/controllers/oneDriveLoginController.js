/**
 * Created by mateusz on 18.05.15.
 */

(function () {

    var OneDriveLoginController = function ($scope, $log, $location, oneDriveLoginFactory, oneDriveSession) {
        $scope.authenticationUrl = '';
        $scope.authenticationCode = '';
        $scope.userName = 'wiesiek';

        $scope.oneDriveSession = oneDriveSession;
        $scope.response = {};


        function init() {
            console.log("OneDriveLoginController: init()");
            oneDriveLoginFactory.getAuthenticationUrl()
                .success(function (response) {
                    $scope.authenticationUrl = response.result;
                })
                .error(function (data, status, headers, config) {
                    // handle error
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
                    $scope.oneDriveSession['sessionId'] = response.result.sessionId;
                    console.log($scope.oneDriveSession.sessionId);
                    console.log(('OneDrive loggedIn: ' + postData));
                    console.log("go to "+hash);
                    $location.path(hash);
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        };
    };

    OneDriveLoginController.$inject = ['$scope', '$log', '$location', 'oneDriveLoginFactory', 'oneDriveSession'];

    angular.module('cloudSyncApp').controller('OneDriveLoginController', OneDriveLoginController);

}());