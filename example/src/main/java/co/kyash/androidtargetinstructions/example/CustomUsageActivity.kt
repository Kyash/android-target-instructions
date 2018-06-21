package co.kyash.androidtargetinstructions.example

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivityCustomUsageBinding
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget
import co.kyash.targetinstructions.targets.Target


class CustomUsageActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, CustomUsageActivity::class.java)
    }

    private lateinit var binding: ActivityCustomUsageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_usage)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showInstructions()
            }
        })

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.fab.setOnClickListener {
            binding.scrollview.smoothScrollTo(0, 0)
            Handler().postDelayed({
                showInstructions()
            }, 150)
        }
    }

    private fun showInstructions() {
        val target1 = SimpleTarget.Builder(this@CustomUsageActivity).setTarget(binding.fab)
                .setTitle("Floating Action Button")
                .setHighlightRadius(100f)
                .setDescription("This is the floating action button.")
                .build()

        val target2 = SimpleTarget.Builder(this@CustomUsageActivity).setTarget(binding.firstText)
                .setTitle("First text")
                .setDescription("This is the first text. After this is hidden, the view scrolls to bottom.")
                .setHighlightPadding(R.dimen.simple_highlight_padding)
                .setListener(object : Target.OnStateChangedListener {
                    override fun onClosed() {
                        binding.scrollview.smoothScrollBy(0, 10000)
                    }
                })
                .build()

        val target3 = SimpleTarget.Builder(this@CustomUsageActivity).setTarget(binding.secondText)
                .setTitle("Second text")
                .setDescription("This is the second text. This is customized instruction.")
                .setMessageLayoutResId(R.layout.layout_instruction_simple_message_black)
                .setHighlightHorizontalPadding(R.dimen.space_16dp)
                .setStartDelayMillis(500L)
                .build()

        TargetInstructions.with(this@CustomUsageActivity)
                .setTargets(arrayListOf(target1, target2, target3))
                .start()
    }

}