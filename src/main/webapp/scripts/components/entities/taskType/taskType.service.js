'use strict';

angular.module('jprojectApp')
    .factory('TaskType', function ($resource, DateUtils) {
        return $resource('api/taskTypes/:id', {}, {
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
