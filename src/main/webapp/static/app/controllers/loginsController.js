/**
 * Created by mateusz on 25.04.15.
 */
(function () {

    var LoginsController = function ($scope, sessionService) {
        $scope.logins = [];
        $scope.sessions = sessionService;

        $scope.logins = [
            {name: 'Google Drive', login: '#/googlelogin', cloud: '#/googlecloud'},
            {name: 'Dropbox', login: '#/dropboxsession', cloud: '#/dropboxmanagement'},
            {name: 'OneDrive', login: '#/onedrivelogin', cloud: '#/onedrivecloud'}
        ];

    };



    //LoginsController.$inject = ['$scope'];

    angular.module('cloudSyncApp').controller('LoginsController', LoginsController);

}());