package com.darkshandev.freshcam.presentation.classifier.views

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.databinding.FragmentScanFruitsBinding
import com.darkshandev.freshcam.utils.createFile
import com.darkshandev.freshcam.utils.uriToFile
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFruitsFragment : Fragment() {
private var binding: FragmentScanFruitsBinding? = null
    private var cameraExecutor: ExecutorService? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    override fun onDestroyView() {
        binding=null
        cameraExecutor?.shutdown()
        cameraExecutor = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
  binding = FragmentScanFruitsBinding.inflate(inflater, container, false)
        setupView()
        prepareCamera()
        return binding?.root
    }

    private fun prepareCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
//        imageCapture = ImageCapture.Builder()
//            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .setTargetRotation(binding?.root?.display?.rotation)
//            .build()

    }

    private fun setupView() {
        binding?.apply {
         backButton.setOnClickListener {
            activity?.onBackPressed()
         }
            captureImage.setOnClickListener {

            }
            flashButton.setOnClickListener {

            }
            galleryButton.setOnClickListener {

            }
        }
    }
    private fun captureImage() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_pick_photo),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri
                    classifyFruitsByThis(image = photoFile)
                }
            }
        )
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            classifyFruitsByThis(image = myFile)
        }
    }
    private fun classifyFruitsByThis(image: File){


    }
}