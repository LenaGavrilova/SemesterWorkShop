window.onload = function () {


    const link = document.getElementById("alertDelete");
    const link2 = document.getElementById("alertLogOut");
    const link3 = document.getElementById("alertOrder")
    link.addEventListener('click', function (event) {
        event.preventDefault();
        const confirmResult = confirm('Вы уверены, что хотите удалить данный товар?');
        if (confirmResult) {
            window.location.href = this.href; // Переходим по ссылке
        }
    });

    link2.addEventListener('click', function (event) {
        event.preventDefault();
        const confirmResult = confirm('Вы уверены, что хотите выйти из своего аккаунта?');
        if (confirmResult) {
            window.location.href = "/auth/login"; // Переходим по ссылке
        }

    });
    link3.addEventListener('click', function (event) {
        event.preventDefault();
        const confirmResult = confirm('Вы уверены, что хотите оформить заказ с выбранными товарами?');
        if (confirmResult) {
            window.location.href = this.href; // Переходим по ссылке
        }

    });

    $(document).on('click', '.password-control', function () {
        if ($('#password').attr('type') == 'password') {
            $(this).html('Показать пароль');
            $('#password').attr('type', 'text');
        } else {
            $(this).html('Скрыть пароль');
            $('#password').attr('type', 'password');
        }
        return false;
    });
}






