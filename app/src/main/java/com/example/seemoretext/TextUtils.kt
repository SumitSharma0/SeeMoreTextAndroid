package com.example.seemoretext

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class TextUtils {

    lateinit var seeMoreSpan: Spannable
    lateinit var seeLessSpan: Spannable
    var actionDisplayText: String = ""

    fun addSeeMoreText(textView: TextView) {
        val description = textView.text
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = description

        val seeMoreClickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                textView.text = TextUtils.concat(description, seeLessSpan)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                setDrawState(textView.context, ds)
            }
        }
        val seeLessClickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                textView.text = TextUtils.concat(actionDisplayText, seeMoreSpan)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                setDrawState(textView.context, ds)
            }
        }

        seeMoreSpan = SpannableString(textView.context.resources.getString(R.string.see_more)).apply {
            setSpan(
                seeMoreClickable,
                0,
                textView.context.resources.getString(R.string.see_more).length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        seeLessSpan = SpannableString(textView.context.resources.getString(R.string.see_less)).apply {
            setSpan(
                seeLessClickable,
                0,
                textView.context.resources.getString(R.string.see_less).length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }


        textView.post {
            if (textView.lineCount > 3) {
                val lastCharShown = textView.layout.getLineVisibleEnd(2)
                actionDisplayText =
                    description.substring(0, lastCharShown - seeMoreSpan.length - 3) + "..."
                textView.text = TextUtils.concat(actionDisplayText, seeMoreSpan)
            }
        }
    }


    private fun setDrawState(context: Context, ds: TextPaint) {
        ds.isUnderlineText = false
//        ResourcesCompat.getFont(context, R.font.roboto_bold)?.let { ds.typeface = it }
        ds.color = ContextCompat.getColor(context, R.color.dark_yellow)
    }
}