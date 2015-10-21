'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('iteration', {})
            .state('iteration.detail', {
                parent: 'entity',
                url: '/project/{projectId}/iteration/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.iteration.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/iteration/iteration-detail.html',
                        controller: 'IterationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('iteration');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Iteration', function($stateParams, Iteration) {
                        return Iteration.get($stateParams);
                    }]
                }
            })
            .state('iteration.new', {
                parent: 'project.detail',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/iteration/iteration-dialog.html',
                        controller: 'IterationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    startDate: null,
                                    endDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', {id:$stateParams.projectId}, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('iteration.edit', {
                parent: 'iteration.detail',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/iteration/iteration-dialog.html',
                        controller: 'IterationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Iteration', function(Iteration) {
                                return Iteration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('iteration.detail', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
