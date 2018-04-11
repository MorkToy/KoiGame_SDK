package koigame.sdk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import koigame.sdk.api.KThread;
import koigame.sdk.view.dialog.KoiAutoLoginDialog;
import koigame.sdk.view.dialog.KoiWaitDialog;

import static android.content.Context.ACTIVITY_SERVICE;

public class AndroidUtils {

	private static AndroidUtils instance = new AndroidUtils();

	private static ProgressDialog dialog;

	private static KoiWaitDialog koiWaitDialog;

	private static KoiAutoLoginDialog koiAutoLoginDialog;



	private AndroidUtils() {

	}

	public static AndroidUtils instance() {
		return instance;
	}

	/**
	 * 获取权限
	 * 
	 * @param permission
	 *            权限
	 * @param path
	 *            路径
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void showAutologinProgress(final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (koiAutoLoginDialog != null) {
					koiAutoLoginDialog.dismiss();
				}
				koiAutoLoginDialog = new KoiAutoLoginDialog(activity, RUtils.getStyle("koi_dialog"));
				koiAutoLoginDialog.show();
			}
		});
	}

	public static KoiAutoLoginDialog closeAutologinPorgress(final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (koiAutoLoginDialog != null) {
					koiAutoLoginDialog.dismiss();
				}
			}
		});
		return koiAutoLoginDialog;
	}

	public static void showCicleProgress(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (koiWaitDialog != null) {
					koiWaitDialog.dismiss();
				}
				koiWaitDialog = new KoiWaitDialog(activity, RUtils.getStyle("koi_dialog"), message);
				koiWaitDialog.show();
			}
		});
	}

	public static void closeCiclePorgress(final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (koiWaitDialog != null) {
					koiWaitDialog.dismiss();
					koiWaitDialog = null;
				}
			}
		});
	}

	/**
	 * 显示处理等待框
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 */
	public static void showProgress(final Activity activity, final CharSequence title, final CharSequence message,
			final boolean indeterminate, final boolean cancelable, final KThread progressThread) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (dialog != null) {
						dialog.dismiss();
					}

					dialog = new ProgressDialog(activity);
					dialog.setTitle(title);
					dialog.setMessage(message);
					dialog.setIndeterminate(indeterminate);
					dialog.setCancelable(cancelable);
					if (cancelable && progressThread != null) {
						dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								progressThread.safeDestory();
							}
						});
					}

					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * 关闭等待框
	 * 
	 * @param activity
	 */
	public static void closeProgress(final Activity activity) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 提示
	 * 
	 * @param activity
	 * @param message
	 * @param type
	 */
	public static void showToast(final Activity activity, final String message, final int type) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(activity, message, type).show();
			}
		});

	}

	/**
	 * 生成设备号
	 * 
	 * @param ctx
	 * @return
	 */
	public String fetchUdid(Context ctx) {
//		String udid = "";
//		try {
//			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
//			String deviceId = tm.getDeviceId();
//			if (null == deviceId || "".equals(deviceId) || deviceId.contains("000000") || deviceId.length() < 7) {
//				WifiManager wifi = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
//				WifiInfo info = wifi.getConnectionInfo();
//				String mac = info.getMacAddress();
//				if (null == mac || "".equals(mac)) {
//					String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
//					if (null == android_id || "".equals(android_id)) {
//
//					} else {
//						udid = android_id;
//					}
//				} else {
//					udid = mac;
//				}
//			} else {
//				udid = deviceId;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		return "udid";
	}

	/**
	 * 获得meta数据
	 * 
	 * @param ctx
	 * @param key
	 * @return
	 */
	public String getMetaData(Context ctx, String key) {
		ApplicationInfo ai;
		try {
			ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			Object obj = bundle.get(key);
			String source = (obj == null ? "" : String.valueOf(obj));

			return source;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获得ApplicationInfo
	 * 
	 * @param activity
	 * @return
	 */
	public ApplicationInfo getApplicationInfo(Activity activity) {
		PackageManager packMgr = activity.getApplication().getPackageManager();
		String packageName = activity.getApplication().getPackageName();
		ApplicationInfo info = null;
		try {
			info = packMgr.getApplicationInfo(packageName, Activity.MODE_WORLD_WRITEABLE);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * cpu类型
	 */
	public String[] getCPUAbi() {
		String[] infos = { "", "" };
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File("/system/build.prop")));

			infos[0] = properties.getProperty("ro.product.cpu.abi");
			infos[1] = properties.getProperty("ro.product.cpu.abi2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 复制assets文件
	 */
	public void copyAssetsFile(Context context, String assetsFilePath, String destFilePath) throws IOException {
		FileUtils.writeStringToFile(new File(destFilePath), "");
		InputStream input = context.getAssets().open(assetsFilePath);
		OutputStream output = new FileOutputStream(destFilePath);
		byte[] buffer = new byte[1024];
		int length = input.read(buffer);
		while (length > 0) {
			output.write(buffer, 0, length);
			length = input.read(buffer);
		}

		output.flush();
		input.close();
		output.close();
	}
	
	/**
	 * 获取本机mac地址
	 * 
	 * @param context
	 * @return
	 */
	 public String getLocalMacAddress(Context context) 
	 {
//	      String macSerial = "simulator";
//	      String str = "";
//	  
//	      try 
//	      {
//	         Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
//	         InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//	         LineNumberReader input = new LineNumberReader(ir);
//	 
//	         for (; null != str;) 
//	         {
//	             str = input.readLine();
//	             if (str != null)
//	             {
//	                 macSerial = str.trim();// 去空格
//	                 break;
//	             }
//	         }
//	     } catch (IOException ex) {
//	         // 赋予默认值
//	         ex.printStackTrace();
//	         return "simulator";
//	     }
	     return "127.0.0.1";
	 }

	/**
	 * 生成本机的ip地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress(Context context) {
//		WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
//		// 判断wifi是否开启
//		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
//		}
//		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//		int ipAddress = wifiInfo.getIpAddress();
//		String ip = intToIp(ipAddress);
//		return ip;
		return "127.0.0.1";
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * 应用是否在前台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取设备型号.
	 * 直接获取通过系统提供API返回的设备制造商, 设备型号, CPU名称, CPU核心数, CPU频率, GPU名称字符串，并使用分割符号#进行拼接。
	 * @return
	 */
	public String getDeviceType(){
		//TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
//		StringBuilder sb = new StringBuilder();
//		sb.append(koi.android.os.Build.MANUFACTURER);
//		sb.append("#");
//		sb.append(koi.android.os.Build.MODEL);
//		sb.append("#");
//		sb.append(koi.android.os.Build.CPU_ABI);
//		sb.append("#");
//		sb.append(getNumberOfCPUCores());
//		sb.append("#");
//		sb.append(getCPUMaxFreqKHz());
//		sb.append("#");
//		sb.append("GPU");
//		return sb.toString();
		return "device type";
	}
	
	
	/**
	 * 获取CPU核数.
	 * @return
	 */
	public static int getNumberOfCPUCores() {
		class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }
	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Print exception
	        e.printStackTrace();
	        //Default to return 1 core
	        return 1;
	    }
	}
	
	public static final int DEVICEINFO_UNKNOWN = -1;  
	/**
	 * 获取CPU时钟频率.
	 * @return
	 */
	public int getCPUMaxFreqKHz() {
		  int maxFreq = DEVICEINFO_UNKNOWN;
		  try {
		    for (int i = 0; i < getNumberOfCPUCores(); i++) {
		      String filename =
		          "/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq";
		      File cpuInfoMaxFreqFile = new File(filename);
		      if (cpuInfoMaxFreqFile.exists()) {
		        byte[] buffer = new byte[128];
		        FileInputStream stream = new FileInputStream(cpuInfoMaxFreqFile);
		        try {
		          stream.read(buffer);
		          int endIndex = 0;
		          //Trim the first number out of the byte buffer.
		          while (buffer[endIndex] >= '0' && buffer[endIndex] <= '9'
		              && endIndex < buffer.length) endIndex++;
		          String str = new String(buffer, 0, endIndex);
		          Integer freqBound = Integer.parseInt(str);
		          if (freqBound > maxFreq) maxFreq = freqBound;
		        } catch (NumberFormatException e) {
		          //Fall through and use /proc/cpuinfo.
		        } finally {
		          stream.close();
		        }
		      }
		    }
		    if (maxFreq == DEVICEINFO_UNKNOWN) {
		      FileInputStream stream = new FileInputStream("/proc/cpuinfo");
		      try {
		        int freqBound = parseFileForValue("cpu MHz", stream);
		        freqBound *= 1000; //MHz -> kHz
		        if (freqBound > maxFreq) maxFreq = freqBound;
		      } finally {
		        stream.close();
		      }
		    }
		  } catch (IOException e) {
		    maxFreq = DEVICEINFO_UNKNOWN; //Fall through and return unknown.
		  }
		  return maxFreq;
		}
	
	private int parseFileForValue(String textToMatch, FileInputStream stream) {  
	    byte[] buffer = new byte[1024];  
	    try {  
	      int length = stream.read(buffer);  
	      for (int i = 0; i < length; i++) {  
	        if (buffer[i] == '\n' || i == 0) {  
	          if (buffer[i] == '\n') i++;  
	          for (int j = i; j < length; j++) {  
	            int textIndex = j - i;  
	            //Text doesn't match query at some point.  
	            if (buffer[j] != textToMatch.charAt(textIndex)) {  
	              break;  
	            }  
	            //Text matches query here.  
	            if (textIndex == textToMatch.length() - 1) {  
	              return extractValue(buffer, j);  
	            }  
	          }  
	        }  
	      }  
	    } catch (IOException e) {  
	      //Ignore any exceptions and fall through to return unknown value.  
	    } catch (NumberFormatException e) {  
	    }  
	    return DEVICEINFO_UNKNOWN;  
	  }  
	
	private int extractValue(byte[] buffer, int index) {  
	    while (index < buffer.length && buffer[index] != '\n') {  
	      if (buffer[index] >= '0' && buffer[index] <= '9') {  
	        int start = index;  
	        index++;  
	        while (index < buffer.length && buffer[index] >= '0' && buffer[index] <= '9') {  
	          index++;  
	        }  
	        String str = new String(buffer, 0, start, index - start);  
	        return Integer.parseInt(str);  
	      }  
	      index++;  
	    }  
	    return DEVICEINFO_UNKNOWN;  
	  } 
	
	public String NetType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if (typeName.equalsIgnoreCase("wifi")) {
            } else {
                typeName = info.getExtraInfo().toLowerCase();
                return typeName;
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return "4G";
    }
	
	public String GetNetworkType(Context context)
	{
	    String strNetworkType = "";
	    
	    NetworkInfo networkInfo =  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected())
	    {
	        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
	        {
	            strNetworkType = "WIFI";
	        }
	        else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
	        {
	            String _strSubTypeName = networkInfo.getSubtypeName();
	            
	            Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
	            
	            // TD-SCDMA   networkType is 17
	            int networkType = networkInfo.getSubtype();
	            switch (networkType) {
	                case TelephonyManager.NETWORK_TYPE_GPRS:
	                case TelephonyManager.NETWORK_TYPE_EDGE:
	                case TelephonyManager.NETWORK_TYPE_CDMA:
	                case TelephonyManager.NETWORK_TYPE_1xRTT:
	                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
	                    strNetworkType = "2G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_UMTS:
	                case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                case TelephonyManager.NETWORK_TYPE_HSDPA:
	                case TelephonyManager.NETWORK_TYPE_HSUPA:
	                case TelephonyManager.NETWORK_TYPE_HSPA:
	                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
	                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
	                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
	                    strNetworkType = "3G";
	                    break;
	                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
	                    strNetworkType = "4G";
	                    break;
	                default:
	                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
	                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
	                    {
	                        strNetworkType = "3G";
	                    }
	                    else
	                    {
	                        strNetworkType = _strSubTypeName;
	                    }
	                    
	                    break;
	             }
	             
	            Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
	        }
	    }
	    
	    Log.e("cocos2d-x", "Network Type : " + strNetworkType);
	    
	    return strNetworkType;
	}
	
	/**
	 * 是否包含非法词汇.
	 * @return
	 */
	@SuppressLint("NewApi")
	public static boolean isIncludeErrorWords(Context context, String word) {
		Properties paramProperties = new Properties(); 
		try {
			paramProperties.load(context.getAssets().open("error_words.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String params[] = paramProperties.getProperty("error_name").split(",");
		for (String param : params) {
			if (!word.isEmpty()&&word.trim().contains(param.trim())) {
				return true;
			}
		}
		return false;
	}

	/*
	*获取屏幕分辨率
	*
 	*/
	public  String getResolution(){
		String resolution = "";
		try {
			DisplayMetrics dm = new DisplayMetrics();
			ActivityKeep.getInstance().getMainActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			resolution = dm.widthPixels + "*" + dm.heightPixels;
		}catch (Exception e){
			resolution = "100*100";
			e.printStackTrace();
		}

		return resolution;
	}

}
