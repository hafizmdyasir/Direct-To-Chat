package com.hamohdy.whatsappdirect

import android.content.Intent
import android.net.Uri
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

//this is used repeatedly. We might as well create a field for.
val deviceDefaultCountry = getDefaultCountry()

fun detectCountry(phoneNumber: String): Country? {
    countries.forEach {
        if (phoneNumber.replace("+", "").startsWith(it.isdCode)) return it
    }
    return null
}

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