<!DOCTYPE html>
<html>
<head>
    <title>User Profile</title>
</head>
<body>
    <h2>User Profile</h2>
    <div id="userProfile">
      <h1>Welcome, <span id="userName">Loading...</span></h1>
      <p>Email: <span id="userEmail">Loading...</span></p>
      <p>User ID: <span id="userID">Loading...</span></p>
      <p>Password Hash: <span id="passwordHash">Loading...</span></p>
      <p>Phone Number: <span id="phoneNumber">Loading...</span></p>
      <p>Address: <span id="address">Loading...</span></p>
      <p>Bookings Count: <span id="bookingsCount">Loading...</span></p>
      <p>Payments Count: <span id="paymentsCount">Loading...</span></p>
      <p>Reviews Count: <span id="reviewsCount">Loading...</span></p>
    </div>

<script>
window.onload = async function () {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        console.error("JWT token not found");
        document.getElementById("userName").innerText = "Guest";
        document.getElementById("userEmail").innerText = "-";
        document.getElementById("userID").innerText = "-";
        document.getElementById("passwordHash").innerText = "-";
        document.getElementById("phoneNumber").innerText = "-";
        document.getElementById("address").innerText = "-";
        document.getElementById("bookingsCount").innerText = "-";
        document.getElementById("paymentsCount").innerText = "-";
        document.getElementById("reviewsCount").innerText = "-";
        return;
    }

    // Decode JWT payload (Base64 decode)
    let payload;
    try {
        payload = JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        console.error("Invalid token", e);
        document.getElementById("userName").innerText = "Invalid token";
        return;
    }

    const tokenUserId = payload.id;

    // Fetch all users
    const url = `http://localhost:8080/Photographer/api/users/`;

    try {
        const response = await fetch(url, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) throw new Error("Failed to fetch users");

        const users = await response.json();

        // Find logged in user by userID
        const matchedUser = users.find(user => user.userID === tokenUserId);

        if (matchedUser) {
            document.getElementById("userName").innerText = matchedUser.fullName || "N/A";
            document.getElementById("userEmail").innerText = matchedUser.email || "N/A";
            document.getElementById("userID").innerText = matchedUser.userID || "N/A";
            document.getElementById("passwordHash").innerText = matchedUser.passwordHash || "N/A";
            document.getElementById("phoneNumber").innerText = matchedUser.phoneNumber || "N/A";
            document.getElementById("address").innerText = matchedUser.address || "N/A";
            document.getElementById("bookingsCount").innerText = matchedUser.bookingsCollection?.length || 0;
            document.getElementById("paymentsCount").innerText = matchedUser.paymentsCollection?.length || 0;
            document.getElementById("reviewsCount").innerText = matchedUser.reviewsCollection?.length || 0;
        } else {
            document.getElementById("userProfile").innerHTML = `<p>User not found</p>`;
        }
    } catch (err) {
        console.error("Failed to load users:", err);
        document.getElementById("userName").innerText = "Error loading user";
    }
};
</script>

</body>
</html>
