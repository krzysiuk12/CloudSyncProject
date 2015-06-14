/**
 * Created by mateusz on 14.06.15.
 */
(function () {

    var SynchronizationInfoController = function ($scope, synchronizationService) {
        $scope.rules = synchronizationService.getRules();
    };

    angular.module('cloudSyncApp').controller('SynchronizationInfoController', SynchronizationInfoController);

}());