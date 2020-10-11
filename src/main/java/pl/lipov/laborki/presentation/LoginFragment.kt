package pl.lipov.laborki.presentation

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.getInstance
import com.spark.submitbutton.SubmitButton
import kotlinx.android.synthetic.main.fragment_login.*


import pl.lipov.laborki.R
import java.lang.Exception

class LoginFragment : Fragment() {

    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_login, container, false)
        val btn: SubmitButton = inflate.findViewById(R.id.loginButton)
        btn.setOnClickListener {
            val userLogin = loginText.text.toString()
            database = getInstance()
            val ref: DatabaseReference = database.getReference("users")

            ref.child(userLogin).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (verifyLogin()) {
                        try {
                            val genericTypeIndicator: GenericTypeIndicator<Map<String, String>> =
                                object : GenericTypeIndicator<Map<String, String>>() {}
                            val password = dataSnapshot.getValue(genericTypeIndicator)
                            val events = password?.values?.toTypedArray()
                            if (events != null) {
                                val fragPassword = PasswordFragment.newInstance(events)
                                ViewRouter().fragmentChange(fragPassword, activity , R.id.fragment_box)
                            }else {
                                loginText.error = getString(R.string.wrong_login)
                                loginErrorDetected()
                            }
                        }catch (e:Exception){}
                    }
                }
            })
        }
        return inflate
    }

    fun verifyLogin():Boolean {
        if(loginText.length()>6) {
            loginText.error = getString(R.string.long_login)
            loginErrorDetected()
            return false
        }else if (loginText.length() == 0) {
            loginText.error = getString(R.string.enter_login)
            loginErrorDetected()
            return false
        }
        return true
    }

    fun loginErrorDetected(){
        loginText.setBackgroundResource(R.drawable.error_login_background)
        Handler().postDelayed({
            loginText.setBackgroundResource(R.drawable.blue_login_background)
            loginText.text.clear()
            loginText.error = null
        }, 2000)
    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}