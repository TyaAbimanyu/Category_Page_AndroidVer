<?php

require "koneksi.php";

$user_name = $_POST['name'];
$type = $_POST['type'];

// Periksa apakah data dengan tipe yang diberikan sudah ada
$mysql_qry_select = "SELECT name FROM categories WHERE type='$type'";
$result_select = mysqli_query($con, $mysql_qry_select);

if ($result_select) {
    // Jika data tidak ditemukan, lakukan operasi insert
    if (mysqli_num_rows($result_select) == 0) {
        $mysql_qry_insert = "INSERT INTO categories (name, type) VALUES ('$user_name', '$type')";
        $result_insert = mysqli_query($con, $mysql_qry_insert);

    } else {
    echo "Error in query: " . mysqli_error($con);
    }
}

// Tutup koneksi ke database
mysqli_close($con);
?>
