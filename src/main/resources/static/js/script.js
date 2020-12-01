$(document).ready(renderCurrentUserTable);


async function renderCurrentUserTable() {
    let currentUserTableHtml = await getCurrentUserTable();
    $('#current-user-data').html(currentUserTableHtml);
}


async function getCurrentUserTable() {
    let userData = ``;
    let response = await fetch("http://localhost:8080/users/current");

    if (response.status === 200) {
        let user = await response.json();
        userData +=
            `<th scope="row" class="align-middle">${user.id}</th>
             <td class="align-middle">${user.name}</td>
             <td class="align-middle">${user.surname}</td>
             <td class="align-middle">${user.age}</td>
             <td class="align-middle">${user.job != null ? user.job.name : 'N/a'}</td>
             <td class="align-middle">${user.job != null ? user.job.salary : 'N/a'}</td>
             <td class="align-middle">${user.email}</td>
             <td class="align-middle">
                <div class="d-flex flex-column">`;
        user.roles.sort(byId).forEach(function(value) {
           userData += `<span>${value.roleName.substr(5)}</span>`;
        });
        userData +=
            `   </div>
             </td>`;
    }

    return userData;
}


function byId(obj1, obj2) {
    return obj1.id - obj2.id;
}