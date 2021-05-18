package kz.iitu.diplom.crm.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import kz.iitu.diplom.crm.R

class AvatarWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : FrameLayout(context, attrs, defStyleAttributeSet) {

    private val frame: FrameLayout
    private val bg: ImageView
    private val fgText: TextView

    init {
        val view = inflate(context, R.layout.avatar_widget, this)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        frame = view.findViewById(R.id.frame)
        bg = view.findViewById(R.id.bg)
        fgText = view.findViewById(R.id.fgText)
        var type: Int? = null
        var bgType: Int? = null
        context.theme.obtainStyledAttributes(attrs, R.styleable.AvatarWidget,0, 0).apply {
            try {
                type = getInteger(R.styleable.AvatarWidget_type, 0)
                bgType = getInteger(R.styleable.AvatarWidget_bg, 0)
            } finally {
                recycle()
            }
        }
        when (type) {
            Type.MINI.alias -> applyTypeMini()
            else -> applyTypeNormal()
        }
        when(bgType) {
            BgType.CLIENT.alias -> setBackgroundType(BgType.CLIENT)
            else -> setBackgroundType(BgType.EMPLOYEE)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setEmployeeLetters(firstName: String?, lastName: String?) {
        fgText.text = "${firstName?.get(0)}${lastName?.get(0)}"
    }

    fun setBackgroundType(type: BgType) {
        when {
            type == BgType.CLIENT -> bg.background = ContextCompat.getDrawable(context, R.drawable.profile_avatar_2)
            else -> bg.background = ContextCompat.getDrawable(context, R.drawable.profile_avatar)
        }
    }

    private fun applyTypeMini() {
        val size = context.resources.getDimension(R.dimen.fab_mini_size).toInt()
        frame.layoutParams = LayoutParams(size, size)
        fgText.setTextSize(12f)
    }

    private fun applyTypeNormal() {
        val size = context.resources.getDimension(R.dimen.fab_normal_size).toInt()
        frame.layoutParams = LayoutParams(size, size)
        fgText.setTextSize(16f)
    }

    private enum class Type(val alias: Int) {
        MINI(0),
        NORMAL(1)
    }

    enum class BgType(val alias: Int) {
        EMPLOYEE(0),
        CLIENT(1)
    }
}

@BindingAdapter("firstName", "lastName")
fun AvatarWidget.setEmployee(firstName: String, lastName: String) {
    this.setEmployeeLetters(firstName, lastName)
}