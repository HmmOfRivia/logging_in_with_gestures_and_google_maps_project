package pl.lipov.laborki.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.lipov.laborki.R
import pl.lipov.laborki.presentation.Map.MapsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        ViewRouter().fragmentChange(LoginFragment(), this, R.id.fragment_box)
    }
}