package nolambda.stream.circle

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object CircleHolder {

    lateinit var context: Context

    /**
     * Initializer for objects holder
     */
    fun init(
        context: Context
    ) {
        this.context = context.applicationContext
    }
}