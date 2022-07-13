package com.hamohdy.whatsappdirect

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import java.util.*

//try to match the device's country code with a country code in our list.
private fun getDefaultCountry(): Country? {

    val deviceCountryCode = Locale.getDefault().country

    //see if any iso code matches device iso code
    for (country in countries) {
        if (country.isoCode.lowercase() == deviceCountryCode.lowercase()) return country
    }

    return null
}

//this is used repeatedly. We might as well create a field for it.
val deviceDefaultCountry = getDefaultCountry()

//extension function on Context to create a toast.
fun Context.createToast(@StringRes messageResource: Int) {
    Toast.makeText(this, messageResource, Toast.LENGTH_SHORT).show()
}

//detect a country using the ISD code in the beginning.
fun detectCountry(phoneNumber: String): Country? {
    countries.forEach {
        if (phoneNumber.replace("+", "").startsWith(it.isdCode)) return it
    }
    return null
}

//generate launch intent with the given parameters
fun getLaunchIntent(phoneNumber: String, message: String, business: Boolean): Intent {

    val total = "https://api.whatsapp.com/send?phone=" +
            phoneNumber.replace("+", "") +
            "&text=${message}"

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(total)
        `package` = if (business) "com.whatsapp.w4b" else "com.whatsapp"
    }
    return intent
}

//Extension function on Intent that launches if the required app is installed or shows
//a toast to inform that the desired application is not installed.
fun Intent.launchIfResolved(context: Context) {
    if (resolveActivity(context.packageManager) == null) context.createToast(R.string.not_installed)
    else context.startActivity(this)
}