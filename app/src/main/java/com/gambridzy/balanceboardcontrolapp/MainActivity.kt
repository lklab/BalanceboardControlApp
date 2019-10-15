package com.gambridzy.balanceboardcontrolapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gambridzy.balanceboardcontrolapp.ui.PlaythingLayout

import kotlinx.android.synthetic.main.activity_main.*
import java.net.Socket
import java.io.IOException

class MainActivity : AppCompatActivity()
{
    var playthingLayoutList: ArrayList<PlaythingLayout>? = null
    var userEventListener: UserEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        plaything0.periodSeekBar.setOnTouchListener(object:View.OnTouchListener
//        {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean
//            {
//                if(event?.action == MotionEvent.ACTION_DOWN ||
//                    event?.action == MotionEvent.ACTION_MOVE)
//                {
//                    mainHorizontalScrollView.requestDisallowInterceptTouchEvent(true)
//                }
//
//                return false
//            }
//        })

        playthingLayoutList = ArrayList()
        userEventListener = UserEventListener()

        for(i in 1..6)
        {
            val playthingLayout = PlaythingLayout()
            supportFragmentManager.beginTransaction()
                .add(R.id.mainLinearLayout, playthingLayout)
                .commit()
            playthingLayout.parentHorizontalScrollView = mainHorizontalScrollView
            playthingLayout.name = "교구 " + i + "번"
            playthingLayout.userEventListener = userEventListener

            playthingLayoutList?.add(playthingLayout)
        }
    }

    inner class UserEventListener : PlaythingLayout.UserEventListener()
    {
        override fun onStartButtonClicked(playthingLayout: PlaythingLayout)
        {
            val playthingLayout = PlaythingLayout()
            supportFragmentManager.beginTransaction()
                .add(R.id.mainLinearLayout, playthingLayout)
                .commit()
            playthingLayout.parentHorizontalScrollView = mainHorizontalScrollView
            playthingLayout.name = "교구 번"
            playthingLayout.userEventListener = userEventListener

            playthingLayoutList?.add(playthingLayout)
        }

        override fun onStopButtonClicked(playthingLayout: PlaythingLayout)
        {
            supportFragmentManager.beginTransaction()
                .remove(playthingLayout)
                .commit()

            playthingLayoutList?.remove(playthingLayout)
        }
    }

//    fun onSynchronizedStartButtonClick(view: View)
//    {
//        val thread = Thread(object:Runnable
//            {
//                val ip = "192.168.0.12"
//                val port = 3000
//
//                override fun run()
//                {
//                    var socket: Socket
//
//                    try
//                    {
//                        socket = Socket(ip, port)
//                    }
//                    catch(e: IOException)
//                    {
//                        System.out.println("IOException!!")
//                        return
//                    }
//
//                    try
//                    {
//                        val outStream = socket.outputStream
//                        var data = ByteArray(5)
//                        data[0] = 0x20
//                        data[1] = 0x01
//                        data[2] = 0x01
//                        data[3] = 0x05
//                        data[4] = 0x12
//                        outStream.write(data)
//                    }
//                    catch(e: IOException)
//                    {
//                        System.out.println("IOException!!")
//                    }
//
//                    socket.close()
//                }
//            })
//
//        thread.start()
//
// //       synchronizedStatusText.text = "hello"
//    }
}
