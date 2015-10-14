'use strict';

angular.module('jprojectApp').controller('TaskTypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskType', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskType, Task) {

        $scope.taskType = entity;
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskType.get({id : id}, function(result) {
                $scope.taskType = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskTypeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskType.id != null) {
                TaskType.update($scope.taskType, onSaveFinished);
            } else {
                TaskType.save($scope.taskType, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
