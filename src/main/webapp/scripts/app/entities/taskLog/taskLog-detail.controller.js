'use strict';

angular.module('jprojectApp')
    .controller('TaskLogDetailController', function ($scope, $rootScope, $stateParams, entity, TaskLog, Task) {
        $scope.taskLog = entity;
        $scope.load = function (id) {
            TaskLog.get({id: id}, function(result) {
                $scope.taskLog = result;
            });
        };
        $rootScope.$on('jprojectApp:taskLogUpdate', function(event, result) {
            $scope.taskLog = result;
        });
    });
