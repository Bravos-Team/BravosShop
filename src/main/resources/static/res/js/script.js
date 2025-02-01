let activeDropdown = null;

function toggleUserMenu() {
    const dropdown = document.getElementById('userDropdown');
    dropdown.classList.toggle('active');

    if (dropdown.classList.contains('active')) {
        setTimeout(() => {
            document.addEventListener('click', closeUserMenu);
        }, 0);
    } else {
        document.removeEventListener('click', closeUserMenu);
    }
}

function closeUserMenu(event) {
    const userMenu = document.querySelector('.user-menu');
    if (!userMenu.contains(event.target)) {
        document.getElementById('userDropdown').classList.remove('active');
        document.removeEventListener('click', closeUserMenu);
    }
}

function toggleSubmenu(element) {
    const submenu = element.nextElementSibling;
    submenu.classList.toggle('active');
    activeDropdown = submenu.classList.contains('active') ? element : null;
}

