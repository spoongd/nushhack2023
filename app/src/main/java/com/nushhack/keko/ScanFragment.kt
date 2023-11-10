package com.nushhack.keko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import com.nushhack.keko.databinding.FragmentScanBinding
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview : Preview = Preview.Builder()
                .build()

            val cameraSelector : CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(requireContext()))

//        fun bindPreview(cameraProvider : ProcessCameraProvider) {
//            var preview : Preview = Preview.Builder()
//                .build()
//
//            var cameraSelector : CameraSelector = CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build()
//
//            preview.setSurfaceProvider(binding.previewView.getSurfaceProvider())
//
//            var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
//        }
    }



}