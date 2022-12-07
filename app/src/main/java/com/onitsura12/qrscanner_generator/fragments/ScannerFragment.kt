package com.onitsura12.qrscanner_generator.fragments


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.onitsura12.qrscanner_generator.databinding.FragmentScannerBinding


class ScannerFragment : Fragment() {


    private lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        } else {
            startScanning()
        }

        binding.scanAgainBtn.setOnClickListener {
            codeScanner.startPreview()
        }

        binding.shareResultButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "${binding.scanResultView.text}")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }


    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }


    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    companion object {
        fun newInstance() = ScannerFragment()
    }


    private fun startScanning() {
        val scannerView: CodeScannerView = binding.scanner
        val activity = requireActivity()
        codeScanner = CodeScanner(activity.applicationContext, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                binding.scanResultView.text = it.text
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }


}