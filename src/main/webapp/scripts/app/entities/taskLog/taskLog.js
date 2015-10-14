'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskLog', {
                parent: 'entity',
                url: '/taskLogs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskLog.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskLog/taskLogs.html',
                        controller: 'TaskLogController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskLog');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskLog.detail', {
                parent: 'entity',
                url: '/taskLog/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskLog.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskLog/taskLog-detail.html',
                        controller: 'TaskLogDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskLog');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskLog', function($stateParams, TaskLog) {
                        return TaskLog.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskLog.new', {
                parent: 'taskLog',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskLog/taskLog-dialog.html',
                        controller: 'TaskLogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    message: null,
                                    createdOn: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taskLog', null, { reload: true });
                    }, function() {
                        $state.go('taskLog');
                    })
                }]
            })
            .state('taskLog.edit', {
                parent: 'taskLog',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskLog/taskLog-dialog.html',
                        controller: 'TaskLogDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskLog', function(TaskLog) {
                                return TaskLog.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskLog', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
