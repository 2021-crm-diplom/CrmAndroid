package kz.iitu.diplom.crm.modules.trades.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kz.iitu.diplom.crm.R
import kz.iitu.diplom.crm.core.AvatarWidget

class ClientWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttributeSet: Int = 0)
    : LinearLayout(context, attrs, defStyleAttributeSet) {

    val view = inflate(context, R.layout.client_widget, this)
    private val clientAvatar = view.findViewById<AvatarWidget>(R.id.client_avatar)
    private val clientName = view.findViewById<TextView>(R.id.clientName)
    private val buttonPhone = view.findViewById<ImageView>(R.id.button_phone)
    private val buttonShare = view.findViewById<ImageView>(R.id.button_share)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        clientAvatar.setBackgroundType(AvatarWidget.BgType.CLIENT)
    }

    @SuppressLint("SetTextI18n")
    fun setClientName(firstName: String?, lastName: String?) {
        clientName.text = "$firstName $lastName"
        clientAvatar.setEmployeeLetters(firstName, lastName)
    }

    fun setClientPhone(phone: String?) {
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        val shareText = "${clientName.text}: $phone"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        buttonPhone.setOnClickListener {
            context.startActivity(dialIntent)
        }
        buttonShare.setOnClickListener {
            context.startActivity(shareIntent)
        }
    }
}