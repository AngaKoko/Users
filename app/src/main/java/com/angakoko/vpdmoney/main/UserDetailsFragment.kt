package com.angakoko.vpdmoney.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.angakoko.vpdmoney.R
import com.angakoko.vpdmoney.ViewModelFactory
import com.angakoko.vpdmoney.databinding.FragmentUserDetailsBinding
import com.angakoko.vpdmoney.model.Permissions
import com.angakoko.vpdmoney.model.User
import com.angakoko.vpdmoney.model.createImageFile
import java.io.File
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class UserDetailsFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailsBinding
    private lateinit var viewModel: MainViewModel
    private var cameraImgUri:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_user_details, container, false)

        val viewModelFactory = ViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        viewModel.setHeader("")

        binding.profileImage.setOnClickListener {
            checkForCameraPermission()
        }

        binding.cameraButton.setOnClickListener {
            checkForCameraPermission()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    private fun checkForCameraPermission(){
        val locationPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                Permissions.CAMERA_REQUEST_CODE
            )
        } else {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().applicationContext.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(requireActivity().applicationContext).apply {
                        // Save a file: path for use with ACTION_VIEW intents
                        val uri = Uri.fromFile(File(absolutePath))
                        cameraImgUri = uri.toString()
                    }
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity().applicationContext,
                        getString(R.string.app_authority),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, Permissions.REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    //Add picture to gallery
    private fun galleryAddPic(currentPhotoPath:String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }

    /**
     * Request for permission on runtime
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Permissions.CAMERA_REQUEST_CODE -> if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Permissions.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            //Check if url is empty
            if(!TextUtils.isEmpty(cameraImgUri)){
                //if not empty add picture to storage
                galleryAddPic(cameraImgUri)
                //get url of image
                val uri = Uri.parse(cameraImgUri)
                //get selected user from view model
                val user = viewModel.getUser().value ?: User()
                //update image url of user
                user.imgUri = uri.toString()
                //set selected user with update img url in view model
                viewModel.setUser(user)
                //update user in room DB
                viewModel.updateUserInDb(user)
            }
        }
    }
}