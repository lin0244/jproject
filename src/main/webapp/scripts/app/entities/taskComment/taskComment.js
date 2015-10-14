'use strict';

angular.module('jprojectApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taskComment', {
                parent: 'entity',
                url: '/taskComments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskComment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskComment/taskComments.html',
                        controller: 'TaskCommentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskComment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taskComment.detail', {
                parent: 'entity',
                url: '/taskComment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jprojectApp.taskComment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taskComment/taskComment-detail.html',
                        controller: 'TaskCommentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taskComment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskComment', function($stateParams, TaskComment) {
                        return TaskComment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taskComment.new', {
                parent: 'taskComment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskComment/taskComment-dialog.html',
                        controller: 'TaskCommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    content: null,
                                    postedOn: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taskComment', null, { reload: true });
                    }, function() {
                        $state.go('taskComment');
                    })
                }]
            })
            .state('taskComment.edit', {
                parent: 'taskComment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/taskComment/taskComment-dialog.html',
                        controller: 'TaskCommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaskComment', function(TaskComment) {
                                return TaskComment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taskComment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
