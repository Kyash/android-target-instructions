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
        val target1 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.fab)
                .setTitle("Floating Action Button")
                .setRadius(100f)
                .setDescription("This is the floating action button.")
                .setTextColorResId(R.color.red)
                .setTitleDimenResId(R.dimen.simple_title)
                .setDescriptionDimenResId(R.dimen.simple_description)
                .build()

        val target2 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.firstText)
                .setTitle("First text")
                .setDescription("This is the first text.")
                .setTitleDimenResId(R.dimen.simple_title)
                .setDescriptionDimenResId(R.dimen.simple_description)
                .setHighlightPadding(R.dimen.simple_hightlight_padding)
                .setListener(object : Target.OnStateChangedListener {
                    override fun onClosed() {
                        binding.scrollview.smoothScrollBy(0, 10000)
                    }
                })
                .build()

        val target3 = SimpleTarget.Builder(this@MainActivity).setCoordinate(binding.secondText)
                .setTitle("Second text")
                .setDescription("This is the second text.")
                .setTitleDimenResId(R.dimen.simple_title)
                .setTextColorResId(android.R.color.white)
                .setBottomCaretDrawableResId(R.drawable.img_caret_bottom_black)
                .setTopCaretDrawableResId(R.drawable.img_caret_top_black)
                .setMessageBgDrawableResId(R.drawable.message_bg_black)
                .setDescriptionDimenResId(R.dimen.simple_description)
                .setMessageMarginBetweenTitleAndDescription(R.dimen.space_32dp)
                .setStartDelayMillis(200L)
                .build()

        TargetInstructions.with(this@MainActivity)
                .setTargets(arrayListOf(target1, target2, target3))
                .start()
    }

}