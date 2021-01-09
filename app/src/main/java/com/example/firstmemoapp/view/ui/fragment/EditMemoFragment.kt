package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstmemoapp.databinding.FragmentEditMemoBinding
import com.example.firstmemoapp.viewModel.EditMemoViewModel

class EditMemoFragment : Fragment() {
    private lateinit var binding: FragmentEditMemoBinding
    private val viewModel = EditMemoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMemoBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}