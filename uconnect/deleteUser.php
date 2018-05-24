<?php 
	$id = $_GET['id'];
	
	require_once('dbConnect.php');
	
	$sql = "DELETE FROM user_detail WHERE id=$id;";
	
	if(mysqli_query($con,$sql)){
		echo 'User Deleted Successfully';
	}else{
		echo 'Could Not Delete User Try Again';
	}
	
	mysqli_close($con);
