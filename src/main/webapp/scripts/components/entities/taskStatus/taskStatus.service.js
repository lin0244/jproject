'use strict';

angular.module('jprojectApp')
    .factory('TaskStatus', function ($resource, DateUtils) {
        return $resource('api/taskStatuss/:id', {}, {
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
