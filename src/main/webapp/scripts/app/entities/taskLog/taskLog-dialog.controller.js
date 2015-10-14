'use strict';

angular.module('jprojectApp').controller('TaskLogDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TaskLog', 'Task',
        function($scope, $stateParams, $modalInstance, entity, TaskLog, Task) {

        $scope.taskLog = entity;
        $scope.tasks = Task.query();
        $scope.load = function(id) {
            TaskLog.get({id : id}, function(result) {
                $scope.taskLog = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskLogUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.taskLog.id != null) {
                TaskLog.update($scope.taskLog, onSaveFinished);
            } else {
                TaskLog.save($scope.taskLog, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
