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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val emailEdit = view.findViewById<EditText>(R.id.email_edit)

        submitButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_mapFragment,
                getBundleWithEmailText(emailEdit)
            )
        }
    }

    private fun getBundleWithEmailText(emailEdit: EditText): Bundle {
        val args = Bundle()
        args.putCharSequence(TITLE_KEY, emailEdit.text.toString())
        return args
    }
}