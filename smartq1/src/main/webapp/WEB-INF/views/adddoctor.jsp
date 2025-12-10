<!-- adddoctor.jsp -->
<h2>Add New Doctor</h2>
<form action="/admin/adddoctor" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="specialization">Specialization:</label>
    <input type="text" id="specialization" name="specialization" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="phone">Phone:</label>
    <input type="text" id="phone" name="phone"><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <label for="status">Status:</label>
    <select id="status" name="status" required>
        <option value="AVAILABLE">Available</option>
        <option value="NOT_AVAILABLE">Not Available</option>
    </select><br><br>

    <button type="submit">Add Doctor</button>
</form>
