<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['mobile']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("users", $_POST['mobile'], $_POST['password'])) {
            echo "Login Success";
        } else echo "Mobile or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
