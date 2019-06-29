package bohdan.sushchak.productsdetector.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import bohdan.sushchak.productsdetector.R
import kotlinx.android.synthetic.main.detected_item.view.*

@SuppressLint("ViewConstructor")
class DetectedItem(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.detected_item, this)

        attrs?.let {
            val attributes = context.theme
                .obtainStyledAttributes(attrs, R.styleable.DetectedItem, 0, 0)
            try {
                initAttrs(attributes)
            } finally {
                attributes.recycle()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initAttrs(attrs: TypedArray) {
        val backgroundColor = attrs.getColor(R.styleable.DetectedItem_backgroundColor, Color.TRANSPARENT)
        var backgroundDrawable = attrs.getDrawable(R.styleable.DetectedItem_background)
        val label = attrs.getString(R.styleable.DetectedItem_label)
        val accuracy = attrs.getString(R.styleable.DetectedItem_accuracy)

        if (backgroundDrawable == null)
            backgroundDrawable = context.getDrawable(R.drawable.detected_item_bg)

        backgroundDrawable?.setColorFilter(backgroundColor, PorterDuff.Mode.OVERLAY)
        rootView.background = backgroundDrawable

        tvDetectedItemName.text = label
        tvDetectedItemAccuracy.text = if (accuracy != null) "$accuracy%" else ""
    }

    fun setOnButtonClickListener(onClickListener: OnClickListener) {
        iBtnAddItem.setOnClickListener(onClickListener)
    }

    var label: String
        get() = tvDetectedItemName.text.toString()
        set(value) {
            tvDetectedItemName.text = value
        }

    var accuracy: String
        get() = tvDetectedItemAccuracy.text.toString()
        set(value) {
            tvDetectedItemAccuracy.text = value
        }
}