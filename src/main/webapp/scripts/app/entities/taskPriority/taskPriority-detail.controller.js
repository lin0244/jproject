'use strict';

angular.module('jprojectApp')
    .controller('TaskPriorityDetailController', function ($scope, $rootScope, $stateParams, entity, TaskPriority, Task) {
        $scope.taskPriority = entity;
        $scope.load = function (id) {
            TaskPriority.get({id: id}, function(result) {
                $scope.taskPriority = result;
            });
        };
        $rootScope.$on('jprojectApp:taskPriorityUpdate', function(event, result) {
            $scope.taskPriority = result;
        });
    });
