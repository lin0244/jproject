'use strict';

angular.module('jprojectApp').controller('IterationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Iteration', 'Project',
        function($scope, $stateParams, $modalInstance, entity, Iteration, Project) {
        $scope.iteration = entity;
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
                var uParams = {projectId: $stateParams.projectId};
                Iteration.update(uParams, $scope.iteration, onSaveFinished);
            } else {
                var uParams = {projectId: $stateParams.id};
                Iteration.save(uParams, $scope.iteration, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
