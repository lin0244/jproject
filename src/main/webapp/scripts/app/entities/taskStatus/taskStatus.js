'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskStatus', {
                parent: 'entity',
                url: '/taskStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskStatus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskStatus/taskStatuss.html',
                        controller: 'TaskStatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskStatus');
                        $translatePartialLoader.addPart('taskStatusState');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskStatus.detail', {
                parent: 'entity',
                url: '/taskStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskStatus.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskStatus/taskStatus-detail.html',
                        controller: 'TaskStatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskStatus');
                        $translatePartialLoader.addPart('taskStatusState');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskStatus', function($stateParams, TaskStatus) {
                        return TaskStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskStatus.new', {
                parent: 'taskStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskStatus/taskStatus-dialog.html',
                        controller: 'TaskStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    status: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taskStatus', null, { reload: true });
                    }, function() {
                        $state.go('taskStatus');
                    })
                }]
            })
            .state('taskStatus.edit', {
                parent: 'taskStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskStatus/taskStatus-dialog.html',
                        controller: 'TaskStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskStatus', function(TaskStatus) {
                                return TaskStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
