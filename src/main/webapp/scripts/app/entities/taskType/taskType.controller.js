'use strict';

angular.module('jprojectApp')
    .controller('TaskTypeController', function ($scope, TaskType) {
        $scope.taskTypes = [];
        $scope.loadAll = function() {
            TaskType.query(function(result) {
               $scope.taskTypes = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskType.get({id: id}, function(result) {
                $scope.taskType = result;
                $('#deleteTaskTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskType = {
                title: null,
                description: null,
                id: null
            };
        };
    });
