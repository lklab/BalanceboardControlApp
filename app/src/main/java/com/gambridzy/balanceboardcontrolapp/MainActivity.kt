package com.gambridzy.balanceboardcontrolapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gambridzy.balanceboardcontrolapp.data.Command
import com.gambridzy.balanceboardcontrolapp.network.ServerInterface
import com.gambridzy.balanceboardcontrolapp.ui.OutfitLayout

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    var outfitLayoutList: ArrayList<OutfitLayout>? = null
    var userEventListener: UserEventListener? = null
    var serverInterface: ServerInterface? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outfitLayoutList = ArrayList()
        userEventListener = UserEventListener()
        serverInterface = ServerInterface(null)

        for(i in 1..6)
        {
            val outfitLayout = OutfitLayout()
            supportFragmentManager.beginTransaction()
                .add(R.id.mainLinearLayout, outfitLayout)
                .commit()
            outfitLayout.parentHorizontalScrollView = mainHorizontalScrollView
            outfitLayout.name = "교구 " + i + "번"
            outfitLayout.userEventListener = userEventListener

            outfitLayoutList?.add(outfitLayout)
        }
    }

    inner class UserEventListener : OutfitLayout.UserEventListener()
    {
        override fun onStartButtonClicked(outfitLayout: OutfitLayout)
        {
            val newOutfitLayout = OutfitLayout()
            supportFragmentManager.beginTransaction()
                .add(R.id.mainLinearLayout, newOutfitLayout)
                .commit()
            newOutfitLayout.parentHorizontalScrollView = mainHorizontalScrollView
            newOutfitLayout.name = "교구 번"
            newOutfitLayout.userEventListener = userEventListener

            outfitLayoutList?.add(newOutfitLayout)

            serverInterface?.getOutfitList()
        }

        override fun onStopButtonClicked(outfitLayout: OutfitLayout)
        {
            supportFragmentManager.beginTransaction()
                .remove(outfitLayout)
                .commit()

            outfitLayoutList?.remove(outfitLayout)

            serverInterface?.command(Command(1, 2, 3, 4))
        }
    }
}
