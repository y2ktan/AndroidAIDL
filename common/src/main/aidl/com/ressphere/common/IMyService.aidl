// IMyService.aidl
package com.ressphere.common;

import com.ressphere.common.IListener;
// Declare any non-default types here with import statements

interface IMyService {
    int add(int a, int b);
    float divide(float a, float b);
    oneway void sendMessage(String id);
    void registerListener(in IListener listener);
    void deregisterListener(in IListener listener);
}