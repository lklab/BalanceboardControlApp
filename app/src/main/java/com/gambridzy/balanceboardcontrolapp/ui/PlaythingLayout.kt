package com.gambridzy.balanceboardcontrolapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.gambridzy.balanceboardcontrolapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.plaything_layout.view.*

class PlaythingLayout : Fragment()
{
    var myField = 2
    var myView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        myView = inflater.inflate(R.layout.plaything_layout,null)

        val width = (200 * resources.displayMetrics.density + 0.5f).toInt()
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        val layoutParams = ViewGroup.LayoutParams(width, height) //view.layoutParams
        myView?.layoutParams = layoutParams

        myView?.periodSeekBar?.setOnTouchListener(object:View.OnTouchListener
        {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean
            {
                if(event?.action == MotionEvent.ACTION_DOWN ||
                    event?.action == MotionEvent.ACTION_MOVE)
                {
                    val horizontalScrollView = myView?.findViewById<HorizontalScrollView>(R.id.mainHorizontalScrollView)
                    horizontalScrollView?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        return myView
    }
}
