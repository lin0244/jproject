'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskImportance', {
                parent: 'entity',
                url: '/taskImportances',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskImportance.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskImportance/taskImportances.html',
                        controller: 'TaskImportanceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskImportance');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskImportance.detail', {
                parent: 'entity',
                url: '/taskImportance/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskImportance.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskImportance/taskImportance-detail.html',
                        controller: 'TaskImportanceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskImportance');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskImportance', function($stateParams, TaskImportance) {
                        return TaskImportance.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskImportance.new', {
                parent: 'taskImportance',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskImportance/taskImportance-dialog.html',
                        controller: 'TaskImportanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    title: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taskImportance', null, { reload: true });
                    }, function() {
                        $state.go('taskImportance');
                    })
                }]
            })
            .state('taskImportance.edit', {
                parent: 'taskImportance',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskImportance/taskImportance-dialog.html',
                        controller: 'TaskImportanceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskImportance', function(TaskImportance) {
                                return TaskImportance.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskImportance', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
