<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		$id = $_POST['id'];
		$password = $_POST['password'];
		
		require_once('dbConnect.php');
		
		$sql = "UPDATE user_detail SET password = '$password' WHERE id = $id;";
		
		if(mysqli_query($con,$sql)){
			echo 'User Password Updated Successfully';
		}else{
			echo 'User Password Not Update Try Again';
		}
		
		mysqli_close($con);
	}