'use strict';

angular.module('jprojectApp')
    .controller('TeamController', function ($scope, Team) {
        $scope.teams = [];
        $scope.loadAll = function() {
            Team.query(function(result) {
               $scope.teams = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
                $('#deleteTeamConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Team.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTeamConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.team = {
                name: null,
                id: null
            };
        };
    });
