package com.example.androidcourse.infra

import android.content.res.Resources
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.androidcourse.BuildConfig
import timber.log.Timber

fun Fragment.logFragmentHierarchy(tag: String? = null) {
    if (BuildConfig.DEBUG) {
        StringBuilder("Nested fragments: ").let {
            fragmentHierarchy(it)
            val timber =
                if (tag == null) Timber.asTree()
                else Timber.tag(tag)
            timber.d(it.toString())
        }
    }
}

private fun Fragment.getNameWithArgs(stringBuilder: StringBuilder) {
    stringBuilder.append(javaClass.simpleName)
    arguments
        ?.toString()
        ?.removePrefix("Bundle[{")
        ?.removeSuffix("}]")
        ?.also { stringBuilder.append("($it)") }
}

private fun Fragment.fragmentHierarchy(stringBuilder: StringBuilder) {
    parentFragment.let { parentFragment ->
        if (parentFragment != null) {
            parentFragment.fragmentHierarchy(stringBuilder)
            stringBuilder.append(" -> ")
        } else {
            activity?.let { activity ->
                stringBuilder.append("${activity.javaClass.simpleName} -> ")
            }
        }
    }
    getNameWithArgs(stringBuilder)
    if (childFragmentManager.backStackEntryCount > 0) {
        stringBuilder.append(" (stack size: ${childFragmentManager.backStackEntryCount})")
    }
}

fun NavController.logBackstack(tag: String? = null) {
    val timber =
        if (tag == null) Timber.asTree()
        else Timber.tag(tag)
    timber.d(backQueue.joinToString(" -> ", "Backstack: ") { it.destination.displayName })
}

fun Resources.getSpannedString(@StringRes resId: Int, vararg formatArgs: CharSequence): Spanned {
    var lastArgIndex = 0
    val spannableStringBuilder = SpannableStringBuilder(getString(resId, *formatArgs))
    for (arg in formatArgs) {
        val argString = arg.toString()
        lastArgIndex = spannableStringBuilder.indexOf(argString, lastArgIndex)
        if (lastArgIndex != -1) {
            spannableStringBuilder.replace(lastArgIndex, lastArgIndex + argString.length, arg)
            lastArgIndex += argString.length
        }
    }
    return spannableStringBuilder
}