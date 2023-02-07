package com.example.mymusiclibrary

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mymusiclibrary.R
import com.example.mymusiclibrary.databinding.FragmentDetailBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var pictureAbsolutePath: Uri
    private lateinit var albums: MutableList<Album>
    private var currentALbum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentALbum = it.getInt("currentExerciseIndex")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albums = getAlbumsList(requireContext()).toMutableList()

        binding.albumInfo.text = Editable.Factory.getInstance().newEditable(
            albums[currentALbum].info
        )
        binding.imageView.setImageURI(Uri.parse(albums[currentALbum].picture))
        binding.saveButton.setOnClickListener {
            albums[currentALbum].info = binding.albumInfo.text.toString()
            saveAlbumsList(requireContext(), albums)
            Navigation.findNavController(binding.root).navigate(R.id.action_detailFragment_to_listFragment)
        }
        binding.deleteButton.setOnClickListener {
            albums.removeAt(currentALbum)
            saveAlbumsList(requireContext(), albums)
            Navigation.findNavController(binding.root).navigate(R.id.action_detailFragment_to_listFragment)
        }
        binding.addPhotoButton.setOnClickListener {
            openCamera()
        }
        binding.backButton.setOnClickListener()
        {
            Navigation.findNavController(binding.root).navigate(R.id.action_detailFragment_to_listFragment)
        }
    }

    private val resultLauncherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(imageBitmap)
                pictureAbsolutePath = saveImage(imageBitmap)
                albums[currentALbum].picture = pictureAbsolutePath.toString()
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                launchCamera()
            }
        }

    private fun showMessageOKCancel(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun openCamera() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.CAMERA) -> {
                showMessageOKCancel(getString(R.string.rationale_camera))
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncherCamera.launch(intent)
    }

    private fun saveImage(bitmap: Bitmap): Uri {
        var file = requireContext().getDir("myGalleryKotlin", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

}