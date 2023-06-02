package com.submission.appstory

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.databinding.ActivityAddBinding
import com.submission.appstory.response.AddStoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                startCameraX()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                startCameraX()
            }
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val choser = Intent.createChooser(intent, "Pilih gambar dari galeri")
            launcherIntentGallery.launch(choser)
        }

        binding.buttonAdd.setOnClickListener {
            upload()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                getFile = file
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddActivity)
                getFile = myFile
                binding.ivPreview.setImageURI(uri)
            }
        }
    }

    private fun upload() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val desc = binding.edAddDescription.text.toString()
            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val token = getSharedPreferences("LoginSession", Context.MODE_PRIVATE).getString("token", "").toString()
            if (binding.edAddDescription.text.isNullOrEmpty()) {
                Toast.makeText(this@AddActivity, "Silahkan tambahkan deskripsi terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                showLoading(true)
                val call = ApiConfig.getApiService(token).addStory(imageMultipart, description)
                call.enqueue(object: Callback<AddStoryResponse> {
                    override fun onResponse(
                        call: Call<AddStoryResponse>,
                        response: Response<AddStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            showLoading(false)
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                Toast.makeText(this@AddActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@AddActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this@AddActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(this@AddActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(this@AddActivity, "Silahkan masukkan berkas terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            ObjectAnimator.ofFloat(binding.buttonAdd, View.ALPHA, 0f).start()
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            ObjectAnimator.ofFloat(binding.buttonAdd, View.ALPHA, 1f).start()
            binding.progressIndicator.visibility = View.GONE
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}