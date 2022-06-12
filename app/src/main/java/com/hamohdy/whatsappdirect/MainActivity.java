package com.hamohdy.whatsappdirect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

import com.hamohdy.whatsappdirect.databinding.ActivityMainBinding;

import java.util.Locale;

/**
 * This is the home page. This is where the magic happens.
 */
public class MainActivity extends AppCompatActivity implements ActivityResultCallback<ActivityResult> {

    /**
     * Launches the {@link CountrySelectorActivity} to pick an isd code.
     */
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
    private Country selectedCountry = null;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.WhatsAppDirect);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        //Dark status bar foreground during day.
        if (!getResources().getBoolean(R.bool.nightMode)) {
            WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), root);
            controller.setAppearanceLightStatusBars(true);
        } else {
            root.setBackgroundColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //means default country hasn't been loaded and neither has the user selected a country
        if (selectedCountry == null) getDefaultCountry();
        setClickListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        launcher.unregister();
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        Country country = null;
        if (result.getResultCode() == RESULT_OK && result.getData() != null)
            country = result.getData().getParcelableExtra("country");
        if (country != null) countrySelected(country);
    }

    private void countrySelected(Country selectedCountry) {

        binding.flag.setImageResource(selectedCountry.flagCode);
        binding.isdCode.setText(selectedCountry.isdCode);
        this.selectedCountry = selectedCountry;
    }

    private void setClickListeners() {

        binding.sendBusiness.setOnClickListener(v -> sendClicked(true));
        binding.send.setOnClickListener(v -> sendClicked(false));

        binding.italic.setOnClickListener(this::formatClicked);
        binding.strike.setOnClickListener(this::formatClicked);
        binding.bold.setOnClickListener(this::formatClicked);
        binding.mono.setOnClickListener(this::formatClicked);

        binding.toggleAbout.setOnClickListener(v
                -> new FragmentAbout().show(getSupportFragmentManager(), "ABOUT"));
    }

    private void getDefaultCountry() {

        int count = 0;
        String deviceCountryCode = Locale.getDefault().getCountry();

        //Search for the entry in list of countries whose iso code matches the device country's iso code.
        for (Country entry : Constants.countries) {
            if (deviceCountryCode.equalsIgnoreCase(entry.isoCode)) {
                countrySelected(entry);
                int finalCount = count;
                binding.selectCountry.setOnClickListener(v -> {
                    Intent intent = new Intent(this, CountrySelectorActivity.class);
                    intent.putExtra("default", entry);
                    intent.putExtra("position", finalCount);
                    launcher.launch(intent);
                });
            }
            ++count;
        }
    }

    /**
     * Using the {@link android.widget.TextView#onKeyDown(int, KeyEvent)} method, it is easy to insert the required characters.
     * <p>
     * Bold is created via asterisk (*).
     * Italics are created using the underscore character.
     * For strikethrough optionalMessage, you need the tilde symbol.
     * And for mono optionalMessage, you need three grave characters.
     */
    private void formatClicked(View v) {

        int id = v.getId();

        //bold is easy via the asterisk or star symbol.
        if (id == R.id.bold) {
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_STAR, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_STAR, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR));
            binding.optionalMessage.setSelection(binding.optionalMessage.getSelectionEnd() - 1);
        }

        //The minus keycode with shift on creates underscore
        else if (id == R.id.italic) {
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_MINUS, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS, 0, KeyEvent.META_SHIFT_ON));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_MINUS, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS, 0, KeyEvent.META_SHIFT_ON));
            binding.optionalMessage.setSelection(binding.optionalMessage.getSelectionEnd() - 1);
        }

        //The grave character with shift pressed creates tilde.
        else if (id == R.id.strike) {
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, KeyEvent.META_SHIFT_ON));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, KeyEvent.META_SHIFT_ON));
            binding.optionalMessage.setSelection(binding.optionalMessage.getSelectionEnd() - 1);
        }

        //The grave character allows for mono optionalMessage but metastate has to be zero.
        else if (id == R.id.mono) {
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(), System.currentTimeMillis(), KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE, 0, 0));
            binding.optionalMessage.setSelection(binding.optionalMessage.getSelectionEnd() - 3);
        }
        binding.optionalMessage.requestFocus();
    }

    private void sendClicked(boolean business) {

        //If the waNumber field has null text or is empty, or has nothing but a plus sign, handle.
        if (binding.waNumber.getText() == null || binding.waNumber.getText().length() == 0 || binding.waNumber.getText().toString().equals("+")) {
            Toast.makeText(this, getString(R.string.enter_number_warn), Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneNumber = binding.waNumber.getText().toString();
        //We don't want an NPE, so use use an empty string if message is null.
        String message = binding.optionalMessage.getText() == null ? "" : binding.optionalMessage.getText().toString();


        //If the user entered a phone number along with ISD code, alert them.
        //Note that for now, this relies on the presence of the "+" symbol in the number.
        if (phoneNumber.contains("+")) {
            FragmentIsdDetected
                    .newInstance(phoneNumber)
                    .setDialogClickListener( () -> {
                        Intent launchIntent = Utilities.getLaunchIntent(phoneNumber, message, business);
                        if (launchIntent.resolveActivity(getPackageManager()) != null)
                            startActivity(launchIntent);
                        else
                            Toast.makeText(this, getString(R.string.not_installed), Toast.LENGTH_SHORT).show();
                        
                    })
                    .show(getSupportFragmentManager(), "ISD DETECTED");
            return;
        }

        String phoneNumberWithIsd = selectedCountry.isdCode + phoneNumber;
        Intent intent = Utilities.getLaunchIntent(phoneNumberWithIsd, message, business);

        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
        else Toast.makeText(this, getString(R.string.not_installed), Toast.LENGTH_SHORT).show();
    }

}