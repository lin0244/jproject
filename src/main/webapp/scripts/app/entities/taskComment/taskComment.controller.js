'use strict';

angular.module('jprojectApp')
    .controller('TaskCommentController', function ($scope, TaskComment) {
        $scope.taskComments = [];
        $scope.loadAll = function() {
            TaskComment.query(function(result) {
               $scope.taskComments = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TaskComment.get({id: id}, function(result) {
                $scope.taskComment = result;
                $('#deleteTaskCommentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TaskComment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskCommentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.taskComment = {
                content: null,
                postedOn: null,
                id: null
            };
        };
    });
