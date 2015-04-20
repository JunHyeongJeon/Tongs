package com.example.jaecheol.ble;

import android.bluetooth.BluetoothDevice;

import java.util.HashMap;

public class MyBeacon 
{
	public static final int PROXIMITY_FAR = 3;
	public static final int PROXIMITY_IMMEDIATE = 1;
	public static final int PROXIMITY_NEAR = 2;
	public static final int PROXIMITY_UNKNOWN = 0;
	protected Double accuracy;
	protected Double accuracy2;
	protected String beaconName;
	protected String bluetoothAddress;
	protected int id = -1;
	protected boolean isRemembered = false;
	protected int major;
	protected int minor;
	protected Integer proximity;
	protected String proximityUuid;
	protected byte[] proximityUuidBytes;
	protected int rssi;
	protected Double runningAverageRssi = null;
	protected int txPower;

	protected MyBeacon() {
	}

	public MyBeacon(MyBeacon beacon) 
	{
		this.id = beacon.id;
		this.beaconName = beacon.beaconName;
		this.isRemembered = beacon.isRemembered;
		this.major = beacon.major;
		this.minor = beacon.minor;
		this.accuracy = beacon.accuracy;
		this.proximity = beacon.proximity;
		this.proximityUuidBytes = beacon.proximityUuidBytes;
		this.runningAverageRssi = beacon.runningAverageRssi;
		this.rssi = beacon.rssi;
		this.proximityUuid = beacon.proximityUuid;
		this.txPower = beacon.txPower;
		this.bluetoothAddress = beacon.bluetoothAddress;
	}

	public MyBeacon(String uuid, int major, int minor) 
	{
		this.proximityUuid = uuid.toLowerCase();
		this.proximityUuidBytes = null;
		this.major = major;
		this.minor = minor;
		this.txPower = -59;
		this.rssi = 0;
	}

	protected MyBeacon(String uuid, int major, int minor, int rssi, int txPower) 
	{		
		this.proximityUuid = uuid.toLowerCase();
		this.proximityUuidBytes = null;
		this.major = major;
		this.minor = minor;
		this.rssi = txPower;
		this.txPower = rssi;
	}

	public static String bytesToHex(byte[] bytes) 
	{
		StringBuilder sb = new StringBuilder();
		int len = bytes.length;
		Byte bObj;
		for (int j = 0; j<len; j++) 
		{
			bObj = Byte.valueOf(bytes[j]);
			sb.append(String.format("%02X", bObj));
		}
		
		return sb.toString();
	}

	public static double calculateAccuracy(int txPower, double rssi) 
	{
		if (rssi == 0.0D)
			return -1.0D;
		
		double d = rssi * 1.0D / txPower;
		if (d < 1.0D)
			return Math.pow(d, 10.0D);
		
		return 0.111D + 0.89976D * Math.pow(d, 7.7095D);
	}
	
	public static double calculateDistance(int txPower, double rssi) 
	{
        if (rssi == 0) 
        {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double mCoefficient1 = 0.42093;
        double mCoefficient2 = 6.9476;
        double mCoefficient3 = 0.54992;

        double ratio = rssi*1.0/txPower;
        double distance;
        if (ratio < 1.0) {
            distance =  Math.pow(ratio,10);
        }
        else {
            distance =  (mCoefficient1)*Math.pow(ratio,mCoefficient2) + mCoefficient3;
        }

        return distance;
    }

	public static int calculateProximity(double accuracy) 
	{
		if (accuracy < 0.0D)
			return PROXIMITY_UNKNOWN;
		if (accuracy < 0.5D)
			return PROXIMITY_IMMEDIATE;
		if (accuracy <= 4.0D)
			return PROXIMITY_NEAR;
		return PROXIMITY_FAR;
	}

	public static MyBeacon fromScanData(byte[] scanRecord, int rssi) 
	{
		return fromScanData(scanRecord, rssi, null);
	}

	public static MyBeacon fromScanData(byte scanRecord[], int rssi, BluetoothDevice device) 
	{
		int j = 5;
		
		MyBeacon beacon = new MyBeacon();
		beacon.major = 256 * (0xff & scanRecord[j + 20]) + (0xff & scanRecord[j + 21]);
		beacon.minor = 256 * (0xff & scanRecord[j + 22]) + (0xff & scanRecord[j + 23]);
		beacon.txPower = scanRecord[j + 24];
		beacon.rssi = rssi;
		beacon.accuracy = Double.valueOf(calculateAccuracy(beacon.txPower, beacon.rssi));
		beacon.accuracy2 = calculateDistance(beacon.txPower, beacon.rssi);
		beacon.proximity = beacon.getProximity();
		
		byte uuidRaw[] = new byte[16];
		System.arraycopy(scanRecord, j + 4, uuidRaw, 0, 16);
		beacon.proximityUuidBytes = uuidRaw;
		String s = bytesToHex(uuidRaw);
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(0, 8));
		sb.append("-");
		sb.append(s.substring(8, 12));
		sb.append("-");
		sb.append(s.substring(12, 16));
		sb.append("-");
		sb.append(s.substring(16, 20));
		sb.append("-");
		sb.append(s.substring(20, 32));
		beacon.proximityUuid = sb.toString();

		return beacon;
	}
	
	public void copyFromBeacon(MyBeacon from) 
	{
		this.id = from.id;
		this.isRemembered = from.isRemembered;
		this.beaconName = new String(from.beaconName);
		this.proximityUuid = new String(from.getProximityUuid());
		this.proximityUuidBytes = null;
		this.major = from.major;
		this.minor = from.minor;
		this.proximity = from.proximity;
		this.accuracy = from.accuracy;
		this.rssi = from.rssi;
		this.txPower = from.txPower;
		this.bluetoothAddress = new String(from.bluetoothAddress);
	}

	public boolean equals(Object obj) 
	{
		if (!(obj instanceof MyBeacon))
			return false;

		MyBeacon beacon = (MyBeacon) obj;
		if ((beacon.getMajor() != getMajor())
				|| (beacon.getMinor() != getMinor())
				|| (!beacon.getProximityUuid().equals(getProximityUuid())))
			return false;

		return true;
	}

	public double getAccuracy() 
	{
		if (this.accuracy == null) {
			double d = this.rssi;
			if (this.runningAverageRssi != null)
				d = this.runningAverageRssi.doubleValue();
			this.accuracy = Double.valueOf(calculateAccuracy(this.txPower, d));
		}
		return this.accuracy.doubleValue();
	}

	public String getBeaconName() 
	{
		return this.beaconName;
	}

	public String getBluetoothAddress() 
	{
		return this.bluetoothAddress;
	}

	public int getId() 
	{
		return this.id;
	}

	public boolean getIsRemembered() 
	{
		return this.isRemembered;
	}

	public int getMajor() 
	{
		return this.major;
	}

	public int getMinor() 
	{
		return this.minor;
	}

	public int getProximity() 
	{
		if (this.proximity == null)
			this.proximity = Integer.valueOf(calculateProximity(getAccuracy()));
		return this.proximity.intValue();
	}

	public String getProximityUuid() 
	{
		return this.proximityUuid;
	}

	public byte[] getProximityUuidBytes() 
	{
		return this.proximityUuidBytes;
	}

	public int getRssi() 
	{
		return this.rssi;
	}

	public int getTxPower() 
	{
		return this.txPower;
	}

	public int hashCode() 
	{
		return this.minor;
	}

	public void setBeaconName(String paramString) 
	{
		this.beaconName = paramString;
	}

	public void setId(int paramInt) 
	{
		this.id = paramInt;
	}

	public void setIsRemembered(boolean paramBoolean) 
	{
		this.isRemembered = paramBoolean;
	}

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }


	public String getSummary()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(beaconName);
		sb.append("_");
		sb.append(proximityUuid);
		sb.append("_");
		sb.append(major);
		sb.append("_");
		sb.append(minor);
		sb.append("_");
        sb.append(String.format("%.2f", accuracy2));
		return sb.toString();
	}
	
	public String toString()
	{
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("beaconName", beaconName);
		map.put("proximityUuid", proximityUuid);
		map.put("major", major);
		map.put("minor", minor);
		map.put("proximity", proximity);
		map.put("accuracy", accuracy);
		map.put("accuracy2", accuracy2);
		map.put("rssi", rssi);
		map.put("txPower", txPower);
		map.put("bluetoothAddress", bluetoothAddress);
		return map.toString();
	}

}
