package co.kyash.androidtargetinstructions.example

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.sip.SipSession
import android.os.Bundle
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import co.kyash.androidtargetinstructions.example.databinding.ActivitySimpleUsageBinding
import co.kyash.targetinstructions.TargetInstructions
import co.kyash.targetinstructions.targets.SimpleTarget
import co.kyash.targetinstructions.targets.Target


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
        // Create Target and set
        val target1 = SimpleTarget.Builder(this@SimpleUsageActivity)
                .setTarget(binding.fab)
                // .setTarget(0f, 0f, 100f, 100f)
                .setTitle("Floating Action Button")
                .setHighlightRadius(100f) // Circle shape
                .setDescription("This is the floating action button.")
                .build()

        val target2 = SimpleTarget.Builder(this@SimpleUsageActivity)
                .setTarget(binding.firstText)
                .setTitle("First text")
                .setMessageInterpolator(FastOutSlowInInterpolator())
                .setDescription("This is the first text.")
                .setHighlightPadding(R.dimen.simple_highlight_padding)
                .setMessageAnimationDuration()
                .setPositionType(Target.Position.ABOVE)
                .setStartDelayMillis(200L)
                .setListener(object: Target.OnStateChangedListener {
                    override fun onClosed() {
                        // Do after target is closed
                    }
                })
                .build()

        TargetInstructions.with(this@SimpleUsageActivity)
                .setTargets(arrayListOf(target1, target2))
                .setFadeDuration(200L)
                .setFadeInterpolator(LinearOutSlowInInterpolator())
                .setOverlayColorResId(R.color.black_alpha) // Background color
                .start()
                // .finish()
    }

}