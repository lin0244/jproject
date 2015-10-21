'use strict';

angular.module('jprojectApp')
    .controller('ProjectDetailController', function ($scope, $state, $rootScope, $stateParams, entity, iterations, Project) {
        $scope.project = entity;
        $scope.iterations = iterations;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        $rootScope.$on('jprojectApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
        $scope.delete = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
                $('#deleteProjectConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Project.delete({id: id},
                function () {
                    $('#deleteProjectConfirmation').on('hidden.bs.modal', function () {
                        $('#deleteProjectConfirmation').off('hidden.bs.modal');
                        $state.go('project');
                    });
                    $('#deleteProjectConfirmation').modal('hide');
                });
        };
    });
