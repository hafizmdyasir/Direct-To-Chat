package com.hamohdy.whatsappdirect;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hamohdy.whatsappdirect.databinding.CountryListItemBinding;
import com.hamohdy.whatsappdirect.databinding.IsdDetectedAlertBinding;

public class FragmentIsdDetected extends BottomSheetDialogFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PHONE_NUMBER = "phoneNumber";

    private Utilities.ButtonClickListener dialogListener;
    private CountryListItemBinding countryBinding;
    private IsdDetectedAlertBinding binding;
    private String phoneNumber;

    public FragmentIsdDetected() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param phoneNumber the detected country.
     * @return A new instance of fragment FragmentIsdDetected.
     */
    public static FragmentIsdDetected newInstance(String phoneNumber) {
        FragmentIsdDetected fragment = new FragmentIsdDetected();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            phoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = IsdDetectedAlertBinding.inflate(inflater, container, false);
        countryBinding = binding.detectedCountry;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Used for displaying the number as entered, without ISD code if a country is detected.
        String strippedNumber = phoneNumber;
        Country detectedCountry = Utilities.detectCountry(phoneNumber);

        /*if the entered country code does not correspond with any of the codes we have saved,
        we can either dismiss or let the user continue anyway.
        This ensures that user can enter a code which we do not have.
        */
        if (detectedCountry == null) {
            binding.message.setText(R.string.isd_detected_message_no_country);
            countryBinding.getRoot().setVisibility(View.GONE);
        }

        //if the above if block wasn't called, it means that a country has been detected.
        //Let us show its details to the user.
        else {
            countryBinding.flag.setImageResource(detectedCountry.flagCode);
            countryBinding.countryCode.setText(detectedCountry.isdCode);
            countryBinding.countryName.setText(detectedCountry.name);

            //If we detected a country, show the stripped number.
            strippedNumber = phoneNumber
                    .replace("+","")
                    .replace(detectedCountry.isdCode, "");
        }

        //The phoneNumber TextView should show text like a phone number. Duh!
        binding.phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.phoneNumber.setText(strippedNumber);

        binding.okContinue.setOnClickListener(v -> dialogListener.buttonClicked());
        binding.cancelButton.setOnClickListener(v -> dismiss());
    }

    public FragmentIsdDetected setDialogClickListener(Utilities.ButtonClickListener listener) {
        this.dialogListener = listener;
        return this;
    }

}