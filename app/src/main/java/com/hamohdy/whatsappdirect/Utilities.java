package com.hamohdy.whatsappdirect;

import android.content.Intent;
import android.net.Uri;

public class Utilities {

    //Called when the buttons in FragmentIsdDetected are clicked
    public interface ButtonClickListener {
        void buttonClicked();
    }

    //try to detect country from phone number. Returns null if not detected.
    public static Country detectCountry(String phoneNumber) {

        for (Country entry:Constants.countries) {
            if (phoneNumber.replace("+","").startsWith(entry.isdCode)) {
                return entry;
            }
        }
        return null;
    }

    public static Intent getLaunchIntent(String phoneNumber, String message, boolean business) {

        if (message == null) message = "";

        String total = "https://api.whatsapp.com/send?phone="
                + phoneNumber.replace("+","")
                + "&text=" + message;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(total));

        intent.setPackage(business ? "com.whatsapp.w4b" : "com.whatsapp");
        return intent;
    }
}
