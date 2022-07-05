package com.hamohdy.whatsappdirect

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hamohdy.whatsappdirect.databinding.CountryListItemBinding
import com.hamohdy.whatsappdirect.databinding.IsdDetectedAlertBinding

/**The [FragmentIsdDetected] fragment is shown when the user inputs a + sign in the phone number field in the [MainActivity].
 *
 * In such a case, we try to detect the country code that comes after that + sign. This helps the user avoid errors.
 *
 * Use [FragmentIsdDetected.newInstance] to create an instance of this fragment.*/
class FragmentIsdDetected : BottomSheetDialogFragment() {
    
    private lateinit var countryBinding: CountryListItemBinding
    private lateinit var binding: IsdDetectedAlertBinding
    
    private lateinit var dialogListener: () -> Unit
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNumber = it.getString(ARG_PHONE_NUMBER).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = IsdDetectedAlertBinding.inflate(inflater, container, false)
        countryBinding = binding.detectedCountry
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Used for displaying the number as entered, without ISD code if a country is detected.
        var strippedNumber = phoneNumber
        val detectedCountry = detectCountry(phoneNumber)

        /*If the entered country code does not correspond with any of the codes we have saved,
        we can either dismiss or let the user continue anyway.
        This ensures that user can enter a code which we do not have.*/

        if (detectedCountry == null) {
            binding.message.setText(R.string.isd_detected_message_no_country)
            countryBinding.root.visibility = View.GONE
            binding.countryHeader.visibility = View.GONE
            binding.okContinue.setText(R.string.ignore)
        }

        //Otherwise, we have been successful in detecting a country.
        else {
            countryBinding.flag.setImageResource(detectedCountry.flagResource)
            countryBinding.countryCode.text = detectedCountry.isdCode
            countryBinding.countryName.text = detectedCountry.name

            //Show the number without the ISD code separately to clarify things up.
            strippedNumber = phoneNumber
                .replace("+", "")
                .replaceFirst(detectedCountry.isdCode, "")
        }

        //The phoneNumber TextView should show text like a phone number. Duh!
        binding.phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.phoneNumber.text = strippedNumber

        binding.okContinue.setOnClickListener {
            dialogListener.invoke()
            dismiss()
        }
        binding.cancelButton.setOnClickListener { dismiss() }
    }

    fun setDialogClickListener(listener: () -> Unit): FragmentIsdDetected {
        dialogListener = listener
        return this
    }

    companion object {
        // the fragment initialization parameter.
        private const val ARG_PHONE_NUMBER = "phoneNumber"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param phoneNumber The phone number the user entered, including the + sign.
         * @return A new instance of fragment FragmentIsdDetected.
         */
        fun newInstance(phoneNumber: String?): FragmentIsdDetected {
            return FragmentIsdDetected().apply {
                arguments = Bundle().apply {
                    putString(ARG_PHONE_NUMBER, phoneNumber)
                }
            }
        }
    }
}