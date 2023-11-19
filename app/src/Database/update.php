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

if ($result_select) {
    // Jika data ditemukan, lakukan operasi update
    if (mysqli_num_rows($result_select) > 0) {
        $mysql_qry_update = "UPDATE categories SET name='$user_name' WHERE type='$type'";
        $result_update = mysqli_query($con, $mysql_qry_update);

        if ($result_update) {
            echo "Update successful";
        } else {
            echo "Failed to update data: " . mysqli_error($con);
        }
    } else {
        echo "Data not found for the given type";
    }
} else {
    echo "Error in query: " . mysqli_error($con);
}

// Tutup koneksi ke database
mysqli_close($con);
?>
