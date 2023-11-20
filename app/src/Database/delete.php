<?php

require "koneksi.php";

$user_name = $_POST['name'];
$type = $_POST['type'];

// Bagian baru untuk menghapus data
$mysql_qry_delete = "DELETE FROM categories WHERE type='$type'";
$result_delete = mysqli_query($con, $mysql_qry_delete);


// Tutup koneksi ke database
mysqli_close($con);
?>
