<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Doctor List</h2>

<!-- Debugging: Check if doctors data is passed -->
<c:if test="${not empty doctors}">
    <p>Doctors data is available.</p>
</c:if>
<c:if test="${empty doctors}">
    <p>No doctors found.</p>
</c:if>

<table border="1">
    <thead>
        <tr>
            <th>Username</th>
            <th>Specialization</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <!-- Iterate through the doctors list -->
        <c:forEach var="doctor" items="${doctors}">
            <tr>
                <td>${doctor.username != null ? doctor.username : 'N/A'}</td>
                <td>${doctor.specialization != null ? doctor.specialization : 'N/A'}</td>
                <td>${doctor.email != null ? doctor.email : 'N/A'}</td>
                <td>${doctor.phone != null ? doctor.phone : 'N/A'}</td>
                <td>${doctor.status != null ? doctor.status : 'N/A'}</td>
                <td>
                    <a href="/admin/editdoctor/${doctor.id}">Edit</a> | 
                    <form action="/admin/deletedoctor/${doctor.id}" method="post" style="display:inline;">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<a href="/admin/adddoctor">Add New Doctor</a>
