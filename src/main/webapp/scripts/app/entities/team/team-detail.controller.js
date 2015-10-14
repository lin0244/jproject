'use strict';

angular.module('jprojectApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $stateParams, entity, Team, Iteration, Person) {
        $scope.team = entity;
        $scope.load = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
            });
        };
        $rootScope.$on('jprojectApp:teamUpdate', function(event, result) {
            $scope.team = result;
        });
    });
