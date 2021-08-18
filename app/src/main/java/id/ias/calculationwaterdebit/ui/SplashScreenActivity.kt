package id.ias.calculationwaterdebit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import id.ias.calculationwaterdebit.R
import id.ias.calculationwaterdebit.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        Glide.with(this)
            .load(R.drawable.loading)
            .circleCrop()
            .into(mBinding.ivLoading)

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, MainMenuActivity::class.java))
            finish()
        }, 5000)
    }
}