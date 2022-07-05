package com.hamohdy.whatsappdirect

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hamohdy.whatsappdirect.databinding.ActivityCountrySelectBinding

/**This activity displays a list of countries to pick a country code form, and also shows the device default country at the top. */
class CountrySelectorActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityCountrySelectBinding

    //countries adapter. Passing a function to the parameter for handling item click.
    private val adapter = CountriesAdapter{ countrySelected(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.WhatsAppDirect)
        super.onCreate(savedInstanceState)

        binding = ActivityCountrySelectBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)

        //We need dark status bar text during the day
        if (resources.getBoolean(R.bool.nightMode)) {
            root.setBackgroundColor(Color.BLACK)
            window.statusBarColor = Color.BLACK
        }

        //setting up layout display
        binding.counter.text = countries.size.toString()
        binding.recycler.adapter = adapter
        binding.scroller.attachRecyclerView(binding.recycler)
        binding.scroller.setSectionIndexer(adapter)

        //setting up the device default country.
        setupDeviceCountry()
    }

    //when a country is selected by the user, its position is sent back to the main activity.
    private fun countrySelected(position: Int) {

        val intent = Intent()
        intent.putExtra("country", position)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setupDeviceCountry() {

        val deviceCountry = deviceDefaultCountry

        //device country will be null if we are unable to find a match for the device's
        // selected country in our list of countries.
        if (deviceCountry == null) {
            binding.deviceDefault.root.visibility = View.GONE
            binding.defaultHeader.visibility = View.GONE
            return
        }

        //setup default country display
        val name = deviceCountry.name + ", " + deviceCountry.isoCode
        val dialingCode = deviceCountry.isdCode
        val referenceId: Int = deviceCountry.flagResource

        binding.deviceDefault.countryName.text = name
        binding.deviceDefault.countryCode.text = dialingCode
        binding.deviceDefault.flag.setImageResource(referenceId)

        binding.deviceDefault.root.setOnClickListener { countrySelected(-1) }
    }

}