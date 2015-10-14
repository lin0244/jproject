'use strict';

angular.module('jprojectApp')
    .controller('TaskLogController', function ($scope, TaskLog) {
        $scope.taskLogs = [];
        $scope.loadAll = function() {
            TaskLog.query(function(result) {
               $scope.taskLogs = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskLog.get({id: id}, function(result) {
                $scope.taskLog = result;
                $('#deleteTaskLogConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskLog.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskLogConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskLog = {
                message: null,
                createdOn: null,
                id: null
            };
        };
    });
