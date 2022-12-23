package com.onitsura12.qrscanner_generator.fragments

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.WriterException
import com.onitsura12.qrscanner_generator.databinding.FragmentGeneratorBinding
import java.io.OutputStream


class GeneratorFragment : Fragment() {

    private lateinit var binding: FragmentGeneratorBinding
    private lateinit var viewModel: GenerateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

            shareButton.setOnClickListener {
                val icon: Bitmap = viewModel.bmpLive.value!!
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"

                val values = ContentValues()
                values.put(Images.Media.TITLE, "title")
                values.put(Images.Media.MIME_TYPE, "image/jpeg")
                val uri: Uri = activity?.contentResolver?.insert(
                    Images.Media.EXTERNAL_CONTENT_URI, values
                )!!


                val outStream: OutputStream
                try {
                    outStream = activity?.contentResolver?.openOutputStream(uri)!!
                    icon.compress(CompressFormat.JPEG, 100, outStream)
                    outStream.close()
                } catch (e: java.lang.Exception) {
                    System.err.println(e.toString())
                }

                share.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(share, "Share Image"))
            }
        }

    }

    companion object {
        fun newInstance() = GeneratorFragment()
    }


}