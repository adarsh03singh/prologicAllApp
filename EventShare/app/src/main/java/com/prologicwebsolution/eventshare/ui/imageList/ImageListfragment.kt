package com.prologicwebsolution.eventshare.ui.imageList


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prologicwebsolution.eventshare.R
import com.prologicwebsolution.eventshare.adapter.ImageAdapter
import com.prologicwebsolution.eventshare.adapter.ImageCategoryAdapter
import com.prologicwebsolution.eventshare.databinding.FragmentImageListfragmentBinding


class ImageListfragment : Fragment() {


    lateinit var viewmodel: ImageListViewModel
    private val itemList: Array<String>
        get() = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6","Item 7")

    lateinit var gridPosition: String
    lateinit var dateButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_listfragment, container, false)
        val binding: FragmentImageListfragmentBinding = FragmentImageListfragmentBinding.bind(view)
        viewmodel = ViewModelProviders.of(this.requireActivity()).get(ImageListViewModel::class.java)
        binding.imageListViewModel = viewmodel
        binding.lifecycleOwner = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateButton = view.findViewById(R.id.dateButton)

        setDataInCategoryrecyclerView()
        showAllImagesInGridView()

        dateButton.setOnClickListener {
           findNavController().navigate(R.id.festivalImageFragment)
        }
    }

    fun showAllImagesInGridView(){
        val gvImages: GridView = requireView().findViewById(R.id.gvImages)

        val adapter = ImageAdapter(requireContext())
        gvImages.adapter = adapter
        gvImages.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->


            gridPosition = position.toString()
//            goToImageActivitydialog()
            val bundle = bundleOf("imagePosition" to position)
            findNavController().navigate(R.id.imageShareFragment, bundle)
            Toast.makeText(
                requireContext(), " Clicked Position: " + (position + 1),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private  fun setDataInCategoryrecyclerView(){
        //getting recyclerview from xml
        val recyclerView = requireView().findViewById(R.id.categoryRecyclerview) as RecyclerView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        //crating an arraylist to store categoryList using the data class ImageCategoryModel
        val categoryList = ArrayList<ImageCategoryModel>()

        //adding some dummy data to the list
        categoryList.add(ImageCategoryModel("GSt"))
        categoryList.add(ImageCategoryModel("Insurance"))
        categoryList.add(ImageCategoryModel("ITR"))
        categoryList.add(ImageCategoryModel("Insurance"))
        categoryList.add(ImageCategoryModel("gst"))
        categoryList.add(ImageCategoryModel("Itr"))
        categoryList.add(ImageCategoryModel("Licence"))
        categoryList.add(ImageCategoryModel("Insurance"))
        categoryList.add(ImageCategoryModel("ITR"))
        categoryList.add(ImageCategoryModel("Insurance"))

        //creating our adapter
        val adapter = ImageCategoryAdapter(categoryList)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }

    data class ImageCategoryModel(val categoryName: String)

    fun goToImageActivitydialog() {
        val mDialogView =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.recipient_dialog_item, null)

        var radioButtonvalue: String? = null

        val cancelButton = mDialogView.findViewById<TextView>(R.id.tvCancel)
        val okButton = mDialogView.findViewById<TextView>(R.id.tvOk)
        val recipientName = mDialogView.findViewById<EditText>(R.id.evRecipientName)
        val radioGroup = mDialogView.findViewById<RadioGroup>(R.id.radioGroup)
        var radioButton: RadioButton


        val mBuilder = android.app.AlertDialog.Builder(requireContext()).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        radioGroup?.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                radioButton = radioGroup.findViewById(checkedId)
                val index: Int = radioGroup!!.indexOfChild(radioButton)
                when (index) {
                    0 ->
                        radioButtonvalue = radioButton.text.toString()
                    1 ->
                        radioButtonvalue = radioButton.text.toString()
                    2 ->
                        radioButtonvalue = radioButton.text.toString()
                }
            }
        })

        cancelButton?.setOnClickListener {
            mAlertDialog.dismiss()
        }

        okButton?.setOnClickListener {
//            if (recipientName.text.toString().equals("")) {
//                Toast.makeText(
//                    this.getApplication(),
//                    "Please Enter recipient name", Toast.LENGTH_LONG).show()
//            } else{
//            val intent = Intent(this, ImageShareActivity::class.java)
//            intent.putExtra("recipient_name", recipientName.text.toString())
//            intent.putExtra("recipient_head", radioButtonvalue)
//            intent.putExtra("image_res", gridPosition)
//            startActivity(intent)

//            }
        }

    }
}