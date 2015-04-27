/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    var DropboxSessionController = function ($scope, $log, $location, dropboxSessionFactory, dropboxSession) {

        $scope.authenticationUrl = '';
        $scope.authenticationCode = '';
        $scope.userName = 'DropboxUser';
        $scope.dropboxSession = dropboxSession;


        function init() {
            dropboxSessionFactory.getAuthenticationUrl()
                .success(function (response) {
                    $scope.authenticationUrl = response.result;
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        };

        init();

        $scope.login = function (redirectPageUrl) {
            postData = {
                "login" : $scope.userName,
                "authorizationCode" : $scope.authenticationCode
            }
            dropboxSessionFactory.login(postData)
                .success(function (response) {
                    console.log('DropboxSessionController - login.');
                    $scope.dropboxSession['sessionId'] = response.result.sessionId;
                    console.log('DropboxSessionController - login - reponse: ' + response);
                    console.log('DropboxSessionController - login - User: ' + $scope.userName + " logged with sessionId: " + $scope.dropboxSession.sessionId)
                    $location.path(redirectPageUrl);
                })
                .error(function (data, status, headers, config) {
                    console.log('DropboxSessionController - error: ' + data.error);
                    $log.log(data.error + ' ' + status);
                });
        };
    };

    DropboxSessionController.$inject = ['$scope', '$log', '$location', 'dropboxSessionFactory', 'dropboxSession'];

    angular.module('cloudSyncApp').controller('DropboxSessionController', DropboxSessionController);

}());