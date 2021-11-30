package com.prologicwebsolution.microatm.customview

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.prologicwebsolution.microatm.R

/**
 * Created by tanvi.hirare on 17-05-2017.
 */
class LoadingSpinner : AppCompatImageView {
    constructor(context: Context?) : super(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageResource(IMAGE_RESOURCE_ID)
        startAnimation()
    }

    fun startAnimation() {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate))
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            startAnimation()
        } else {
            clearAnimation()
        }
    }

    companion object {
        private const val IMAGE_RESOURCE_ID = R.drawable.ic_spinner
    }
}