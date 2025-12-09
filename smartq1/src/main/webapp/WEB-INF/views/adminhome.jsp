 src/main/webapp/WEB-INF/views/admin/admin_home.jsp 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
</head>
<body>
    <h2>Admin Dashboard</h2>
    
    <div>
        <h3>Welcome, Admin!</h3>
        <nav>
            <ul>
                <li><a href="/admin/manage-users">Manage Users</a></li>
                <li><a href="/admin/view-reports">View Reports</a></li>
                <li><a href="/admin/appointments">Manage Appointments</a></li>
                <li><a href="/logout">Logout</a></li>
            </ul>
        </nav>
    </div>
</body>
</html>