// IListener.aidl
package com.ressphere.common;

interface IListener {
    void onSuccess(in Bundle result);
    void onFailure(in String reason);
}