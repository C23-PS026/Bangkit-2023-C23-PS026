package com.capstoneproject.meatfyi.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstoneproject.meatfyi.databinding.ActivityResultDetectBinding
import com.capstoneproject.meatfyi.ml.Meatmodel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID


class ResultDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultDetectBinding
    private val GALLERY_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Detection"

        openCamera()
        openGallery()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun openCamera() {
        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicturePreview.launch(null)
            } else {
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun openGallery() {
        binding.galleryButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            } else {
                requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePicturePreview.launch(null)
            } else {
                Toast.makeText(this, "Izin ditolak! Coba lagi", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                binding.uploadImage.setImageBitmap(bitmap)
                uploadImageToApi(bitmap)
                outputModel(resizeBitmap(bitmap))
            }
        }

    private val onresult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("TAG", "Result : ${result.data} ${result.resultCode}")
            onResultReceived(GALLERY_REQUEST_CODE, result)
        }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val matrix = Matrix()
        matrix.postScale(width.toFloat() / bitmap.width, height.toFloat() / bitmap.height)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        val bitmap =
                            BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        binding.uploadImage.setImageBitmap(bitmap)
                        uploadImageToApi(bitmap)
                        outputModel(resizeBitmap(bitmap))
                    }
                } else {
                    Log.e("TAG", "Error in selecting image")

                }
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap): (ByteBuffer) {
        val resizedBitmap = resizeBitmap(bitmap, 256, 256)

        // Create a ByteBuffer with the appropriate size
        val byteBufferSize = 786432
        val byteBuffer = ByteBuffer.allocateDirect(byteBufferSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Iterate over the pixels of the resized bitmap and store them in the ByteBuffer
        val pixels = IntArray(256 * 256)
        resizedBitmap.getPixels(pixels, 0, 256, 0, 0, 256, 256)

        for (pixel in pixels) {
            val red = (pixel shr 16) and 0xFF
            val green = (pixel shr 8) and 0xFF
            val blue = pixel and 0xFF

            byteBuffer.put(red.toByte())
            byteBuffer.put(green.toByte())
            byteBuffer.put(blue.toByte())
        }

        byteBuffer.flip()
        byteBuffer.limit(byteBufferSize)
        return byteBuffer
    }

    private fun outputModel(byteBuffer: ByteBuffer) {
        // Instantiate the Meatmodel using the current context
        val model = Meatmodel.newInstance(this)

        // Create an input tensor buffer with the specified dimensions
        val inputShape = intArrayOf(1, 256, 256, 3)
        val inputFeature0 = TensorBuffer.createFixedSize(inputShape, DataType.FLOAT32)
        // Ensure that the byte buffer is in the correct format and size
        val expectedSize = inputFeature0.buffer.capacity()

        if (byteBuffer.capacity() != expectedSize) {
            throw IllegalArgumentException("The size of the byte buffer does not match the expected input shape.")
        }

        // Load the input buffer with the provided byteBuffer
        inputFeature0.loadBuffer(byteBuffer)

        // Run model inference and retrieve the outputs
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val labels = arrayOf(
            "Fresh Beef",
            "Not Fresh Beef",
            "Fresh Chicken",
            "Not Fresh Chicken"
        ) // Replace with your actual labels
        val (predictedLabel, confRate) = getDataFromOutputModel(outputFeature0, labels)
        binding.result.text = "Result : " + predictedLabel
        val convertConfRate = confRate * 100
        binding.confRate.text = "Confidence Rate : " + convertConfRate.toInt() + "%"

        // Release model resources
        model.close()
    }

    private fun getDataFromOutputModel(
        outputTensorBuffer: TensorBuffer,
        labels: Array<String>
    ): Pair<String, Float> {
        val buffer = outputTensorBuffer.buffer
        val maxIndex = findMaxIndex(buffer)
        validateMaxProbability(maxIndex)

        val confidenceRate = buffer.getFloat(maxIndex * Float.SIZE_BYTES)
        val predictedLabel = labels[maxIndex]

        return predictedLabel to confidenceRate
    }

    private fun findMaxIndex(buffer: ByteBuffer): Int {
        buffer.rewind()
        var maxIdx = 0
        var maxProb = Float.NEGATIVE_INFINITY

        for (i in 0 until buffer.limit() / Float.SIZE_BYTES) {
            val value = buffer.float
            if (!value.isNaN()) {
                if (value > maxProb) {
                    maxProb = value
                    maxIdx = i
                }
            }
        }

        return maxIdx
    }

    private fun validateMaxProbability(maxIndex: Int) {
        if (maxIndex == -1) {
            throw IllegalStateException("Unable to find maximum probability in the output tensor.")
        }
    }

    private fun uploadImageToApi(bitmap: Bitmap) {
        val streamImage = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, streamImage)
        val byteArray = streamImage.toByteArray()

        val client = OkHttpClient()
        val requestBody = RequestBody.create("image/png".toMediaTypeOrNull(), byteArray)

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "${UUID.randomUUID()}.png", requestBody)
            .build()

        val req = Request.Builder()
            .url("https://bangkit-2023-c23-ps026-f5nput3awq-et.a.run.app/uploadImage")
            .post(multipartBody)
            .build()

        client.newCall(req).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                // Handle the success case
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.e("RESPONSE BODY", responseBody)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle the failure case
            }
        })
    }
}