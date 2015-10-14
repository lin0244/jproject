'use strict';

angular.module('jprojectApp')
    .controller('TaskCommentDetailController', function ($scope, $rootScope, $stateParams, entity, TaskComment, Person, Task) {
        $scope.taskComment = entity;
        $scope.load = function (id) {
            TaskComment.get({id: id}, function(result) {
                $scope.taskComment = result;
            });
        };
        $rootScope.$on('jprojectApp:taskCommentUpdate', function(event, result) {
            $scope.taskComment = result;
        });
    });
