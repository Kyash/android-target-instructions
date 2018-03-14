package co.kyash.androidtargetinstructions.example

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivityMainBinding
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val target1 = SimpleTarget.Builder(this@MainActivity).setPoint(binding.fab)
                        .setTitle("First title")
                        .setRadius(100f)
                        .setDescription("First description")
                        .build()

                val target2 = SimpleTarget.Builder(this@MainActivity).setPoint(binding.title)
                        .setTitle("Second title")
                        .setDescription("Second description")
                        .build()

                TargetInstructions.with(this@MainActivity)
                        .setTargets(arrayListOf(target1, target2))
                        .start()
            }
        })
    }

}