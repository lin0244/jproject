'use strict';

angular.module('jprojectApp')
    .controller('TaskTypeDetailController', function ($scope, $rootScope, $stateParams, entity, TaskType, Task) {
        $scope.taskType = entity;
        $scope.load = function (id) {
            TaskType.get({id: id}, function(result) {
                $scope.taskType = result;
            });
        };
        $rootScope.$on('jprojectApp:taskTypeUpdate', function(event, result) {
            $scope.taskType = result;
        });
    });
