package activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.divyakhare.minilibrary.R

class RegisterConfirmation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_confirmation)
        Handler().postDelayed(
            {
                val startIntent = Intent(this@RegisterConfirmation,Login::class.java)
                startActivity(startIntent)
                finish()
            },3000
        )
    }
}
