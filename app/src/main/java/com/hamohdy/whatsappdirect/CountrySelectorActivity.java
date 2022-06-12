package com.hamohdy.whatsappdirect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

import com.hamohdy.whatsappdirect.CountryListAdapter.CountrySelectedListener;
import com.hamohdy.whatsappdirect.databinding.ActivityCountrySelectBinding;

/**This activity displays a list of countries to pick a country code form, and also shows the device default country at the top.*/
public class CountrySelectorActivity extends AppCompatActivity implements CountrySelectedListener {

    private final CountryListAdapter adapter = new CountryListAdapter(this);
    private ActivityCountrySelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.WhatsAppDirect);
        super.onCreate(savedInstanceState);

        binding = ActivityCountrySelectBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        //We need dark status bar text during the day
        if (!getResources().getBoolean(R.bool.nightMode)) {
            WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), root);
            controller.setAppearanceLightStatusBars(true);
        } else {
            root.setBackgroundColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.BLACK);
        }

        binding.counter.setText(Constants.size);
        binding.recycler.post(() -> {
            binding.recycler.setAdapter(adapter);
            binding.scroller.attachRecyclerView(binding.recycler);
            binding.scroller.setSectionIndexer(adapter);
        });
        getDeviceCountry();
    }

    @Override
    public void countrySelected(int position) {
        Intent intent = new Intent();
        if (position != -1) intent.putExtra("country", Constants.countries.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }


    /**Since the main activity will always be launched first, we are relying on the launch intent to get the device default country. If it is not passed, just remove visibility to prevent bad experience.*/
    private void getDeviceCountry() {

        Country deviceCountry = getIntent().getParcelableExtra("default");
        if (deviceCountry == null) {
            binding.deviceDefault.getRoot().setVisibility(View.GONE);
            binding.defaultHeader.setVisibility(View.GONE);
            return;
        }

        String name = deviceCountry.name + ", " + deviceCountry.isoCode;
        String dialingCode = deviceCountry.isdCode;
        int referenceId = deviceCountry.flagCode;

        binding.deviceDefault.countryName.setText(name);
        binding.deviceDefault.countryCode.setText(dialingCode);
        binding.deviceDefault.flag.setImageResource(referenceId);

        binding.deviceDefault.getRoot().setOnClickListener(v -> countrySelected(getIntent().getIntExtra("position", -1)));
    }

}
