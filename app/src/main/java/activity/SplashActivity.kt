package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.divyakhare.minilibrary.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
                val startIntent = Intent(this@SplashActivity,Login::class.java)
                startActivity(startIntent)
                finish()
            },3000
        )
    }
}
