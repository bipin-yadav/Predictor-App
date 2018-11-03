(function () {
    'use strict';

    angular
        .module('app')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$rootScope'];
    function DashboardController($rootScope) {
            var vm = this;

            initController();

            function initController() {
               console.log("load data here, for the day.");
            }

    }

})();