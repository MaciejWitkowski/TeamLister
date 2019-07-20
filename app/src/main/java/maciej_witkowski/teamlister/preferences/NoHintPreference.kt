package maciej_witkowski.teamlister.preferences

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import androidx.preference.EditTextPreference



internal class NoHintPreference : EditTextPreference, EditTextPreference.OnBindEditTextListener {
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onBindEditText(editText: EditText) {
        editText.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    override fun getOnBindEditTextListener(): OnBindEditTextListener? {
        return this
    }
}