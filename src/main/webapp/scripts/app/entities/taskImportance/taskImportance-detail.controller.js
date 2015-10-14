'use strict';

angular.module('jprojectApp')
    .controller('TaskImportanceDetailController', function ($scope, $rootScope, $stateParams, entity, TaskImportance, Task) {
        $scope.taskImportance = entity;
        $scope.load = function (id) {
            TaskImportance.get({id: id}, function(result) {
                $scope.taskImportance = result;
            });
        };
        $rootScope.$on('jprojectApp:taskImportanceUpdate', function(event, result) {
            $scope.taskImportance = result;
        });
    });
