async function deleteUser(id) {
    if (!confirm("Yakin ingin menghapus data ini?")) return;

    const responseBox = document.getElementById("response");
    responseBox.innerHTML = "Menghapus...";

    try {
        const response = await fetch(`http://localhost:8080/destroy/${id}`, {
            method: "DELETE"
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
        responseBox.innerHTML = "❌ Gagal menghapus: " + error.message;
        responseBox.style.color = "red";
    }
}