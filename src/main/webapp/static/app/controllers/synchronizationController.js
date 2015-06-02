/**
 * Created by mateusz on 24.05.15.
 */

(function () {

    var SynchronizationController = function ($scope, synchronizationFactory, synchroConf) {
        $scope.login = synchroConf.login;
        $scope.source = synchroConf.source;
        $scope.destinations = synchroConf.destination;
        $scope.type = "FORWARD_PROPAGATION";
        console.log(synchroConf.source);
        console.log(synchroConf.destination);


        $scope.synchronize = function () {
            synchronizationFactory.synchronize({
                "login" : $scope.login,
                "source" : $scope.source,
                "destination" : $scope.destinations,
                "type" : $scope.type
            })
                .success(function (response) {
                    console.log('SynchronizationController - synchronize - success.');
                })
                .error(function (error) {
                    console.log('SynchronizationController - synchronize - error.');
                    $scope.errors = error;
                });
        };

    };



    SynchronizationController.$inject = ['$scope', 'synchronizationFactory', 'synchroConf'];

    angular.module('cloudSyncApp').controller('SynchronizationController', SynchronizationController);

}());