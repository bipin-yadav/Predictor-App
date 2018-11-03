(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.GetAll = GetAll;
        service.GetById = GetById;
        service.GetByUsername = GetByUsername;
        service.Create = Create;
        service.Update = Update;
        service.Delete = Delete;
        service.UpdateValue = UpdateValue;
        service.GetAllFixedPredictions = GetAllFixedPredictions;
        service.GetFixedPredictionsByDate = GetFixedPredictionsByDate;
        service.CreateFixedPredictions = CreateFixedPredictions;
        service.DeleteFixedPredictions = DeleteFixedPredictions;
        service.CreateUserPredictions = CreateUserPredictions;
        service.GetUserPredictionsByUserName = GetUserPredictionsByUserName;

        return service;

        function GetAll() {
            return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function GetById(id) {
            return $http.get('/api/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function GetByUsername(username) {
            return $http.get('/api/users?username=' + username).then(handleSuccess, handleError('Error getting user by username'));
        }

        function Create(user) {
            return $http.post('/api/users', user).then(handleSuccess, handleError('Error creating user, use another username.'));
        }

        function Update(user) {
            return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function Delete(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        function UpdateValue(user) {
        console.log(user);
            return $http.post('/api/users/values', user).then(handleSuccess, handleError('Error updating value'));
        }

        function GetAllFixedPredictions() {
            return $http.get('/api/fixed/predictions').then(handleSuccess, handleError('Error getting all info.'));
        }

        function GetFixedPredictionsByDate(date) {
            return $http.get('/api/fixed/predictions?date=' + date).then(handleSuccess, handleError('Error getting details by id.'));
        }

        function CreateFixedPredictions(userprediction) {
            return $http.post('/api/fixed/predictions', userprediction).then(handleSuccess,
            handleError('Error updating value, u are exceeding the update limit.'));
        }

        function DeleteFixedPredictions(date) {
            return $http.delete('/api/fixed/predictions?date=' + date).then(handleSuccess, handleError('Error deleting'));
        }

                function CreateUserPredictions(userprediction) {
                    return $http.post('/api/user/predictions', userprediction).then(handleSuccess, handleError('Error updating value, pls try later.'));
                }

        function GetUserPredictionsByUserName(userName) {
            return $http.get('/api/user/predictions?username=' + userName).then(handleSuccess, handleError('Error getting details by id.'));
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