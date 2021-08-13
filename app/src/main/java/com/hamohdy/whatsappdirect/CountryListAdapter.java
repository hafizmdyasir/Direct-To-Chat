package com.hamohdy.whatsappdirect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.l4digital.fastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

/**An Adapter class that extends from {@link RecyclerView.Adapter} and is used with a recyclerview to show a list of countries the user can select to pick a country code*/
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryHolder>
        implements FastScroller.SectionIndexer {

    /**Listener to listen to item clicks and pass the position as a parameter. Why doesn't Google include an inbuilt feature for this?*/
    public interface CountrySelectedListener {
        void countrySelected(int position);
    }

    private final List<Country> filteredCountries = new ArrayList<>(Constants.countries);
    private final Context context;
    
    public CountryListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.country_list_item, parent, false);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.CountryHolder holder, int position) {

        Country country = filteredCountries.get(position);

        String name = country.name + ", " + country.isoCode;
        String dialingCode = country.isdCode;
        int referenceId = country.flagCode;

        holder.name.setText(name);
        holder.code.setText(dialingCode);
        holder.flag.setImageResource(referenceId);

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof CountrySelectedListener)
                ((CountrySelectedListener) context).countrySelected(Constants.countries.indexOf(country));
        });
    }

    @Override
    public int getItemCount() {
        return filteredCountries.size();
    }

    @Override
    public CharSequence getSectionText(int i) {
        return filteredCountries.get(i).isoCode;
    }

    protected static class CountryHolder extends RecyclerView.ViewHolder {
        private final TextView name, code;
        private final ImageView flag;
        public CountryHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.country_name);
            code = itemView.findViewById(R.id.country_code);
            flag = itemView.findViewById(R.id.flag);
        }
    }
}
