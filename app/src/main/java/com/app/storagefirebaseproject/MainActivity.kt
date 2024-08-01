package com.app.storagefirebaseproject

import android.app.AlertDialog
import android.content.DialogInterface

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val fileName : String = System.currentTimeMillis().toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Storage
        storage = Firebase.storage

        // Register the ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val fileUri: Uri? = result.data!!.data
                fileUri?.let { uploadFile(it) }
            }
        }

        // Button click to open file chooser
        findViewById<Button>(R.id.uploadButton).setOnClickListener {
            findViewById<TextView>(R.id.file_link).text = ""
            openFileChooser()
        }
        // Button click to download file
        findViewById<Button>(R.id.downloadButton).setOnClickListener {
            downloadFile(fileName)
        }
    }

    private fun downloadFile(fileName: String) {
        val storageRef = storage.reference.child("uploads/"+fileName+".jpg")
        val localFile = File.createTempFile("tempFile", "jpg") // Change extension if needed

        storageRef.getFile(localFile).addOnSuccessListener {
            // File downloaded successfully
            showAlert("Success", "File downloaded to: ${localFile.absolutePath}")
            findViewById<TextView>(R.id.file_link).setText("Cliquez pour ouvrir le fichier : "+localFile.name)
        }.addOnFailureListener { exception ->
            showAlert("Failure", "Failed to download file: ${exception.message}")
        }
        findViewById<TextView>(R.id.file_link).setOnClickListener({
            openFile(localFile)
        })
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun openFile(file: File) {
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No app found to open this file", Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadFile(fileUri: Uri) {
        val storageRef = storage.reference
        val fileRef = storageRef.child("uploads/${fileName}.jpg")

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                // File uploaded successfully
                        fileRef.downloadUrl.addOnSuccessListener { uri ->
                            showAlert("Success", "File has been uploaded")

                        }
            }
            .addOnFailureListener {
                showAlert("Failure", "Failed to upload file: ${it.message}")

            }
    }
    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        })
        builder.show()
    }
}