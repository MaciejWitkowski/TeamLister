package maciej_witkowski.teamlister.preferences

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.EditText
import androidx.preference.EditTextPreference
import android.text.InputType



internal class FileNamePreference : EditTextPreference, EditTextPreference.OnBindEditTextListener {
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
        editText.inputType =InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        editText.filters = arrayOf(InputFilter { src, _, _, _, _, _ ->
            if (src == "") {
                return@InputFilter src
            }
            if (src.toString().matches("[a-zA-Z0-9_]+".toRegex())) {
                src
            } else ""
        })
    }

    override fun getOnBindEditTextListener(): EditTextPreference.OnBindEditTextListener? {
        return this
    }
}