'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskPriority', {
                parent: 'entity',
                url: '/taskPrioritys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskPriority.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskPriority/taskPrioritys.html',
                        controller: 'TaskPriorityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskPriority');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskPriority.detail', {
                parent: 'entity',
                url: '/taskPriority/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskPriority.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskPriority/taskPriority-detail.html',
                        controller: 'TaskPriorityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskPriority');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskPriority', function($stateParams, TaskPriority) {
                        return TaskPriority.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskPriority.new', {
                parent: 'taskPriority',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskPriority/taskPriority-dialog.html',
                        controller: 'TaskPriorityDialogController',
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
                        $state.go('taskPriority', null, { reload: true });
                    }, function() {
                        $state.go('taskPriority');
                    })
                }]
            })
            .state('taskPriority.edit', {
                parent: 'taskPriority',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskPriority/taskPriority-dialog.html',
                        controller: 'TaskPriorityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskPriority', function(TaskPriority) {
                                return TaskPriority.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskPriority', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
