'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('iteration', {
                parent: 'entity',
                url: '/iterations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.iteration.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/iteration/iterations.html',
                        controller: 'IterationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('iteration');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('iteration.detail', {
                parent: 'entity',
                url: '/iteration/{id}',
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
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Iteration', function($stateParams, Iteration) {
                        return Iteration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('iteration.new', {
                parent: 'iteration',
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
                        $state.go('iteration', null, { reload: true });
                    }, function() {
                        $state.go('iteration');
                    })
                }]
            })
            .state('iteration.edit', {
                parent: 'iteration',
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
                        $state.go('iteration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
