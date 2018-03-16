package co.kyash.androidtargetinstructions.example

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivityMainBinding
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget
import co.kyash.targetinstructions.targets.Target


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showInstructions()
            }
        })

        binding.fab.setOnClickListener {
            binding.scrollview.smoothScrollTo(0, 0)
            Handler().postDelayed({
                showInstructions()
            }, 150)

        }
    }

    private fun showInstructions() {
        val target1 = SimpleTarget.Builder(this@MainActivity).setTarget(binding.fab)
                .setTitle("Floating Action Button")
                .setHighlightRadius(100f)
                .setDescription("This is the floating action button.")
                .build()

        val target2 = SimpleTarget.Builder(this@MainActivity).setTarget(binding.firstText)
                .setTitle("First text")
                .setDescription("This is the first text.")
                .setHighlightPadding(R.dimen.simple_hightlight_padding)
                .setListener(object : Target.OnStateChangedListener {
                    override fun onClosed() {
                        binding.scrollview.smoothScrollBy(0, 10000)
                    }
                })
                .build()

        val target3 = SimpleTarget.Builder(this@MainActivity).setTarget(binding.secondText)
                .setTitle("Second text")
                .setDescription("This is the second text.")
                .setMessageLayoutResId(R.layout.layout_instruction_simple_message_black)
                .setHighlightHorizontalPadding(R.dimen.space_minus_16dp)
                .setStartDelayMillis(2000L)
                .build()

        TargetInstructions.with(this@MainActivity)
                .setTargets(arrayListOf(target1, target2, target3))
                .start()
    }

}