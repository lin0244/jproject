'use strict';

angular.module('jprojectApp')
    .controller('ProjectController', function ($scope, Project) {
        $scope.projects = [];
        $scope.loadAll = function() {
            Project.query(function(result) {
               $scope.projects = result;
            });
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.project = {
                code: null,
                name: null,
                id: null
            };
        };
    });
