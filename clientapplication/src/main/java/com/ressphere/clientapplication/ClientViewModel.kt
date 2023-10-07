package com.ressphere.clientapplication

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.IInterface
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.ressphere.common.IListener
import com.ressphere.common.IMyService
import com.ressphere.common.MyData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClientViewModel(private val application: Application): AndroidViewModel(application) {
    private var mService: IMyService? = null
    private val _successResult = MutableStateFlow(MyData("", 0))
    val successResult = _successResult.asStateFlow()

    private val listener = object : IListener.Stub() {
        override fun asBinder(): IBinder {
            return this
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onSuccess(result: Bundle?) {
            result?.let {
                it.classLoader = MyData::class.java.classLoader
                val data = it.getParcelable("result", MyData::class.java)
                data?.let {
                    _successResult.value = data.copy()
                    //val snackbar = Snackbar.make(application, "Message is deleted", Snackbar.LENGTH_LONG)
                }
            }

        }

        override fun onFailure(reason: String?) {
            Log.e(TAG, "reason: $reason")
        }

    }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?,
                                        service: IBinder?) {
            Log.d(TAG, "onServiceConnected: $name")
            mService = IMyService.Stub.asInterface(service)
            mService?.registerListener(listener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: $name")
            // Service disconnected, perform any necessary cleanup
            mService?.deregisterListener(listener)
            mService = null
            //mService?.deregisterListener(listener)
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.d(TAG, "onBindingDied: $name")
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.d(TAG, "onNullBinding: $name")
            super.onNullBinding(name)
        }
    }

    fun addOperation(num1: Int, num2: Int): Int {
        val result = mService?.add(num1, num2)
        Log.d(TAG, "$num1 + $num2= $result")
        return result ?: 0
    }

    fun findDataById(id: String) {
        mService?.sendMessage(id)
    }

    fun divideOperation(num1: Float, num2: Float): Float {
        return mService?.divide(num1, num2) ?: 0.0f
    }

    private fun bindToServerService() {
        Log.d(TAG, "bind to the service")
        val componentName = ComponentName(
            "com.ressphere.myaidl",
            "com.ressphere.myaidl.ServerService")
        val serviceIntent = Intent().also {
            it.component = componentName
        }
        application.applicationContext.bindService(serviceIntent,
            serviceConnection, Context.BIND_AUTO_CREATE)
    }

    init {
        bindToServerService()
    }

    override fun onCleared() {
        Log.d(TAG, "unbind from the service")
        application.unbindService(serviceConnection)
    }

    companion object {
        private const val TAG = "ClientViewModel"
    }
}