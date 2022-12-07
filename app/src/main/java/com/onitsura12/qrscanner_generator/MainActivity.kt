package com.onitsura12.qrscanner_generator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onitsura12.qrscanner_generator.databinding.ActivityMainBinding
import com.onitsura12.qrscanner_generator.fragments.GeneratorFragment
import com.onitsura12.qrscanner_generator.fragments.ScannerFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initGenerate()
        binding.apply {
            btnFrgmGenerate.setOnClickListener{
                initGenerate()
            }
            btnFrgmScan.setOnClickListener {
                initScanner()
            }
        }
    }


    private fun initGenerate(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frgm_cont, GeneratorFragment.newInstance())
            .commit()
    }

    private fun initScanner(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frgm_cont, ScannerFragment.newInstance())
            .commit()
    }


}