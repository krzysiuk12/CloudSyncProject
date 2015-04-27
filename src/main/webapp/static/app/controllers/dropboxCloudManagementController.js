/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    angular.module('cloudSyncApp')
        .constant('initialDirectoryPath', '/')
        .controller('DropboxCloudManagementController', function ($scope, $log, dropboxCloudManagementFactory, initialDirectoryPath) {

            $scope.files = null;
            $scope.currentDirectoryPath = null;

            $scope.listFiles = function (directoryPath) {
                $scope.currentDirectoryPath = directoryPath;
                dropboxCloudManagementFactory.listFiles({
                    "id": 0,
                    "path": directoryPath,
                    "type": "DIRECTORY",
                    "fileName": "",
                    "extension": "",
                    "size": 0,
                    "lastModificationDate": null
                })
                .success(function (response) {
                    console.log('DropboxCloudManagementController - listFiles - success.');
                    $scope.files = response.result;
                })
                .error(function (error) {
                    console.log('DropboxCloudManagementController - listFiles - error.');
                    $scope.errors = error
                    $log.log(error);
                });
            };

            if ($scope.files === null) {
                $scope.listFiles(initialDirectoryPath);
            }
        });
}());