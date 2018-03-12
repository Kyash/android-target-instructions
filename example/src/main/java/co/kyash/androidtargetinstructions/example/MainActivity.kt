package co.kyash.androidtargetinstructions.example

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivityMainBinding
import co.kyash.spotinstructions.SimpleSpot
import co.kyash.spotinstructions.SpotInstructions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val spot1 = SimpleSpot.Builder(this@MainActivity).setPoint(binding.fab)
                        .setTitle("First title")
                        .setDescription("First description")
                        .build()

                val spot2 = SimpleSpot.Builder(this@MainActivity).setPoint(binding.title)
                        .setTitle("Second title")
                        .setDescription("Second description")
                        .build()

                SpotInstructions.with(this@MainActivity)
                        .setSpots(arrayListOf(spot1, spot2))
                        .start()
            }
        })
    }

}
