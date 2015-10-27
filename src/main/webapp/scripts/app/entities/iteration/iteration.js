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
                    },
                    'tab-content@iteration.detail': {
                        templateUrl: 'scripts/app/entities/iteration/iteration-dashboard.html',
                        controller: 'IterationDashboardController'
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
                url: '/iterations/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'Project', function($stateParams, $state, $modal, Project) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/iteration/iteration-dialog.html',
                        controller: 'IterationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                var now = moment().utc().startOf('day').toDate();
                                return {
                                    title: null,
                                    startDate: now,
                                    endDate: moment(now).add(1, 'months').toDate(),
                                    id: null,
                                    project: Project.get({id:$stateParams.id})
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('project.detail', {id : $stateParams.id}, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('iteration.edit', {
                parent: 'iteration.detail',
                url: '/edit',
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
                                return Iteration.get({projectId: $stateParams.projectId, id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('iteration.detail', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('iteration.tasks', {
                parent: 'iteration.detail',
                url: '/tasks',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'tab-content@iteration.detail': {
                        templateUrl: 'scripts/app/entities/iteration/iteration-tasks.html',
                        controller: 'IterationTaskController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('task');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Iteration', function($stateParams, Iteration) {
                        return Iteration.get($stateParams);
                    }]
                }
            })
            .state('iteration.teams', {
                parent: 'iteration.detail',
                url: '/teams',
                data: {
                    authorities: ['ROLE_USER'],
                },
                views: {
                    'tab-content@iteration.detail': {
                        templateUrl: 'scripts/app/entities/iteration/iteration-teams.html',
                        controller: 'IterationTeamController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('team');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Iteration', function($stateParams, Iteration) {
                        return Iteration.get($stateParams);
                    }]
                }
            });
    });
