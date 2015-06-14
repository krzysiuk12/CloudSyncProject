/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    angular.module('cloudSyncApp')
        .constant('initialDirectoryPath', '/')
        .controller('DropboxCloudManagementController', function ($scope, $log, dropboxCloudManagementFactory, initialDirectoryPath, sessionService, $modal) {

            $scope.files = null;
            $scope.currentDirectoryPath = initialDirectoryPath;
            $scope.parentsPaths = [];

            $scope.sessions = sessionService;

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
                console.log("goBack()");
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
                            $scope.errors = error;
                            $log.log(error);
                        });
                } else {
                    console.log("Already in the root");
                }
            };

            // todo: firstly check session availability
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
                        source : function () {
                            return {
                                "sessionId" : sessionService.getDropbox().sessionId,
                                "fileName" : srcFile.fileName,
                                "cloudPath" : {
                                    "path" : pathString,
                                    "type" : "DROPBOX"
                                }
                            };
                        }
                    }
                });

                modalInstance.result.then(function () {}, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.syncOneDrive = function (srcFile) {
                var pathString = '';
                if ($scope.currentDirectoryPath === '/') {
                    pathString = '/' + srcFile.fileName;
                } else {
                    pathString = $scope.currentDirectoryPath + '/' + srcFile.fileName;
                }
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: '/static/app/views/modalWindow.html',
                    controller: 'OneDriveModalController',
                    resolve: {
                        source : function () {
                            return {
                                "sessionId" : sessionService.getDropbox().sessionId,
                                "fileName" : srcFile.fileName,
                                "cloudPath" : {
                                    "path" : pathString,
                                    "type" : "DROPBOX"
                                }
                            };
                        }
                    }
                });

                modalInstance.result.then(function () {}, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };


            if ($scope.files === null) {
                $scope.listFiles(initialDirectoryPath);
            }
        });
}());