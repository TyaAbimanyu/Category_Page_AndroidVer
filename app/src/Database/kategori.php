<?php

require "koneksi.php";
$user_name = $_POST['name'];
$type = $_POST['type'];

$mysql_qry = "SELECT name FROM categories where type='$type'";
$result = mysqli_query($con, $mysql_qry);

?>