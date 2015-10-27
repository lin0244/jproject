'use strict';

angular.module('jprojectApp').controller('IterationDashboardController',
    ['$scope', '$rootScope', '$stateParams', 'Iteration', 'Project',
        function($scope, $rootScope, $stateParams, Iteration, Project) {
            $scope.iteration = $rootScope.iteration;
        }
    ]);
