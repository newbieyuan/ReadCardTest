/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.yy.keyboxlib;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * 串口通讯的 寻检器
 * Serial communication finder
 */
public class SerialPortFinder {

	/**
	 * 设备类
	 * Device class
	 */
	public static class Driver {
		public Driver(String name, String root) {
			mDriverName = name;
			mDeviceRoot = root;
		}
		private String mDriverName;//设备名称
		private String mDeviceRoot;//设备根节点
		Vector<File> mDevices = null;//设备集合
		/**
		 * 获取设备集合 这个是特定类型的设备 比如USB等
		 * Get the device collection, which is a particular type of device, such as USB
		 * @return
		 */
		public Vector<File> getDevices() {
			//如果设备已经添加过，就不添加了，否则就必须添加
			//If the device has been added, it will not be added, otherwise it must be added
			if (mDevices == null) {
				mDevices = new Vector<File>();
				File dev = new File("/dev");
				//获取 /dev 下的设备
				//Get the equipment under /dev
				File[] files = dev.listFiles();
				int i;
				for (i=0; i<files.length; i++) {
					//这里是拿文件的路径和我们 传递进来的路径进行对比，看看开头是不是我们传递捡来的  ，传递进来的函数是 外部类 getDrivers()
					//Here is the path to the file, comparing it with the path we sent in to see if the first one is passed or picked up, and the passed function is the external class getDrivers ()
					//比如 我们传递进来的文件路径是：  /dev/ttyUSB 那么我们获取道的文件的绝对路径 如：/dev/ttyUSB1 /dev/ttyUSB2
					//For example, the file path we pass in is: /dev/ttyUSB, then we get the absolute path to the file of the Tao, such as: /dev/ttyUSB1, /dev/ttyUSB2
					//就满足下面这个条件 ，就会加到集合中去
					//Satisfied with the following conditions, will be added to the collection

					if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
						Log.d(TAG, "Found new device: " + files[i]);
						mDevices.add(files[i]);
					}
				}
			}
			return mDevices;
		}
		public String getName() {
			return mDriverName;
		}
	}

	private static final String TAG = "SerialPort";

	/**
	 * 获取设备
	 * Acquisition device
	 * @return
	 * @throws IOException
	 * 其实就是读取 /proc/tty/drivers 这个文件
	 * drivers中有设备的地址的总地址添加到一个集合中
	 */
	private static List<Driver> getDriver() throws IOException {
		ArrayList var0 = new ArrayList();
		//读取行的流对象
		LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));

		String l;
		while(!TextUtils.isEmpty(l = r.readLine())) {
			// Issue 3:
			// Since driver name may contain spaces, we do not extract driver name with split()
			//看下面这个会发现 原来我们的 这个第一字符到第二个字符之间是21个，这也就是为什么这里面要用的 0x15 这个原因了
			//dev/tty             /dev/tty        5       0 system:/dev/tty
			String driverName = l.substring(0, 0x15).trim();//其实就是获取第一个非空格字符串
			String[] w =l.split(" +"); //这里是正则表达式：" +" 表示有一个或者多个空格
			if (w.length >= 5 && w[w.length - 1].equals("serial")) {
				Log.d(TAG, "Found new driver " + driverName + " on " + w[w.length-4]);
				var0.add(new SerialPortFinder.Driver(driverName, w[w.length - 4]));
			}
		}

		r.close();
		return var0;
	}
	/**
	 * 获取道设备的路径
	 * Gets the path to the lane device
	 * @return
	 */
	public static List<String> getSerialPortPathList() throws IOException {
		ArrayList var0 = new ArrayList();
		Iterator var1 = getDriver().iterator();

		while(var1.hasNext()) {
			Iterator var2 = ((SerialPortFinder.Driver)var1.next()).getDevices().iterator();

			while(var2.hasNext()) {
				File var3 = (File)var2.next();
				var0.add(var3.getAbsolutePath());
			}
		}

		return var0;
	}
	/**
	 * 得到所有的设备的名称
	 * Get the name of all the equipment
	 * @return
	 */
	public String[] getAllDevices() {
		Vector<String> devices = new Vector<String>();
		// Parse each driver
		Iterator<Driver> itdriv;
		try {
			itdriv = getDriver().iterator();//获取道设备的根地址
			while(itdriv.hasNext()) {//迭代获取具体的根路径
				Driver driver = itdriv.next();
				Iterator<File> itdev = driver.getDevices().iterator();//获取道包含根地址的设备对象的集合
				while(itdev.hasNext()) {//迭代获得具体的设备
					String device = itdev.next().getName();
					String value = String.format("%s (%s)", device, driver.getName());
					devices.add(value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devices.toArray(new String[devices.size()]);
	}
}
