package com.hamohdy.whatsappdirect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**A simple bottom sheet dialog fragment to display about text, a disclaimer, and a couple of attributions about the libraries and sources used.*/
public class FragmentAbout extends BottomSheetDialogFragment {

    public FragmentAbout() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //The movement method makes the links in TextView clickable.
        //These links are visible in the R.string file and were created using the <a> tag.
        ((TextView) view.findViewById(R.id.scroller_attribute)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) view.findViewById(R.id.fluent_attribute)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) view.findViewById(R.id.flags_attribute)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) view.findViewById(R.id.icon_attribute)).setMovementMethod(LinkMovementMethod.getInstance());

        view.findViewById(R.id.more).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://hafizmdyasir.github.io"));
            startActivity(Intent.createChooser(intent, getString(R.string.open_via)));
        });
    }
}