/**
 * Created by mateusz on 26.04.15.
 */
(function () {

    var GoogleCloudController = function ($scope, $log, googleCloudFactory) {
        $scope.files = {};

        function init() {
            googleCloudFactory.getAllDirectoryFiles({
                "id" : 0,
                "path" : "root",
                "type" : "DIRECTORY",
                "fileName" : "",
                "extension" : "",
                "size" : 0,
                "lastModificationDate" : null
            })
                .success(function (response) {
                    $scope.files = response.result;
                })
                .error(function (data, status, headers, config) {
                    // handle error
                    $log.log(data.error + ' ' + status);
                });
        };

        init();
    };

    GoogleCloudController.$inject = ['$scope', '$log', 'googleCloudFactory'];

    angular.module('cloudSyncApp').controller('GoogleCloudController', GoogleCloudController);

}());