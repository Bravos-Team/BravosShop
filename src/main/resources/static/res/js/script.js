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

// Sample function to handle adding a new category
document.querySelector('#addCategoryModal .btn-primary').addEventListener('click', function() {
    const categoryName = document.querySelector('#categoryName').value;
    const parentCategory = document.querySelector('#parentCategory').value;
    const categoryDescription = document.querySelector('#categoryDescription').value;
    // Perform add category logic here
    console.log('Category Name:', categoryName);
    console.log('Parent Category:', parentCategory);
    console.log('Category Description:', categoryDescription);
});

// Sample function to handle editing a category
document.querySelector('#editCategoryModal .btn-primary').addEventListener('click', function() {
    const editCategoryName = document.querySelector('#editCategoryName').value;
    const editParentCategory = document.querySelector('#editParentCategory').value;
    const editCategoryDescription = document.querySelector('#editCategoryDescription').value;
    // Perform edit category logic here
    console.log('Edit Category Name:', editCategoryName);
    console.log('Edit Parent Category:', editParentCategory);
    console.log('Edit Category Description:', editCategoryDescription);
});

