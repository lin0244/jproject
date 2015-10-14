'use strict';

angular.module('jprojectApp').controller('PersonDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Person', 'Team', 'Task', 'TaskComment',
        function($scope, $stateParams, $modalInstance, entity, Person, Team, Task, TaskComment) {

        $scope.person = entity;
        $scope.teams = Team.query();
        $scope.tasks = Task.query();
        $scope.taskcomments = TaskComment.query();
        $scope.load = function(id) {
            Person.get({id : id}, function(result) {
                $scope.person = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:personUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.person.id != null) {
                Person.update($scope.person, onSaveFinished);
            } else {
                Person.save($scope.person, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
