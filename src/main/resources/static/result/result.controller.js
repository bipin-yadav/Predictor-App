(function () {
    'use strict';

    angular
        .module('app')
        .controller('ResultController', ResultController);

    ResultController.$inject = ['$rootScope'];
    function ResultController($rootScope) {
            var vm = this;

            initController();

            function initController() {
               console.log("Result data here, by day.");
            }

    }

})();