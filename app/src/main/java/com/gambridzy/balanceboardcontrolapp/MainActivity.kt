package com.gambridzy.balanceboardcontrolapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gambridzy.balanceboardcontrolapp.data.Command
import com.gambridzy.balanceboardcontrolapp.data.Outfit
import com.gambridzy.balanceboardcontrolapp.network.RequestResult
import com.gambridzy.balanceboardcontrolapp.network.ResultState
import com.gambridzy.balanceboardcontrolapp.network.ServerInterface
import com.gambridzy.balanceboardcontrolapp.ui.OutfitLayout

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private var outfitLayoutList: ArrayList<OutfitLayout> = ArrayList()
    private var userEventListener: UserEventListener = UserEventListener()
    private var serverInterface: ServerInterface = ServerInterface(OnServerResponseListener())

    private var outfitMonitoringTaskHandler = Handler()
    private var outfitMonitoringTask = OutfitMonitoringTask()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outfitMonitoringTaskHandler.post(outfitMonitoringTask)

        val newOutfitLayout = OutfitLayout()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainLinearLayout, newOutfitLayout)
            .commit()
        newOutfitLayout.parentHorizontalScrollView = mainHorizontalScrollView
        newOutfitLayout.name = "교구 번"
        newOutfitLayout.userEventListener = userEventListener

        outfitLayoutList.add(newOutfitLayout)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        outfitMonitoringTaskHandler.removeCallbacks(outfitMonitoringTask)
    }

    inner class OutfitMonitoringTask : Runnable
    {
        override fun run()
        {
            serverInterface.getOutfitList()
            outfitMonitoringTaskHandler.postDelayed(this, 1000)
        }
    }

    inner class OnServerResponseListener : ServerInterface.OnResponseListener()
    {
        override fun onGetOutfitList(requestResult: RequestResult<ArrayList<Outfit>?>)
        {
            if(requestResult.resultState == ResultState.SUCCESS)
            {
                requestResult.data?.let{
                    /* check update */
                    for(outfit in it)
                    {
                        var foundLayout: OutfitLayout? = null
                        for(layout in outfitLayoutList)
                        {
                            if(outfit.id == layout.outfitId)
                            {
                                foundLayout = layout
                                break
                            }
                        }

                        if(foundLayout != null)
                            foundLayout.updateOutfit(outfit)
                        else
                        {
                            val outfitLayout = OutfitLayout()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.mainLinearLayout, outfitLayout)
                                .commit()
                            outfitLayout.parentHorizontalScrollView = mainHorizontalScrollView
                            outfitLayout.userEventListener = userEventListener
                            outfitLayout.setOutfit(outfit)

                            outfitLayoutList.add(outfitLayout)
                        }
                    }

                    /* check disconnected */
                    val toRemoveOutfitLayoutList = ArrayList<OutfitLayout>()
                    for(layout in outfitLayoutList)
                    {
                        var found = false
                        for(outfit in it)
                        {
                            if(outfit.id == layout.outfitId)
                            {
                                found = true
                                break
                            }
                        }

                        if(!found)
                        {
                            supportFragmentManager.beginTransaction()
                                .remove(layout)
                                .commit()

                            toRemoveOutfitLayoutList.add(layout)
                        }
                    }
                    for(layout in toRemoveOutfitLayoutList)
                        outfitLayoutList.remove(layout)
                }
            }
        }

        override fun onCommand(requestResult: RequestResult<Void?>)
        {
            // do nothing
        }
    }

    inner class UserEventListener : OutfitLayout.UserEventListener()
    {
        override fun onStartButtonClicked(outfitLayout: OutfitLayout)
        {
            var command = outfitLayout.getCommand()
            command.type = 2 /* command_start */
            serverInterface.command(command)
        }

        override fun onStopButtonClicked(outfitLayout: OutfitLayout)
        {
            var command = outfitLayout.getCommand()
            command.type = 3 /* command_stop */
            serverInterface.command(command)
        }
    }
}
