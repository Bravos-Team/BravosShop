// Sample data for charts
const salesData = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [{
        label: 'Sales',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
        data: [65, 59, 80, 81, 56, 55, 40]
    }]
};

const usersData = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [{
        label: 'Users',
        backgroundColor: 'rgba(153, 102, 255, 0.2)',
        borderColor: 'rgba(153, 102, 255, 1)',
        borderWidth: 1,
        data: [45, 35, 60, 70, 50, 55, 60]
    }]
};

// Initialize charts
const salesChart = new Chart(document.getElementById('salesChart'), {
    type: 'line',
    data: salesData,
    options: {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Sales Chart'
            }
        }
    }
});

const usersChart = new Chart(document.getElementById('usersChart'), {
    type: 'bar',
    data: usersData,
    options: {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Users Chart'
            }
        }
    }
});

function showDefNotification() {
    let messageDiv = document.getElementById('message');
    if(messageDiv) {
        let message = messageDiv.textContent;
        document.getElementById('message-content').textContent = message;
        if(message && message.length > 0) {
            var myModal = new bootstrap.Modal(document.getElementById('notificationModal'));
            myModal.show();
        }
    }
}

function showNotification(message) {
    document.getElementById('message-content').textContent = message;
    var myModal = new bootstrap.Modal(document.getElementById('notificationModal'));
    myModal.show();
}


