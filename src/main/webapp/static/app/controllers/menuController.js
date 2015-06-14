/**
 * Created by mateusz on 14.06.15.
 */
(function () {

    var MenuController = function ($scope, sessionService) {
        $scope.sessions = sessionService;
    };

    angular.module('cloudSyncApp').controller('MenuController', MenuController);

}());