/**
 * Created by mateusz on 25.04.15.
 */
(function () {

    var LoginsController = function ($scope) {
        $scope.logins = [];

        $scope.logins = [
            {name: 'Google Drive', url: '#/googlelogin'},
            {name: 'Dropbox', url: '#/dropboxsession'},
            {name: 'OneDrive', url: '#/onedrivelogin'}
        ];
    };

    LoginsController.$inject = ['$scope'];

    angular.module('cloudSyncApp').controller('LoginsController', LoginsController);

}());