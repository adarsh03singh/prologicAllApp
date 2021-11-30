package com.prologicwebsolution.eventshare.ui.imageShare

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.adapter.ColorAdapter
import com.prologicwebsolution.eventshare.adapter.TextSizeAdapter
import com.prologicwebsolution.eventshare.databinding.FragmentImageListfragmentBinding
import com.prologicwebsolution.eventshare.databinding.FragmentImageShareBinding
import com.prologicwebsolution.eventshare.ui.imageList.ImageListViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class ImageShareFragment : Fragment() {


    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    lateinit var rootLayout: ViewGroup

    lateinit var nametxt: TextView
    lateinit var positionTxt: TextView
    lateinit var numbertxt: TextView
    lateinit var emailTxt: TextView
    lateinit var Img: ImageView

    lateinit var closeColorlayoutButton: ImageView
    lateinit var closeSizelayoutButton: ImageView
    lateinit var buttonShare: Button
    lateinit var changeColorbutton: Button
    lateinit var resetAllButton: Button
    lateinit var changetextSizeButton: Button
    lateinit var colorLayout: LinearLayoutCompat
    lateinit var sizeLayout: LinearLayoutCompat
    lateinit var buttonLayout: ConstraintLayout

//for dragging
    var pressed_x:Int = 0
    var pressed_y:Int = 0

    lateinit var viewmodel: ImageShareViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_share, container, false)
        val binding: FragmentImageShareBinding = FragmentImageShareBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(ImageShareViewModel::class.java)
        binding.imageShareiewModel = viewmodel
        binding.lifecycleOwner = this

        // use for support file after 24 version of android
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting ids of views
        rootLayout = view.findViewById<ViewGroup>(R.id.view_root)
//        constraintLayout = findViewById<LinearLayoutCompat>(R.id.parent_layout)
        nametxt = view.findViewById<TextView>(R.id.userNameId)
//        positionTxt = findViewById<TextView>(R.id.tvPosition)
        numbertxt = view.findViewById<TextView>(R.id.phoneNumberid)
        emailTxt = view.findViewById<TextView>(R.id.emailId)
        Img = view.findViewById<ImageView>(R.id.imgId)

        closeColorlayoutButton = view.findViewById(R.id.closeImageButton)
        buttonShare = view.findViewById(R.id.buttonSubmit)
        changeColorbutton = view.findViewById(R.id.changeColorButton)
        resetAllButton = view.findViewById(R.id.resetButton)
        changetextSizeButton = view.findViewById(R.id.changeTextSize)
        closeSizelayoutButton = view.findViewById(R.id.sizeCloseImageButton)
        colorLayout = view.findViewById(R.id.colorlayout)
        buttonLayout = view.findViewById(R.id.buttonLayout)
        sizeLayout = view.findViewById(R.id.textSizelayout)

        // getting imgId by previous fragment
        val imagePosition = arguments?.getInt("imagePosition")
//        val imageRes:String = "0"


        // getting data from sharedPreference
        val sharedPreference = requireContext().getSharedPreferences("demo_data", Context.MODE_PRIVATE)
        val nameValue = sharedPreference.getString("name", "")
        val positionValue = sharedPreference.getString("position", "")
        val numberValue = sharedPreference.getString("number", "")
        val emailValue = sharedPreference.getString("email", "")

        Log.d("Tag", emailValue.toString())

        nametxt.text = nameValue
//        positionTxt.text = positionValue
        numbertxt.text = numberValue
        emailTxt.text = emailValue

        checkPermissions()

        buttonShare.setOnClickListener {
            checkPermissions()
            if (checkPermissions() == true) {
                share()
            }
        }

        changeColorbutton.setOnClickListener {
//          settextColor()
            if (colorLayout.visibility == View.GONE) {
                colorLayout.visibility = View.VISIBLE
                buttonLayout.visibility = View.GONE
            }
        }
        closeColorlayoutButton.setOnClickListener {
            if (colorLayout.visibility == View.VISIBLE) {
                colorLayout.visibility = View.GONE
                buttonLayout.visibility = View.VISIBLE
            }
        }

        resetAllButton.setOnClickListener {
            resetAll()

        }
        changetextSizeButton.setOnClickListener {
            if (sizeLayout.visibility == View.GONE) {
                sizeLayout.visibility = View.VISIBLE
                buttonLayout.visibility = View.GONE
            }
        }


        closeSizelayoutButton.setOnClickListener {
            if (sizeLayout.visibility == View.VISIBLE) {
                sizeLayout.visibility = View.GONE
                buttonLayout.visibility = View.VISIBLE
            }
        }

        when (imagePosition.toString()) {
            "0" ->
                Img.setImageDrawable(resources.getDrawable(R.drawable.one))

            "1" ->
                Img.setImageDrawable(resources.getDrawable(R.drawable.two))

            "2" ->

                Img.setImageDrawable(resources.getDrawable(R.drawable.three))

            "3" ->

                Img.setImageDrawable(resources.getDrawable(R.drawable.four))

            "4" ->

                Img.setImageDrawable(resources.getDrawable(R.drawable.five))
        }
        changeTextSize()
        changeColor()
        dragableMethod()
//        colorLayoutVisibility()
//        setParams()

    }

    @SuppressLint("ClickableViewAccessibility")
    fun dragableMethod() {

//        val constraintLayout = ConstraintLayout.LayoutParams(150, 150)
//        nametxt.layoutParams = constraintLayout
//        nametxt.setOnTouchListener(ChoiceTouchListener())

        var listener = View.OnTouchListener(function = { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.y = motionEvent.rawY - view.height / 2
                view.x = motionEvent.rawX - view.width / 2
            }
            true
        })
//        Img.setOnTouchListener(listener)
        nametxt.setOnTouchListener(mOnTouchListenerTv1)
        numbertxt.setOnTouchListener(listener)
        emailTxt.setOnTouchListener(listener)

    }

    val mOnTouchListenerTv1: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            val layoutParams: ConstraintLayout.LayoutParams = nametxt.getLayoutParams() as ConstraintLayout.LayoutParams
            when (event.getActionMasked()) {
                MotionEvent.ACTION_DOWN -> {
                    // Where the user started the drag
                    pressed_x = event.getRawX().toInt()
                    pressed_y = event.getRawY().toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    // Where the user's finger is during the drag
                    val x = event.getRawX().toInt()
                    val y = event.getRawY().toInt()

                    // Calculate change in x and change in y
                    val dx: Int = x - pressed_x
                    val dy: Int = y - pressed_y

                    // Update the margins
                    layoutParams.leftMargin += dx
                    layoutParams.topMargin += dy
                    nametxt.setLayoutParams(layoutParams)

                    // Save where the user's finger was for the next ACTION_MOVE
                    pressed_x = x
                    pressed_y = y
                }
                MotionEvent.ACTION_UP -> Log.d("TAG", "@@@@ tutorialTxt ACTION_UP")
            }
            return true
        }
    }


    @SuppressLint("SetWorldReadable", "ClickableViewAccessibility")
    private fun share() {

        val bitmap: Bitmap = getBitmapFromView(rootLayout)
        try {
            val file = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )

// wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
            val bmpUri = FileProvider.getUriForFile(requireContext(), "com.codepath.file_provider", file)
//           val file = File(externalCacheDir.toString() + "/" + "prologicWebSolution" + ".png")
            val fout = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
            fout.flush()
            fout.close()
            file.setReadable(true, false)
            //for the saving images in gallery
            MediaStore.Images.Media.insertImage(
                requireContext().getContentResolver(),
                bitmap,
                "Prologic welcome page",
                "Prologic"
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri)
            intent.setType("image/png")
            startActivity(Intent.createChooser(intent, "share by"))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawable: Drawable = view.background

        if (drawable != null) {
            drawable.draw(canvas)
        } else {
            canvas.drawColor(android.R.color.white)
        }
        view.draw(canvas)

        return bitmap

    }

    //use for check camera and gallery permission
    private fun checkPermissions(): Boolean {

        /*   val camera = ContextCompat.checkSelfPermission(
               this,
               Manifest.permission.CAMERA
           )*/
        val wtite =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        /*   if (camera != PackageManager.PERMISSION_GRANTED) {
               listPermissionsNeeded.add(Manifest.permission.CAMERA)
           }*/
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true

    }

    fun resetAll() {
        nametxt.textSize = 10F
        nametxt.setTextColor(getResources().getColor(R.color.black));
        nametxt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.black))

        numbertxt.textSize = 10F
        numbertxt.setTextColor(getResources().getColor(R.color.black));
        numbertxt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.black))

        emailTxt.textSize = 10F
        emailTxt.setTextColor(getResources().getColor(R.color.black));
        emailTxt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.black))

//        requireActivity().recreate()
        findNavController().navigate(
            R.id.imageShareFragment,
            arguments,
            NavOptions.Builder()
                .setPopUpTo(R.id.imageShareFragment, true)
                .build()
        )

    }

    fun changeColor() {
        //getting recyclerview from xml
        val recyclerView = requireView().findViewById(R.id.colorRecyclerView) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        //crating an arraylist to store colorList using the data class ColorModel
        val colorList = ArrayList<ColorModel>()

        //adding some dummy data to the list
        colorList.add(ColorModel(Color.WHITE))
        colorList.add(ColorModel(Color.BLACK))
        colorList.add(ColorModel(Color.BLUE))
        colorList.add(ColorModel(Color.GRAY))
        colorList.add(ColorModel(Color.DKGRAY))
        colorList.add(ColorModel(Color.RED))
        colorList.add(ColorModel(Color.GREEN))
        colorList.add(ColorModel(Color.LTGRAY))
        colorList.add(ColorModel(Color.CYAN))
        colorList.add(ColorModel(Color.YELLOW))
        colorList.add(ColorModel(Color.MAGENTA))

        //creating our adapter
        val adapter = ColorAdapter(colorList) { position ->
            if (!position.equals(null)) {
                settextColor(position)
            }
        }

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }

    fun settextColor(positionColor: Int) {

        nametxt.setTextColor(positionColor)
        nametxt.getCompoundDrawables()[0].setTint(positionColor)

        numbertxt.setTextColor(positionColor)
        numbertxt.getCompoundDrawables()[0].setTint(positionColor)

        emailTxt.setTextColor(positionColor)
        emailTxt.getCompoundDrawables()[0].setTint(positionColor)
    }

    fun changeTextSize() {
        //getting recyclerview from xml
        val recyclerView = requireView().findViewById(R.id.sizeRecyclerView) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        //crating an arraylist to store colorList using the data class ColorModel
        val sizeList = ArrayList<SizeModel>()

        //adding some dummy data to the list
        sizeList.add(SizeModel(10))
        sizeList.add(SizeModel(12))
        sizeList.add(SizeModel(14))
        sizeList.add(SizeModel(16))
        sizeList.add(SizeModel(18))
        sizeList.add(SizeModel(20))
        sizeList.add(SizeModel(22))
        sizeList.add(SizeModel(24))
        sizeList.add(SizeModel(26))
        sizeList.add(SizeModel(28))
        sizeList.add(SizeModel(30))
        sizeList.add(SizeModel(32))
        sizeList.add(SizeModel(34))

        //creating our adapter
        val adapter = TextSizeAdapter(sizeList) { position ->
            if (!position.equals(null)) {
                settextSize(position)
            }
        }

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }

    fun settextSize(positionSize: Int) {

        nametxt.textSize = positionSize.toFloat()
        numbertxt.textSize = positionSize.toFloat()
        emailTxt.textSize = positionSize.toFloat()

    }


    data class ColorModel(val buttonColor: Int)
    data class SizeModel(val textSize: Int)

}