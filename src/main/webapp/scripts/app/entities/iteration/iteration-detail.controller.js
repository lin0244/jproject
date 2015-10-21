'use strict';

angular.module('jprojectApp')
    .controller('IterationDetailController', function ($scope, $rootScope, $stateParams, entity, Iteration, Project, Team, Task) {
        $scope.iteration = entity;
        $scope.load = function (projectId, id) {
            Iteration.get({projectId: projectId, id: id}, function(result) {
                $scope.iteration = result;
            });
        };
        $rootScope.$on('jprojectApp:iterationUpdate', function(event, result) {
            $scope.iteration = result;
        });
    });
