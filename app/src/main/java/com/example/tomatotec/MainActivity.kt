package com.example.tomatotec

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tomatotec.databinding.ActivityMainBinding
import com.example.tomatotec.ml.Model
import com.google.android.material.navigation.NavigationView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FragmentHome.OnFragmentInteractionListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager

    private val imageSize = 224
    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.red)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.bringToFront()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, FragmentHome()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        fragmentManager = supportFragmentManager
        openFragment(FragmentHome())

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> openFragment(FragmentHome())
                R.id.severity -> openFragment(FragmentSeverity())
            }
            true
        }

        binding.fab.setOnClickListener {
            showBottomDialog()
        }
    }

    private fun openFragment(fragment: Fragment) {
        Log.d("MainActivity", "Replacing fragment with ${fragment.javaClass.simpleName}")
        try {
            fragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error replacing fragment: ${e.message}")
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val scanTomatoBtnLayout = dialog.findViewById<LinearLayout>(R.id.scanTomatoBtnLayout)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // camera or gallery image selection option
        scanTomatoBtnLayout.setOnClickListener {
            dialog.dismiss()

            if (hasPermission()) {
                openImageOption()
            } else {
                requestPermission()
            }
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    // CNN Algorithm for predicting and analyzing tomato plant
    private fun classifyImage(image: Bitmap?) {
        try {
            val cnnModel = Model.newInstance(applicationContext)

            // Resize the image to match the input size expected by the model
            val resizedImage = resizeImage(image!!, imageSize)

            // Normalize pixel values and store them in the input tensor buffer
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            val imageBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * 3)
            imageBuffer.order(ByteOrder.nativeOrder())

            // Convert the resized image to byte array
            val pixels = IntArray(imageSize * imageSize)
            resizedImage?.getPixels(pixels, 0, resizedImage.width, 0, 0, resizedImage.width, resizedImage.height)


            for (pixelValue in pixels) {
                // Extract RGB components from the pixel value
                val red = Color.red(pixelValue)
                val green = Color.green(pixelValue)
                val blue = Color.blue(pixelValue)

                // Normalize the pixel values and add them to the buffer
                imageBuffer.put((red and 0xFF).toByte())
                imageBuffer.put((green and 0xFF).toByte())
                imageBuffer.put((blue and 0xFF).toByte())
            }

            inputFeature0.loadBuffer(imageBuffer)

            // Runs model inference and gets result.
            val outputs = cnnModel.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val classes = arrayOf("Healthy","Bacterial Wilt")
            val predictedClass = classes[maxPos]

            if(predictedClass == "Bacterial Wilt") {
                // Open intent Activity
                val intent = Intent(this, BWResultActivity::class.java)
                intent.putExtra("predictedClass",predictedClass)
                startActivity(intent)
            } else if(predictedClass == "Healthy"){
                Toast.makeText(this, "Your Tomato is Healthy!", Toast.LENGTH_SHORT).show()

                val showPopUp = FragmentTomatoHealthy()
                showPopUp.show(supportFragmentManager, "FragmentTomatoHealthy")
            }

            cnnModel.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    // camera and gallery permission requests
    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun openImageOption() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setFixAspectRatio(true) // Enforce fixed aspect ratio
            .setAllowFlipping(false) // Disable flipping
            .setAllowRotation(false) // Disable rotation
            .start(this)
    }

    // image resize function
    private fun resizeImage(image: Bitmap?, targetSize: Int): Bitmap? {
        if (image == null) return null
        val width = image.width
        val height = image.height
        val aspectRatio = width.toFloat() / height.toFloat()
        val targetWidth: Int
        val targetHeight: Int
        if (width > height) {
            targetWidth = targetSize
            targetHeight = (targetSize / aspectRatio).toInt()
        } else {
            targetHeight = targetSize
            targetWidth = (targetSize * aspectRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, targetWidth, targetHeight, true)
    }


    override fun navigateToFragmentSeverity() {
        openFragment(FragmentSeverity())
        binding.bottomNavigationView.selectedItemId = R.id.severity
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                openFragment(FragmentHome())
                binding.bottomNavigationView.selectedItemId = R.id.home
            }
            R.id.nav_scan -> {
                showBottomDialog()
            }
            R.id.nav_severity -> {
                openFragment(FragmentSeverity())
                binding.bottomNavigationView.selectedItemId = R.id.severity
            }
            R.id.nav_about -> {
                val intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_reference -> {
                val intent = Intent(this, ReferencesActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImageUri = result.uri
                // Load the cropped image and pass it to the classifyImage function
                val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, croppedImageUri)
                // Resize the image while maintaining aspect ratio
                val resizedImage = resizeImage(imageBitmap, imageSize)
                // Pass the resized image to the classifyImage function
                classifyImage(resizedImage)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                // Handle crop error
                Toast.makeText(this, "Crop failed: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}