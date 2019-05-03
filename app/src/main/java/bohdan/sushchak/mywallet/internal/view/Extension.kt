package bohdan.sushchak.mywallet.internal.view

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import bohdan.sushchak.mywallet.R

fun View.startFadeInAnimation(context: Context?) {
    context.let {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_fade_in)
        this.startAnimation(anim)
    }
}