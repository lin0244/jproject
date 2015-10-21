'use strict';

angular.module('jprojectApp').controller('IterationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Iteration', 'Project', 'Team', 'Task',
        function($scope, $stateParams, $modalInstance, entity, Iteration, Project, Team, Task) {

        $scope.iteration = entity;
        $scope.projects = Project.query();
        $scope.teams = Team.query();
        $scope.tasks = Task.query();
        $scope.load = function(projectId, id) {
            Iteration.get({projectId: projectId, id : id}, function(result) {
                $scope.iteration = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:iterationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.iteration.id != null) {
                Iteration.update($scope.iteration, onSaveFinished);
            } else {
                Iteration.save($scope.iteration, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
