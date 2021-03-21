package com.example.campus_activity.ui.handler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ImageHelper
@Inject
    constructor(
        @ActivityContext val context: Context
    ) {
    val PICK_IMAGE = 100
    val CROP_IMAGE = 200

    fun pickImage() : Intent{
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    }

    fun cropImage(uri:Uri, aspectX:Int, aspectY:Int):Intent{
        val tmpUri:Uri? = null
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(uri, "image/*")
        cropIntent.putExtra("crop", true)
        cropIntent.putExtra("aspectX", aspectX)
        cropIntent.putExtra("aspectY", aspectY)
//        cropIntent.putExtra("outputX", outputX)
//        cropIntent.putExtra("outputY", outputY)
        cropIntent.putExtra("return-data", true)
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpUri)
        return cropIntent
    }

    fun cropImage(uri:Uri):Intent{
        val tmpUri:Uri? = null
        val cropIntent = Intent("com.android.camera.action.CROP")
        cropIntent.setDataAndType(uri, "image/*")
        cropIntent.putExtra("crop", true)
//        cropIntent.putExtra("aspectX", aspectX)
//        cropIntent.putExtra("aspectY", aspectY)
//        cropIntent.putExtra("outputX", outputX)
//        cropIntent.putExtra("outputY", outputY)
        cropIntent.putExtra("return-data", true)
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tmpUri)
        return cropIntent
    }
}