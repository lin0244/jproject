'use strict';

angular.module('jprojectApp').controller('TeamDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Team', 'Iteration', 'Person',
        function($scope, $stateParams, $modalInstance, entity, Team, Iteration, Person) {

        $scope.team = entity;
        $scope.iterations = Iteration.query();
        $scope.persons = Person.query();
        $scope.load = function(id) {
            Team.get({id : id}, function(result) {
                $scope.team = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jprojectApp:teamUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.team.id != null) {
                Team.update($scope.team, onSaveFinished);
            } else {
                Team.save($scope.team, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
