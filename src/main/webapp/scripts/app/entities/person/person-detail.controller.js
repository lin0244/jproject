'use strict';

angular.module('jprojectApp')
    .controller('PersonDetailController', function ($scope, $rootScope, $stateParams, entity, Person, Team, Task, TaskComment) {
        $scope.person = entity;
        $scope.load = function (id) {
            Person.get({id: id}, function(result) {
                $scope.person = result;
            });
        };
        $rootScope.$on('jprojectApp:personUpdate', function(event, result) {
            $scope.person = result;
        });
    });
