'use strict';

angular.module('jprojectApp')
    .controller('TaskDetailController', function ($scope, $rootScope, $stateParams, entity, Task, Iteration, Person, TaskType, TaskStatus, TaskPriority, TaskImportance, TaskComment, TaskLog) {
        $scope.task = entity;
        $scope.load = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
            });
        };
        $rootScope.$on('jprojectApp:taskUpdate', function(event, result) {
            $scope.task = result;
        });
    });
