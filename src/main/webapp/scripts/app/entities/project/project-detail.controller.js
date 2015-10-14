'use strict';

angular.module('jprojectApp')
    .controller('ProjectDetailController', function ($scope, $rootScope, $stateParams, entity, Project, Iteration) {
        $scope.project = entity;
        $scope.load = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
            });
        };
        $rootScope.$on('jprojectApp:projectUpdate', function(event, result) {
            $scope.project = result;
        });
    });
