package com.jc.candycatch

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.jc.candycatch.advanced.AdvancedActivity
import com.jc.candycatch.basic.BasicActivity
import com.jc.candycatch.databinding.ActivityMainBinding
import com.jc.candycatch.difficult.DifficultActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            basicBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, BasicActivity::class.java))
            }
            advancedBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, AdvancedActivity::class.java))
            }
            difficultBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, DifficultActivity::class.java))
            }
        }
    }

}