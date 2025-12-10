<!-- editdoctor.jsp -->
<h2>Edit Doctor</h2>
<form action="/admin/editdoctor/${doctor.id}" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="${doctor.username}" required><br><br>

    <label for="specialization">Specialization:</label>
    <input type="text" id="specialization" name="specialization" value="${doctor.specialization}" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${doctor.email}" required><br><br>

    <label for="phone">Phone:</label>
    <input type="text" id="phone" name="phone" value="${doctor.phone}"><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" value="${doctor.password}" required><br><br>

    <label for="status">Status:</label>
    <select id="status" name="status" required>
        <option value="AVAILABLE" ${doctor.status == 'AVAILABLE' ? 'selected' : ''}>Available</option>
        <option value="NOT_AVAILABLE" ${doctor.status == 'NOT_AVAILABLE' ? 'selected' : ''}>Not Available</option>
    </select><br><br>

    <button type="submit">Update Doctor</button>
</form>
