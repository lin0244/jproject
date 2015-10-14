'use strict';

angular.module('jprojectApp')
    .factory('TaskLog', function ($resource, DateUtils) {
        return $resource('api/taskLogs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdOn = DateUtils.convertDateTimeFromServer(data.createdOn);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
