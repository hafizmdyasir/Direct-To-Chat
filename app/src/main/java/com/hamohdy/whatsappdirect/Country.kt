package com.hamohdy.whatsappdirect

/**A data class to hold data about countries.
 * @param name The country name
 * @param isoCode ISO Code like IN, USA, ENG, etc.
 * @param isdCode ISD Code like +91, +971, etc. Note that these are stored without the + sign.
 * @param flagResource Resource ID for the mipmap of country flag.*/
data class Country(val name: String, val isoCode: String, val isdCode: String, val flagResource: Int)