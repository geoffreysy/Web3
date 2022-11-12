package com.example.lifecoin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RedeemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RedeemFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences0: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spEditor : SharedPreferences.Editor
    private lateinit var viewModel: MainActivityViewModel

    lateinit var tvRedeemBalance : TextView
    lateinit var ivVoucher1: ImageView
    lateinit var ivVoucher2: ImageView

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
        var view = inflater.inflate(R.layout.fragment_redeem, container, false)

        sharedPreferences0 = requireActivity().getSharedPreferences("lifecoin", Context.MODE_PRIVATE)
        val mobile = sharedPreferences0.getString("mobile", "xxx")

        sharedPreferences = requireActivity().getSharedPreferences(mobile.toString(), Context.MODE_PRIVATE)
        spEditor = sharedPreferences.edit()

        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        tvRedeemBalance = view.findViewById<TextView>(R.id.tvRedeemBalance)
        tvRedeemBalance.text = String.format("%.0f", viewModel.lifecoin.value)

        ivVoucher1 = view.findViewById<ImageView>(R.id.ivVoucher1)
        ivVoucher2 = view.findViewById<ImageView>(R.id.ivVoucher2)

        ivVoucher1.setOnClickListener {
            if (viewModel.lifecoin.value!! >= 30) {
                viewModel.lifecoin.value = viewModel.lifecoin.value!!.minus(30)
                tvRedeemBalance.text = String.format("%.0f", viewModel.lifecoin.value)
                Toast.makeText(requireActivity(), "Purchase successful.", Toast.LENGTH_SHORT).show()
                saveLifeCoin()
            } else {
                Toast.makeText(requireActivity(), "Not enough credits.", Toast.LENGTH_SHORT).show()
            }
        }

        ivVoucher2.setOnClickListener {
            if (viewModel.lifecoin.value!! >= 50) {
                viewModel.lifecoin.value = viewModel.lifecoin.value!!.minus(50)
                tvRedeemBalance.text = String.format("%.0f", viewModel.lifecoin.value)
                Toast.makeText(requireActivity(), "Purchase successful.", Toast.LENGTH_SHORT).show()
                saveLifeCoin()
            } else {
                Toast.makeText(requireActivity(), "Not enough credits.", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RedeemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RedeemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun saveLifeCoin() {
        Log.i("MYTAG", "Saving lifecoin")
        spEditor.putFloat("lifecoin", viewModel.lifecoin.value!!)
        spEditor.commit()
    }
}