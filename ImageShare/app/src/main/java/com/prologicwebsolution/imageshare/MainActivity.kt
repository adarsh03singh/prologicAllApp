package com.prologicwebsolution.imageshare


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*


class MainActivity : AppCompatActivity(){
    var pressed_x:Int = 0
     var pressed_y:Int = 0
    var pressed_x1:Int = 0
    var pressed_y1:Int = 0
    private var mainLayout: ViewGroup? = null
    var img: ImageView? = null
    var tutorialTxt: TextView? = null
    var msg: String? = null
     lateinit var topLayout: LinearLayout
    lateinit var bottomLayout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.main);
        img = findViewById(R.id.imageView)
        tutorialTxt = findViewById(R.id.textView2)
        topLayout = findViewById<LinearLayout>(R.id.topLayout)
        bottomLayout = findViewById<LinearLayout>(R.id.bottomLayout)

//        tutorialTxt!!.setOnTouchListener(this)
//        img!!.setOnTouchListener(this)

//        topLayout.setOnDragListener(dragListener)
//        bottomLayout.setOnDragListener(dragListener)

        tutorialTxt!!.setOnTouchListener(mOnTouchListenerTv1)

/*
        tutorialTxt!!.setOnLongClickListener {
           val clipText = "This is your clipdata text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes,item)

            val dragshadowBuilder = DragShadowBuilder(it)
            it.startDragAndDrop(data,dragshadowBuilder, it, 0)

            it.visibility = INVISIBLE
            true
        }*/


    }

    val mOnTouchListenerTv1: OnTouchListener = object : OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            val layoutParams: LinearLayout.LayoutParams = tutorialTxt!!.getLayoutParams() as LinearLayout.LayoutParams
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
                    tutorialTxt!!.setLayoutParams(layoutParams)

                    // Save where the user's finger was for the next ACTION_MOVE
                    pressed_x = x
                    pressed_y = y
                }
                MotionEvent.ACTION_UP -> Log.d("TAG", "@@@@ tutorialTxt ACTION_UP")
            }
            return true
        }
    }
 /*   val mOnTouchListenerTv2: OnTouchListener = object : OnTouchListener() {
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            val relativeLayoutParams1: RelativeLayout.LayoutParams =
                tv2.getLayoutParams() as RelativeLayout.LayoutParams
            when (event.getActionMasked()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("TAG", "@@@@ TV2 ACTION_DOWN")
                    // Where the user started the drag
                    pressed_x1 = event.getRawX() as Int
                    pressed_y1 = event.getRawY() as Int
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("TAG", "@@@@ TV2 ACTION_MOVE")
                    // Where the user's finger is during the drag
                    val x = event.getRawX() as Int
                    val y = event.getRawY() as Int

                    // Calculate change in x and change in y
                    val dx: Int = x - pressed_x1
                    val dy: Int = y - pressed_y1

                    // Update the margins
                    relativeLayoutParams1.leftMargin += dx
                    relativeLayoutParams1.topMargin += dy
                    tv2.setLayoutParams(relativeLayoutParams1)

                    // Save where the user's finger was for the next ACTION_MOVE
                    pressed_x1 = x
                    pressed_y1 = y
                }
                MotionEvent.ACTION_UP -> Log.d("TAG", "@@@@ TV2 ACTION_UP")
            }
            return true
        }
    }*/



    /*  private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {
          private val shadow = ColorDrawable(Color.LTGRAY)

          override fun onProvideShadowMetrics(size: Point, touch: Point) {
              val width: Int = (view.width / 1.2F).toInt()
              val height: Int = (view.height / 1.2F).toInt()
              //val width: Int = view.width / 2
              //val height: Int = view.height / 2

              shadow.setBounds(0, 0, width, height)
              size.set(width, height)
              touch.set(width / 2, height / 2)
          }
          override fun onDrawShadow(canvas: Canvas) {
              shadow.draw(canvas)
          }
      }

      // Creates a new drag event listener
      private val dragListen = View.OnDragListener { v, event ->
          val receiverView:TextView = v as TextView

          when (event.action) {
              DragEvent.ACTION_DRAG_STARTED -> {
                  if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                      receiverView.setBackgroundColor(Color.CYAN)
                      receiverView.text = "Hold and drag here."
                      v.invalidate()
                      true
                  } else {
                      false
                  }
              }

              DragEvent.ACTION_DRAG_ENTERED -> {
                  receiverView.setBackgroundColor(Color.GREEN)
                  receiverView.text = "Good, put here."
                  v.invalidate()
                  true
              }

              DragEvent.ACTION_DRAG_LOCATION ->
                  true

              DragEvent.ACTION_DRAG_EXITED -> {
                  receiverView.setBackgroundColor(Color.YELLOW)
                  receiverView.text = "Oh! you exited."
                  v.invalidate()
                  true
              }

              DragEvent.ACTION_DROP -> {
                  val item: ClipData.Item = event.clipData.getItemAt(0)
                  val dragData = item.text
                  receiverView.text = "You dropped : $dragData"
                  v.invalidate()
                  true
              }

              DragEvent.ACTION_DRAG_ENDED -> {
                  receiverView.setBackgroundColor(Color.WHITE)
                  v.invalidate()


                  when(event.result) {
                      true ->
                          // drop was handled
                          receiverView.setBackgroundColor(Color.WHITE)
                      else ->{
                          // drop didn't work
                          receiverView.text = "Drop failed."
                          receiverView.setBackgroundColor(Color.RED)
                      }
                  }

                  // returns true; the value is ignored.
                  true
              }

              else -> {
                  // An unknown action type was received.
                  false
              }
          }
      }
  */


       /* img!!.setOnLongClickListener(OnLongClickListener { v ->
            val item = ClipData.Item(v.tag as CharSequence)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val dragData = ClipData(v.tag.toString(), mimeTypes, item)
            val myShadow = DragShadowBuilder(img)
            v.startDrag(dragData, myShadow, null, 0)
            true
        })
        img!!.setOnDragListener(OnDragListener { v, event ->

            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    layoutParams = v.layoutParams as RelativeLayout.LayoutParams
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED")
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED")
                     x_cord = event.x.toInt()
                     y_cord = event.y.toInt()
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED")
                    x_cord = event.x.toInt()
                    y_cord = event.y.toInt()
                    layoutParams!!.leftMargin = x_cord!!
                    layoutParams!!.topMargin = y_cord!!
                    v.layoutParams = layoutParams
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION")
                    x_cord = event.x.toInt()
                    y_cord = event.y.toInt()
                }
                DragEvent.ACTION_DRAG_ENDED -> Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED")
                DragEvent.ACTION_DROP -> Log.d(msg, "ACTION_DROP event")
                else -> {
                }
            }
            true
        })

        img!!.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = DragShadowBuilder(img)
                img!!.startDrag(data, shadowBuilder, img, 0)
                img!!.setVisibility(INVISIBLE)
                true
            } else {
                false
            }
        })*/
//    }


}
