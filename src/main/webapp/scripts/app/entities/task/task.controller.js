'use strict';

angular.module('jprojectApp')
    .controller('TaskController', function ($scope, Task, ParseLinks) {
        $scope.tasks = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Task.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.tasks = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
                $('#deleteTaskConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Task.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.task = {
                title: null,
                id: null
            };
        };
    });
