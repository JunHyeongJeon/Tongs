package com.tongs.user.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

public class BleManager implements BluetoothAdapter.LeScanCallback
{
	private static final String TAG = "BleManager";
	
	public static boolean isDebug = false; 
	
	public static final int SCAN_INTERVAL = 300000;
	public static final int SCAN_PERIOD = 5000;
	public static final int STATE_ERROR = -1;
	public static final int STATE_IDLE = 1;
	public static final int STATE_NONE = 0;
	public static final int STATE_SCANNING = 2;
	
	private static BleManager mBleManager;
	private static Context mContext = null;
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	
	private ArrayList<MyBeacon> mBeaconList;
	private ArrayList<BluetoothDevice> mDeviceList;
	
	
	private int mState = 0;
	

	private BleManager(Context context, Handler handler) 
	{
		mHandler = handler;
		mContext = context;
		
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mBeaconList = new ArrayList<MyBeacon>();
		mDeviceList = new ArrayList<BluetoothDevice>();
	}

	public static BleManager getInstance(Context context, Handler handler) 
	{
		if (mBleManager == null)
			mBleManager = new BleManager(context, handler);
		return mBleManager;
	}

	public void finalize() 
	{
		if (this.mAdapter == null)
			return;
		
		stopScanning();
	}

	public ArrayList<MyBeacon> getBeaconList()
	{
		return this.mBeaconList;
	}

	public void deleteBeaconName(String uuid, int major, int minor) 
	{
        MyBeacon foundObj = null;
		for(MyBeacon beacon : mBeaconList)
		{
			if(beacon.getProximityUuid().equalsIgnoreCase(uuid) && beacon.getMajor() == major && beacon.getMinor() == minor)
			{
				beacon.setBeaconName(null);
				foundObj = beacon;
				break;
			}
		}
		
		if(foundObj != null)
			mBeaconList.remove(foundObj);
	}
	
	public void sendHandler(int msgId)
	{
		if(mHandler == null)
			return;
		
		this.mHandler.obtainMessage(msgId).sendToTarget();
	}
	
	public void sendHandler(int msgId, Object data)
	{
		if(mHandler == null)
			return;
		
		this.mHandler.obtainMessage(msgId, data).sendToTarget();
	}
	
	private void stopScanning() 
	{
		this.mAdapter.stopLeScan(this);
//		this.mAdapter.getBluetoothLeScanner().stopScan(this);
		if(this.mState == STATE_SCANNING)
			sendHandler(111);
		this.mState = STATE_IDLE;
	}
	  
	public boolean scanLeDevice(boolean isActive)
	{
		if (!isActive)
		{
			stopScanning();
			return true;
		}
		
		if (this.mState == STATE_SCANNING)
			return false;

		if (mAdapter.startLeScan(this)) 
		{
			this.mState = STATE_SCANNING;
			this.mDeviceList.clear();
			this.mBeaconList.clear();
			this.mHandler.postDelayed(new Runnable() 
			{
				public void run() 
				{
					stopScanning();
				}
			}, SCAN_PERIOD);
			sendHandler(111);
			
			return true;
		}
		else
		{
			sendHandler(112, "Fail startLeScan");
			stopScanning();
			return false;
		}
	}
	
	
	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) 
	{
		if ((device != null) && (scanRecord != null)) 
		{
//			BleManager.this.mDeviceList.add(device);
			String name = device.getName();

            MyBeacon beacon = MyBeacon.fromScanData(scanRecord, rssi, device);
			beacon.setBeaconName(name);

            if( rssi < 0.1 )
       			sendHandler(112, beacon.getSummary());
		}
	}
}
