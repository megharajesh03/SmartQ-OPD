<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Home</title>
</head>
<body>
    <h2>Welcome, Doctor ${doctor.name}</h2>
    <p>Current Status: ${doctor.status}</p>

    <!-- Form to mark doctor as Available -->
    <form action="/doctor/markAvailable" method="post">
        <input type="hidden" name="doctorId" value="${doctor.id}">
        <button type="submit">Mark as Available</button>
    </form>

    <!-- Form to mark doctor as Not Available -->
    <form action="/doctor/markNotAvailable" method="post">
        <input type="hidden" name="doctorId" value="${doctor.id}">
        <button type="submit">Mark as Not Available</button>
    </form>

    <p><a href="/logout">Logout</a></p>
</body>
</html>
