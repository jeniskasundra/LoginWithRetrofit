<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Getting values
		$name = $_POST['name'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		$image = $_POST['image'];
		
		require_once('dbConnect.php');
		
		$sql ="SELECT id FROM user_detail ORDER BY id ASC";

        $res = mysqli_query($con,$sql);

		$id=1;
        while($row = mysqli_fetch_array($res)){
                $id = $row['id']+1;
        }

        $path = "images/".$id.".png";
        $actualpath  = $id.".png";
		
		//Creating an sql query
		$sql = "INSERT INTO user_detail(name,email,password,image) VALUES ('$name','$email','$password','$actualpath')";
		
		//Importing our db connection script
		
		
		//Executing query to database
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));			
			echo 'User Register Successfully';
		}else{
			echo 'Could Not Register User Try Again';
		}
		
		//Closing the database 
		mysqli_close($con);
	}