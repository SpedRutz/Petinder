
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class profileactivity : AppCompatActivity() {


    var imageuri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileactivity)



        //signs the user out of the user activity and puts them back to the main activty
        val Logout = findViewById<Button>(R.id.logout)



        Logout.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val ProfilePicture = findViewById<ImageView>(R.id.Profilepic)

    //Activty result for the gallery to choose a picture
        val galleryActivityResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            //Sets the image to the profile imageview, throws an exception if the image is not found
            try {
                imageuri = uri
                ProfilePicture.setImageURI(uri)
            } catch (e:Exception) {
                Log.d("Error", "error Loading Image: ${e.localizedMessage}")
            }
        }

//Opens the phones gallery and allows user to pick picture by using an intent
        ProfilePicture.setOnClickListener{ view ->
            galleryActivityResult.launch("image/*")
        }

  //Button to upload the profile picture url to the firebase storage
        val uploadButton = findViewById<Button>(R.id.buttonUpload)

        uploadButton.setOnClickListener { view ->
            if (imageuri != null){
                uploadimage()
            } else {
                Toast.makeText(this@profileactivity," Please select an Image", Toast.LENGTH_SHORT).show()
            }
        }



    }

    fun  uploadimage() {
        //progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Check if the ProgressBar view was found
        if (progressBar == null) {
            Log.e("Error", "ProgressBar not found in layout")
            return
        }
        //Upload the image to firebase storage using the image uri (URL)
        val imageref = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString())

        imageref.putFile(imageuri!!).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                // Save the URL in a variable or pass it to a function
                val downloadUrl = uri.toString()
                //Updates the firebase storage with the users current profile picture
                FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().uid + "/profilePicture").setValue(downloadUrl)
            }

            //Image was successfully uploaded
            progressBar.visibility =View.INVISIBLE
            Toast.makeText(this@profileactivity," Image uploaded sucessfully", Toast.LENGTH_SHORT).show()

        }// Adds a on failure listener incase image fails to upload

            .addOnFailureListener { e ->
                progressBar.visibility = View.INVISIBLE
                Log.d("Error", "Error uploading image: ${e.localizedMessage}")
                Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
            }
    }
}

