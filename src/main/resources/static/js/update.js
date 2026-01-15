// update.js
document.getElementById("updateForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("userId").value;
    const formData = new FormData(e.target);

    const responseBox = document.getElementById("response");
    responseBox.innerHTML = "Updating...";

    try {
        const response = await fetch(`http://localhost:8080/users/update/${id}`, {
            method: "PUT",
            body: formData
        });

        if (!response.ok) {
            throw new Error("HTTP Error " + response.status);
        }

        const result = await response.json();

        if (result.status) {
            responseBox.innerHTML = "✅ " + result.message;
            responseBox.style.color = "green";
        } else {
            responseBox.innerHTML = "❌ " + result.message;
            responseBox.style.color = "red";
        }

    } catch (error) {
        responseBox.innerHTML = "❌ Gagal update: " + error.message;
        responseBox.style.color = "red";
    }
});
