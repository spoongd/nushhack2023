package com.nushhack.keko

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.camera.core.Camera
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import com.nushhack.keko.databinding.FragmentScanBinding
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.TimeoutError
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Locale

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var fixedLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture : ImageCapture

    private lateinit var camera : Camera

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in listOf(android.Manifest.permission.CAMERA) && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(context,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                Log.i("SCAN", "mimimeow")

                startCamera()
            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityResultLauncher.launch(arrayOf(android.Manifest.permission.CAMERA))
        binding.galleryButton.isEnabled = true
        binding.photoButton.isEnabled = true

        fixedLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            binding.galleryButton.isEnabled = true
            binding.photoButton.isEnabled = true
        }

        photoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
//                        photoChanged = true
                val photoUri = res.data?.data!!
//                        binding.pfpImageview.setImageURI(photoUri)
//                        updateButton()
                Log.i("NICETIFY", "starting")

                stopCamera()
                binding.galleryButton.isEnabled = false
                binding.photoButton.isEnabled = false
                binding.progress.show()

                val volleyMultipartRequest: VolleyMultipartRequest =
                    object : VolleyMultipartRequest(
                        Method.POST, "http://34.125.233.174:5000/nicetify",
                        { response ->
                            val path = String(response.data, Charset.defaultCharset())
                            Log.i("NICETIFY", "we did it http://34.125.233.174:5000/$path")

                            val intent = Intent(context, ImageActivity::class.java)
                            intent.putExtra(getString(R.string.key_path), path)
                            fixedLauncher.launch(intent)
                        },
                        {
                            Log.e("NICETIFY", "err... $it")
                            Snackbar.make(binding.root, "Error: $it", Snackbar.LENGTH_SHORT).show()
                        }) {

                        override fun getByteData(): Map<String, DataPart> {
                            Log.i("NICETIFY", "we got this far")
                            val params: MutableMap<String, DataPart> = HashMap()
                            params["file"] = DataPart("mimimeow.png",  requireContext().contentResolver.openInputStream(photoUri)?.use { it.buffered().readBytes() })
                            return params
                        }
                    }

                //adding the request to volley
                Volley.newRequestQueue(context).add(volleyMultipartRequest)
            }
        }
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO
//            ) != PackageManager.PERMISSION_GRANTED ) {
//            ActivityCompat.requestPermissions(this,
//                arrayOf(android.Manifest.permission.RECORD_AUDIO), 1345)
//        }
        binding.photoButton.setOnClickListener { takePhoto() }
        binding.galleryButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            photoLauncher.launch(imageIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        binding.progress.show()

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e("SCAN", "Use case binding failed", exc)
            }

            binding.progress.hide()

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProviderFuture.get().unbindAll()

        }, ContextCompat.getMainExecutor(requireContext()))
    }
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture

        binding.galleryButton.isEnabled = false
        binding.photoButton.isEnabled = false
        binding.progress.show()

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()



        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("SCAN", "Photo capture failed: ${exc.message}", exc)
                    startCamera()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
//                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d("SCAN", msg)
                    stopCamera()

                    val volleyMultipartRequest: VolleyMultipartRequest =
                        object : VolleyMultipartRequest(
                            Method.POST, "http://34.125.233.174:5000/nicetify",
                            { response ->
                                val path = String(response.data, Charset.defaultCharset())
                                Log.i("NICETIFY", "we did it http://34.125.233.174:5000/$path")

                                val intent = Intent(context, ImageActivity::class.java)
                                intent.putExtra(getString(R.string.key_path), path)
                                fixedLauncher.launch(intent)
                            },
                            {
                                Log.e("NICETIFY", "err... $it")
                                Snackbar.make(binding.root, "Error: $it", Snackbar.LENGTH_SHORT).show()

                            }) {

                            override fun getByteData(): Map<String, DataPart> {
                                Log.i("NICETIFY", "we got this far")
                                val params: MutableMap<String, DataPart> = HashMap()
                                params["file"] = DataPart("mimimeow.png", requireContext().contentResolver.openInputStream(output.savedUri!!)?.use { it.buffered().readBytes() })
                                return params
                            }
                        }

                    //adding the request to volley
                    volleyMultipartRequest.retryPolicy = DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                    Volley.newRequestQueue(context).add(volleyMultipartRequest)
                }
            }
        )
    }


}