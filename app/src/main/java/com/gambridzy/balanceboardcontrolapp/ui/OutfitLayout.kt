package com.gambridzy.balanceboardcontrolapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.gambridzy.balanceboardcontrolapp.R
import kotlinx.android.synthetic.main.outfit_layout.view.*

class OutfitLayout : Fragment()
{
    var myView: View? = null
    var name: String = ""
    var parentHorizontalScrollView: HorizontalScrollView? = null
    var userEventListener: UserEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        myView = inflater.inflate(R.layout.outfit_layout,null)

        /* setup size (layout parameter */
        val width = (200 * resources.displayMetrics.density + 0.5f).toInt()
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        val layoutParams = ViewGroup.LayoutParams(width, height) //view.layoutParams
        myView?.layoutParams = layoutParams

        /* setting seekbar with scroll view */
        myView?.periodSeekBar?.setOnTouchListener(object:View.OnTouchListener
        {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean
            {
                if(event?.action == MotionEvent.ACTION_DOWN ||
                    event?.action == MotionEvent.ACTION_MOVE)
                {
                    parentHorizontalScrollView?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        myView?.changeTimeSeekBar?.setOnTouchListener(object:View.OnTouchListener
        {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean
            {
                if(event?.action == MotionEvent.ACTION_DOWN ||
                    event?.action == MotionEvent.ACTION_MOVE)
                {
                    parentHorizontalScrollView?.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }
        })

        /* bind button events */
        myView?.startButton?.setOnClickListener((
        {
            userEventListener?.onStartButtonClicked(this)
        }))

        myView?.stopButton?.setOnClickListener((
        {
            userEventListener?.onStopButtonClicked(this)
        }))

        /* initialize content value */
        myView?.titleText?.text = name

        return myView
    }

    abstract class UserEventListener
    {
        abstract fun onStartButtonClicked(outfitLayout: OutfitLayout)
        abstract fun onStopButtonClicked(outfitLayout: OutfitLayout)
    }
}
