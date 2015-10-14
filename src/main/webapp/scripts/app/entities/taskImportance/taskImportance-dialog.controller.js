'use strict';

angular.module('jprojectApp').controller('TaskImportanceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskImportance', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskImportance, Task) {

        $scope.taskImportance = entity;
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskImportance.get({id : id}, function(result) {
                $scope.taskImportance = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskImportanceUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskImportance.id != null) {
                TaskImportance.update($scope.taskImportance, onSaveFinished);
            } else {
                TaskImportance.save($scope.taskImportance, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
