/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\VeepEmPro\\app\\src\\main\\aidl\\com\\example\\ant_test\\service\\aidl\\IPMListener.aidl
 */
package com.example.ant_test.service.aidl;
public interface IPMListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.ant_test.service.aidl.IPMListener
{
private static final java.lang.String DESCRIPTOR = "com.example.ant_test.service.aidl.IPMListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.ant_test.service.aidl.IPMListener interface,
 * generating a proxy if needed.
 */
public static com.example.ant_test.service.aidl.IPMListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.ant_test.service.aidl.IPMListener))) {
return ((com.example.ant_test.service.aidl.IPMListener)iin);
}
return new com.example.ant_test.service.aidl.IPMListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onReceiveMessage:
{
data.enforceInterface(DESCRIPTOR);
com.example.ant_test.service.bean.EmPushMessage _arg0;
if ((0!=data.readInt())) {
_arg0 = com.example.ant_test.service.bean.EmPushMessage.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onReceiveMessage(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.ant_test.service.aidl.IPMListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onReceiveMessage(com.example.ant_test.service.bean.EmPushMessage message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((message!=null)) {
_data.writeInt(1);
message.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onReceiveMessage, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onReceiveMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onReceiveMessage(com.example.ant_test.service.bean.EmPushMessage message) throws android.os.RemoteException;
}
