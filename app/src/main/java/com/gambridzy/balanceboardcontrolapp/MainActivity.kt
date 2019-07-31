package com.gambridzy.balanceboardcontrolapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity()
{
    private var synchronizedStatusText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        synchronizedStatusText = findViewById(R.id.synchronizedStatusText)
    }

    fun onSynchronizedStartButtonClick(view: View)
    {
        synchronizedStatusText?.text = "hello"
    }
}
