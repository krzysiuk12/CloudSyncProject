/**
 * Created by mateusz on 25.04.15.
 */
(function () {

    var GoogleLoginController = function ($scope, $log, $location, googleLoginFactory, googleSession) {
        $scope.authenticationUrl = '';
        $scope.authenticationCode = '';
        $scope.userName = 'wiesiek';

        $scope.googleSession = googleSession;
        $scope.response = {};


        function init() {
            googleLoginFactory.getAuthenticationUrl()
                .success(function (response) {
                    $scope.authenticationUrl = response.result;
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        };

        init();

        $scope.logIn = function (hash) {
            postData = {
                "login" : $scope.userName,
                "authorizationCode" : $scope.authenticationCode
            }
            googleLoginFactory.logIn(postData)
                .success(function (response) {
                    $scope.response = response;
                    $scope.googleSession['sessionId'] = response.result.sessionId;
                    console.log($scope.googleSession.sessionId);
                    console.log("go to "+hash);
                    $location.path(hash);
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        };
    };

    GoogleLoginController.$inject = ['$scope', '$log', '$location', 'googleLoginFactory', 'googleSession'];

    angular.module('cloudSyncApp').controller('GoogleLoginController', GoogleLoginController);

}());