/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    angular.module('cloudSyncApp')
        .constant('dropboxRedirectUrl', '/dropboxmanagement')
        .controller('DropboxSessionController', function ($scope, $log, $location, dropboxSessionFactory, sessionService, dropboxRedirectUrl) {

            $scope.authorization = {
                url : null,
                code : null
            };
            $scope.userName = "Janek";
            $scope.errors = null;

            $scope.getAuthorizationUrl = function() {
                dropboxSessionFactory.getAuthorizationUrl()
                    .success(function (response) {
                        $scope.authorization.url = response.result;
                    })
                    .error(function (error) {
                        $scope.errors = error;
                        $log.log(error);
                    });
            };

            $scope.login = function () {
                var postData = {
                    "login" : $scope.userName,
                    "authorizationCode" : $scope.authorization.code
                };
                dropboxSessionFactory.login(postData)
                    .success(function (response) {
                        console.log('DropboxSessionController - login.' + postData);
                        sessionService.setDropbox({sessionId : response.result.sessionId});
                        console.log('DropboxSessionController - login - User: ' + $scope.userName + " logged with sessionId: " + response.result.sessionId);
                    })
                    .error(function (error) {
                        $scope.errors = error;
                        $log.log(error);
                    })
                    .finally(function() {
                        $location.path(dropboxRedirectUrl);
                    });
            };

            if($scope.authorization.code === null) {
                $scope.getAuthorizationUrl();
            }

        });
}());