/**
 * Created by Krzysztof Kicinger on 2015-04-27.
 */
(function () {

    var DropboxCloudManagementController = function ($scope, $log, dropboxCloudManagementFactory) {
        $scope.files = {};

        function init() {
            dropboxCloudManagementFactory.listFiles({
                "id" : 0,
                "path" : "/",
                "type" : "DIRECTORY",
                "fileName" : "",
                "extension" : "",
                "size" : 0,
                "lastModificationDate" : null
            })
            .success(function (response) {
                    console.log('DropboxCloudManagementController - init - response: ' + response);
                $scope.files = response.result;
            })
            .error(function (data, status, headers, config) {
                    console.log('DropboxCloudManagementController - init - error: ' + data.error);
                $log.log(data.error + ' ' + status);
            });
        };

        init();
    };

    DropboxCloudManagementController.$inject = ['$scope', '$log', 'dropboxCloudManagementFactory'];

    angular.module('cloudSyncApp').controller('DropboxCloudManagementController', DropboxCloudManagementController);

}());