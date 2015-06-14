/**
 * Created by mateusz on 18.05.15.
 */
(function () {

    var OneDriveCloudController = function ($scope, $log, oneDriveCloudFactory, initialDirectoryPath, sessionService, $modal) {
        $scope.files = null;
        $scope.currentDirectoryPath = initialDirectoryPath;
        $scope.parentsPaths = [];

        $scope.sessions = sessionService;

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

        $scope.syncGoogle = function (srcFile) {
            var pathString = '';
            if ($scope.currentDirectoryPath === '/') {
                pathString = '/' + srcFile.fileName;
            } else {
                pathString = $scope.currentDirectoryPath + '/' + srcFile.fileName;
            }
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/static/app/views/modalWindow.html',
                controller: 'GoogleModalController',
                resolve: {
                    source: function () {
                        return {
                            "sessionId": sessionService.getOneDrive().sessionId,
                            "fileName": srcFile.fileName,
                            "cloudPath": {
                                "path": pathString,
                                "type": "ONE_DRIVE"
                            }
                        };
                    }
                }
            });

            modalInstance.result.then(function () {
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        $scope.syncDropbox = function (srcFile) {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/static/app/views/modalWindow.html',
                controller: 'DropboxModalController',
                resolve: {
                    source: function () {
                        return {
                            "sessionId": sessionService.getOneDrive().sessionId,
                            "fileName": srcFile.fileName,
                            "cloudPath": {
                                "path": srcFile.path.path,
                                "type": "ONE_DRIVE"
                            }
                        };
                    }
                }
            });

            modalInstance.result.then(function () {
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };


        if ($scope.files === null) {
            $scope.listFiles(initialDirectoryPath);
        }
    };

    angular.module('cloudSyncApp').controller('OneDriveCloudController', OneDriveCloudController);

}());