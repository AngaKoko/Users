package com.angakoko.vpdmoney.model

import android.content.Context
import android.os.Environment
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.angakoko.vpdmoney.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun UserResponse.toUser(): User{
    return User(
        id = this.id,
        name = this.name,
        username = this.username,
        email = email,
        street = address.street,
        suite = address.suite,
        city = address.city,
        zipcode = address.zipcode,
        company = company.companyName,
        catchPhrase = company.catchPhrase,
        bs = company.bs,
        lat = address.geo.lat,
        lng = address.geo.lat,
    )
}

/**
 * Used to get a file path of image. Used when taking picture with the camera
 */
@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}

@BindingAdapter("avatar")
fun avatar(view: ImageView, avatar: String?) {
    Glide.with(view.context.applicationContext)
        .load(avatar)
        .placeholder(R.drawable.ic_person)
        .apply(
            RequestOptions.circleCropTransform()
                //.placeholder(R.drawable.circle_default_background)
                .error(R.drawable.ic_person)
        )
        .into(view)
}

object Permissions{
    const val CAMERA_REQUEST_CODE = 202
    const val REQUEST_TAKE_PHOTO = 201
}