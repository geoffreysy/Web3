package com.example.lifecoin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vishnusivadas.advanced_httpurlconnection.PutData


class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var tvMobile: EditText
    private lateinit var tvPassword: EditText
    private lateinit var tvLoginResult: TextView
    private lateinit var viewModel: MainActivityViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var spEditor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        tvMobile = findViewById(R.id.tvMobile)
        tvPassword = findViewById(R.id.tvPassword)
        tvLoginResult = findViewById(R.id.tvLoginResult)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)


        sharedPreferences = getSharedPreferences("lifecoin", Context.MODE_PRIVATE)
        spEditor = sharedPreferences.edit()


        btnLogin.setOnClickListener {
            loginCheck()
        }

        btnSignUp.setOnClickListener {
            SignUp()
        }

    }

    private fun loginCheck() {
        //Start ProgressBar first (Set visibility VISIBLE)
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            //Starting Write and Read data with URL
            //Creating array for parameters
            val field = arrayOfNulls<String>(2)
            field[0] = "mobile"
            field[1] = "password"
            //Creating array for data
            val data = arrayOfNulls<String>(2)
            data[0] = tvMobile.text.toString()
            data[1] = tvPassword.text.toString()

            spEditor.putString("mobile",data[0].toString())
            spEditor.commit()

//            Log.i("MYTAG-SP-LOGIN-MOBILE", viewModel.mobile.value.toString())

            Log.i("MyTAG", "Login fields value")
            Log.i("MyTAG", data[0].toString())
            Log.i("MyTAG", data[1].toString())

            var serverUrl = getString(R.string.server)
            serverUrl = serverUrl.plus("/lifecoin/login.php")
            Log.i("MYTAG",serverUrl.toString())
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
                    Log.i("MYTAG", result)

                    if (result == "Login Success") {
                        Log.i("MYTAG", "Switch Activity")
                        val myIntent = Intent(this, MainActivity::class.java)
                        startActivity(myIntent)
                        tvLoginResult.text = "Login Success!"
                    } else {
                        tvLoginResult.text = "Login fail! Mobile or Password is incorrect."
                    }
                }
            }
            //End Write and Read data with URL

        }

    }

    private fun SignUp() {
        //Start ProgressBar first (Set visibility VISIBLE)
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            //Starting Write and Read data with URL
            //Creating array for parameters
            val field = arrayOfNulls<String>(2)
            field[0] = "mobile"
            field[1] = "password"
            //Creating array for data
            val data = arrayOfNulls<String>(2)
            data[0] = tvMobile.text.toString()
            data[1] = tvPassword.text.toString()
            spEditor.putString("mobile",data[0].toString())
            spEditor.commit()

            Log.i("MyTAG", "Sign up fields value")
            Log.i("MyTAG", data[0].toString())
            Log.i("MyTAG", data[1].toString())

            var serverUrl = getString(R.string.server)
            serverUrl = serverUrl.plus("/lifecoin/signup.php")
            val putData = PutData(
                serverUrl,
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    //End ProgressBar (Set visibility to GONE)
                    Log.i("MYTAG", result)

                    if (result == "Sign Up Success") {
                        Log.i("MYTAG", "Switch Activity")
                        val myIntent = Intent(this, MainActivity::class.java)
                        startActivity(myIntent)
                        tvLoginResult.text = "Sign Up Success!"
                    } else {
                        tvLoginResult.text = "Sign Up Fail!"
                    }
                }
            }
            //End Write and Read data with URL

        }

    }



}