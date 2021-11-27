package com.example.tusabes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tusabes.databinding.FragmentProfesorBinding

class ProfesorFragment : Fragment() {
    private var _binding: FragmentProfesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfesorBinding.inflate(inflater,container,false)


        return binding.root
    }
}