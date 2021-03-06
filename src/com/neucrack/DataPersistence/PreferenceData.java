package com.neucrack.DataPersistence;

import java.util.prefs.Preferences;

public class PreferenceData {
	private final String KEY_TRANSPARENTVALUE="transparentValue";
	public final static int MAXTRASPARENTVALUE=90;
	public static int MAX_DANMU_NUMBER_DEFAULT = 8;
	
	private final String KEY_ISSAVEROOMID="isSaveRoomID";
	private final String KEY_ROOMID="roomID";
	private final String KEY_DANMU_DIS_NUMBER="danMuDisNumber";
	public final static String DEFAULT_ROOMID="313180";
	private final String KEY_VOICE_NAME = "voiceName";
	private final String KEY_ENABLE_VOICE = "isEnableVoice";
	public final String DEFAULT_VOICE_NAME = "xiaoyan";
	
	public final static String TTS_APPID = "56849aec";
	
	
	Preferences prefs; 
	
	public PreferenceData() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}

	public boolean SaveTransparentValue(int value){
		prefs.putInt(KEY_TRANSPARENTVALUE, value);
		return true;
	}

	public int getmTransparentValue() {
		return prefs.getInt(KEY_TRANSPARENTVALUE,45);
	}
	public boolean SaveIsSaveRoomID(boolean isSaveRoomID){
		prefs.putBoolean(KEY_ISSAVEROOMID, isSaveRoomID);
		return true;
	}
	public boolean IsSaveRoomID(){
		return prefs.getBoolean(KEY_ISSAVEROOMID,false);
	}
	
	public boolean SaveRoomID(String roomID){
		prefs.put(KEY_ROOMID, roomID);
		return true;
	}
	public String GetRoomID(){
		return prefs.get(KEY_ROOMID,DEFAULT_ROOMID);
	}
	public boolean SaveDanMuDisNumber(int value){
		prefs.putInt(KEY_DANMU_DIS_NUMBER, value);
		return true;
	}
	public int GetDanMuDisNumber(){
		return prefs.getInt(KEY_DANMU_DIS_NUMBER,MAX_DANMU_NUMBER_DEFAULT);
	}
	public boolean SaveVoiceName(String voiceName){
		prefs.put(KEY_VOICE_NAME, voiceName);
		return true;
	}
	public String GetVoiceName(){
		return prefs.get(KEY_VOICE_NAME,DEFAULT_VOICE_NAME);
	}
	public boolean SaveIsEnableVoicee(boolean enableVoice){
		prefs.putBoolean(KEY_ENABLE_VOICE, enableVoice);
		return true;
	}
	public boolean GetIsEnableVoice(){
		return prefs.getBoolean(KEY_ENABLE_VOICE,false);
	}
	
}
