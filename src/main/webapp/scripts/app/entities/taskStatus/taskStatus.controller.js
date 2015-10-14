'use strict';

angular.module('jprojectApp')
    .controller('TaskStatusController', function ($scope, TaskStatus) {
        $scope.taskStatuss = [];
        $scope.loadAll = function() {
            TaskStatus.query(function(result) {
               $scope.taskStatuss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskStatus.get({id: id}, function(result) {
                $scope.taskStatus = result;
                $('#deleteTaskStatusConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskStatus.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskStatusConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskStatus = {
                title: null,
                status: null,
                description: null,
                id: null
            };
        };
    });
