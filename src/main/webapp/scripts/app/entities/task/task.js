'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('task', {
                parent: 'entity',
                url: '/tasks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.task.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/task/tasks.html',
                        controller: 'TaskController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('task');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('task.detail', {
                parent: 'entity',
                url: '/task/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.task.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/task/task-detail.html',
                        controller: 'TaskDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('task');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Task', function($stateParams, Task) {
                        return Task.get({id : $stateParams.id});
                    }]
                }
            })
            .state('task.new', {
                parent: 'iteration.detail',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('^', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('task.edit', {
                parent: 'iteration.detail',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Task', function(Task) {
                                return Task.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('^', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
