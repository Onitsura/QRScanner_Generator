package com.onitsura12.qrscanner_generator.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import com.google.zxing.WriterException
import com.onitsura12.qrscanner_generator.BuildConfig
import com.onitsura12.qrscanner_generator.databinding.FragmentGeneratorBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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

            binding.shareButton.setOnClickListener {
//                val filename = "QR"
//
//                viewModel.bmpLive.value?.compress(Bitmap.CompressFormat.JPEG, 95, FileOutputStream(filename))


                val bitmap: Bitmap = viewModel.bmpLive.value!!
//                val root: File = Environment.getExternalStorageDirectory()
//                val cachePath = File(root.absolutePath + "/DCIM/Camera/image.jpg")
//                try {
//                    cachePath.createNewFile()
//                    val ostream = FileOutputStream(cachePath)
//                    bitmap.compress(CompressFormat.JPEG, 95, ostream)
//                    ostream.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                val uri = FileProvider.getUriForFile(
//                    requireActivity().applicationContext,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    cachePath
//                )

                //Это работает кое-как
//                val imagePath = File(requireActivity().applicationContext.filesDir, "QR")
//                val newFile = File(imagePath, ".")
//                val contentUri: Uri = getUriForFile(requireActivity().applicationContext, BuildConfig.APPLICATION_ID + ".provider", newFile)
//                addImageToGallery(filePath = imagePath.toString(), requireActivity().applicationContext)
//                try {
//                    imagePath.createNewFile()
//                    val ostream = FileOutputStream(imagePath)
//                    bitmap.compress(CompressFormat.PNG, 100, ostream)
//                    ostream.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//
//
//
//                val share = Intent(Intent.ACTION_SEND)
//
//
//
//                share.putExtra(Intent.EXTRA_STREAM, contentUri)
//                share.type = "image/*"
//                share.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                startActivity(Intent.createChooser(share, "Share via"))

                //Пробуем новое
                val icon: Bitmap = bitmap
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"

                val values = ContentValues()
                values.put(Images.Media.TITLE, "title")
                values.put(Images.Media.MIME_TYPE, "image/jpeg")
                val uri: Uri = activity?.contentResolver?.insert(
                    Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )!!


                val outstream: OutputStream
                try {
                    outstream = activity?.contentResolver?.openOutputStream(uri)!!
                    icon.compress(CompressFormat.JPEG, 100, outstream)
                    outstream.close()
                } catch (e: java.lang.Exception) {
                    System.err.println(e.toString())
                }

                share.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(share, "Share Image"))
            }
        }

    }
    fun addImageToGallery(filePath: String?, context: Context) {
        val values = ContentValues()
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.MediaColumns.DATA, filePath)
        context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    companion object {
        fun newInstance() = GeneratorFragment()
    }


}