/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\VeepEmPro\\app\\src\\main\\aidl\\com\\example\\ant_test\\service\\aidl\\IPMService.aidl
 */
package com.example.ant_test.service.aidl;
public interface IPMService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.ant_test.service.aidl.IPMService
{
private static final java.lang.String DESCRIPTOR = "com.example.ant_test.service.aidl.IPMService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.ant_test.service.aidl.IPMService interface,
 * generating a proxy if needed.
 */
public static com.example.ant_test.service.aidl.IPMService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.ant_test.service.aidl.IPMService))) {
return ((com.example.ant_test.service.aidl.IPMService)iin);
}
return new com.example.ant_test.service.aidl.IPMService.Stub.Proxy(obj);
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
case TRANSACTION_setListener:
{
data.enforceInterface(DESCRIPTOR);
com.example.ant_test.service.aidl.IPMListener _arg0;
_arg0 = com.example.ant_test.service.aidl.IPMListener.Stub.asInterface(data.readStrongBinder());
this.setListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.ant_test.service.aidl.IPMService
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
@Override public void setListener(com.example.ant_test.service.aidl.IPMListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void setListener(com.example.ant_test.service.aidl.IPMListener listener) throws android.os.RemoteException;
}
