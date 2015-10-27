'use strict';

angular.module('jprojectApp')
    .controller('IterationDetailController', function ($scope, $rootScope, $stateParams, $state, entity, Iteration) {
        $scope.iteration = entity;
        $scope.load = function (projectId, id) {
            Iteration.get({projectId: projectId, id: id}, function(result) {
                $scope.iteration = result;
            });
        };
        $rootScope.$on('jprojectApp:iterationUpdate', function(event, result) {
            $scope.iteration = result;
        });

        $scope.delete = function () {
            Iteration.get({projectId: $stateParams.projectId, id: $stateParams.id}, function(result) {
                $scope.project = result;
                $('#deleteIterationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function () {
            Iteration.delete({projectId: $stateParams.projectId, id: $stateParams.id},
                function () {
                    $('#deleteIterationConfirmation').on('hidden.bs.modal', function () {
                        $('#deleteIterationConfirmation').off('hidden.bs.modal');
                        $state.go('project.detail', {id : $stateParams.projectId});
                    });
                    $('#deleteIterationConfirmation').modal('hide');
                });
        };
    });
