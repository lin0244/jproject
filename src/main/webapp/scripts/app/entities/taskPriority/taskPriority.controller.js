'use strict';

angular.module('jprojectApp')
    .controller('TaskPriorityController', function ($scope, TaskPriority) {
        $scope.taskPrioritys = [];
        $scope.loadAll = function() {
            TaskPriority.query(function(result) {
               $scope.taskPrioritys = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskPriority.get({id: id}, function(result) {
                $scope.taskPriority = result;
                $('#deleteTaskPriorityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskPriority.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskPriorityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskPriority = {
                code: null,
                title: null,
                description: null,
                id: null
            };
        };
    });
