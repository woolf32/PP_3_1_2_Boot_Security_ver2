document.addEventListener('DOMContentLoaded', function () {
    loadUsers();
    loadRolesForNewUser();
    showInfoEmployeeInNavbar();
    loadInfoCurrentEmployee();

});

//loadAllEmployees
function loadUsers() {
    fetch('http://localhost:8080/api/admin', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const table = document.getElementById('users-table-body');
            table.innerHTML = '';
            data.forEach(employee => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${employee.id}</td>
                    <td>${employee.name}</td>
                    <td>${employee.surname}</td>
                    <td>${employee.password}</td>
                    <td>${employee.salary}</td>
                    <td>${employee.department}</td>
                    <td>${employee.roles.map(role => role.name).join(' ')}</td>
                    <td>
                        <button class="btn btn-info btn-sm text-white" onclick="openEditModal(${employee.id})">Edit</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-sm" onclick="openDeleteModal(${employee.id})">Delete</button>
                    </td>
                `;
                table.appendChild(tr);
            });
        });
}

//edit employees
function openEditModal(id) {
    fetch(`http://localhost:8080/api/admin/${id}`)
        .then(response => response.json())
        .then(employee => {
            document.getElementById('editUserId').value = employee.id;
            document.getElementById('editName').value = employee.name;
            document.getElementById('editSurname').value = employee.surname;
            document.getElementById('editPassword').value = employee.password;
            document.getElementById('editSalary').value = employee.salary;
            document.getElementById('editDepartment').value = employee.department;


            const rolesContainer = document.getElementById('edit-roles-checkboxes');
            rolesContainer.innerHTML = '';

            fetch('http://localhost:8080/api/employees/roles')
                .then(response => response.json())
                .then(roles => {
                    roles.forEach(role => {
                        const isChecked = employee.roles.some(empRole => empRole.id === role.id);

                        const checkbox = `
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="role-${role.id}" value="${role.id}" data-name="${role.name}" ${isChecked ? 'checked' : ''}>
                                <label class="form-check-label" for="role-${role.id}">${role.name}</label>
                            </div>
                        `;
                        rolesContainer.innerHTML += checkbox;
                    });
                });

            new bootstrap.Modal(document.getElementById('editUserModal')).show();
        });
}

document.getElementById('editUserForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const id = document.getElementById('editUserId').value;

    const employee = {
        name: document.getElementById('editName').value,
        surname: document.getElementById('editSurname').value,
        password: document.getElementById('editPassword').value,
        salary: document.getElementById('editSalary').value,
        department: document.getElementById('editDepartment').value,
        roles: [...document.querySelectorAll('#editUserModal input[type=checkbox]:checked')].map(cb => ({
            id: cb.value,
            name: cb.dataset.name
        }))
    };

    fetch(`http://localhost:8080/api/admin/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(employee)
    })
        .then(response => {
            // Логируем ответ
            console.log(response);

            // Проверяем, есть ли контент
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }

            // Проверяем, что ответ не пустой
            return response.text().then(text => {
                return text ? JSON.parse(text) : {};
            });
        })
        .then(data => {
            loadUsers();
            document.getElementById('editUserModal').hide();
            new bootstrap.Modal(document.getElementById('editUserModal')).hide();
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
});

//create employees
function loadRolesForNewUser() {
    fetch('http://localhost:8080/api/employees/roles', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(roles => {
            const rolesContainer = document.getElementById('new-roles-checkboxes'); // Добавьте контейнер для чекбоксов в модальном окне
            rolesContainer.innerHTML = ''; // Очищаем контейнер перед заполнением
            roles.forEach(role => {
                const checkbox = `
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="new-role-${role.id}" value="${role.id}" data-name="${role.name}">
                        <label class="form-check-label" for="new-role-${role.id}">${role.name}</label>
                    </div>
                `;
                rolesContainer.innerHTML += checkbox; // Добавляем чекбоксы
            });
        });
}

document.getElementById('newUserForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const employee = {
        name: document.getElementById('NewName').value,
        surname: document.getElementById('NewSurname').value,
        password: document.getElementById('NewPassword').value,
        salary: document.getElementById('NewSalary').value,
        department: document.getElementById('NewDepartment').value,
        roles: [...document.querySelectorAll('#newUserForm input[type=checkbox]:checked')].map(cb => ({id: cb.value}))
    };

    fetch(`/api/admin`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(employee)
    })
        .then(response => response.json())
        .then(() => {
            loadUsers();
            document.getElementById('newUserForm').reset();
            new bootstrap.Modal(document.getElementById('newUserModal')).hide();
        });
});

//navBar
async function dataAboutEmployee() {
    const response = await fetch("/api/employees")
    return await response.json();
}

async function showInfoEmployeeInNavbar() {
    const currentEmployeeNameNavbar = document.getElementById("current-user")
    const currentEmployee = await dataAboutEmployee();
    currentEmployeeNameNavbar.innerHTML =
        `<strong>${currentEmployee.name}</strong>
         with roles:
         ${currentEmployee.roles.map(role => role.name).join(', ')}`;
}

//delete employees
function openDeleteModal(id) {

    document.getElementById('delete-id').value = id;
    new bootstrap.Modal(document.getElementById('deleteUserModal')).show();
}

document.getElementById('deleteUserForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const id = document.getElementById('delete-id').value;

    fetch(`http://localhost:8080/api/admin/${id}`, {
        method: 'DELETE'
    })
        .then(() => {
            loadUsers();
            const modal = bootstrap.Modal.getInstance(document.getElementById(`deleteUserModal`));
            modal.hide();
        });
});

//infoEmployees
async function loadInfoCurrentEmployee() {
    const currentEmployeeTable = document.getElementById("employee-info-body");
    const employee = await dataAboutEmployee();

    let currentEmployeeTableHtml = "";
    currentEmployeeTableHtml +=
        `<tr>
    <td>${employee.id}</td>
    <td>${employee.name}</td>
    <td>${employee.surname}</td>
    <td>${employee.password}</td>
    <td>${employee.salary}</td>
    <td>${employee.department}</td>
    <td>${employee.roles.map(role => role.name).join(' ')}</td>
        </tr>`
    currentEmployeeTable.innerHTML = currentEmployeeTableHtml;
}

