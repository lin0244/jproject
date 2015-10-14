'use strict';

angular.module('jprojectApp')
    .controller('TaskStatusDetailController', function ($scope, $rootScope, $stateParams, entity, TaskStatus, Task) {
        $scope.taskStatus = entity;
        $scope.load = function (id) {
            TaskStatus.get({id: id}, function(result) {
                $scope.taskStatus = result;
            });
        };
        $rootScope.$on('jprojectApp:taskStatusUpdate', function(event, result) {
            $scope.taskStatus = result;
        });
    });
