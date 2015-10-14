'use strict';

angular.module('jprojectApp')
    .factory('TaskPriority', function ($resource, DateUtils) {
        return $resource('api/taskPrioritys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
