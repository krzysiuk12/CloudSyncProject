/**
 * Created by mateusz on 14.06.15.
 */

(function() {
    var sessionService = function () {

        var google = null;
        var dropbox = null;
        var oneDrive = null;

        return {
            getGoogle : function () {
                console.log('getGoogle()');
                return google;
            },
            setGoogle : function (session) {
                console.log('setGoogle');
                console.log(session);
                google = session;
                return google;
            },
            getDropbox : function () {
                console.log('getDropbox()');
                return dropbox;
            },
            setDropbox : function (session) {
                console.log('setDropbox()');
                console.log(session);
                dropbox = session;
                return dropbox;
            },
            getOneDrive : function () {
                console.log('getOneDrive');
                return oneDrive;
            },
            setOneDrive : function (session) {
                console.log('setOneDrive()');
                console.log(session);
                oneDrive = session;
                return oneDrive;
            }
        };
    };

    angular.module('cloudSyncApp').factory('sessionService', sessionService);
}());