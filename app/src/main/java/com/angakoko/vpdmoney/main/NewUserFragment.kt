package com.angakoko.vpdmoney.main

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.angakoko.vpdmoney.R
import com.angakoko.vpdmoney.ViewModelFactory
import com.angakoko.vpdmoney.databinding.FragmentNewUserBinding
import com.angakoko.vpdmoney.model.User

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class NewUserFragment : Fragment() {

    private lateinit var binding: FragmentNewUserBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_user, container, false)

        val viewModelFactory = ViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        viewModel.setHeader(getString(R.string.new_user))

        binding.saveButton.setOnClickListener {
            check()
        }

        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    private fun check(): Boolean{
        val user = User()
        if(TextUtils.isEmpty(binding.name.text.toString())){
            binding.name.requestFocus()
            binding.name.error = getString(R.string.required)
            return false
        }
        user.name = binding.name.text.toString()

        if(TextUtils.isEmpty(binding.username.text.toString())){
            binding.username.requestFocus()
            binding.username.error = getString(R.string.required)
            return false
        }
        user.username = binding.username.text.toString()

        if(TextUtils.isEmpty(binding.email.text.toString())){
            binding.email.requestFocus()
            binding.email.error = getString(R.string.required)
            return false
        }

        user.email = binding.email.text.toString()

        user.street = binding.address.text.toString()
        user.suite = binding.suite.text.toString()
        user.city = binding.city.text.toString()
        user.zipcode = binding.zipCode.text.toString()
        user.company = binding.companyName.text.toString()
        user.catchPhrase = binding.catchphrase.text.toString()

        viewModel.insetUserInDb(user)
        return true
    }
}