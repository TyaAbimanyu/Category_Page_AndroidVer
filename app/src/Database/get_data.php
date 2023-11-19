<?php

require "koneksi.php";

// Menerima data dari POST request
$user_name = $_POST['name'];
$type = $_POST['type'];

// Membuat kueri SQL
$mysql_qry = "SELECT name FROM categories WHERE type='$type'";
$result = mysqli_query($con, $mysql_qry);

// Memeriksa apakah kueri berhasil dieksekusi
if (!$result) {
    die('Error: ' . mysqli_error($con));
}

// Menyiapkan array untuk menyimpan data
$data = array();

// Mengambil data dari hasil kueri dan menyimpannya ke dalam array
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

// Menutup koneksi ke database
mysqli_close($con);

// Mengirimkan data sebagai respons JSON
header('Content-Type: application/json');
echo json_encode($data);

?>
