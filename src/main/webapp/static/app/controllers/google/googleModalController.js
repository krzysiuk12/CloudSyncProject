/**
 * Created by mateusz on 14.06.15.
 */

(function () {
    // Please note that $modalInstance represents a modal window (instance) dependency.
// It is not the same as the $modal service used above.

    angular.module('cloudSyncApp').controller('GoogleModalController', function ($scope, $log, googleCloudFactory, sessionService, synchronizationService, $modalInstance, source) {

        $scope.files = {};
        $scope.currentDirPath = {"path" : "root"};
        $scope.parents = [];

        $scope.source = source;
        $scope.destination = {
            "sessionId" : sessionService.getGoogle().sessionId,
            "fileName" : "",
            "cloudPath" : {
                "path" : "",
                "type" : "GOOGLE_DRIVE"
            }
        };
        $scope.destinationSelected = false;

        function init() {
            console.log("GoogleCloudController: init()");
            googleCloudFactory.getAllDirectoryFiles({ "path" : "root"})
                .success(function (response) {
                    $scope.files = response.result;
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        }

        $scope.goTo = function (file) {
            if (file.type == "DIRECTORY") {
                console.log("go to: " + file.path.path);
                googleCloudFactory.getAllDirectoryFiles(file.path)
                    .success(function (response) {
                        $scope.files = response.result;
                        $scope.parents.push($scope.currentDirPath);
                        $scope.currentDirPath = file.path;
                    })
                    .error(function (data, status, headers, config) {
                        // handle error
                        $log.log(data.error + ' ' + status);
                    });

            } else {
                console.log(file.fileName + " is not directory");
            }
        };

        $scope.goBack = function () {
            if ($scope.parents.length > 0) {
                parent = $scope.parents.pop();
                console.log("Back to: " + parent.path);
                googleCloudFactory.getAllDirectoryFiles(parent)
                    .success(function (response) {
                        $scope.files = response.result;
                        $scope.currentDirPath = parent;
                    })
                    .error(function (data, status, headers, config) {
                        // handle error
                        $log.log(data.error + ' ' + status);
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

        init();

    });
}());