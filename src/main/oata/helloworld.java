package main.oata;

import main.oata.Log.LogType;

public class helloworld {
	
	public static void main(String[] args) {
		Log.initLog(helloworld.class.getName());
		Log.write(LogType.INFO, "hellowworld");
		Log.write(LogType.INFO, "``````hellowworld1");
		
	}

}
