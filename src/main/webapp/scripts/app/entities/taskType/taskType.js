'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskType', {
                parent: 'entity',
                url: '/taskTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskType/taskTypes.html',
                        controller: 'TaskTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskType.detail', {
                parent: 'entity',
                url: '/taskType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskType/taskType-detail.html',
                        controller: 'TaskTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskType', function($stateParams, TaskType) {
                        return TaskType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskType.new', {
                parent: 'taskType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskType/taskType-dialog.html',
                        controller: 'TaskTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taskType', null, { reload: true });
                    }, function() {
                        $state.go('taskType');
                    })
                }]
            })
            .state('taskType.edit', {
                parent: 'taskType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskType/taskType-dialog.html',
                        controller: 'TaskTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskType', function(TaskType) {
                                return TaskType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
