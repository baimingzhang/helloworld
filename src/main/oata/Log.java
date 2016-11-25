
package main.oata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;


/**
 * @Name: Log.java
 * @Description: This class supply some method to help log information
 * @Author: Simple.Zhang
 * @Date: Dec 14, 2015
 */
public class Log {

	// the log file name
	private static String sLogName = null;
	private static String sExcelLogName = null;
	private static boolean bIsInitialized = false;
	// the folder that the log will be stored
	private static String sBaseDir = "";
	// iSerialNum will be use in the screenshot file name to count the
	// screenshot number
	static int iSerialNum = 0;
	// support png and jpg format
	private static final String IMAGEFORMAT = "png";
	static Dimension dDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private static String sLogPath; // the log path

	public static String getsLogPath() {
		return sLogPath;
	}

	public enum LogType {
		INFO, DEBUG, PASS, FAILED, ERROR, WARN
	}

	public Log() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @Name: initLog
	 * @Description: initialize the log file
	 * @param sClassName
	 *            module name which will be used in the log name
	 * @Return: void
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void initLog(String sClassName) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyyMMddHHmmss");
		String sDate = sdfDate.format(new Date());
		String sDateTime = sdfTime.format(new Date());
		sBaseDir = System.getProperty("user.dir");
		sLogPath = sBaseDir + "\\Log\\" + sDate + "\\" + sClassName + "_" + sDateTime;
		sLogName = sLogPath + "\\" + sClassName + "_" + sDateTime + ".txt";
		System.out.println(sLogName);
		sExcelLogName = sLogPath + "\\" + sClassName + "_" + sDateTime + ".xlsx";
		File fLogFolder = new File(sLogPath);
		File fLog = new File(sLogName);
		if (!fLogFolder.exists()) {
			fLogFolder.mkdirs();
		}
		if (!fLog.exists()) {
			try {
				fLog.createNewFile();
				bIsInitialized = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Name: write
	 * @Description: This method is used to write message into log file
	 * @param logType
	 *            the type of log, such as INFO, DEBUG, PASS, FAIL, ERROR etc
	 * @param content
	 *            the information which will be written in the log file
	 * @Return: void
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void write(LogType logType, String content) {
		write(logType, content, "");
	}

	/**
	 * @Name: write
	 * @Description: This method is used to write message into log file
	 * @param logType
	 *            the type of log, such as INFO, DEBUG, PASS, FAIL, ERROR etc
	 * @param content
	 *            the information which will be written in the log file
	 * @param screenShotName
	 *            when you want to take screenshot, please provide the
	 *            screenshot name
	 * @Return: void
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void write(LogType logType, String content, String screenShotName) {

		if (bIsInitialized) {
			String sMessage = (new Date()).toString() + " : " + logType + " -- " + content + "\r\n";
			try {
				FileWriter fwWriter = new FileWriter(sLogName, true);
				fwWriter.write(sMessage);
				fwWriter.close();
				if ("" != screenShotName) {
					takeScreenShot(screenShotName);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Log has not been initialized!");
		}

	}

	/**
	 * @Name: write
	 * @Description: write the error message into the log file and take
	 *               screenshot
	 * @param e
	 *            the Exception instance
	 * @Return: return_type
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void write(Throwable e) {
		write(e, "StackTrace");
	}

	/**
	 * @Name: write
	 * @Description: write the error message into the log file and take
	 *               screenshot
	 * @param e
	 *            the Exception instance
	 * @param screenShotName
	 *            the screenshot name
	 * @Return: return_type
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void write(Throwable e, String screenShotName) {

		if (bIsInitialized) {
			try {
				String sMessage1 = (new Date()).toString() + " : ERROR" + " -- " + FormatStackTrace(e) + "\r\n";
				FileWriter fwWriter = new FileWriter(sLogName, true);
				fwWriter.write(sMessage1);
				fwWriter.close();
				if ("" != screenShotName) {
					takeScreenShot(screenShotName);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			System.out.println("Log has not been initialized!");
		}
	}

	/**
	 * @Name: FormatStackTrace
	 * @Description: get the stack trace information from exception
	 * @param e
	 * @Return: String
	 * @Author: Simple.Zhang
	 * @Date: Dec 15, 2015
	 */

	public static String FormatStackTrace(Throwable e) {
		if (e == null)
			return "";
		String sStackTrace = e.getStackTrace().toString();
		try {
			Writer wWriter = new StringWriter();
			PrintWriter pwPrintWriter = new PrintWriter(wWriter);
			e.printStackTrace(pwPrintWriter);
			pwPrintWriter.flush();
			wWriter.flush();
			sStackTrace = wWriter.toString();
			pwPrintWriter.close();
			wWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sStackTrace;
	}

	/**
	 * @Name: takeScreenShot
	 * @Description: take screenshot and save the screenshot file to the log
	 *               folder
	 * @Param sScreenShotName the screenshot file name
	 * @Return: void
	 * @Author: Simple.Zhang
	 * @Date: Dec 14, 2015
	 */
	public static void takeScreenShot(String sScreenShotName) {

		try {
			// copy screen to a BufferedImage object biScreenshot
			BufferedImage biScreenshot = (new Robot()).createScreenCapture(
					new Rectangle(0, 0, (int) dDimension.getWidth(), (int) dDimension.getHeight()));
			String sName = sLogPath + File.separator + sScreenShotName + "_" + String.valueOf(++iSerialNum) + "."
					+ IMAGEFORMAT;
			File fFile = new File(sName);
			ImageIO.write(biScreenshot, IMAGEFORMAT, fFile);
			Log.write(LogType.INFO, "take screenshot : " + sName);
		} catch (Exception ex) {
			Log.write(LogType.ERROR, ex.toString(), "takeScreenShot_failed");
		}
	}

}