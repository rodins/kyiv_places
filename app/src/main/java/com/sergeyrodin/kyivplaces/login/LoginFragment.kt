package com.sergeyrodin.kyivplaces.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.sergeyrodin.kyivplaces.R

private const val TITLE_KEY = "title"

class LoginFragment : Fragment() {

    private lateinit var emailEdit: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        emailEdit = view.findViewById(R.id.email_edit)

        submitButton.setOnClickListener {
            navigateToMapFragment()
        }
    }

    private fun navigateToMapFragment() {
        findNavController().navigate(
            R.id.action_loginFragment_to_mapFragment,
            getBundleWithEmailText()
        )
    }

    private fun getBundleWithEmailText(): Bundle {
        val args = Bundle()
        args.putCharSequence(TITLE_KEY, emailEdit.text.toString())
        return args
    }
}