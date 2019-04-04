package com.yabu.httprelayer

import android.content.res.Resources
import android.view.View

/**
 * Shows this [View] with [View.VISIBLE].
 * @receiver [View]
 */
fun View.show(): View {
    alpha = 1f
    this.visibility = View.VISIBLE
    return this
}

/**
 * Shows this [View] with [View.VISIBLE] animated.
 * @receiver [View]
 */
fun View.showAnimated(): View {
    if (this.visibility != View.VISIBLE) {
        this.alpha = 0f
        this.visibility = View.VISIBLE
        this.animate().alpha(1f).start()
    }
    return this
}

/**
 * Shows this [View] with [View.VISIBLE] animated with a 200f translation.
 * @receiver [View]
 */
fun View.showAnimatedWithTranslation(): View {
    this.alpha = 0f
    this.translationY = 200f
    this.visibility = View.VISIBLE
    this.animate().alpha(1f).translationYBy(-200f).start()
    return this
}

/**
 * Shows this [View] with [View.VISIBLE] animated with a 200f translation.
 * @receiver [View]
 */
fun View.showAnimatedWithTranslationWithEndAction(action: () -> Unit): View {
    this.alpha = 0f
    this.translationY = 200f
    this.visibility = View.VISIBLE
    this.animate().alpha(1f).translationYBy(-200f).withEndAction(action).start()
    return this
}

/**
 * Hides this [View] with [View.INVISIBLE].
 * @receiver [View]
 */
fun View.hide(): View {
    this.visibility = View.INVISIBLE
    return this
}

/**
 * Hides this [View] with [View.INVISIBLE] animated.
 * @receiver [View]
 */
fun View.hideAnimated(): View {
    this.animate().alpha(0f).withEndAction { this.visibility = View.INVISIBLE }.start()
    return this
}

/**
 * Hides this [View] with [View.INVISIBLE] animated with a 200f translation.
 * @receiver [View]
 */
fun View.hideAnimatedWithTranslation(): View {
    this.animate().alpha(0f).withEndAction { this.visibility = View.INVISIBLE }.translationY(200f).start()
    return this
}

/**
 * Hides this [View] with [View.GONE].
 * @receiver [View]
 */
fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

/**
 * Hides this [View] with [View.GONE] animated.
 * @receiver [View]
 */
fun View.goneAnimated(): View {
    this.animate().alpha(0f).withEndAction { this.visibility = View.GONE }.start()
    return this
}

/**
 * Hides this [View] with [View.GONE] animated with a 200f translation.
 * @receiver [View]
 */
fun View.goneAnimatedWithTranslation(): View {
    this.animate().alpha(0f).withEndAction { this.visibility = View.GONE }.translationY(200f).start()
    return this
}

/**
 * Helper to get screen dimensions in [Int] pixels.
 * @return a [Pair] with width FIRST, height SECOND.
 */
val screenDimension
    get() = Pair(Resources.getSystem().displayMetrics.widthPixels, Resources.getSystem().displayMetrics.heightPixels)