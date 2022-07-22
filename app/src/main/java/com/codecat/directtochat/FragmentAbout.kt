package com.codecat.directtochat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codecat.directtochat.databinding.FragmentAboutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**A simple bottom sheet dialog fragment to display about text, a disclaimer, and a couple of attributions about the libraries and sources used. */
class FragmentAbout : BottomSheetDialogFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //The movement method makes the links in TextView clickable.
        //These links are visible in the R.string file and were created using the <a> tag.
        binding.attributions.movementMethod = LinkMovementMethod.getInstance()

        binding.more.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://hafizmdyasir.github.io")
            startActivity(Intent.createChooser(intent, getString(R.string.open_via)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}