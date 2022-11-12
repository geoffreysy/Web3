package com.example.lifecoin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tvSteps: TextView
    lateinit var tvLifeCoin: TextView
    lateinit var tvScans: TextView
    private lateinit var viewModel: MainActivityViewModel
    lateinit var tvCoins: TextView
    lateinit var btnScan : Button
    private lateinit var sharedPreferences0: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spEditor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        tvSteps = view.findViewById<TextView>(R.id.tvSteps)
        tvLifeCoin = view.findViewById<TextView>(R.id.tvLifeCoin)
        tvScans = view.findViewById<TextView>(R.id.tvScans)
        tvCoins = view.findViewById<TextView>(R.id.tvCoins)

        viewModel.steps.observe(requireActivity(), {
            tvSteps.text = it.toString()
            tvLifeCoin.text = "L$ " + String.format("%.0f", viewModel.lifecoin.value)
            tvCoins.text = String.format("%.0f", viewModel.lifecoin.value)
        })

        viewModel.scans.observe(requireActivity(), {
            tvScans.text = it.toString()
            tvLifeCoin.text = "L$ " + String.format("%.0f", viewModel.lifecoin.value)
            tvCoins.text = String.format("%.0f", viewModel.lifecoin.value)
        })

        sharedPreferences0 = requireActivity().getSharedPreferences("lifecoin", Context.MODE_PRIVATE)
        val mobile = sharedPreferences0.getString("mobile", "xxx")

        sharedPreferences = requireActivity().getSharedPreferences(mobile.toString(), Context.MODE_PRIVATE)
        spEditor = sharedPreferences.edit()

        if (viewModel.scanCode.value == true) {
            viewModel.scanCode.value = false
            scanCode()
        }
        return view
    }

    private fun scanCode(): Boolean {
        val barLauncher = registerForActivityResult(ScanContract()) {
            if (it.contents != null) {
                Log.i("MYTAG", it.contents)
                viewModel.scans.value = viewModel.scans.value?.plus(1)
                viewModel.lifecoin.value = viewModel.lifecoin.value?.plus(1000)
                saveLifeCoin()
            }
        }

        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = CaptureAct().javaClass
        barLauncher.launch(options)
        return true
    }

    private fun saveLifeCoin() {
        Log.i("MYTAG", "Saving lifecoin")
        spEditor.putFloat("lifecoin", viewModel.lifecoin.value!!)
        spEditor.putInt("scans", viewModel.scans.value!!)
        spEditor.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}