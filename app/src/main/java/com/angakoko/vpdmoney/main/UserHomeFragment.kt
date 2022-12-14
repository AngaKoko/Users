package com.angakoko.vpdmoney.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angakoko.vpdmoney.R
import com.angakoko.vpdmoney.ViewModelFactory
import com.angakoko.vpdmoney.databinding.FragmentUserHomeBinding
import com.angakoko.vpdmoney.model.User

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class UserHomeFragment : Fragment(), ListUserAdapter.OnClickListener {

    private lateinit var binding: FragmentUserHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_user_home, container, false)

        val viewModelFactory = ViewModelFactory(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        //Set the header to [Users]
        viewModel.setHeader(getString(R.string.users))

        listUserAdapter = ListUserAdapter(this)
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = listUserAdapter
        }

        viewModel.getListUsers().observe(requireActivity(), Observer {
            listUserAdapter.swapList(it)
        })

        viewModel.getIsRefreshing().observe(requireActivity(), Observer {
            binding.swipeRefresh.isRefreshing = it
        })

        //Listen to on new button click
        binding.fab.setOnClickListener {
            //Navigate to new user fragment when new button is clicked
            findNavController().navigate(R.id.action_userHomeFragment_to_newUserFragment)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.queryUsers()
        }

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    //List user on click lister from Adapter
    override fun onUserClicked(item: User) {
        //set selected user in view model
        viewModel.setUser(item)
        findNavController().navigate(R.id.action_userHomeFragment_to_userDetailsFragment)
    }
}