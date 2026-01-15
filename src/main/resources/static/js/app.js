// app.js
document.getElementById("userForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const form = e.target;

    const name = form.querySelector('input[name="name"]').value;
    const email = form.querySelector('input[name="email"]').value;
    const photo = form.querySelector('input[name="photo"]').files[0];

    const formData = new FormData();
    formData.append("name", name);
    formData.append("email", email);
    formData.append("photo", photo);

    const responseBox = document.getElementById("response");
    responseBox.innerHTML = "Loading...";

    try {
        const response = await fetch("http://localhost:8080/users/store", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        const result = await response.json();

        // Custom Response Handling
        if (result.status === true) {
            responseBox.innerHTML = "✅ Data berhasil disimpan";
            responseBox.className = "msg success";
            form.reset();
        } else {
            responseBox.innerHTML = "⚠️ Respon tidak dikenal: " + result;
            responseBox.className = "msg error";
        }

    } catch (error) {
        responseBox.innerHTML = "❌ Gagal upload: " + error.message;
        responseBox.className = "msg error";
    }
});
