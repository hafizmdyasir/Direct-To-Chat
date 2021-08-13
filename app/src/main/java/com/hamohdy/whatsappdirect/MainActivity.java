package com.hamohdy.whatsappdirect;

import android.content.Intent;
import android.net.Uri;
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

/**This is the home page. This is where the magic happens.*/
public class MainActivity extends AppCompatActivity implements ActivityResultCallback<ActivityResult> {

    /**Launches the {@link CountrySelectorActivity} to pick an isd code.*/
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

        //Dark status bar text during day.
        if (!getResources().getBoolean(R.bool.nightMode)) {
            WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), root);
            controller.setAppearanceLightStatusBars(true);
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
        for (Country entry: Constants.countries) {
            if(deviceCountryCode.equalsIgnoreCase(entry.isoCode)) {
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
     *
     * Bold is created via asterisk (*).
     * Italics are created using the underscore character.
     * For strikethrough text, you need the tilde symbol.
     * And for mono text, you need three grave characters.*/
    private void formatClicked(View v) {

        int id = v.getId();

        //bold is easy via the asterisk or star symbol.
        if (id == R.id.bold) {
            binding.text.onKeyDown(KeyEvent.KEYCODE_STAR, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR));
            binding.text.onKeyDown(KeyEvent.KEYCODE_STAR, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_STAR));
            binding.text.setSelection(binding.text.getSelectionEnd()-1);
        }

        //The minus keycode with shift on creates underscore
        else if (id == R.id.italic) {
            binding.text.onKeyDown(KeyEvent.KEYCODE_MINUS, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS,0, KeyEvent.META_SHIFT_ON));
            binding.text.onKeyDown(KeyEvent.KEYCODE_MINUS, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MINUS,0, KeyEvent.META_SHIFT_ON));
            binding.text.setSelection(binding.text.getSelectionEnd()-1);
        }

        //The grave character with shift pressed creates tilde.
        else if (id == R.id.strike) {
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, KeyEvent.META_SHIFT_ON));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, KeyEvent.META_SHIFT_ON));
            binding.text.setSelection(binding.text.getSelectionEnd()-1);
        }

        //The grave character allows for mono text but metastate has to be zero.
        else if (id == R.id.mono) {
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.onKeyDown(KeyEvent.KEYCODE_GRAVE, new KeyEvent(System.currentTimeMillis(),System.currentTimeMillis(),KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_GRAVE,0, 0));
            binding.text.setSelection(binding.text.getSelectionEnd() - 3);
        }
        binding.text.requestFocus();
    }

    private void sendClicked(boolean business) {

        if (binding.numberEntry.length() == 0) {
            Toast.makeText(this, getString(R.string.enter_number_warn), Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = binding.numberEntry.getText().toString();
        String code = selectedCountry.isdCode;
        String message = binding.text.getText() == null ? "" : binding.text.getText().toString();

        String total = "https://api.whatsapp.com/send?phone=" + code + phone + "&text=" + message;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(total));

        intent.setPackage(business ? "com.whatsapp.w4b" : "com.whatsapp");
        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
        else Toast.makeText(this, getString(R.string.not_installed), Toast.LENGTH_SHORT).show();
    }

}