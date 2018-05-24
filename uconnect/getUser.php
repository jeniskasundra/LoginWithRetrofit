<?php 
	$id = $_GET['id'];
	
	require_once('dbConnect.php');
	
	$sql = "SELECT * FROM user_detail WHERE id=$id";
	$r = mysqli_query($con,$sql);
	$row = mysqli_fetch_array($r);
	$arr = array(
			"id"=>$row['id'],
			"name"=>$row['name'],
			"email"=>$row['email'],
			"password"=>$row['password'],
			"image"=>$row['image']
		);

	echo json_encode($arr);
	
	mysqli_close($con);