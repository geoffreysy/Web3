package com.example.lifecoin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.vishnusivadas.advanced_httpurlconnection.PutData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WithdrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WithdrawFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btnWithdraw:Button
    private lateinit var viewModel: MainActivityViewModel
    lateinit var tvWallet: EditText
    lateinit var tvLifeCoinBalance: TextView
    lateinit var tvWithdrawResult: TextView
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
        val view = inflater.inflate(R.layout.fragment_withdraw, container, false)

        sharedPreferences0 = requireActivity().getSharedPreferences("lifecoin", Context.MODE_PRIVATE)
        val mobile = sharedPreferences0.getString("mobile", "xxx")

        sharedPreferences = requireActivity().getSharedPreferences(mobile.toString(), Context.MODE_PRIVATE)
        spEditor = sharedPreferences.edit()


        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        tvWallet = view.findViewById<EditText>(R.id.tvWallet)
        tvLifeCoinBalance = view.findViewById<EditText>(R.id.tvLifeCoinBalance)
        tvWithdrawResult = view.findViewById<TextView>(R.id.tvWithdrawResult)

        tvLifeCoinBalance.text = String.format("%.0f", viewModel.lifecoin.value)

        btnWithdraw = view.findViewById<Button>(R.id.btnWithdraw)
        btnWithdraw.setOnClickListener {
            withdrawCoins()

        }


        return view
    }

    private fun withdrawCoins() {
        //Start ProgressBar first (Set visibility VISIBLE)
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            //Starting Write and Read data with URL
            //Creating array for parameters
            val field = arrayOfNulls<String>(2)
            field[0] = "wallet"
            field[1] = "coins"
            //Creating array for data
            val data = arrayOfNulls<String>(2)
            data[0] = tvWallet.text.toString()
            data[1] = viewModel.lifecoin.value?.toInt().toString()
            Log.i("MYTAG-WITHDRAW-WALLET", data[0].toString())
            Log.i("MYTAG-WITHDRAW-COINS", data[1].toString())

            var serverUrl = getString(R.string.server)
            serverUrl = serverUrl.plus("/lifecoin/withdraw.php")
            Log.i("MYTAG-WITHDRAW-URL",serverUrl.toString())
            val putData = PutData(
                serverUrl.toString(),
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("MYTAG-WITHDRAW-RESULT", result)

                    if (result == "Success") {
                        Log.i("MYTAG-WITHDRAW", "Withdraw Success")
//                        tvLoginResult.text = "Login Success!"
                            tvWithdrawResult.text = "Success!"
                            viewModel.lifecoin.value = 0f
                            tvLifeCoinBalance.text = "0"
                            tvWallet.text.clear()
                            saveLifeCoin()
                    } else {
                        Log.i("MYTAG-WITHDRAW", "Withdraw Fail")
                        tvWithdrawResult.text = "Withdraw Fail"
                    }
                }
            }
            //End Write and Read data with URL

        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WithdrawFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WithdrawFragment().apply {
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