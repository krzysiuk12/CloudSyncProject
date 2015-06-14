/**
 * Created by mateusz on 11.06.15.
 */
/**
 * Created by mateusz on 24.05.15.
 */

(function() {
    var synchronizationService = function ($http) {

        var config = {
            "login" : 'Janek',
            "source" : {},
            "destination" : [],
            "type" : "FORWARD_PROPAGATION"
        };

        var source = {
            "sessionId" : '',
                "cloudPath" : {
                    "path" : 'init path',
                    "type" : "init type"
                }
        };
        var destination = [];
        var rules = [];

        return {
            synchronize: function (request) {
                console.log('synchronize()');
                console.log(request);
                $http.defaults.headers.common['Content-Type'] = 'application/json';
                return $http.post('/synchronization', request);
            },
            getSource : function () {
                console.log('getSource');
                return source;
            },
            setSource : function (src) {
                console.log('setSource');
                console.log(source);
                source = src;
                return source;
            },
            getRules : function () {
                console.log('getRules()');
                return rules;
            },
            addRule : function (rule) {
                console.log('addRule()');
                console.log(rule);
                return rules.push(rule);
            }
        };
    };

    angular.module('cloudSyncApp').factory('synchronizationService', synchronizationService);
}());