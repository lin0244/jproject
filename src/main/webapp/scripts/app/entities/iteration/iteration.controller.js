'use strict';

angular.module('jprojectApp')
    .controller('IterationController', function ($scope, Iteration) {
        $scope.iterations = [];
        $scope.loadAll = function() {
            Iteration.query(function(result) {
               $scope.iterations = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Iteration.get({id: id}, function(result) {
                $scope.iteration = result;
                $('#deleteIterationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Iteration.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteIterationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.iteration = {
                title: null,
                startDate: null,
                endDate: null,
                id: null
            };
        };
    });
