package kz.iitu.diplom.crm.modules.profile.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kz.iitu.diplom.crm.R


class ProfileAvatarWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : FrameLayout(context, attrs, defStyleAttributeSet) {

    val view = inflate(context, R.layout.profile_avatar_widget, this)
}