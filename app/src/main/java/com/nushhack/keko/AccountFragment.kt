package com.nushhack.keko

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nushhack.keko.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loggedIn = sharedPref.getBoolean("loggedIn", false)

        if (loggedIn) {
            binding.name.text = sharedPref.getString("name", "New User")
            binding.email.text = sharedPref.getString("email", "example@example.com")
            binding.logout.text = "Log Out"

            binding.logout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.logout_title)
                    .setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.logout_pos) { dialog, _ ->
                        dialog.dismiss()
//                    auth.signOut()
//                    startActivity(Intent(context, OnboardingActivity::class.java))
                        with (sharedPref.edit()) {
                            putBoolean("loggedIn", false)
                        }
                        activity?.finish()
                    }
                    .setNegativeButton(R.string.logout_neg) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            binding.name.text = "You are not signed in!"
            binding.email.text = "Your progress will not be saved"
            binding.logout.text = "Log In"

            binding.logout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.logout_title)
                    .setMessage(R.string.logout_message)
                    .setPositiveButton(R.string.logout_pos) { dialog, _ ->
                        dialog.dismiss()
//                    auth.signOut()
//                    startActivity(Intent(context, OnboardingActivity::class.java))
                        with (sharedPref.edit()) {
                            putBoolean("loggedIn", true)
                        }
                        activity?.finish()
                    }
                    .setNegativeButton(R.string.logout_neg) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

//        binding.logout.setOnClickListener {
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle(R.string.logout_title)
//                .setMessage(R.string.logout_message)
//                .setPositiveButton(R.string.logout_pos) { dialog, _ ->
//                    dialog.dismiss()
////                    auth.signOut()
////                    startActivity(Intent(context, OnboardingActivity::class.java))
//                    activity?.finish()
//                }
//                .setNegativeButton(R.string.logout_neg) { dialog, _ ->
//                    dialog.dismiss()
//                }
//                .show()
//        }
    }
}