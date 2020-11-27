$(document).ready(renderUsersTable);


async function renderUsersTable() {
    let usersTableHtml = await getUsersTable();
    $('#users-table').html(usersTableHtml);
}


async function getUsersTable() {
    let response = await fetch('http://localhost:8080/admin/users');
    let usersData = ``;
    if (response.status === 200) {

        let users = await response.json();
        for (user of users) {
            usersData +=
                `<tr>
                    <th scope="row" class="align-middle">${user.id}</th>
                    <td id="name-${user.id}" class="align-middle">${user.name}</td>
                    <td id="surname-${user.id}" class="align-middle">${user.surname}</td>
                    <td id="age-${user.id}" class="align-middle">${user.age}</td>
                    <td id="job-${user.id}" class="align-middle">${user.job != null ? user.job.name : 'N/a'}</td>
                    <td id="salary-${user.id}" class="align-middle">${user.job != null ? user.job.salary : 'N/a'}</td>
                    <td id="email-${user.id}" class="align-middle">${user.email}</td>
                    <td id="roles-${user.id}" class="align-middle">
                        <div class="d-flex flex-column">`;
            user.roles.sort(byId).forEach(function(value) {
                usersData += `<span>${value.roleName.substr(5)}</span>`;
            });
            usersData +=
                `        </div>
                    </td>
                    <td class="align-middle text-center border-left">
                        <button type="button" class="btn btn-info" data-toggle="modal" data-target="#editUserModal" data-userid="${user.id}">Edit</button>
                        <button type="button" class="btn btn-danger ml-1" data-toggle="modal" data-target="#deleteUserModal" data-userid="${user.id}">Delete</button>
                    </td>
                </tr>`;
        }
    }

    return usersData;
}


function byId(obj1, obj2) {
    return obj1.id - obj2.id;
}


$('#deleteUserModal').on('show.bs.modal', async function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var userId = button.data('userid') // Extract info from data-* attributes
    let response = await fetch('http://localhost:8080/admin/users/' + userId);

    if (response.status === 200) {
        let user = await response.json();
        // Update the modal's content with jQuery
        let modal = $(this)
        modal.find('#id').val(user.id)
        modal.find('#name').val(user.name)
        modal.find('#surname').val(user.surname)
        modal.find('#age').val(user.age)
        if (user.job != null) {
            modal.find('#job\\.name').val(user.job.name)
            modal.find('#job\\.salary').val(user.job.salary)
        } else {
            modal.find('#job\\.name').val('N/a')
            modal.find('#job\\.salary').val('N/a')
        }
        modal.find('#email').val(user.email)

        let selectElement = modal.find('#roles');
        selectElement.attr('size', user.roles.length);

        let options = ``;
        for (role of user.roles.sort(byId)) {
            options += `<option value="${role.roleName}">${role.roleName.substr(5)}</option>`;
        }

        selectElement.html(options);

        modal.find('#delete-user-submit').on('click', async function(event) {
            event.preventDefault();
            let response = await fetch('http://localhost:8080/admin/users/' + user.id, {
                method: 'DELETE'
            })
            if (response.status === 204) {
                $('#deleteUserModal').modal('hide');
                renderUsersTable();
            }
        });
    }
});


$('#nav-tab a').on('click', function(event) {
    event.preventDefault();
    $(this).tab('show');
    switch (this.id) {
        case "nav-userstable-tab":
            renderUsersTable();
            break;
        case "nav-newuser-tab":
            loadAvailableRoles('#new-user-form select');
            break;
    }
});


async function loadAvailableRoles(selectSelector) {
    let response = await fetch('http://localhost:8080/admin/roles');
    if (response.status === 200) {
        let roles = await response.json();
        let targetSelectElement = $(selectSelector);
        targetSelectElement.attr('size', roles.length);

        let options = ``;
        for (role of roles) {
            options += `<option value="${role.roleName}">${role.roleName.substr(5)}</option>`;
        }

        targetSelectElement.html(options);
    }
}


$('#new-user-submit').on('click', async function(event) {
    event.preventDefault();
    let form = $('#new-user-form');
    let user = {};
    let job = {};
    let roles = [];
    form.find('input, select').each(function() {
        switch (this.id) {
            case "job.name":
                job["name"] = $(this).val();
                break;
            case "job.salary":
                job["salary"] = $(this).val();
                break;
            case "roles":
                $(this).val().forEach(function (value) {
                    roles.push({ roleName: value });
                });
                break;
            default:
                user[this.id] = $(this).val();
                break;
        }
    });
    user["job"] = job;
    user["roles"] = roles;

    let response = await fetch('http://localhost:8080/admin/users', {
        method: 'POST',
        headers: {
            'Content-type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });

    let result;
    if (response.status === 201) {
        form.trigger('reset');
        $('#nav-userstable-tab').trigger('click');
    } else if (response.status === 400) {
        result = await response.json();
            clearPreviousValidationErrors(form);
        for ([key, value] of Object.entries(result)) {
            showValidationErrors(form, key, value);
        }
    } else if (response.status === 500) {
        result = await response.text();
        clearPreviousValidationErrors(form);
        showServerErrorModal(result);
    }
})


function clearPreviousValidationErrors(form) {
    form.find('input, select').removeClass('is-invalid');
    $('.invalid-feedback').remove();
}


function showValidationErrors(form, fieldName, errMsg) {
    let sel = jqId(fieldName);
    let inputElement = form.find(sel);

    inputElement.addClass('is-invalid');
    inputElement.after(
        `<div class="invalid-feedback">
             ${errMsg}
         </div>`
    );
}


function jqId(unescapedId) {
    return '#' + unescapedId.replace(/\./g, "\\.");
}


function showServerErrorModal(errorMsg) {
    let modalElement =
        `<div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="errorModalLabel">Error info from server:</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body px-2 justify-content-center">
                        <div class="alert alert-danger rounded mx-auto text-monospace">
                            <b>${errorMsg}</b>
                        </div>
                    </div>
                    <div class="modal-footer justify-content-center">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
         </div>`;
    $('html').append(modalElement);
    let errorModalElement = $('#errorModal');
    errorModalElement.modal('show');
    errorModalElement.on('hidden.bs.modal', function(event) {
        $(this).remove();
    });
}