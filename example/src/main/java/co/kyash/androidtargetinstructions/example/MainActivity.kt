package co.kyash.androidtargetinstructions.example

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.kyash.androidtargetinstructions.example.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            simpleUsage.setOnClickListener {
                startActivity(SimpleUsageActivity.createIntent(this@MainActivity))
            }
            customUsage.setOnClickListener {

            }
        }
    }

}