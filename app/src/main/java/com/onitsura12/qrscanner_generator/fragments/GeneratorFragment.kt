package com.onitsura12.qrscanner_generator.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.WriterException
import com.onitsura12.qrscanner_generator.databinding.FragmentGeneratorBinding


class GeneratorFragment : Fragment() {

    private lateinit var binding: FragmentGeneratorBinding
    private lateinit var viewModel: GenerateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = GenerateViewModel()
        binding = FragmentGeneratorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.bmpLive.observe(viewLifecycleOwner) {
                ivQRCode.setImageBitmap(it)
            }

            btnGenerate.setOnClickListener {
                val data = etData.text.toString().trim()
                if (data.isEmpty()) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "You're should type something ^_^",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    try {
                        viewModel.generateQr(data = data)

                    } catch (e: WriterException) {
                        Log.e("Writer fail", e.toString())
                    }
                }

            }

            binding.shareButton.setOnClickListener{

            }
        }

    }

    companion object {
        fun newInstance() = GeneratorFragment()
    }


}