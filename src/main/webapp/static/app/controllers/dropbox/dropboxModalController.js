/**
 * Created by mateusz on 14.06.15.
 */

(function () {
    angular.module('cloudSyncApp').controller('DropboxModalController', function ($scope, $log, dropboxCloudManagementFactory, initialDirectoryPath, sessionService, synchronizationService, $modalInstance, source) {

        $scope.files = null;
        $scope.currentDirectoryPath = initialDirectoryPath;
        $scope.parentsPaths = [];

        $scope.source = source;
        $scope.destination = {
            "sessionId" : sessionService.getDropbox().sessionId,
            "fileName" : "",
            "cloudPath" : {
                "path" : "",
                "type" : "DROPBOX"
            }
        };
        $scope.destinationSelected = false;
        $scope.sessions = sessionService;
        $scope.modalTitle = "Dropbox Cloud";

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

        $scope.markAsDest = function (destFile) {
            $scope.destination.cloudPath.path = destFile.path.path;
            $scope.destination.fileName = destFile.fileName;
            $scope.destinationSelected = true;
        };

        $scope.synchronize = function () {
            console.log('SynchronizationController - synchronize()');
            var syncRule = {
                "login" : "Janek",
                "source" : $scope.source,
                "destination" : [$scope.destination],
                "type" : "FORWARD_PROPAGATION"
            };
            synchronizationService.synchronize(syncRule)
                .success(function (response) {
                    synchronizationService.addRule(syncRule);
                    console.log('SynchronizationController - synchronize - success.');
                })
                .error(function (error) {
                    console.log('SynchronizationController - synchronize - error.');
                    $scope.errors = error;
                });
        };

        $scope.ok = function () {
            $scope.synchronize();
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        if ($scope.files === null) {
            $scope.listFiles(initialDirectoryPath);
        }

    });
}());