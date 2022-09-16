package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        file_name.setTextColor(resources.getColor(R.color.colorPrimaryDark,null))
        file_name.text = intent.getStringExtra("FileName")
        val state = intent.getStringExtra("state")
        if (state == "Successful" )
            status.setTextColor(resources.getColor(R.color.colorPrimary,null))
        else
            status.setTextColor(resources.getColor(R.color.fail,null))
            status.text = intent.getStringExtra("state")

        button.setOnClickListener{
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }


        motion_layout.transitionToEnd()


    }


}
