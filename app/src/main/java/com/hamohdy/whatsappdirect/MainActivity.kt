package com.hamohdy.whatsappdirect

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hamohdy.whatsappdirect.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    //launch the country selector activity to pick an ISD code
    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        //the result code must be RESULT_OK. Otherwise, do nothing.
        if (it.resultCode != RESULT_OK || it.data == null) return@registerForActivityResult
        val countryIndex = it.data?.getIntExtra("country", -1)

        //-1 corresponds to device default selected.
        if (countryIndex == null || countryIndex == -1) countrySelected(deviceDefaultCountry)
        else countrySelected(countries[countryIndex])
    }

    //selected country
    private var selectedCountry: Country? = null

    //view binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.WhatsAppDirect)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        setClickListeners()

        if (selectedCountry != null) return

        //If the flow comes here, it means default country hasn't been loaded
        //and neither has the user selected a country

        //first, select the default country. If the saved instance state has a position,
        //load country from that position.
        var defaultCountry = deviceDefaultCountry
        if (savedInstanceState != null) {
            val position = savedInstanceState.getInt("country", -1)
            if (position != -1) defaultCountry = countries[position]

            val enteredNumber = savedInstanceState.getString("number")
            val enteredText = savedInstanceState.getString("message")

            binding.waNumber.setText(enteredNumber)
            binding.optionalMessage.setText(enteredText)
        }

        countrySelected(defaultCountry)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("country", countries.indexOf(selectedCountry))
        outState.putString("number", binding.waNumber.text.toString())
        outState.putString("message", binding.optionalMessage.text.toString())
    }

    override fun onResume() {

        super.onResume()

        //check if clipboard has a phone number. This has to be in onResume so that
        //the paste button becomes visible whenever the user copies and comes back to the activity.
        val clipboardManager = getSystemService(ClipboardManager::class.java)
        if (!clipboardManager?.hasPrimaryClip()!!) {
            binding.paste.visibility = View.GONE
            return
        }

        val clipItem = clipboardManager.primaryClip?.getItemAt(0)
        if (clipItem?.text == null) {
            binding.paste.visibility = View.GONE
            return
        }

        val copiedText = clipItem.text.toString()
        if (!PhoneNumberUtils.isGlobalPhoneNumber(copiedText)) {
            binding.paste.visibility = View.GONE
            return
        }

        binding.paste.visibility = View.VISIBLE
        binding.paste.setOnClickListener { binding.waNumber.setText(copiedText) }

    }

    override fun onDestroy() {
        super.onDestroy()
        activityLauncher.unregister()
    }


    private fun countrySelected(selectedCountry: Country?) {

        if (selectedCountry == null) return

        binding.flag.setImageResource(selectedCountry.flagResource)
        binding.isdCode.text = selectedCountry.isdCode
        this.selectedCountry = selectedCountry
    }

    private fun setClickListeners() {

        binding.selectCountry.setOnClickListener {
            activityLauncher.launch(Intent(this, CountrySelectorActivity::class.java))
        }

        binding.sendBusiness.setOnClickListener { sendClicked(business = true) }
        binding.send.setOnClickListener { sendClicked(business = false) }

        binding.italic.setOnClickListener { formatClicked(it) }
        binding.strike.setOnClickListener { formatClicked(it) }
        binding.bold.setOnClickListener { formatClicked(it) }
        binding.mono.setOnClickListener { formatClicked(it) }

        binding.toggleAbout.setOnClickListener {
            FragmentAbout().show(supportFragmentManager, "ABOUT")
        }

    }

    /*
    Using the android.widget.TextView#onKeyDown(int, KeyEvent) method,
    it is easy to insert the required characters.
    
    Bold is created via asterisk (*).
    Italics are created using the underscore character.
    For strikethrough optionalMessage, you need the tilde symbol.
    
    And for mono optionalMessage, you need three grave characters.
    */

    private fun formatClicked(button: View) {

        val id = button.id

        val start = binding.optionalMessage.selectionStart
        val end = binding.optionalMessage.selectionEnd

        when (id) {

            //bold via asterisk
            R.id.bold -> {
                binding.optionalMessage.setSelection(start)
                binding.optionalMessage.onKeyDown(
                    KeyEvent.KEYCODE_STAR, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR))

                binding.optionalMessage.setSelection(end+1)
                binding.optionalMessage.onKeyDown(
                    KeyEvent.KEYCODE_STAR, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR))
            }

            //italics via underscore, which is minus with shift
            R.id.italic -> {

                val current = System.currentTimeMillis()
                binding.optionalMessage.setSelection(start)
                binding.optionalMessage.onKeyDown(
                    KeyEvent.KEYCODE_MINUS,
                    KeyEvent(current, current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS,
                        0, KeyEvent.META_SHIFT_ON))
                binding.optionalMessage.setSelection(end+1)
                binding.optionalMessage.onKeyDown(
                    KeyEvent.KEYCODE_MINUS,
                    KeyEvent(current, current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS,
                        0, KeyEvent.META_SHIFT_ON))
            }

            //The grave character with shift pressed creates tilde.
            R.id.strike -> {
                val current = System.currentTimeMillis()
                binding.optionalMessage.setSelection(start)
                binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE,
                    KeyEvent(current, current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,
                        0, KeyEvent.META_SHIFT_ON))
                binding.optionalMessage.setSelection(end+1)
                binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE,
                    KeyEvent(current, current, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,
                        0, KeyEvent.META_SHIFT_ON))
            }

            //The grave character allows for mono optionalMessage but meta state has to be zero.
            //And we need three presses
            R.id.mono -> {
                binding.optionalMessage.setSelection(start)
                repeat(3) {
                    binding.optionalMessage.onKeyDown(
                        KeyEvent.KEYCODE_GRAVE, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE))
                }
                binding.optionalMessage.setSelection(end+1+2)
                repeat(3) {
                    binding.optionalMessage.onKeyDown(
                        KeyEvent.KEYCODE_GRAVE, KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE))
                }
            }

            else -> {}
        }

        //when nothing is selected, insert the characters and go to their middle.
        if (start == end) {
            binding.optionalMessage.apply {
                setSelection( if (id == R.id.mono) (selectionStart - 3) else (selectionStart - 1))
            }
        }
        //request focus
        binding.optionalMessage.requestFocus()
    }

    private fun sendClicked(business: Boolean) {

        //If the waNumber field has null text or is empty, or has nothing but a plus sign, handle.
        if (binding.waNumber.text == null || binding.waNumber.text.isEmpty() || binding.waNumber.text.toString() == "+") {
            createToast(R.string.enter_number_warn)
            return
        }

        val phoneNumber = binding.waNumber.text.toString()
        val message = binding.optionalMessage.text.toString()

        //If the user entered a phone number along with ISD code, alert them.
        //Note that for now, this relies on the presence of the "+" symbol in the number.
        if (phoneNumber.contains("+")) {
            FragmentIsdDetected
                .newInstance(phoneNumber)
                .setDialogClickListener {
                    getLaunchIntent(phoneNumber, message, business).launchIfResolved(this)
                }
                .show(supportFragmentManager, "ISD DETECTED")
            return
        }

        val phoneNumberWithIsd = selectedCountry?.isdCode + phoneNumber
        getLaunchIntent(phoneNumberWithIsd, message, business).launchIfResolved(this)
    }

}