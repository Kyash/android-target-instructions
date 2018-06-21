package co.kyash.androidtargetinstructions.example

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivitySimpleUsageBinding
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget


class SimpleUsageActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, SimpleUsageActivity::class.java)
    }

    private lateinit var binding: ActivitySimpleUsageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_usage)

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
            showInstructions()
        }
    }

    private fun showInstructions() {
        val target1 = SimpleTarget.Builder(this@SimpleUsageActivity)
                .setTarget(binding.fab)
                .setTitle("Floating Action Button")
                .setHighlightRadius(100f)
                .setDescription("This is the floating action button.")
                .build()

        val target2 = SimpleTarget.Builder(this@SimpleUsageActivity)
                .setTarget(binding.firstText)
                .setTitle("First text")
                .setDescription("This is the first text.")
                .setHighlightPadding(R.dimen.simple_highlight_padding)
                .build()

        TargetInstructions.with(this@SimpleUsageActivity)
                .setTargets(arrayListOf(target1, target2))
                .start()
    }

}