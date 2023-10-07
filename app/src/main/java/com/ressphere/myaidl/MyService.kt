package com.ressphere.myaidl

import android.os.Bundle
import android.os.DeadObjectException
import android.util.Log
import com.ressphere.common.*

class MyService: IMyService.Stub() {
    private val listeners = mutableListOf<IListener>()
    override fun add(a: Int, b: Int): Int {
        return a + b
    }

    override fun divide(a: Float, b: Float): Float {
        return if (b != 0.00f) a / b else 0.0f
    }

    override fun sendMessage(id: String?) {
        if((id?.toInt()?.rem(2) ?: 0) == 0) {
            listeners.forEach {
                it.onFailure("$id not found")
            }
            return
        }
        val myData = MyData("anonymous", 100)

        val result = Bundle().also {
            it.putParcelable("result", myData)
        }
        val deadListeners = mutableListOf<IListener>()
        for(listener in listeners) {
            try {
                listener.onSuccess(result)
            } catch(deadObject: DeadObjectException) {
                Log.w(TAG, "listener is dead!!!")
                deadListeners.add(listener)
            }
        }
        listeners.removeAll(deadListeners)
    }

    override fun registerListener(listener: IListener) {
        listeners.add(listener)
    }

    override fun deregisterListener(listener: IListener) {
        listeners.remove(listener)
    }


    companion object {
        private const val TAG = "MyService"
    }
}