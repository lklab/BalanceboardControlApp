package com.gambridzy.balanceboardcontrolapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.SeekBar
import com.gambridzy.balanceboardcontrolapp.R
import com.gambridzy.balanceboardcontrolapp.data.Command
import com.gambridzy.balanceboardcontrolapp.data.Outfit
import kotlinx.android.synthetic.main.outfit_layout.view.*

class OutfitLayout : Fragment()
{
    var myView: View? = null

    var outfitId: Int = -1

    var name: String = ""
    var status: String = ""

    var signalPeriod: Int = 0
    var changeTime: Int = 0

    var parentHorizontalScrollView: HorizontalScrollView? = null
    var userEventListener: UserEventListener? = null

    private var signalPeriodChangedInternally = false
    private var changeTimeChangedInternally = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        myView = inflater.inflate(R.layout.outfit_layout, null)

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

        /* bind seek bar events */
        myView?.periodSeekBar?.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                if(signalPeriodChangedInternally)
                {
                    signalPeriodChangedInternally = false
                    return
                }

                signalPeriod = progress
                signalPeriodChangedInternally = true
                myView?.periodText?.setText(signalPeriod.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        myView?.changeTimeSeekBar?.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                if(changeTimeChangedInternally)
                {
                    changeTimeChangedInternally = false
                    return
                }

                changeTime = progress
                changeTimeChangedInternally = true
                myView?.changeTimeText?.setText(changeTime.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        /* bind edit text events */
        myView?.periodText?.addTextChangedListener(object:TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if(signalPeriodChangedInternally)
                {
                    signalPeriodChangedInternally = false
                    return
                }

                if(myView?.periodText?.text.toString() == "")
                    signalPeriod = 0
                else
                {
                    try {
                        signalPeriod = Integer.parseInt(myView?.periodText?.text.toString())
                    } catch(e: NumberFormatException){
                        signalPeriodChangedInternally = true
                        myView?.periodText?.setText(signalPeriod.toString())
                        return
                    }
                }

                if(signalPeriod > 3000)
                {
                    signalPeriod = 3000
                    signalPeriodChangedInternally = true
                    myView?.periodText?.setText(signalPeriod.toString())
                }
                else if(signalPeriod < 0)
                {
                    signalPeriod = 0
                    signalPeriodChangedInternally = true
                    myView?.periodText?.setText(signalPeriod.toString())
                }

                signalPeriodChangedInternally = true
                myView?.periodSeekBar?.progress = signalPeriod
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        myView?.changeTimeText?.addTextChangedListener(object:TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {
                if(changeTimeChangedInternally)
                {
                    changeTimeChangedInternally = false
                    return
                }

                if(myView?.changeTimeText?.text.toString() == "")
                    changeTime = 0
                else
                {
                    try {
                        changeTime = Integer.parseInt(myView?.changeTimeText?.text.toString())
                    } catch(e: NumberFormatException){
                        changeTimeChangedInternally = true
                        myView?.changeTimeText?.setText(changeTime.toString())
                        return
                    }
                }

                if(changeTime > 10)
                {
                    changeTime = 10
                    changeTimeChangedInternally = true
                    myView?.changeTimeText?.setText(changeTime.toString())
                }
                else if(changeTime < 0)
                {
                    changeTime = 0
                    changeTimeChangedInternally = true
                    myView?.changeTimeText?.setText(changeTime.toString())
                }

                changeTimeChangedInternally = true
                myView?.changeTimeSeekBar?.progress = changeTime
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        /* initialize content value */
        initializeUI()
        updateUI()

        return myView
    }

    fun setOutfit(outfit: Outfit)
    {
        outfitId = outfit.id

        name = "교구 " + outfitId.toString() + "번"
        signalPeriod = outfit.signalPeriod
        changeTime = outfit.changeTime

        updateOutfit(outfit)
    }

    fun updateOutfit(outfit: Outfit)
    {
        status = when(outfit.exercise)
        {
            "NO" ->
                "대기중"
            "BI" ->
                "BI운동-${outfit.level}-${outfit.motion}"
            "DI" ->
                "방향성-${outfit.level}-${outfit.motion}"
            "BA" ->
                "균형-${outfit.level}-${outfit.motion}"
            "RO" ->
                "회전-${outfit.level}-${outfit.motion}"
            else ->
                "대기중"
        }

        updateUI()
    }

    fun getCommand() : Command
    {
        val exercise = when(myView?.exerciseSpinner?.selectedItem)
        {
            "BI 운동" -> "BI"
            "방향성" -> "DI"
            "균형" -> "BA"
            "회전" -> "RO"
            else -> "BI"
        }

        val level = Integer.parseInt(myView?.levelSpinner?.selectedItem as String)

        return Command(
            outfitId,
            0, /* command_none */
            exercise,
            level,
            signalPeriod,
            changeTime
        )
    }

    private fun initializeUI()
    {
        myView?.titleText?.text = name

        if(signalPeriod > 3000)
            signalPeriod = 3000
        else if(signalPeriod < 0)
            signalPeriod = 0

        if(changeTime > 10)
            changeTime = 10
        else if(changeTime < 0)
            changeTime = 0

        myView?.periodSeekBar?.progress = signalPeriod
        myView?.periodText?.setText(signalPeriod.toString())
        myView?.changeTimeSeekBar?.progress = changeTime
        myView?.changeTimeText?.setText(changeTime.toString())
    }

    private fun updateUI()
    {
        myView?.statusText?.text = status
    }

    abstract class UserEventListener
    {
        abstract fun onStartButtonClicked(outfitLayout: OutfitLayout)
        abstract fun onStopButtonClicked(outfitLayout: OutfitLayout)
    }
}
