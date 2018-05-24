<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
		$name = $_POST['name'];
		$image = $_POST['image'];
		
		require_once('dbConnect.php');
		
        $path = "images/".$id.".png";    
		$sql = "UPDATE user_detail SET name = '$name' WHERE id = $id;";
		
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));
			echo 'User Updated Successfully';
		}else{
			echo 'Could Not Update User Try Again';
		}
		
		mysqli_close($con);
	}