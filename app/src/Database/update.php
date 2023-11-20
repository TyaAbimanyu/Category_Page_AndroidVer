<?php

require "koneksi.php";

$user_name = $_POST['name'];
$type = $_POST['type'];

// Lakukan validasi jenis data untuk mencegah SQL injection
$user_name = mysqli_real_escape_string($con, $user_name);
$type = mysqli_real_escape_string($con, $type);

// Periksa apakah data dengan tipe yang diberikan sudah ada
$mysql_qry_select = "SELECT name FROM categories WHERE type='$type'";
$result_select = mysqli_query($con, $mysql_qry_select);


// Tutup koneksi ke database
mysqli_close($con);
?>
