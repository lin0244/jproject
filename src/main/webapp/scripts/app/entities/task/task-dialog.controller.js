'use strict';

angular.module('jprojectApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Task', 'Iteration', 'Person', 'TaskType', 'TaskStatus', 'TaskPriority', 'TaskImportance', 'TaskComment', 'TaskLog',
        function($scope, $stateParams, $modalInstance, entity, Task, Iteration, Person, TaskType, TaskStatus, TaskPriority, TaskImportance, TaskComment, TaskLog) {

        $scope.task = entity;
        $scope.iterations = Iteration.query();
        $scope.persons = Person.query();
        $scope.tasktypes = TaskType.query();
        $scope.taskstatuss = TaskStatus.query();
        $scope.taskprioritys = TaskPriority.query();
        $scope.taskimportances = TaskImportance.query();
        $scope.tasks = Task.query();
        $scope.taskcomments = TaskComment.query();
        $scope.tasklogs = TaskLog.query();
        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:taskUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.task.id != null) {
                Task.update($scope.task, onSaveFinished);
            } else {
                Task.save($scope.task, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
