package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mobdeve.s12.group8.glimpse.databinding.ActivityLoginBinding
import com.mobdeve.s12.group8.glimpse.databinding.ActivityRegisterBinding
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "REGISTER_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.regContinueBtn.setOnClickListener {
            createUser()
        }

        binding.regLoginRedirectTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun createUser() {
        val username = binding.regUsernameEt.text.toString()
        val password = binding.regPasswordEt.text.toString()
        val email = binding.regEmailEt.text.toString()

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_LONG).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please provide a valid email!", Toast.LENGTH_LONG).show()
        }
        else if(password.length < 6) {
            Toast.makeText(this, "Password should be minimum 6 characters!", Toast.LENGTH_LONG).show()
        }
        else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userDoc = FirestoreReferences.getUserbyUsername(username).await()
                    val profileImg = FirestoreReferences.getDefaultUserPhoto().await().toString()
                    val user = User(username, email, password, profileImg)

                    if(!userDoc.isEmpty) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@RegisterActivity, "Username Taken", Toast.LENGTH_LONG).show()
                        }
                    }
                    else {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        val res = FirestoreReferences.addUser(user).await()

                        Log.d(TAG, "User added with ID: ${res.id}")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegisterActivity, "Successfully Registered", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                }
                catch (err : FirebaseAuthUserCollisionException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Email Already Registered",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                catch (err : Exception){
                withContext(Dispatchers.Main){
                    Log.d(TAG, "$err")
                    Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            }
            }
        }
    }
}