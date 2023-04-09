package com.example.tasks.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.task.AddTaskDialog
import com.example.task.ItemAdapter
import com.example.task.MainViewModel
import com.example.tasks.MainActivity
import com.example.tasks.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = (context as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemAdapter(requireContext(), viewModel.tasks.value!!)
        _binding?.gridRecyclerView?.adapter = adapter

        binding.fab.setOnClickListener { view ->
            val dialog = AddTaskDialog(viewModel)
            dialog.show(parentFragmentManager, "Add New Task")
            adapter.notifyDataSetChanged()
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}