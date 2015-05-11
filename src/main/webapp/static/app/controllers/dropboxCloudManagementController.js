/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    angular.module('cloudSyncApp')
        .constant('initialDirectoryPath', '/')
        .controller('DropboxCloudManagementController', function ($scope, $log, dropboxCloudManagementFactory, initialDirectoryPath) {

            $scope.files = null;
            $scope.currentDirectoryPath = initialDirectoryPath;
            $scope.parentsPaths = [];

            $scope.listFiles = function (directoryPath) {
                dropboxCloudManagementFactory.listFiles({
                    "path": directoryPath
                })
                .success(function (response) {
                    console.log('DropboxCloudManagementController - listFiles - success.');
                    $scope.files = response.result;
                    $scope.parentsPaths.push($scope.currentDirectoryPath);
                    $scope.currentDirectoryPath = directoryPath;
                })
                .error(function (error) {
                    console.log('DropboxCloudManagementController - listFiles - error.');
                    $scope.errors = error
                    $log.log(error);
                });
            };


            $scope.goBack = function () {
                console.log("goBack()")
                if ($scope.parentsPaths.length > 0) {
                    parentPath = $scope.parentsPaths.pop();
                    dropboxCloudManagementFactory.listFiles({
                        "path": parentPath
                    })
                        .success(function (response) {
                            console.log('DropboxCloudManagementController - goBack - success.');
                            $scope.files = response.result;
                            $scope.currentDirectoryPath = parentPath;
                        })
                        .error(function (error) {
                            console.log('DropboxCloudManagementController - goBack - error.');
                            $scope.errors = error
                            $log.log(error);
                        });
                } else {
                    console.log("Already in the root");
                }
            };


            if ($scope.files === null) {
                $scope.listFiles(initialDirectoryPath);
            }
        });
}());