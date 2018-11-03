(function () {
    'use strict';

    angular
        .module('app')
        .factory('HomeService', HomeService);

    HomeService.$inject = ['$http'];
    function HomeService($http) {
        var service = {};

        service.GetAll = GetAll;
        service.GetByDate = GetByDate;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;

        return service;

        function GetAll() {
            return $http.get('/api/predictions').then(handleSuccess, handleError('Error getting all info.'));
        }

        function GetByDate(date) {
            return $http.get('/api/predictions?date=' + date).then(handleSuccess, handleError('Error getting details by id.'));
        }

        function Create(user) {
            return $http.post('/api/predictions', userprediction).then(handleSuccess, handleError('Error creating user, use another username.'));
        }

        function Delete(date) {
            return $http.delete('/api/predictions?date=' + date).then(handleSuccess, handleError('Error deleting'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        }
    }

})();