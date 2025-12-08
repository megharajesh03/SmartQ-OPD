<!-- src/main/webapp/WEB-INF/views/user/user_home.jsp -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
</head>
<body>
    <h2>User Dashboard</h2>
    
    <div>
        <h3>Welcome, User!</h3>
        <nav>
            <ul>
                <li><a href="/user/view-appointments">View Appointments</a></li>
                <li><a href="/user/view-prescriptions">View Prescriptions</a></li>
                <li><a href="/user/view-medical-history">View Medical History</a></li>
                <li><a href="/logout">Logout</a></li>
            </ul>
        </nav>
    </div>
</body>
</html>
