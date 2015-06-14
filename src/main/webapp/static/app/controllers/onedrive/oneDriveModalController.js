/**
 * Created by mateusz on 14.06.15.
 */
(function () {
    angular.module('cloudSyncApp').controller('OneDriveModalController', function ($scope, $log, oneDriveCloudFactory, initialDirectoryPath, sessionService, synchronizationService, $modalInstance, source) {

        $scope.files = null;
        $scope.currentDirectoryPath = initialDirectoryPath;
        $scope.parentsPaths = [];

        $scope.source = source;
        $scope.destination = {
            "sessionId" : sessionService.getOneDrive().sessionId,
            "fileName" : "",
            "cloudPath" : {
                "path" : "",
                "type" : "ONE_DRIVE"
            }
        };
        $scope.destinationSelected = false;
        $scope.sessions = sessionService;
        $scope.modalTitle = "OneDrive Cloud";


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