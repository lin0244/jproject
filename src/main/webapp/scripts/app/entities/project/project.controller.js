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

        $scope.delete = function (id) {
            Project.get({id: id}, function(result) {
                $scope.project = result;
                $('#deleteProjectConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Project.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProjectConfirmation').modal('hide');
                    $scope.clear();
                });
        };

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
