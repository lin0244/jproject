'use strict';

angular.module('jprojectApp').controller('TaskPriorityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskPriority', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskPriority, Task) {

        $scope.taskPriority = entity;
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskPriority.get({id : id}, function(result) {
                $scope.taskPriority = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskPriorityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskPriority.id != null) {
                TaskPriority.update($scope.taskPriority, onSaveFinished);
            } else {
                TaskPriority.save($scope.taskPriority, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
