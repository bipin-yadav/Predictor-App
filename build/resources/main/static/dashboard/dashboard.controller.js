(function () {
    'use strict';

    angular
        .module('app')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['UserService', '$rootScope'];
    function DashboardController(UserService, $rootScope) {
        console.log("load data here, for the day.");
    }

})();