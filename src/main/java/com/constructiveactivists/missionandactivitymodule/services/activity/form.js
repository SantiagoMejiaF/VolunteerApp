function handleFileSelect(event) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onloadend = function () {
        const base64String = reader.result.split(',')[1];
        document.getElementById('imageBase64').value = base64String;
    };
    reader.readAsDataURL(file);
}
