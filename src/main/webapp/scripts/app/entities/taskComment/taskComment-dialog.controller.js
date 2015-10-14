'use strict';

angular.module('jprojectApp').controller('TaskCommentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskComment', 'Person', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskComment, Person, Task) {

        $scope.taskComment = entity;
        $scope.persons = Person.query();
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskComment.get({id : id}, function(result) {
                $scope.taskComment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskCommentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskComment.id != null) {
                TaskComment.update($scope.taskComment, onSaveFinished);
            } else {
                TaskComment.save($scope.taskComment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
