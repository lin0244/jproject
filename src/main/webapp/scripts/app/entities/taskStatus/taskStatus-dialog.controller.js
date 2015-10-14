'use strict';

angular.module('jprojectApp').controller('TaskStatusDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskStatus', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskStatus, Task) {

        $scope.taskStatus = entity;
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskStatus.get({id : id}, function(result) {
                $scope.taskStatus = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskStatusUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskStatus.id != null) {
                TaskStatus.update($scope.taskStatus, onSaveFinished);
            } else {
                TaskStatus.save($scope.taskStatus, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
