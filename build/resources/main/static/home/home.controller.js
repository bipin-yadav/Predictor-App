(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', '$location', '$rootScope', 'FlashService'];
    function HomeController(UserService, $location, $rootScope, FlashService) {
        var vm = this;

        vm.user = null;
        vm.updateValue = updateValue;


        initController();

        function initController() {
            loadCurrentUser();
        }

        function loadCurrentUser() {
            UserService.GetByUsername($rootScope.globals.currentUser.username)
                .then(function (user) {
                    //console.log(user);
                    vm.user = user;
                });
        }

        function updateValue() {
            vm.dataLoading = true;
            //console.log(vm.user);
            UserService.UpdateValue(vm.user)
            .then(function (response) {
            console.log(response);
                    if (response.success) {
                        FlashService.Success('Update successful', true);
                        $location.path('/');
                    } else {
                        FlashService.Error(response.message);
                        vm.dataLoading = false;
                    }
                });
        }

    }

})();