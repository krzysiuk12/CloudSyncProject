/**
 * Created by mateusz on 18.05.15.
 */
(function () {

    var OneDriveCloudController = function ($scope, $log, oneDriveCloudFactory, initialDirectoryPath) {
        $scope.files = null;
        $scope.currentDirectoryPath = initialDirectoryPath;
        $scope.parentsPaths = [];

        $scope.listFiles = function (directoryPath) {
            oneDriveCloudFactory.getAllDirectoryFiles({
                "path": directoryPath
            })
                .success(function (response) {
                    console.log('OneDriveCloudController - listFiles - success.');
                    $scope.files = response.result;
                    $scope.parentsPaths.push($scope.currentDirectoryPath);
                    $scope.currentDirectoryPath = directoryPath;
                })
                .error(function (error) {
                    console.log('OneDriveCloudController - listFiles - error.');
                    $scope.errors = error;
                    $log.log(error);
                });
        };

        $scope.goTo = function (file) {
            if (file.type == "DIRECTORY") {
                console.log("go to: " + file.path.path);
                $scope.listFiles(file.path.path);
            }
        };


        $scope.goBack = function () {
            console.log("goBack()")
            if ($scope.parentsPaths.length > 0) {
                parentPath = $scope.parentsPaths.pop();
                oneDriveCloudFactory.getAllDirectoryFiles({
                    "path": parentPath
                })
                    .success(function (response) {
                        console.log('OneDriveCloudController - goBack - success.');
                        $scope.files = response.result;
                        $scope.currentDirectoryPath = parentPath;
                    })
                    .error(function (error) {
                        console.log('OneDriveCloudController - goBack - error.');
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
    };

    OneDriveCloudController.$inject = ['$scope', '$log', 'oneDriveCloudFactory', 'initialDirectoryPath'];

    angular.module('cloudSyncApp').controller('OneDriveCloudController', OneDriveCloudController);

}());