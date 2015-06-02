/**
 * Created by mateusz on 26.04.15.
 */
(function () {

    var GoogleCloudController = function ($scope, $log, googleCloudFactory, googleSession, synchroConf) {
        $scope.files = {};
        $scope.currentDirPath = {"path" : "root"};
        $scope.parents = [];

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

        $scope.asSource = function (file) {
            synchroConf.source = {
                "sessionId" : googleSession.sessionId,
                "cloudPath" : {
                    "path" : file.path.path,
                    "type" : "GOOGLE_DRIVE"
                }
            }
        };
        $scope.asDestination = function (file) {
            synchroConf.destination.push({
                    "sessionId" : googleSession.sessionId,
                    "cloudPath" : {
                        "path" : file.path.path,
                        "type" : "GOOGLE_DRIVE"
                    }
                }
            );
        };

        init();
    };

    GoogleCloudController.$inject = ['$scope', '$log', 'googleCloudFactory', 'googleSession', 'synchroConf'];

    angular.module('cloudSyncApp').controller('GoogleCloudController', GoogleCloudController);

}());