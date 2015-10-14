'use strict';

angular.module('jprojectApp')
    .controller('TaskImportanceController', function ($scope, TaskImportance) {
        $scope.taskImportances = [];
        $scope.loadAll = function() {
            TaskImportance.query(function(result) {
               $scope.taskImportances = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskImportance.get({id: id}, function(result) {
                $scope.taskImportance = result;
                $('#deleteTaskImportanceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskImportance.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskImportanceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskImportance = {
                code: null,
                title: null,
                description: null,
                id: null
            };
        };
    });
