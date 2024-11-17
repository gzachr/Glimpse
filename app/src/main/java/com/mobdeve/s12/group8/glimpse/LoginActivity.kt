package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.mobdeve.s12.group8.glimpse.databinding.ActivityLoginBinding
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginContinueBtn.setOnClickListener {
            login()
        }

        binding.loginRegRedirectTv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val username = binding.usernameEt.text.toString()
        val password = binding.passwordEt.text.toString()
        auth = FirebaseAuth.getInstance()

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
        }
        else {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val res = FirestoreReferences.getUserbyUsername(username).await()
                    val email = res.documents[0].toObject(User::class.java)?.email!!
                    auth.signInWithEmailAndPassword(email, password).await()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Succesfully Logged in", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)

                        intent.apply {
                            // put extra as needed
                        }

                        startActivity(intent)
                        finishAffinity()
                    }

                } catch (err: FirebaseAuthInvalidUserException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    }
                } catch (err: FirebaseAuthInvalidCredentialsException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}