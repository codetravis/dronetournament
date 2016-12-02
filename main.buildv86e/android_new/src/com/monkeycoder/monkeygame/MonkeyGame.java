
//${PACKAGE_BEGIN}
package com.monkeycoder.monkeygame;
//${PACKAGE_END}

//${IMPORTS_BEGIN}
import java.lang.Math;
import java.lang.reflect.Array;
import java.util.Vector;
import java.text.NumberFormat;
import java.text.ParseException;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.reflect.*;
import android.os.*;
import android.app.*;
import android.media.*;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import android.hardware.*;
import android.widget.*;
import android.view.inputmethod.*;
import android.content.res.*;
import android.opengl.*;
import android.text.*;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import com.monkey.LangUtil;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
//${IMPORTS_END}

class MonkeyConfig{
//${CONFIG_BEGIN}
static final String ADMOB_ANDROID_TEST_DEVICE1="TEST_EMULATOR";
static final String ADMOB_ANDROID_TEST_DEVICE2="ABCDABCDABCDABCDABCDABCDABCDABCD";
static final String ADMOB_ANDROID_TEST_DEVICE3="";
static final String ADMOB_ANDROID_TEST_DEVICE4="";
static final String ADMOB_PUBLISHER_ID="abcdabcdabcdabc";
static final String ANDROID_APP_LABEL="Monkey Game";
static final String ANDROID_APP_PACKAGE="com.monkeycoder.monkeygame";
static final String ANDROID_GAMEPAD_ENABLED="0";
static final String ANDROID_KEY_ALIAS="release-key-alias";
static final String ANDROID_KEY_ALIAS_PASSWORD="password";
static final String ANDROID_KEY_STORE="../../release-key.keystore";
static final String ANDROID_KEY_STORE_PASSWORD="password";
static final String ANDROID_LANGUTIL_ENABLED="1";
static final String ANDROID_LIBGOOGLEPLAY_AVAILABLE="1";
static final String ANDROID_MANIFEST_ACTIVITY="\n";
static final String ANDROID_MANIFEST_APPLICATION="\n";
static final String ANDROID_MANIFEST_MAIN="\n";
static final String ANDROID_NATIVE_GL_ENABLED="0";
static final String ANDROID_SCREEN_ORIENTATION="portrait";
static final String ANDROID_SIGN_APP="0";
static final String ANDROID_VERSION_CODE="1";
static final String ANDROID_VERSION_NAME="1.0";
static final String BINARY_FILES="*.bin|*.dat";
static final String BRL_GAMETARGET_IMPLEMENTED="1";
static final String BRL_HTTPREQUEST_IMPLEMENTED="1";
static final String BRL_THREAD_IMPLEMENTED="1";
static final String CD="";
static final String CONFIG="debug";
static final String HOST="macos";
static final String IMAGE_FILES="*.png|*.jpg|*.gif|*.bmp";
static final String LANG="java";
static final String MODPATH="";
static final String MOJO_DRIVER_IMPLEMENTED="1";
static final String MOJO_HICOLOR_TEXTURES="1";
static final String MOJO_IMAGE_FILTERING_ENABLED="1";
static final String MUSIC_FILES="*.wav|*.ogg|*.mp3|*.m4a";
static final String OPENGL_GLES20_ENABLED="0";
static final String SAFEMODE="0";
static final String SOUND_FILES="*.wav|*.ogg|*.mp3|*.m4a";
static final String TARGET="android";
static final String TEXT_FILES="*.txt|*.xml|*.json";
//${CONFIG_END}
}

//${TRANSCODE_BEGIN}

// Java Monkey runtime.
//
// Placed into the public domain 24/02/2011.
// No warranty implied; use at your own risk.



class bb_std_lang{

	//***** Error handling *****

	static String errInfo="";
	static Vector errStack=new Vector();
	
	static float D2R=0.017453292519943295f;
	static float R2D=57.29577951308232f;
	
	static NumberFormat numberFormat=NumberFormat.getInstance();
	
	static boolean[] emptyBoolArray=new boolean[0];
	static int[] emptyIntArray=new int[0];
	static float[] emptyFloatArray=new float[0];
	static String[] emptyStringArray=new String[0];
	
	static void pushErr(){
		errStack.addElement( errInfo );
	}
	
	static void popErr(){
		if( errStack.size()==0 ) throw new Error( "STACK ERROR!" );
		errInfo=(String)errStack.remove( errStack.size()-1 );
	}
	
	static String stackTrace(){
		if( errInfo.length()==0 ) return "";
		String str=errInfo+"\n";
		for( int i=errStack.size()-1;i>0;--i ){
			str+=(String)errStack.elementAt(i)+"\n";
		}
		return str;
	}
	
	static int print( String str ){
		System.out.println( str );
		return 0;
	}
	
	static int error( String str ){
		throw new RuntimeException( str );
	}
	
	static String makeError( String err ){
		if( err.length()==0 ) return "";
		return "Monkey Runtime Error : "+err+"\n\n"+stackTrace();
	}
	
	static int debugLog( String str ){
		print( str );
		return 0;
	}
	
	static int debugStop(){
		error( "STOP" );
		return 0;
	}
	
	//***** String stuff *****

	static public String[] stringArray( int n ){
		String[] t=new String[n];
		for( int i=0;i<n;++i ) t[i]="";
		return t;
	}
	
	static String slice( String str,int from ){
		return slice( str,from,str.length() );
	}
	
	static String slice( String str,int from,int term ){
		int len=str.length();
		if( from<0 ){
			from+=len;
			if( from<0 ) from=0;
		}else if( from>len ){
			from=len;
		}
		if( term<0 ){
			term+=len;
		}else if( term>len ){
			term=len;
		}
		if( term>from ) return str.substring( from,term );
		return "";
	}
	
	static public String[] split( String str,String sep ){
		if( sep.length()==0 ){
			String[] bits=new String[str.length()];
			for( int i=0;i<str.length();++i){
				bits[i]=String.valueOf( str.charAt(i) );
			}
			return bits;
		}else{
			int i=0,i2,n=1;
			while( (i2=str.indexOf( sep,i ))!=-1 ){
				++n;
				i=i2+sep.length();
			}
			String[] bits=new String[n];
			i=0;
			for( int j=0;j<n;++j ){
				i2=str.indexOf( sep,i );
				if( i2==-1 ) i2=str.length();
				bits[j]=slice( str,i,i2 );
				i=i2+sep.length();
			}
			return bits;
		}
	}
	
	static public String join( String sep,String[] bits ){
		if( bits.length<2 ) return bits.length==1 ? bits[0] : "";
		StringBuilder buf=new StringBuilder( bits[0] );
		boolean hasSep=sep.length()>0;
		for( int i=1;i<bits.length;++i ){
			if( hasSep ) buf.append( sep );
			buf.append( bits[i] );
		}
		return buf.toString();
	}
	
	static public String replace( String str,String find,String rep ){
		int i=0;
		for(;;){
			i=str.indexOf( find,i );
			if( i==-1 ) return str;
			str=str.substring( 0,i )+rep+str.substring( i+find.length() );
			i+=rep.length();
		}
	}
	
	static public String fromChars( int[] chars ){
		int n=chars.length;
		char[] chrs=new char[n];
		for( int i=0;i<n;++i ){
			chrs[i]=(char)chars[i];
		}
		return new String( chrs,0,n );
	}
	
	static int[] toChars( String str ){
		int[] arr=new int[str.length()];
		for( int i=0;i<str.length();++i ) arr[i]=(int)str.charAt( i );
		return arr;
	}
	
	//***** Array Stuff *****
	
	static int length( Object arr ){
		return arr!=null ? Array.getLength( arr ) : 0;
	}
	
	static Object sliceArray( Object arr,int from ){
		if( arr==null ) return null;
		return sliceArray( arr,from,Array.getLength( arr ) );
	}
	
	static Object sliceArray( Object arr,int from,int term ){
		if( arr==null ) return null;
		int len=Array.getLength( arr );
		if( from<0 ){
			from+=len;
			if( from<0 ) from=0;
		}else if( from>len ){
			from=len;
		}
		if( term<0 ){
			term+=len;
		}else if( term>len ){
			term=len;
		}
		if( term<from ) term=from;
		int newlen=term-from;
		Object res=Array.newInstance( arr.getClass().getComponentType(),newlen );
		if( newlen>0 ) System.arraycopy( arr,from,res,0,newlen );
		return res;
	}
	
	static String[] resize( String[] arr,int newlen ){
		if( arr==null ) return stringArray( newlen );
		int len=arr.length;
		String[] res=new String[newlen];
		int n=Math.min( len,newlen );
		if( n>0 ) System.arraycopy( arr,0,res,0,n );
		while( len<newlen ) res[len++]="";
		return res;		
	}
	
	static Object resize( Object arr,int newlen,Class elemTy ){
		if( arr==null ) return Array.newInstance( elemTy,newlen );
		int len=Array.getLength( arr );
		Object res=Array.newInstance( elemTy,newlen );
		int n=Math.min( len,newlen );
		if( n>0 ) System.arraycopy( arr,0,res,0,n );
		return res;
	}
	
	public static <T> T as( Class<T> t,Object o ){
		return t.isInstance( o ) ? t.cast( o ) : null;
	}
}

class ThrowableObject extends RuntimeException{
	ThrowableObject(){
		super( "Uncaught Monkey Exception" );
	}
}



class BBGameEvent{
	static final int KeyDown=1;
	static final int KeyUp=2;
	static final int KeyChar=3;
	static final int MouseDown=4;
	static final int MouseUp=5;
	static final int MouseMove=6;
	static final int TouchDown=7;
	static final int TouchUp=8;
	static final int TouchMove=9;
	static final int MotionAccel=10;
}

class BBGameDelegate{
	void StartGame(){}
	void SuspendGame(){}
	void ResumeGame(){}
	void UpdateGame(){}
	void RenderGame(){}
	void KeyEvent( int event,int data ){}
	void MouseEvent( int event,int data,float x,float y ){}
	void TouchEvent( int event,int data,float x,float y ){}
	void MotionEvent( int event,int data,float x,float y,float z ){}
	void DiscardGraphics(){}
}

class BBDisplayMode{
	public int width;
	public int height;
	public int format;
	public int hertz;
	public int flags;
	public BBDisplayMode(){}
	public BBDisplayMode( int width,int height ){ this.width=width;this.height=height; }
}

abstract class BBGame{

	protected static BBGame _game;

	protected BBGameDelegate _delegate;
	protected boolean _keyboardEnabled;
	protected int _updateRate;
	protected boolean _debugExs;
	protected boolean _started;
	protected boolean _suspended;
	protected long _startms;
	
	public BBGame(){
		_game=this;
		_debugExs=MonkeyConfig.CONFIG.equals( "debug" );
		_startms=System.currentTimeMillis();
	}
	
	public static BBGame Game(){
		return _game;
	}
	
	public boolean Started(){
		return _started;
	}
	
	public boolean Suspended(){
		return _suspended;
	}

	public void SetDelegate( BBGameDelegate delegate ){
		_delegate=delegate;
	}
	
	public BBGameDelegate Delegate(){
		return _delegate;
	}
	
	public void SetKeyboardEnabled( boolean enabled){
		_keyboardEnabled=enabled;
	}
	
	public boolean KeyboardEnabled(){
		return _keyboardEnabled;
	}

	public void SetUpdateRate( int hertz ){
		_updateRate=hertz;
	}
	
	public int UpdateRate(){
		return _updateRate;
	}

	public int Millisecs(){
		return (int)(System.currentTimeMillis()-_startms);
	}
	
	public void GetDate( int[] date ){
		int n=date.length;
		if( n>0 ){
			Calendar c=Calendar.getInstance();
			date[0]=c.get( Calendar.YEAR );
			if( n>1 ){
				date[1]=c.get( Calendar.MONTH )+1;
				if( n>2 ){
					date[2]=c.get( Calendar.DATE );
					if( n>3 ){
						date[3]=c.get( Calendar.HOUR_OF_DAY );
						if( n>4 ){
							date[4]=c.get( Calendar.MINUTE );
							if( n>5 ){
								date[5]=c.get( Calendar.SECOND );
								if( n>6 ){
									date[6]=c.get( Calendar.MILLISECOND );
								}
							}
						}
					}
				}
			}
		}
	}
	
	public int SaveState( String state ){
		return -1;
	}

	public String LoadState(){
		return "";
	}

	public String LoadString( String path ){
		byte[] bytes=LoadData( path );
		if( bytes!=null ) return loadString( bytes );
		return "";
	}
	
	public int CountJoysticks( boolean update ){
		return 0;
	}
	
	public boolean PollJoystick( int port,float[] joyx,float[] joyy,float[] joyz,boolean[] buttons ){
		return false;
	}
	
	public void SetMouseVisible( boolean visible ){
	}

	int GetDeviceWidth(){
		return 0;
	}
	
	int GetDeviceHeight(){
		return 0;
	}
	
	void SetDeviceWindow( int width,int height,int flags ){
	}
	
	BBDisplayMode[] GetDisplayModes(){
		return new BBDisplayMode[0];
	}
	
	BBDisplayMode GetDesktopMode(){
		return null;
	}
	
	void SetSwapInterval( int interval ){
	}
	
	public void OpenUrl( String url ){	
	}
	
	String PathToFilePath( String path ){
		return "";
	}
	
	//***** Java Game *****
	
	public RandomAccessFile OpenFile( String path,String mode ){
		try{
			return new RandomAccessFile( PathToFilePath( path ),mode );
		}catch( IOException ex ){
		}
		return null;
	}
	
	public InputStream OpenInputStream( String path ){
		try{
			if( path.startsWith( "http:" ) || path.startsWith( "https:" ) ){
				URL url=new URL( path );
				URLConnection con=url.openConnection();
				return new BufferedInputStream( con.getInputStream() );
			}
			return new FileInputStream( PathToFilePath( path ) );
		}catch( IOException ex ){
		}
		return null;
	}
	
	byte[] LoadData( String path ){
		try{
			InputStream in=OpenInputStream( path );
			if( in==null ) return null;
			
			ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
			byte[] buf=new byte[4096];
			
			for(;;){
				int n=in.read( buf );
				if( n<0 ) break;
				out.write( buf,0,n );
			}
			in.close();
			return out.toByteArray();
			
		}catch( IOException e ){
		}
		return null;
	}
	
	//***** INTERNAL *****
	
	public void Quit(){
		System.exit( 0 );
	}
	
	public void Die( RuntimeException ex ){
	
		String msg=ex.getMessage();
		if( msg!=null && msg.equals("") ){
			Quit();
			return;
		}
		if( _debugExs ){
			if( msg==null ) msg=ex.toString();
			bb_std_lang.print( "Monkey Runtime Error : "+msg );
			bb_std_lang.print( bb_std_lang.stackTrace() );
		}
		throw ex;
	}

	void StartGame(){
	
		if( _started ) return;
		_started=true;
		
		try{
			synchronized( _delegate ){
				_delegate.StartGame();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void SuspendGame(){
	
		if( !_started || _suspended ) return;
		_suspended=true;
		
		try{
			synchronized( _delegate ){
				_delegate.SuspendGame();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void ResumeGame(){

		if( !_started || !_suspended ) return;
		_suspended=false;
		
		try{
			synchronized( _delegate ){
				_delegate.ResumeGame();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void UpdateGame(){

		if( !_started || _suspended ) return;

		try{
			synchronized( _delegate ){
				_delegate.UpdateGame();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void RenderGame(){
	
		if( !_started ) return;
		
		try{
			synchronized( _delegate ){
				_delegate.RenderGame();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void KeyEvent( int event,int data ){

		if( !_started ) return;
	
		try{
			synchronized( _delegate ){
				_delegate.KeyEvent( event,data );
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void MouseEvent( int event,int data,float x,float y ){

		if( !_started ) return;
		
		try{
			synchronized( _delegate ){
				_delegate.MouseEvent( event,data,x,y );
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void TouchEvent( int event,int data,float x,float y ){

		if( !_started ) return;
		
		try{
			synchronized( _delegate ){
				_delegate.TouchEvent( event,data,x,y );
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void MotionEvent( int event,int data,float x,float y,float z ){

		if( !_started ) return;
		
		try{
			synchronized( _delegate ){
				_delegate.MotionEvent( event,data,x,y,z );
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	void DiscardGraphics(){

		if( !_started ) return;
		
		try{
			synchronized( _delegate ){
				_delegate.DiscardGraphics();
			}
		}catch( RuntimeException ex ){
			Die( ex );
		}
	}
	
	//***** Private *****
	
	private String toString( byte[] buf ){
		int n=buf.length;
		char tmp[]=new char[n];
		for( int i=0;i<n;++i ){
			tmp[i]=(char)(buf[i] & 0xff);
		}
		return new String( tmp );
	}
	
	private String loadString( byte[] buf ){
	
		int n=buf.length;
		StringBuilder out=new StringBuilder();
		
		int t0=n>0 ? buf[0] & 0xff : -1;
		int t1=n>1 ? buf[1] & 0xff : -1;
		
		if( t0==0xfe && t1==0xff ){
			int i=2;
			while( i<n-1 ){
				int x=buf[i++] & 0xff;
				int y=buf[i++] & 0xff;
				out.append( (char)((x<<8)|y) ); 
			}
		}else if( t0==0xff && t1==0xfe ){
			int i=2;
			while( i<n-1 ){
				int x=buf[i++] & 0xff;
				int y=buf[i++] & 0xff;
				out.append( (char)((y<<8)|x) ); 
			}
		}else{
			int t2=n>2 ? buf[2] & 0xff : -1;
			int i=(t0==0xef && t1==0xbb && t2==0xbf) ? 3 : 0;
			boolean fail=false;
			while( i<n ){
				int c=buf[i++] & 0xff;
				if( (c & 0x80)!=0 ){
					if( (c & 0xe0)==0xc0 ){
						if( i>=n || (buf[i] & 0xc0)!=0x80 ){
							fail=true;
							break;
						}
						c=((c & 0x1f)<<6) | (buf[i] & 0x3f);
						i+=1;
					}else if( (c & 0xf0)==0xe0 ){
						if( i+1>=n || (buf[i] & 0xc0)!=0x80 || (buf[i+1] & 0xc0)!=0x80 ){
							fail=true;
							break;
						}
						c=((c & 0x0f)<<12) | ((buf[i] & 0x3f)<<6) | (buf[i+1] & 0x3f);
						i+=2;
					}else{
						fail=true;
						break;
					}
				}
				out.append( (char)c );
			}
			if( fail ){
				return toString( buf );
			}
		}
		return out.toString();
	}
}





class ActivityDelegate{

	public void onStart(){
	}
	public void onRestart(){
	}
	public void onResume(){
	}
	public void onPause(){
	}
	public void onStop(){
	}
	public void onDestroy(){
	}
	public void onActivityResult( int requestCode,int resultCode,Intent data ){
	}
	public void onNewIntent( Intent intent ){
	}
}

class BBAndroidGame extends BBGame implements GLSurfaceView.Renderer,SensorEventListener{

	static BBAndroidGame _androidGame;
	
	Activity _activity;
	GameView _view;
	
	List<ActivityDelegate> _activityDelegates=new LinkedList<ActivityDelegate>();
	
	int _reqCode;
	
	Display _display;
	
	long _nextUpdate;
	
	long _updatePeriod;
	
	boolean _canRender;
	
	float[] _joyx=new float[2];
	float[] _joyy=new float[2];
	float[] _joyz=new float[2];
	boolean[] _buttons=new boolean[32];
	
	public BBAndroidGame( Activity activity,GameView view ){
		_androidGame=this;

		_activity=activity;
		_view=view;
		
		_display=_activity.getWindowManager().getDefaultDisplay();
		
		System.setOut( new PrintStream( new LogTool() ) );
	}
	
	public static BBAndroidGame AndroidGame(){

		return _androidGame;
	}
	
	//***** LogTool ******	

	static class LogTool extends OutputStream{
	
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		
		@Override
		public void write( int b ) throws IOException{
			if( b==(int)'\n' ){
				Log.i( "[Monkey]",new String( this.out.toByteArray() ) );
				this.out=new ByteArrayOutputStream();
			}else{
				this.out.write(b);
			}
		}
	}
	
	void ValidateUpdateTimer(){
		_nextUpdate=0;
		_updatePeriod=0;
		if( _updateRate!=0 ) _updatePeriod=1000000000/_updateRate;
		
	}
	
	//***** GameView *****
	
	public static class GameView extends GLSurfaceView{
	
		Object args1[]=new Object[1];
		float[] _touchX=new float[32];
		float[] _touchY=new float[32];

		boolean _useMulti;
		Method _getPointerCount,_getPointerId,_getX,_getY;
		
		boolean _useGamepad;
		Method _getSource,_getAxisValue;

		void init(){
		
			//get multi-touch methods
			try{
				Class cls=Class.forName( "android.view.MotionEvent" );
				Class intClass[]=new Class[]{ Integer.TYPE };
				_getPointerCount=cls.getMethod( "getPointerCount" );
				_getPointerId=cls.getMethod( "getPointerId",intClass );
				_getX=cls.getMethod( "getX",intClass );
				_getY=cls.getMethod( "getY",intClass );
				_useMulti=true;
			}catch( Exception ex ){
			}
			
			if( MonkeyConfig.ANDROID_GAMEPAD_ENABLED.equals( "1" ) ){
				try{
					//get gamepad methods
					Class cls=Class.forName( "android.view.MotionEvent" );
					Class intClass[]=new Class[]{ Integer.TYPE };
					_getSource=cls.getMethod( "getSource" );
					_getAxisValue=cls.getMethod( "getAxisValue",intClass );
					_useGamepad=true;
				}catch( Exception ex ){
				}
			}
		}

		public GameView( Context context ){
			super( context );
			init();
		}
		
		public GameView( Context context,AttributeSet attrs ){
			super( context,attrs );
			init();
		}
		
		public InputConnection onCreateInputConnection( EditorInfo outAttrs ){
			//voodoo to disable various undesirable soft keyboard features such as predictive text and fullscreen mode.
			outAttrs.inputType=InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
			outAttrs.imeOptions=EditorInfo.IME_FLAG_NO_FULLSCREEN|EditorInfo.IME_FLAG_NO_EXTRACT_UI;			
			return null;
		}
		
		//View event handling
		public boolean dispatchKeyEventPreIme( KeyEvent event ){

			if( _useGamepad ){
				int button=-1;
				switch( event.getKeyCode() ){
				case 96: button=0;break;	//A
				case 97: button=1;break;	//B
				case 99: button=2;break;	//X
				case 100:button=3;break;	//Y
				case 102:button=4;break;	//LB
				case 103:button=5;break;	//RB
				case 108:button=7;break;	//START
				case 21: button=8;break;	//LEFT
				case 19: button=9;break;	//UP
				case 22: button=10;break;	//RIGHT
				case 20: button=11;break;	//DOWN
				}
				if( button!=-1 ){
					_androidGame._buttons[button]=(event.getAction()==KeyEvent.ACTION_DOWN);
					return true;
				}
			}
			
			//Convert back button to ESC in soft keyboard mode...
			if( _androidGame._keyboardEnabled ){
				if( event.getKeyCode()==KeyEvent.KEYCODE_BACK ){
					if( event.getAction()==KeyEvent.ACTION_DOWN ){
						_androidGame.KeyEvent( BBGameEvent.KeyChar,27 );
					}
					return true;
				}
			}
			return false;
		}
		
		public boolean onKeyDown( int key,KeyEvent event ){
		
			int vkey=-1;
			switch( event.getKeyCode() ){
			case KeyEvent.KEYCODE_MENU:vkey=0x1a1;break;
			case KeyEvent.KEYCODE_SEARCH:vkey=0x1a3;break;
			}
			if( vkey!=-1 ){
				_androidGame.KeyEvent( BBGameEvent.KeyDown,vkey );
				_androidGame.KeyEvent( BBGameEvent.KeyUp,vkey );
				return true;
			}
			
			if( !_androidGame._keyboardEnabled ) return false;
			
			if( event.getKeyCode()==KeyEvent.KEYCODE_DEL ){
				_androidGame.KeyEvent( BBGameEvent.KeyChar,8 );
			}else{
				int chr=event.getUnicodeChar();
				if( chr!=0 ){
					_androidGame.KeyEvent( BBGameEvent.KeyChar,chr==10 ? 13 : chr );
				}
			}
			return true;
		}
		
		public boolean onKeyMultiple( int keyCode,int repeatCount,KeyEvent event ){
			if( !_androidGame._keyboardEnabled ) return false;
		
			String str=event.getCharacters();
			for( int i=0;i<str.length();++i ){
				int chr=str.charAt( i );
				if( chr!=0 ){
					_androidGame.KeyEvent( BBGameEvent.KeyChar,chr==10 ? 13 : chr );
				}
			}
			return true;
		}
		
		public boolean onTouchEvent( MotionEvent event ){
		
			if( !_useMulti ){
				//mono-touch version...
				//
				switch( event.getAction() ){
				case MotionEvent.ACTION_DOWN:
					_androidGame.TouchEvent( BBGameEvent.TouchDown,0,event.getX(),event.getY() );
					break;
				case MotionEvent.ACTION_UP:
					_androidGame.TouchEvent( BBGameEvent.TouchUp,0,event.getX(),event.getY() );
					break;
				case MotionEvent.ACTION_MOVE:
					_androidGame.TouchEvent( BBGameEvent.TouchMove,0,event.getX(),event.getY() );
					break;
				}
				return true;
			}
	
			try{
	
				//multi-touch version...
				//
				final int ACTION_DOWN=0;
				final int ACTION_UP=1;
				final int ACTION_POINTER_DOWN=5;
				final int ACTION_POINTER_UP=6;
				final int ACTION_POINTER_INDEX_SHIFT=8;
				final int ACTION_MASK=255;
				
				int index=-1;
				int action=event.getAction();
				int masked=action & ACTION_MASK;
				
				if( masked==ACTION_DOWN || masked==ACTION_POINTER_DOWN || masked==ACTION_UP || masked==ACTION_POINTER_UP ){
	
					index=action>>ACTION_POINTER_INDEX_SHIFT;
					
					args1[0]=Integer.valueOf( index );
					int pid=((Integer)_getPointerId.invoke( event,args1 )).intValue();
	
					float x=_touchX[pid]=((Float)_getX.invoke( event,args1 )).floatValue();
					float y=_touchY[pid]=((Float)_getY.invoke( event,args1 )).floatValue();
					
					if( masked==ACTION_DOWN || masked==ACTION_POINTER_DOWN ){
						_androidGame.TouchEvent( BBGameEvent.TouchDown,pid,x,y );
					}else{
						_androidGame.TouchEvent( BBGameEvent.TouchUp,pid,x,y );
					}
				}
	
				int pointerCount=((Integer)_getPointerCount.invoke( event )).intValue();
			
				for( int i=0;i<pointerCount;++i ){
					if( i==index ) continue;
	
					args1[0]=Integer.valueOf( i );
					int pid=((Integer)_getPointerId.invoke( event,args1 )).intValue();
	
					float x=((Float)_getX.invoke( event,args1 )).floatValue();
					float y=((Float)_getY.invoke( event,args1 )).floatValue();
	
					if( x!=_touchX[pid] || y!=_touchY[pid] ){
						_touchX[pid]=x;
						_touchY[pid]=y;
						_androidGame.TouchEvent( BBGameEvent.TouchMove,pid,x,y );
					}
				}
			}catch( Exception ex ){
			}
	
			return true;
		}
		
		//New! Dodgy gamepad support...
		public boolean onGenericMotionEvent( MotionEvent event ){
		
			if( !_useGamepad ) return false;
			
			try{
				int source=((Integer)_getSource.invoke( event )).intValue();

				if( (source&16)==0 ) return false;
			
				BBAndroidGame g=_androidGame;
			
				args1[0]=Integer.valueOf( 0  );g._joyx[0]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				args1[0]=Integer.valueOf( 1  );g._joyy[0]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				args1[0]=Integer.valueOf( 17 );g._joyz[0]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				
				args1[0]=Integer.valueOf( 11 );g._joyx[1]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				args1[0]=Integer.valueOf( 14 );g._joyy[1]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				args1[0]=Integer.valueOf( 18 );g._joyz[1]=((Float)_getAxisValue.invoke( event,args1 )).floatValue();
				
				return true;
				
			}catch( Exception ex ){
			}

			return false;
		}
	}
	
	//***** BBGame ******
	
	public int GetDeviceWidth(){
		return _view.getWidth();
	}
	
	public int GetDeviceHeight(){
		return _view.getHeight();
	}
	
	public void SetKeyboardEnabled( boolean enabled ){
		super.SetKeyboardEnabled( enabled );

		InputMethodManager mgr=(InputMethodManager)_activity.getSystemService( Context.INPUT_METHOD_SERVICE );
		
		if( _keyboardEnabled ){
			// Hack for someone's phone...My LG or Samsung don't need it...
			mgr.hideSoftInputFromWindow( _view.getWindowToken(),0 );
			mgr.showSoftInput( _view,0 );		//0 is 'magic'! InputMethodManager.SHOW_IMPLICIT does weird things...
		}else{
			mgr.hideSoftInputFromWindow( _view.getWindowToken(),0 );
		}
	}
	
	public void SetUpdateRate( int hertz ){
		super.SetUpdateRate( hertz );
		ValidateUpdateTimer();
	}	

	public int SaveState( String state ){
		SharedPreferences prefs=_activity.getPreferences( 0 );
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString( ".monkeystate",state );
		editor.commit();
		return 1;
	}
	
	public String LoadState(){
		SharedPreferences prefs=_activity.getPreferences( 0 );
		String state=prefs.getString( ".monkeystate","" );
		if( state.equals( "" ) ) state=prefs.getString( "gxtkAppState","" );
		return state;
	}
	
	static public String LoadState_V66b(){
		SharedPreferences prefs=_androidGame._activity.getPreferences( 0 );
		return prefs.getString( "gxtkAppState","" );
	}
	
	static public void SaveState_V66b( String state ){
		SharedPreferences prefs=_androidGame._activity.getPreferences( 0 );
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString( "gxtkAppState",state );
		editor.commit();
	}
	
	public boolean PollJoystick( int port,float[] joyx,float[] joyy,float[] joyz,boolean[] buttons ){
		if( port!=0 ) return false;
		joyx[0]=_joyx[0];joyy[0]=_joyy[0];joyz[0]=_joyz[0];
		joyx[1]=_joyx[1];joyy[1]=_joyy[1];joyz[1]=_joyz[1];
		for( int i=0;i<32;++i ) buttons[i]=_buttons[i];
		return true;
	}
	
	public void OpenUrl( String url ){
		Intent browserIntent=new Intent( Intent.ACTION_VIEW,android.net.Uri.parse( url ) );
		_activity.startActivity( browserIntent );
	}
	
	String PathToFilePath( String path ){
		if( !path.startsWith( "monkey://" ) ){
			return path;
		}else if( path.startsWith( "monkey://internal/" ) ){
			File f=_activity.getFilesDir();
			if( f!=null ) return f+"/"+path.substring(18);
		}else if( path.startsWith( "monkey://external/" ) ){
			File f=Environment.getExternalStorageDirectory();
			if( f!=null ) return f+"/"+path.substring(18);
		}
		return "";
	}

	String PathToAssetPath( String path ){
		if( path.startsWith( "monkey://data/" ) ) return "monkey/"+path.substring(14);
		return "";
	}

	public InputStream OpenInputStream( String path ){
		if( !path.startsWith( "monkey://data/" ) ) return super.OpenInputStream( path );
		try{
			return _activity.getAssets().open( PathToAssetPath( path ) );
		}catch( IOException ex ){
		}
		return null;
	}

	public Activity GetActivity(){
		return _activity;
	}

	public GameView GetGameView(){
		return _view;
	}
	
	public void AddActivityDelegate( ActivityDelegate delegate ){
		if( _activityDelegates.contains( delegate ) ) return;
		_activityDelegates.add( delegate );
	}
	
	public int AllocateActivityResultRequestCode(){
		return ++_reqCode;
	}
	
	public void RemoveActivityDelegate( ActivityDelegate delegate ){
		_activityDelegates.remove( delegate );
	}

	public Bitmap LoadBitmap( String path ){
		try{
			InputStream in=OpenInputStream( path );
			if( in==null ) return null;

			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inPreferredConfig=Bitmap.Config.ARGB_8888;
			opts.inPurgeable=true;

			Bitmap bitmap=BitmapFactory.decodeStream( in,null,opts );
			in.close();
			
			return bitmap;
		}catch( IOException e ){
		}
		return null;
	}

	public int LoadSound( String path,SoundPool pool ){
		try{
			if( path.startsWith( "monkey://data/" ) ){
				return pool.load( _activity.getAssets().openFd( PathToAssetPath( path ) ),1 );
			}else{
				return pool.load( PathToFilePath( path ),1 );
			}
		}catch( IOException ex ){
		}
		return 0;
	}
	
	public MediaPlayer OpenMedia( String path ){
		try{
			MediaPlayer mp;
			
			if( path.startsWith( "monkey://data/" ) ){
				AssetFileDescriptor fd=_activity.getAssets().openFd( PathToAssetPath( path ) );
				mp=new MediaPlayer();
				mp.setDataSource( fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength() );
				mp.prepare();
				fd.close();
			}else{
				mp=new MediaPlayer();
				mp.setDataSource( PathToFilePath( path ) );
				mp.prepare();
			}
			return mp;
			
		}catch( IOException ex ){
		}
		return null;
	}
	
	//***** INTERNAL *****
	
	public void SuspendGame(){
		super.SuspendGame();
		ValidateUpdateTimer();
		_canRender=false;
	}
	
	public void ResumeGame(){
		super.ResumeGame();
		ValidateUpdateTimer();
	}

	public void UpdateGame(){
		//
		//Ok, this isn't very polite - if keyboard enabled, we just thrash showSoftInput.
		//
		//But showSoftInput doesn't seem to be too reliable - esp. after onResume - and I haven't found a way to
		//determine if keyboard is showing, so what can yer do...
		//
		if( _keyboardEnabled ){
			InputMethodManager mgr=(InputMethodManager)_activity.getSystemService( Context.INPUT_METHOD_SERVICE );
			mgr.showSoftInput( _view,0 );		//0 is 'magic'! InputMethodManager.SHOW_IMPLICIT does weird things...
		}
		super.UpdateGame();
	}
	
	public void Run(){

		//touch input handling	
		SensorManager sensorManager=(SensorManager)_activity.getSystemService( Context.SENSOR_SERVICE );
		List<Sensor> sensorList=sensorManager.getSensorList( Sensor.TYPE_ACCELEROMETER );
		Iterator<Sensor> it=sensorList.iterator();
		if( it.hasNext() ){
			Sensor sensor=it.next();
			sensorManager.registerListener( this,sensor,SensorManager.SENSOR_DELAY_GAME );
		}
		
		//audio volume control
		_activity.setVolumeControlStream( AudioManager.STREAM_MUSIC );

		//GL version
		if( MonkeyConfig.OPENGL_GLES20_ENABLED.equals( "1" ) ){
			//
			//_view.setEGLContextClientVersion( 2 );	//API 8 only!
			//
			try{
				Class clas=_view.getClass();
				Class parms[]=new Class[]{ Integer.TYPE };
				Method setVersion=clas.getMethod( "setEGLContextClientVersion",parms );
				Object args[]=new Object[1];
				args[0]=Integer.valueOf( 2 );
				setVersion.invoke( _view,args );
			}catch( Exception ex ){
			}
		}

		_view.setRenderer( this );
		_view.setFocusableInTouchMode( true );
		_view.requestFocus();
	}
	
	//***** GLSurfaceView.Renderer *****

	public void onDrawFrame( GL10 gl ){
		if( !_canRender ) return;
		
		StartGame();
		
		if( _updateRate==0 ){
			UpdateGame();
			RenderGame();
			return;
		}
			
		if( _nextUpdate==0 ){
			_nextUpdate=System.nanoTime();
		}else{
			long delay=_nextUpdate-System.nanoTime();
			if( delay>0 ){
				try{
					Thread.sleep( delay/1000000 );
				}catch( InterruptedException e ){
					_nextUpdate=0;
				}
			}
		}
			
		int i=0;
		for( ;i<4;++i ){
		
			UpdateGame();
			if( _nextUpdate==0 ) break;
			
			_nextUpdate+=_updatePeriod;
			if( _nextUpdate>System.nanoTime() ) break;
		}
		if( i==4 ) _nextUpdate=0;
		
		RenderGame();		
	}
	
	public void onSurfaceChanged( GL10 gl,int width,int height ){
	}
	
	public void onSurfaceCreated( GL10 gl,EGLConfig config ){
		_canRender=true;
		DiscardGraphics();
	}
	
	//***** SensorEventListener *****
	
	public void onAccuracyChanged( Sensor sensor,int accuracy ){
	}
	
	public void onSensorChanged( SensorEvent event ){
		Sensor sensor=event.sensor;
		float x,y,z;
		switch( sensor.getType() ){
		case Sensor.TYPE_ORIENTATION:
			break;
		case Sensor.TYPE_ACCELEROMETER:
//			switch( _display.getRotation() ){
			switch( _display.getOrientation() ){	//deprecated in API 8, but we support 3...
			case Surface.ROTATION_0:
				x=event.values[0]/-9.81f;
				y=event.values[1]/9.81f;
				break;
			case Surface.ROTATION_90:
				x=event.values[1]/9.81f;
				y=event.values[0]/9.81f;
				break;
			case Surface.ROTATION_180:
				x=event.values[0]/9.81f;
				y=event.values[1]/-9.81f;
				break;
			case Surface.ROTATION_270:
				x=event.values[1]/-9.81f;
				y=event.values[0]/-9.81f;
				break;
			default:
				x=event.values[0]/-9.81f;
				y=event.values[1]/9.81f;
				break;
			}
			z=event.values[2]/-9.81f;
			MotionEvent( BBGameEvent.MotionAccel,-1,x,y,z );
			break;
		}
	}
}

class AndroidGame extends Activity{

	BBAndroidGame _game;
	
	GameView _view;
	
	//***** GameView *****

	public static class GameView extends BBAndroidGame.GameView{

		public GameView( Context context ){
			super( context );
		}
		
		public GameView( Context context,AttributeSet attrs ){
			super( context,attrs );
		}
	}
	
	//***** Activity *****
	public void onWindowFocusChanged( boolean hasFocus ){
		if( hasFocus ){
			_view.onResume();
			_game.ResumeGame();
		}else{
			_game.SuspendGame();
			_view.onPause();
		}
	}

	@Override
	public void onBackPressed(){
		//deprecating this!
		_game.KeyEvent( BBGameEvent.KeyDown,27 );
		_game.KeyEvent( BBGameEvent.KeyUp,27 );
		
		//new KEY_BACK value...
		_game.KeyEvent( BBGameEvent.KeyDown,0x1a0 );
		_game.KeyEvent( BBGameEvent.KeyUp,0x1a0 );
	}

	@Override
	public void onStart(){
		super.onResume();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onStart();
		}
	}
	
	@Override
	public void onRestart(){
		super.onResume();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onRestart();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onResume();
		}
	}
	
	@Override 
	public void onPause(){
		super.onPause();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onPause();
		}
	}

	@Override
	public void onStop(){
		super.onResume();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onStop();
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onDestroy();
		}
	}
	
	@Override
	protected void onActivityResult( int requestCode,int resultCode,Intent data ){
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onActivityResult( requestCode,resultCode,data );
		}
	}
	
	@Override
	public void onNewIntent( Intent intent ){
		super.onNewIntent( intent );
		for( ActivityDelegate delegate : _game._activityDelegates ){
			delegate.onNewIntent( intent );
		}
	}
}


class BBMonkeyGame extends BBAndroidGame{

	public BBMonkeyGame( AndroidGame game,AndroidGame.GameView view ){
		super( game,view );
	}
}

public class MonkeyGame extends AndroidGame{

	public static class GameView extends AndroidGame.GameView{

		public GameView( Context context ){
			super( context );
		}
		
		public GameView( Context context,AttributeSet attrs ){
			super( context,attrs );
		}
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.main );
		
		_view=(GameView)findViewById( R.id.gameView );
		
		_game=new BBMonkeyGame( this,_view );
		
		try{
				
			bb_.bbInit();
			bb_.bbMain();
			
		}catch( RuntimeException ex ){

			_game.Die( ex );

			finish();
		}

		if( _game.Delegate()==null ) finish();
		
		_game.Run();
	}
};


// Android mojo runtime.
//
// Copyright 2011 Mark Sibly, all rights reserved.
// No warranty implied; use at your own risk.






class gxtkGraphics{

	static class RenderOp{
		int type,count,alpha;
		gxtkSurface surf;
	};

	static final int MAX_VERTICES=65536/20;
	static final int MAX_RENDEROPS=MAX_VERTICES/2;
	static final int MAX_QUAD_INDICES=MAX_VERTICES/4*6;
	
	static int seq=1;
	
	BBAndroidGame game;
	
	boolean gles20;
	int width,height;
	
	float alpha;
	float r,g,b;
	int colorARGB;
	int blend;
	float ix,iy,jx,jy,tx,ty;
	boolean tformed;
	
	RenderOp renderOps[]=new RenderOp[MAX_RENDEROPS];
	RenderOp rop,nullRop;
	int nextOp,vcount;

	float[] vertices=new float[MAX_VERTICES*4];	//x,y,u,v
	int[] colors=new int[MAX_VERTICES];	//rgba
	int vp,cp;
	
	FloatBuffer vbuffer;
	IntBuffer cbuffer;
	int vbo,vbo_seq,ibo;
	
	gxtkGraphics(){
	
		game=BBAndroidGame.AndroidGame();
		
		width=game.GetGameView().getWidth();
		height=game.GetGameView().getHeight();
		
		gles20=MonkeyConfig.OPENGL_GLES20_ENABLED.equals( "1" );
		if( gles20 ) return;
	
		for( int i=0;i<MAX_RENDEROPS;++i ){
			renderOps[i]=new RenderOp();
		}
		nullRop=new RenderOp();
		nullRop.type=-1;

		vbuffer=FloatBuffer.wrap( vertices,0,MAX_VERTICES*4 );
		cbuffer=IntBuffer.wrap( colors,0,MAX_VERTICES );
	}
	
	void Reset(){
		rop=nullRop;
		nextOp=0;
		vcount=0;
	}

	void Flush(){
		if( vcount==0 ) return;
		
		//'discard' buffer contents...
		GLES11.glBufferData( GLES11.GL_ARRAY_BUFFER,MAX_VERTICES*20,null,GLES11.GL_DYNAMIC_DRAW );
		
		GLES11.glBufferSubData( GLES11.GL_ARRAY_BUFFER,0,vcount*16,vbuffer );
		GLES11.glBufferSubData( GLES11.GL_ARRAY_BUFFER,vcount*16,vcount*4,cbuffer );
		GLES11.glColorPointer( 4,GLES11.GL_UNSIGNED_BYTE,0,vcount*16 );

		GLES11.glDisable( GLES11.GL_TEXTURE_2D );
		GLES11.glDisable( GLES11.GL_BLEND );

		int index=0;
		boolean blendon=false;
		gxtkSurface surf=null;

		for( int i=0;i<nextOp;++i ){

			RenderOp op=renderOps[i];
			
			if( op.surf!=null ){
				if( op.surf!=surf ){
					if( surf==null ) GLES11.glEnable( GLES11.GL_TEXTURE_2D );
					surf=op.surf;
					surf.Bind();
				}
			}else{
				if( surf!=null ){
					GLES11.glDisable( GLES11.GL_TEXTURE_2D );
					surf=null;
				}
			}
			
			//should just have another blend mode...
			if( blend==1 || (op.alpha>>>24)!=0xff || (op.surf!=null && op.surf.hasAlpha) ){
				if( !blendon ){
					GLES11.glEnable( GLES11.GL_BLEND );
					blendon=true;
				}
			}else{
				if( blendon ){
					GLES11.glDisable( GLES11.GL_BLEND );
					blendon=false;
				}
			}
			
			switch( op.type ){
			case 1:
				GLES11.glDrawArrays( GLES11.GL_POINTS,index,op.count );
				break;
			case 2:
				GLES11.glDrawArrays( GLES11.GL_LINES,index,op.count );
				break;
			case 3:
				GLES11.glDrawArrays( GLES11.GL_TRIANGLES,index,op.count );
				break;
			case 4:
				GLES11.glDrawElements( GLES11.GL_TRIANGLES,op.count/4*6,GLES11.GL_UNSIGNED_SHORT,(index/4*6+(index&3)*MAX_QUAD_INDICES)*2 );
				break;
			default:
				for( int j=0;j<op.count;j+=op.type ){
					GLES11.glDrawArrays( GLES11.GL_TRIANGLE_FAN,index+j,op.type );
				}
			}
			
			index+=op.count;
		}
		
		Reset();
	}
	
	void Begin( int type,int count,gxtkSurface surf ){
		if( vcount+count>MAX_VERTICES ){
			Flush();
		}
		if( type!=rop.type || surf!=rop.surf ){
			if( nextOp==MAX_RENDEROPS ) Flush();
			rop=renderOps[nextOp];
			nextOp+=1;
			rop.type=type;
			rop.surf=surf;
			rop.count=0;
			rop.alpha=~0;
		}
		rop.alpha&=colorARGB;
		rop.count+=count;
		vp=vcount*4;
		cp=vcount;
		vcount+=count;
	}

	//***** GXTK API *****

	int Width(){
		return width;
	}
	
	int Height(){
		return height;
	}
	
	int BeginRender(){

		width=game.GetGameView().getWidth();
		height=game.GetGameView().getHeight();
		
		if( gles20 ) return 0;
		
		if( vbo_seq!=seq ){

			vbo_seq=seq;
			
			int[] bufs=new int[2];
			GLES11.glGenBuffers( 2,bufs,0 );
			vbo=bufs[0];
			ibo=bufs[1];
			
			GLES11.glBindBuffer( GLES11.GL_ARRAY_BUFFER,vbo );
			GLES11.glBufferData( GLES11.GL_ARRAY_BUFFER,MAX_VERTICES*20,null,GLES11.GL_DYNAMIC_DRAW );
			
			short[] idxs=new short[MAX_QUAD_INDICES*4];
			for( int j=0;j<4;++j ){
				int k=j*MAX_QUAD_INDICES;
				for( int i=0;i<MAX_QUAD_INDICES/6;++i ){
					idxs[i*6+k+0]=(short)(i*4+j);
					idxs[i*6+k+1]=(short)(i*4+j+1);
					idxs[i*6+k+2]=(short)(i*4+j+2);
					idxs[i*6+k+3]=(short)(i*4+j);
					idxs[i*6+k+4]=(short)(i*4+j+2);
					idxs[i*6+k+5]=(short)(i*4+j+3);
				}
			}
			ShortBuffer ibuffer=ShortBuffer.wrap( idxs,0,idxs.length );
			GLES11.glBindBuffer( GLES11.GL_ELEMENT_ARRAY_BUFFER,ibo );
			GLES11.glBufferData( GLES11.GL_ELEMENT_ARRAY_BUFFER,idxs.length*2,ibuffer,GLES11.GL_STATIC_DRAW );
		}
		
		GLES11.glViewport( 0,0,Width(),Height() );
		
		GLES11.glMatrixMode( GLES11.GL_PROJECTION );
		GLES11.glLoadIdentity();
		GLES11.glOrthof( 0,Width(),Height(),0,-1,1 );
		
		GLES11.glMatrixMode( GLES11.GL_MODELVIEW );
		GLES11.glLoadIdentity();
		
		GLES11.glEnable( GLES11.GL_BLEND );
		GLES11.glBlendFunc( GLES11.GL_ONE,GLES11.GL_ONE_MINUS_SRC_ALPHA );

		GLES11.glBindBuffer( GLES11.GL_ARRAY_BUFFER,vbo );
		GLES11.glBindBuffer( GLES11.GL_ELEMENT_ARRAY_BUFFER,ibo );
		GLES11.glEnableClientState( GLES11.GL_VERTEX_ARRAY );
		GLES11.glEnableClientState( GLES11.GL_TEXTURE_COORD_ARRAY );
		GLES11.glEnableClientState( GLES11.GL_COLOR_ARRAY );
		GLES11.glVertexPointer( 2,GLES11.GL_FLOAT,16,0 );
		GLES11.glTexCoordPointer( 2,GLES11.GL_FLOAT,16,8 );
		GLES11.glColorPointer( 4,GLES11.GL_UNSIGNED_BYTE,0,MAX_VERTICES*16 );

		Reset();
		
		return 1;
	}
	
	void EndRender(){
		if( gles20 ) return;
		Flush();
	}
	
	boolean LoadSurface__UNSAFE__( gxtkSurface surface,String path ){
		Bitmap bitmap=game.LoadBitmap( path );
		if( bitmap==null ) return false;
		surface.SetBitmap( bitmap );
		return true;
	}
	
	gxtkSurface LoadSurface( String path ){
		gxtkSurface surf=new gxtkSurface();
		if( !LoadSurface__UNSAFE__( surf,path ) ) return null;
		return surf;
	}
	
	gxtkSurface CreateSurface( int width,int height ){
		Bitmap bitmap=Bitmap.createBitmap( width,height,Bitmap.Config.ARGB_8888 );
		if( bitmap!=null ) return new gxtkSurface( bitmap );
		return null;
	}
	
	void DiscardGraphics(){
		gxtkSurface.FlushDiscarded( false );
		seq+=1;
	}

	int SetAlpha( float alpha ){
		this.alpha=alpha;
		int a=(int)(alpha*255);
		colorARGB=(a<<24) | ((int)(b*alpha)<<16) | ((int)(g*alpha)<<8) | (int)(r*alpha);
		return 0;
	}

	int SetColor( float r,float g,float b ){
		this.r=r;
		this.g=g;
		this.b=b;
		int a=(int)(alpha*255);
		colorARGB=(a<<24) | ((int)(b*alpha)<<16) | ((int)(g*alpha)<<8) | (int)(r*alpha);
		return 0;
	}
	
	int SetBlend( int blend ){
		if( blend==this.blend ) return 0;
		
		Flush();
		
		this.blend=blend;
		
		switch( blend ){
		case 1:
			GLES11.glBlendFunc( GLES11.GL_ONE,GLES11.GL_ONE );
			break;
		default:
			GLES11.glBlendFunc( GLES11.GL_ONE,GLES11.GL_ONE_MINUS_SRC_ALPHA );
		}
		return 0;
	}
	
	int SetScissor( int x,int y,int w,int h ){
		Flush();
		
		if( x!=0 || y!=0 || w!=Width() || h!=Height() ){
			GLES11.glEnable( GLES11.GL_SCISSOR_TEST );
			y=Height()-y-h;
			GLES11.glScissor( x,y,w,h );
		}else{
			GLES11.glDisable( GLES11.GL_SCISSOR_TEST );
		}
		return 0;
	}
	
	int SetMatrix( float ix,float iy,float jx,float jy,float tx,float ty ){
	
		tformed=(ix!=1 || iy!=0 || jx!=0 || jy!=1 || tx!=0 || ty!=0);
		this.ix=ix;
		this.iy=iy;
		this.jx=jx;
		this.jy=jy;
		this.tx=tx;
		this.ty=ty;
		
		return 0;
	}
	
	int Cls( float r,float g,float b ){
		Reset();
		
		GLES11.glClearColor( r/255.0f,g/255.0f,b/255.0f,1 );
		GLES11.glClear( GLES11.GL_COLOR_BUFFER_BIT );	//|GLES11.GL_DEPTH_BUFFER_BIT ); //GL_DEPTH_BUFFER_BIT crashes someone's phone...
		
		return 0;
	}
	
	int DrawPoint( float x,float y ){
	
		if( tformed ){
			float px=x;
			x=px * ix + y * jx + tx;
			y=px * iy + y * jy + ty;
		}
		
		Begin( 1,1,null );
		
		vertices[vp]=x+.5f;vertices[vp+1]=y+.5f;
		
		colors[cp]=colorARGB;
		
		return 0;
	}
	
	int DrawLine( float x0,float y0,float x1,float y1 ){
		
		if( tformed ){
			float tx0=x0,tx1=x1;
			x0=tx0 * ix + y0 * jx + tx;
			y0=tx0 * iy + y0 * jy + ty;
			x1=tx1 * ix + y1 * jx + tx;
			y1=tx1 * iy + y1 * jy + ty;
		}

		Begin( 2,2,null );

		vertices[vp]=x0+.5f;vertices[vp+1]=y0+.5f;
		vertices[vp+4]=x1+.5f;vertices[vp+5]=y1+.5f;

		colors[cp]=colors[cp+1]=colorARGB;

		return 0;
 	}

	int DrawRect( float x,float y,float w,float h ){
	
		float x0=x,x1=x+w,x2=x+w,x3=x;
		float y0=y,y1=y,y2=y+h,y3=y+h;
		
		if( tformed ){
			float tx0=x0,tx1=x1,tx2=x2,tx3=x3;
			x0=tx0 * ix + y0 * jx + tx;
			y0=tx0 * iy + y0 * jy + ty;
			x1=tx1 * ix + y1 * jx + tx;
			y1=tx1 * iy + y1 * jy + ty;
			x2=tx2 * ix + y2 * jx + tx;
			y2=tx2 * iy + y2 * jy + ty;
			x3=tx3 * ix + y3 * jx + tx;
			y3=tx3 * iy + y3 * jy + ty;
		}

		Begin( 4,4,null );
		
		vertices[vp]=x0;vertices[vp+1]=y0;
		vertices[vp+4]=x1;vertices[vp+5]=y1;
		vertices[vp+8]=x2;vertices[vp+9]=y2;
		vertices[vp+12]=x3;vertices[vp+13]=y3;
		
		colors[cp]=colors[cp+1]=colors[cp+2]=colors[cp+3]=colorARGB;

		return 0;
	}
	
	int DrawOval( float x,float y,float w,float h ){

		float xr=w/2.0f;
		float yr=h/2.0f;

		int n;	
		if( tformed ){
			float xx=xr*ix,xy=xr*iy,xd=(float)Math.sqrt(xx*xx+xy*xy);
			float yx=yr*jx,yy=yr*jy,yd=(float)Math.sqrt(yx*yx+yy*yy);
			n=(int)( xd+yd );
		}else{
			n=(int)( Math.abs(xr)+Math.abs(yr) );
		}

		if( n>MAX_VERTICES ){
			n=MAX_VERTICES;
		}else if( n<12 ){
			n=12;
		}else{
			n&=~3;
		}
		
		x+=xr;
		y+=yr;
		
		Begin( n,n,null );
		
		for( int i=0;i<n;++i ){
			float th=i * 6.28318531f / n;
			float x0=(float)(x+Math.cos(th)*xr);
			float y0=(float)(y+Math.sin(th)*yr);
			if( tformed ){
				float tx0=x0;
				x0=tx0 * ix + y0 * jx + tx;
				y0=tx0 * iy + y0 * jy + ty;
			}
			vertices[vp]=x0;
			vertices[vp+1]=y0;
			colors[cp]=colorARGB;
			vp+=4;
			cp+=1;
		}
		
		return 0;
	}
	
	int DrawPoly( float[] verts ){
		if( verts.length<6 || verts.length>MAX_VERTICES*2 ) return 0;

		Begin( verts.length/2,verts.length/2,null );		
		
		if( tformed ){
			for( int i=0;i<verts.length;i+=2 ){
				vertices[vp  ]=verts[i] * ix + verts[i+1] * jx + tx;
				vertices[vp+1]=verts[i] * iy + verts[i+1] * jy + ty;
				colors[cp]=colorARGB;
				vp+=4;
				cp+=1;
			}
		}else{
			for( int i=0;i<verts.length;i+=2 ){
				vertices[vp  ]=verts[i];
				vertices[vp+1]=verts[i+1];
				colors[cp]=colorARGB;
				vp+=4;
				cp+=1;
			}
		}
		
		return 0;
	}
	
	int DrawPoly2( float[] verts,gxtkSurface surface,int srcx,int srcy ){
	
		int n=verts.length/4;
		if( n<1 || n>MAX_VERTICES ) return 0;
		
		Begin( n,n,surface );
		
		for( int i=0;i<n;++i ){
			int j=i*4;
			if( tformed ){
				vertices[vp  ]=verts[j] * ix + verts[j+1] * jx + tx;
				vertices[vp+1]=verts[j] * iy + verts[j+1] * jy + ty;
			}else{
				vertices[vp  ]=verts[j];
				vertices[vp+1]=verts[j+1];
			}
			vertices[vp+2]=(srcx+verts[j+2])*surface.uscale;
			vertices[vp+3]=(srcy+verts[j+3])*surface.vscale;
			colors[cp]=colorARGB;
			vp+=4;
			cp+=1;
		}
		
		return 0;
	}
	
	int DrawSurface( gxtkSurface surface,float x,float y ){
	
		float w=surface.width;
		float h=surface.height;
		float u0=0,u1=w*surface.uscale;
		float v0=0,v1=h*surface.vscale;
		
		float x0=x,x1=x+w,x2=x+w,x3=x;
		float y0=y,y1=y,y2=y+h,y3=y+h;
		
		if( tformed ){
			float tx0=x0,tx1=x1,tx2=x2,tx3=x3;
			x0=tx0 * ix + y0 * jx + tx;
			y0=tx0 * iy + y0 * jy + ty;
			x1=tx1 * ix + y1 * jx + tx;
			y1=tx1 * iy + y1 * jy + ty;
			x2=tx2 * ix + y2 * jx + tx;
			y2=tx2 * iy + y2 * jy + ty;
			x3=tx3 * ix + y3 * jx + tx;
			y3=tx3 * iy + y3 * jy + ty;
		}
	
		Begin( 4,4,surface );
		
		vertices[vp]=x0;vertices[vp+1]=y0;vertices[vp+2]=u0;vertices[vp+3]=v0;
		vertices[vp+4]=x1;vertices[vp+5]=y1;vertices[vp+6]=u1;vertices[vp+7]=v0;
		vertices[vp+8]=x2;vertices[vp+9]=y2;vertices[vp+10]=u1;vertices[vp+11]=v1;
		vertices[vp+12]=x3;vertices[vp+13]=y3;vertices[vp+14]=u0;vertices[vp+15]=v1;

		colors[cp]=colors[cp+1]=colors[cp+2]=colors[cp+3]=colorARGB;

		return 0;
	}
	
	int DrawSurface2( gxtkSurface surface,float x,float y,int srcx,int srcy,int srcw,int srch ){
	
		float w=srcw;
		float h=srch;
		float u0=srcx*surface.uscale,u1=(srcx+srcw)*surface.uscale;
		float v0=srcy*surface.vscale,v1=(srcy+srch)*surface.vscale;
		
		float x0=x,x1=x+w,x2=x+w,x3=x;
		float y0=y,y1=y,y2=y+h,y3=y+h;
		
		if( tformed ){
			float tx0=x0,tx1=x1,tx2=x2,tx3=x3;
			x0=tx0 * ix + y0 * jx + tx;
			y0=tx0 * iy + y0 * jy + ty;
			x1=tx1 * ix + y1 * jx + tx;
			y1=tx1 * iy + y1 * jy + ty;
			x2=tx2 * ix + y2 * jx + tx;
			y2=tx2 * iy + y2 * jy + ty;
			x3=tx3 * ix + y3 * jx + tx;
			y3=tx3 * iy + y3 * jy + ty;
		}

		Begin( 4,4,surface );
		
		vertices[vp]=x0;vertices[vp+1]=y0;vertices[vp+2]=u0;vertices[vp+3]=v0;
		vertices[vp+4]=x1;vertices[vp+5]=y1;vertices[vp+6]=u1;vertices[vp+7]=v0;
		vertices[vp+8]=x2;vertices[vp+9]=y2;vertices[vp+10]=u1;vertices[vp+11]=v1;
		vertices[vp+12]=x3;vertices[vp+13]=y3;vertices[vp+14]=u0;vertices[vp+15]=v1;

		colors[cp]=colors[cp+1]=colors[cp+2]=colors[cp+3]=colorARGB;

		return 0;
	}
	
	int ReadPixels( int[] pixels,int x,int y,int width,int height,int offset,int pitch ){
	
		Flush();
		
		int[] texels=new int[width*height];
		IntBuffer buf=IntBuffer.wrap( texels );

		GLES11.glReadPixels( x,Height()-y-height,width,height,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_BYTE,buf );

		int i=0;
		for( int py=height-1;py>=0;--py ){
			int j=offset+py*pitch;
			for( int px=0;px<width;++px ){
				int p=texels[i++];
				//RGBA -> BGRA, Big Endian!
				pixels[j++]=(p&0xff000000)|((p<<16)&0xff0000)|(p&0xff00)|((p>>16)&0xff);
			}
		}
	
		return 0;
	}

	int WritePixels2( gxtkSurface surface,int[] pixels,int x,int y,int width,int height,int offset,int pitch ){
	
		surface.bitmap.setPixels( pixels,offset,pitch,x,y,width,height );
		
		surface.Invalidate();
	
		return 0;
	}
	
}

class gxtkSurface{

	Bitmap bitmap;
	
	int width,height;
	int twidth,theight;
	float uscale,vscale;
	boolean hasAlpha;
	int format,type;
	int texId,seq;

	static Vector discarded=new Vector();
	
	gxtkSurface(){
	}
	
	gxtkSurface( Bitmap bitmap ){
		SetBitmap( bitmap );
	}
	
	void SetBitmap( Bitmap bitmap ){
		this.bitmap=bitmap;
		width=bitmap.getWidth();
		height=bitmap.getHeight();
		hasAlpha=bitmap.hasAlpha();
		twidth=Pow2Size( width );
		theight=Pow2Size( height );
		uscale=1.0f/(float)twidth;
		vscale=1.0f/(float)theight;
	}

	protected void finalize(){
		Discard();
	}
	
	int Pow2Size( int n ){
		int i=1;
		while( i<n ) i*=2;
		return i;
	}
	
	static void FlushDiscarded( boolean deltexs ){
		if( deltexs ){
			int n=discarded.size();
			if( n>0 ){
				int[] texs=new int[n];
				for( int i=0;i<n;++i ){
					texs[i]=((Integer)discarded.elementAt(i)).intValue();
				}
				GLES11.glDeleteTextures( n,texs,0 );
			}
		}
		discarded.clear();
	}
	
	void Invalidate(){
		if( seq==gxtkGraphics.seq ){
			discarded.add( Integer.valueOf( texId ) );
			seq=0;
		}
	}

	//Experimental version...
	//
	void Bind2(){
	
		if( seq==gxtkGraphics.seq ){
			GLES11.glBindTexture( GLES11.GL_TEXTURE_2D,texId );
			return;
		}
        
        if( bitmap==null ) throw new Error( "Attempt to use discarded image" );
		
		FlushDiscarded( true );

		int[] texs=new int[1];
		GLES11.glGenTextures( 1,texs,0 );
		texId=texs[0];
		if( texId==0 ) throw new Error( "glGenTextures failed" );
		seq=gxtkGraphics.seq;
		
		GLES11.glBindTexture( GLES11.GL_TEXTURE_2D,texId );
		
		if( MonkeyConfig.MOJO_IMAGE_FILTERING_ENABLED.equals( "1" ) ){
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MAG_FILTER,GLES11.GL_LINEAR );
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MIN_FILTER,GLES11.GL_LINEAR );
		}else{
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MAG_FILTER,GLES11.GL_NEAREST );
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MIN_FILTER,GLES11.GL_NEAREST );
		}

		GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_WRAP_S,GLES11.GL_CLAMP_TO_EDGE );
		GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_WRAP_T,GLES11.GL_CLAMP_TO_EDGE );
		
		int pwidth=(width==twidth) ? width : width+1;
		int pheight=(height==theight) ? height : height+1;

		Bitmap ibitmap=bitmap,bitmap2=null;
		
		if( width!=pwidth || height!=pheight ){
		
			bitmap2=Bitmap.createBitmap( twidth,theight,bitmap.getConfig() );
			Canvas canvas=new Canvas( bitmap2 );
			canvas.drawBitmap( bitmap,0,0,null );

			if( width!=pwidth ){
				canvas.save();
				canvas.clipRect( width,0,pwidth,height );
				canvas.drawBitmap( bitmap,1,0,null );
				canvas.restore();
				if( height!=pheight ){
					canvas.save();
					canvas.clipRect( 0,height,pwidth,pheight );
					canvas.drawBitmap( bitmap2,0,1,null );
					canvas.restore();
				}
			}else if( height!=pheight ){
				canvas.save();
				canvas.clipRect( 0,height,width,pheight );
				canvas.drawBitmap( bitmap,0,1,null );
				canvas.restore();
			}

			ibitmap=bitmap2;
		}
		
		int format=GLUtils.getInternalFormat( ibitmap ),type=GLUtils.getType( ibitmap );
		
		GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,format,twidth,theight,0,format,type,null );
		
		GLUtils.texSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,ibitmap );

		if( bitmap2!=null ) bitmap2.recycle();
	}

	void Bind(){
	
		if( seq==gxtkGraphics.seq ){
			GLES11.glBindTexture( GLES11.GL_TEXTURE_2D,texId );
			return;
		}
        
        if( bitmap==null ) throw new Error( "Attempt to use discarded image" );
		
		FlushDiscarded( true );

		int[] texs=new int[1];
		GLES11.glGenTextures( 1,texs,0 );
		texId=texs[0];
		if( texId==0 ) throw new Error( "glGenTextures failed" );
		seq=gxtkGraphics.seq;
		
		GLES11.glBindTexture( GLES11.GL_TEXTURE_2D,texId );
		
		if( MonkeyConfig.MOJO_IMAGE_FILTERING_ENABLED.equals( "1" ) ){
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MAG_FILTER,GLES11.GL_LINEAR );
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MIN_FILTER,GLES11.GL_LINEAR );
		}else{
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MAG_FILTER,GLES11.GL_NEAREST );
			GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_MIN_FILTER,GLES11.GL_NEAREST );
		}

		GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_WRAP_S,GLES11.GL_CLAMP_TO_EDGE );
		GLES11.glTexParameteri( GLES11.GL_TEXTURE_2D,GLES11.GL_TEXTURE_WRAP_T,GLES11.GL_CLAMP_TO_EDGE );
		
		int pwidth=(width==twidth) ? width : width+1;
		int pheight=(height==theight) ? height : height+1;

		int sz=pwidth*pheight;
		int[] pixels=new int[sz];
		bitmap.getPixels( pixels,0,pwidth,0,0,width,height );
		
		//pad edges for non pow-2 images - not sexy!
		if( width!=pwidth ){
			for( int y=0;y<height;++y ){
				pixels[y*pwidth+width]=pixels[y*pwidth+width-1];
			}
		}
		if( height!=pheight ){
			for( int x=0;x<width;++x ){
				pixels[height*pwidth+x]=pixels[height*pwidth+x-pwidth];
			}
		}
		if( width!=pwidth && height!=pheight ){
			pixels[height*pwidth+width]=pixels[height*pwidth+width-pwidth-1];
		}
		
		GLES11.glPixelStorei( GLES11.GL_UNPACK_ALIGNMENT,1 );
		
		boolean hicolor_textures=MonkeyConfig.MOJO_HICOLOR_TEXTURES.equals( "1" );
		
		if( hicolor_textures && hasAlpha ){

			//RGBA8888...
			ByteBuffer buf=ByteBuffer.allocate( sz*4 );
			buf.order( ByteOrder.BIG_ENDIAN );

			for( int i=0;i<sz;++i ){
				int p=pixels[i];
				int a=(p>>24) & 255;
				int r=((p>>16) & 255)*a/255;
				int g=((p>>8) & 255)*a/255;
				int b=(p & 255)*a/255;
				buf.putInt( (r<<24)|(g<<16)|(b<<8)|a );
			}
			buf.position( 0 );
			GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,GLES11.GL_RGBA,twidth,theight,0,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_BYTE,null );
			GLES11.glTexSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,pwidth,pheight,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_BYTE,buf );

		}else if( hicolor_textures && !hasAlpha ){
		
			//RGB888...
			ByteBuffer buf=ByteBuffer.allocate( sz*3 );
			buf.order( ByteOrder.BIG_ENDIAN );
			
			for( int i=0;i<sz;++i ){
				int p=pixels[i];
				int r=(p>>16) & 255;
				int g=(p>>8) & 255;
				int b=p & 255;
				buf.put( (byte)r );
				buf.put( (byte)g );
				buf.put( (byte)b );
			}
			buf.position( 0 );
			GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,GLES11.GL_RGB,twidth,theight,0,GLES11.GL_RGB,GLES11.GL_UNSIGNED_BYTE,null );
			GLES11.glTexSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,pwidth,pheight,GLES11.GL_RGB,GLES11.GL_UNSIGNED_BYTE,buf );
			
		}else if( !hicolor_textures && hasAlpha ){

			//16 bit RGBA...
			ByteBuffer buf=ByteBuffer.allocate( sz*2 );
			buf.order( ByteOrder.LITTLE_ENDIAN );
			
			//do we need 4 bit alpha?
			boolean a4=false;
			for( int i=0;i<sz;++i ){
				int a=(pixels[i]>>28) & 15;
				if( a!=0 && a!=15 ){
					a4=true;
					break;
				}
			}
			if( a4 ){
				//RGBA4444...
				for( int i=0;i<sz;++i ){
					int p=pixels[i];
					int a=(p>>28) & 15;
					int r=((p>>20) & 15)*a/15;
					int g=((p>>12) & 15)*a/15;
					int b=((p>> 4) & 15)*a/15;
					buf.putShort( (short)( (r<<12)|(g<<8)|(b<<4)|a ) );
				}
				buf.position( 0 );
				GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,GLES11.GL_RGBA,twidth,theight,0,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_SHORT_4_4_4_4,null );
				GLES11.glTexSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,pwidth,pheight,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_SHORT_4_4_4_4,buf );
			}else{
				//RGBA5551...
				for( int i=0;i<sz;++i ){
					int p=pixels[i];
					int a=(p>>31) & 1;
					int r=((p>>19) & 31)*a;
					int g=((p>>11) & 31)*a;
					int b=((p>> 3) & 31)*a;
					buf.putShort( (short)( (r<<11)|(g<<6)|(b<<1)|a ) );
				}
				buf.position( 0 );
				GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,GLES11.GL_RGBA,twidth,theight,0,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_SHORT_5_5_5_1,null );
				GLES11.glTexSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,pwidth,pheight,GLES11.GL_RGBA,GLES11.GL_UNSIGNED_SHORT_5_5_5_1,buf );
			}
		}else if( !hicolor_textures && !hasAlpha ){
		
			ByteBuffer buf=ByteBuffer.allocate( sz*2 );
			buf.order( ByteOrder.LITTLE_ENDIAN );
			
			//RGB565...
			for( int i=0;i<sz;++i ){
				int p=pixels[i];
				int r=(p>>19) & 31;
				int g=(p>>10) & 63;
				int b=(p>> 3) & 31;
				buf.putShort( (short)( (r<<11)|(g<<5)|b ) );
			}
			buf.position( 0 );
			GLES11.glTexImage2D( GLES11.GL_TEXTURE_2D,0,GLES11.GL_RGB,twidth,theight,0,GLES11.GL_RGB,GLES11.GL_UNSIGNED_SHORT_5_6_5,null );
			GLES11.glTexSubImage2D( GLES11.GL_TEXTURE_2D,0,0,0,pwidth,pheight,GLES11.GL_RGB,GLES11.GL_UNSIGNED_SHORT_5_6_5,buf );
		}
	}

	//***** GXTK API *****
	
	int Discard(){
		Invalidate();
		bitmap=null;
		return 0;
	}

	int Width(){
		return width;
	}
	
	int Height(){
		return height;
	}

	int Loaded(){
		return 1;
	}
	
	void OnUnsafeLoadComplete(){
	}
}

class gxtkAudio{

	static class gxtkChannel{
		int stream;		//SoundPool stream ID, 0=none
		float volume=1;
		float rate=1;
		float pan;
		int state;
	};
	
	BBAndroidGame game;
	SoundPool pool;
	MediaPlayer music;
	float musicVolume=1;
	int musicState=0;
	
	gxtkChannel[] channels=new gxtkChannel[32];
	
	gxtkAudio(){
		game=BBAndroidGame.AndroidGame();
		pool=new SoundPool( 32,AudioManager.STREAM_MUSIC,0 );
		for( int i=0;i<32;++i ){
			channels[i]=new gxtkChannel();
		}
	}
	
	void OnDestroy(){
		for( int i=0;i<32;++i ){
			if( channels[i].state!=0 ) pool.stop( channels[i].stream );
		}
		pool.release();
		pool=null;
	}
	
	//***** GXTK API *****
	int Suspend(){
		if( musicState==1 ) music.pause();
		for( int i=0;i<32;++i ){
			if( channels[i].state==1 ) pool.pause( channels[i].stream );
		}
		return 0;
	}
	
	int Resume(){
		if( musicState==1 ) music.start();
		for( int i=0;i<32;++i ){
			if( channels[i].state==1 ) pool.resume( channels[i].stream );
		}
		return 0;
	}
	

	boolean LoadSample__UNSAFE__( gxtkSample sample,String path ){
		gxtkSample.FlushDiscarded( pool );
		int sound=game.LoadSound( path,pool );
		if( sound==0 ) return false;
		sample.SetSound( sound );
		return true;
	}
	
	gxtkSample LoadSample( String path ){
		gxtkSample sample=new gxtkSample();
		if( !LoadSample__UNSAFE__( sample,path ) ) return null;
		return sample;
	}
	
	int PlaySample( gxtkSample sample,int channel,int flags ){
		gxtkChannel chan=channels[channel];
		if( chan.state!=0 ) pool.stop( chan.stream );
		float rv=(chan.pan * .5f + .5f) * chan.volume;
		float lv=chan.volume-rv;
		int loops=(flags&1)!=0 ? -1 : 0;

		//chan.stream=pool.play( sample.sound,lv,rv,0,loops,chan.rate );
		//chan.state=1;
		//return 0;
		
		//Ugly as hell, but seems to work for now...pauses 10 secs max...
		for( int i=0;i<100;++i ){
			chan.stream=pool.play( sample.sound,lv,rv,0,loops,chan.rate );
			if( chan.stream!=0 ){
				chan.state=1;
				return 0;
			}
//			throw new Error( "PlaySample failed to play sound" );
			try{
				Thread.sleep( 100 );
			}catch( java.lang.InterruptedException ex ){
			}
		}
		throw new Error( "PlaySample failed to play sound" );
	}
	
	int StopChannel( int channel ){
		gxtkChannel chan=channels[channel];
		if( chan.state!=0 ){
			pool.stop( chan.stream );
			chan.state=0;
		}
		return 0;
	}
	
	int PauseChannel( int channel ){
		gxtkChannel chan=channels[channel];
		if( chan.state==1 ){
			pool.pause( chan.stream );
			chan.state=2;
		}
		return 0;
	}
	
	int ResumeChannel( int channel ){
		gxtkChannel chan=channels[channel];
		if( chan.state==2 ){
			pool.resume( chan.stream );
			chan.state=1;
		}
		return 0;
	}
	
	int ChannelState( int channel ){
		return -1;
	}
	
	int SetVolume( int channel,float volume ){
		gxtkChannel chan=channels[channel];
		chan.volume=volume;
		if( chan.stream!=0 ){
			float rv=(chan.pan * .5f + .5f) * chan.volume;
			float lv=chan.volume-rv;
			pool.setVolume( chan.stream,lv,rv );
		}
		return 0;
	}
	
	int SetPan( int channel,float pan ){
		gxtkChannel chan=channels[channel];
		chan.pan=pan;
		if( chan.stream!=0 ){
			float rv=(chan.pan * .5f + .5f) * chan.volume;
			float lv=chan.volume-rv;
			pool.setVolume( chan.stream,lv,rv );
		}
		return 0;
	}

	int SetRate( int channel,float rate ){
		gxtkChannel chan=channels[channel];
		chan.rate=rate;
		if( chan.stream!=0 ){
			pool.setRate( chan.stream,chan.rate );
		}
		return 0;
	}
	
	int PlayMusic( String path,int flags ){
		StopMusic();
		music=game.OpenMedia( path );
		if( music==null ) return -1;
		music.setLooping( (flags&1)!=0 );
		music.setVolume( musicVolume,musicVolume );
		music.start();
		musicState=1;
		return 0;
	}
	
	int StopMusic(){
		if( musicState!=0 ){
			music.stop();
			music.release();
			musicState=0;
			music=null;
		}
		return 0;
	}
	
	int PauseMusic(){
		if( musicState==1 && music.isPlaying() ){
			music.pause();
			musicState=2;
		}
		return 0;
	}
	
	int ResumeMusic(){
		if( musicState==2 ){
			music.start();
			musicState=1;
		}
		return 0;
	}
	
	int MusicState(){
		if( musicState==1 && !music.isPlaying() ) musicState=0;
		return musicState;
	}
	
	int SetMusicVolume( float volume ){
		if( musicState!=0 ) music.setVolume( volume,volume );
		musicVolume=volume;
		return 0;
	}	
}

class gxtkSample{

	int sound;
	
	static Vector discarded=new Vector();
	
	gxtkSample(){
	}
	
	gxtkSample( int sound ){
		this.sound=sound;
	}
	
	void SetSound( int sound ){
		this.sound=sound;
	}
	
	protected void finalize(){
		Discard();
	}
	
	static void FlushDiscarded( SoundPool pool ){
		int n=discarded.size();
		if( n==0 ) return;
		Vector out=new Vector();
		for( int i=0;i<n;++i ){
			Integer val=(Integer)discarded.elementAt(i);
			if( pool.unload( val.intValue() ) ){
//				bb_std_lang.print( "unload OK!" );
			}else{
//				bb_std_lang.print( "unload failed!" );
				out.add( val );
			}
		}
		discarded=out;
//		bb_std_lang.print( "undiscarded="+out.size() );
	}

	//***** GXTK API *****
	
	int Discard(){
		if( sound!=0 ){
			discarded.add( Integer.valueOf( sound ) );
			sound=0;
		}
		return 0;
	}
}


class BBThread implements Runnable{

	Object _result;
	boolean _running;
	Thread _thread;
	
	boolean IsRunning(){
		return _running;
	}
	
	void Start(){
		if( _running ) return;
		_result=null;
		_running=true;
		_thread=new Thread( this );
		_thread.start();
	}
	
	void SetResult( Object result ){
		_result=result;
	}
	
	Object Result(){
		return _result;
	}
	
	void Wait(){
		while( _running ){
			try{
				_thread.join();
			}catch( InterruptedException ex ){
			}
		}
	}
	
	void Run__UNSAFE__(){
	}

	public void run(){
		Run__UNSAFE__();
		_running=false;
	}
}


class BBHttpRequest extends BBThread{

	HttpURLConnection _con;
	String _response;
	int _status;
	int _recv;
	
	String _sendText,_encoding;
	
	void Open( String req,String url ){
		try{
			URL turl=new URL( url );
			_con=(HttpURLConnection)turl.openConnection();
			_con.setRequestMethod( req );
		}catch( Exception ex ){
		}		
		_response="";
		_status=-1;
		_recv=0;
	}
	
	void SetHeader( String name,String value ){
		_con.setRequestProperty( name,value );
	}
	
	void Send(){
		_sendText=_encoding=null;
		Start();
	}
	
	void SendText( String text,String encoding ){
		_sendText=text;
		_encoding=encoding;
		Start();
	}
	
	String ResponseText(){
		return _response;
	}
	
	int Status(){
		return _status;
	}
	
	void Run__UNSAFE__(){
		try{
			if( _sendText!=null ){
			
				byte[] bytes=_sendText.getBytes( "UTF-8" );

				_con.setDoOutput( true );
				_con.setFixedLengthStreamingMode( bytes.length );
				
				OutputStream out=_con.getOutputStream();
				out.write( bytes,0,bytes.length );
				out.close();
			}
			
			InputStream in=_con.getInputStream();

			byte[] buf=new byte[4096];
			ByteArrayOutputStream out=new ByteArrayOutputStream( 1024 );
			for(;;){
				int n=in.read( buf );
				if( n<0 ) break;
				out.write( buf,0,n );
				_recv+=n;
			}
			in.close();
			
			_response=new String( out.toByteArray(),"UTF-8" );

			_status=_con.getResponseCode();
			
		}catch( IOException ex ){
		}
		
		_con.disconnect();
	}
	
	int BytesReceived(){
		return _recv;
	}
}

class c_App extends Object{
	public final c_App m_App_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<152>";
		if((bb_app.g__app)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<152>";
			bb_std_lang.error("App has already been created");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<153>";
		bb_app.g__app=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<154>";
		bb_app.g__delegate=(new c_GameDelegate()).m_GameDelegate_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<155>";
		bb_app.g__game.SetDelegate(bb_app.g__delegate);
		bb_std_lang.popErr();
		return this;
	}
	public final int p_OnResize(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public int p_OnCreate(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnSuspend(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public int p_OnResume(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public int p_OnUpdate(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnLoading(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public int p_OnRender(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnClose(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<177>";
		bb_app.g_EndApp();
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnBack(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<181>";
		p_OnClose();
		bb_std_lang.popErr();
		return 0;
	}
}
class c_DroneTournamentGame extends c_App{
	public final c_DroneTournamentGame m_DroneTournamentGame_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<16>";
		super.m_App_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<16>";
		bb_std_lang.popErr();
		return this;
	}
	int m_SCREEN_WIDTH=0;
	int m_SCREEN_HEIGHT=0;
	String m_game_state="";
	boolean m_keyboard_enabled=false;
	float m_timer_begin=.0f;
	c_User m_user=null;
	String m_tournament_server_url="https://evolvinggames.herokuapp.com/dronetournament";
	c_MultiplayerService m_multiplayer_service=null;
	c_Game m_game=null;
	c_Image m_play_tutorial_button_image=null;
	c_Image m_play_multiplayer_button_image=null;
	c_Image m_join_button_image=null;
	c_Image m_win_button=null;
	c_Image m_lose_button=null;
	c_Image m_end_turn_button_image=null;
	c_Image m_small_end_turn_button_image=null;
	c_Image m_t_fighter_img=null;
	c_Image m_eye_fighter_img=null;
	c_Image m_single_turret_img=null;
	public final int p_LoadImages(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<72>";
		m_play_tutorial_button_image=bb_graphics.g_LoadImage("images/play_tutorial_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<73>";
		m_play_multiplayer_button_image=bb_graphics.g_LoadImage("images/play_multiplayer_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<74>";
		m_join_button_image=bb_graphics.g_LoadImage("images/join_game.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<75>";
		m_win_button=bb_graphics.g_LoadImage("images/win_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<76>";
		m_lose_button=bb_graphics.g_LoadImage("images/lose_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<77>";
		m_end_turn_button_image=bb_graphics.g_LoadImage("images/end_turn_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<78>";
		m_small_end_turn_button_image=bb_graphics.g_LoadImage("images/small_end_turn_button.png",1,c_Image.m_DefaultFlags);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<80>";
		m_t_fighter_img=bb_graphics.g_LoadImage("images/t_fighter.png",1,1);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<81>";
		m_eye_fighter_img=bb_graphics.g_LoadImage("images/eye_fighter.png",1,1);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<82>";
		m_single_turret_img=bb_graphics.g_LoadImage("images/single_turret.png",1,1);
		bb_std_lang.popErr();
		return 0;
	}
	c_Button m_play_tutorial_button=null;
	c_Button m_play_multiplayer_button=null;
	c_Button m_join_button=null;
	c_Button m_end_turn_button=null;
	c_Button m_small_end_turn_button=null;
	public final int p_CreateUIElements(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<86>";
		this.m_play_tutorial_button=(new c_Button()).m_Button_new(10.0f,-100.0f,110.0f,60.0f,440.0f,170.0f,this.m_play_tutorial_button_image);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<87>";
		this.m_play_multiplayer_button=(new c_Button()).m_Button_new(10.0f,100.0f,60.0f,260.0f,540.0f,170.0f,this.m_play_multiplayer_button_image);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<88>";
		this.m_join_button=(new c_Button()).m_Button_new(10.0f,100.0f,60.0f,260.0f,540.0f,170.0f,this.m_join_button_image);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<89>";
		this.m_end_turn_button=(new c_Button()).m_Button_new(0.0f,500.0f,50.0f,650.0f,540.0f,170.0f,this.m_end_turn_button_image);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<92>";
		this.m_small_end_turn_button=(new c_Button()).m_Button_new((float)(m_SCREEN_WIDTH-138),(float)(m_SCREEN_HEIGHT-74),(float)(m_SCREEN_WIDTH-138),(float)(m_SCREEN_HEIGHT-74),128.0f,64.0f,this.m_small_end_turn_button_image);
		bb_std_lang.popErr();
		return 0;
	}
	c_Camera m_game_cam=null;
	public final int p_OnCreate(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<53>";
		bb_std_lang.print("Creating Game");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<54>";
		m_SCREEN_WIDTH=bb_app.g_DeviceWidth();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<55>";
		m_SCREEN_HEIGHT=bb_app.g_DeviceHeight();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<56>";
		m_game_state="menu";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<57>";
		this.m_keyboard_enabled=false;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<58>";
		this.m_timer_begin=(float)(bb_app.g_Millisecs());
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<59>";
		bb_app.g_SetUpdateRate(30);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<60>";
		m_user=(new c_User()).m_User_new("","0","");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<62>";
		m_multiplayer_service=(new c_MultiplayerService()).m_MultiplayerService_new(m_tournament_server_url);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<63>";
		m_game=(new c_Game()).m_Game_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<65>";
		p_LoadImages();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<66>";
		p_CreateUIElements();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<67>";
		m_game_cam=(new c_Camera()).m_Camera_new(640,480,m_SCREEN_WIDTH,m_SCREEN_HEIGHT);
		bb_std_lang.popErr();
		return 0;
	}
	public final void p_GetUsername(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<160>";
		if(this.m_keyboard_enabled){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<161>";
			int t_char=bb_input.g_GetChar();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<162>";
			if(t_char==13){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<163>";
				bb_input.g_DisableKeyboard();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<164>";
				this.m_keyboard_enabled=false;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<165>";
				this.m_game_state="get_password";
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<166>";
				if(t_char==8 || t_char==127){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<167>";
					if(this.m_user.m_username.length()<=1){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<168>";
						this.m_user.m_username="";
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<170>";
						this.m_user.m_username=bb_std_lang.slice(this.m_user.m_username,0,-1);
					}
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<172>";
					if(t_char>0 && this.m_user.m_username.length()<13){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<173>";
						this.m_user.m_username=this.m_user.m_username+String.valueOf((char)(t_char));
					}
				}
			}
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<176>";
			bb_input.g_EnableKeyboard();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<177>";
			this.m_keyboard_enabled=true;
		}
		bb_std_lang.popErr();
	}
	public final void p_SignIn(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<204>";
		String t_json_body="{ \"username\": \""+this.m_user.m_username+"\", \"password\": \""+this.m_user.m_password+"\" }";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<205>";
		this.m_multiplayer_service.p_PostJsonRequest("/sign_in",t_json_body);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<206>";
		this.m_game_state="server";
		bb_std_lang.popErr();
	}
	public final void p_GetPassword(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<182>";
		if(this.m_keyboard_enabled){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<183>";
			int t_char=bb_input.g_GetChar();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<184>";
			if(t_char==13){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<185>";
				bb_input.g_DisableKeyboard();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<186>";
				this.m_keyboard_enabled=false;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<187>";
				p_SignIn();
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<188>";
				if(t_char==8 || t_char==127){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<189>";
					if(this.m_user.m_password.length()<=1){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<190>";
						this.m_user.m_password="";
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<192>";
						this.m_user.m_password=bb_std_lang.slice(this.m_user.m_password,0,-1);
					}
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<194>";
					if(t_char>0 && this.m_user.m_password.length()<13){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<195>";
						this.m_user.m_password=this.m_user.m_password+String.valueOf((char)(t_char));
					}
				}
			}
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<198>";
			bb_input.g_EnableKeyboard();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<199>";
			this.m_keyboard_enabled=true;
		}
		bb_std_lang.popErr();
	}
	c_Unit m_tutorial_unit=null;
	int m_moves=0;
	public final void p_SetupTutorial(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<368>";
		c_UnitType t_t_type=(new c_UnitType()).m_UnitType_new((new c_JsonObject()).m_JsonObject_new3("{\"id\": 1, \"name\": \"T-Fighter\", \"speed\": 100, \"turn\": 5, \"armor\": 6, \"full_energy\": 100, \"charge_energy\": 6, \"image_name\": \"t_fighter.png\"}"));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<369>";
		this.m_game.m_opponents=(new c_List()).m_List_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<370>";
		this.m_tutorial_unit=(new c_Unit()).m_Unit_new(1,150.0f,150.0f,-30.0f,t_t_type,"1",1,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<372>";
		c_UnitType t_eye_type=(new c_UnitType()).m_UnitType_new((new c_JsonObject()).m_JsonObject_new3("{\"id\": 2, \"name\": \"Eye-Fighter\", \"speed\": 120, \"turn\": 4, \"armor\": 2, \"full_energy\": 100, \"charge_energy\": 4, \"image_name\": \"eye_fighter.png\"}"));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<373>";
		for(int t_i=0;t_i<=3;t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<374>";
			float t_xrand=bb_random.g_Rnd2(200.0f,580.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<375>";
			float t_yrand=bb_random.g_Rnd2(200.0f,420.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<376>";
			c_Unit t_opponent=(new c_Unit()).m_Unit_new(t_i+2,t_xrand,t_yrand,30.0f,t_eye_type,"0",2,0.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<377>";
			this.m_game.m_opponents.p_AddLast(t_opponent);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<379>";
		this.m_moves=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<380>";
		this.m_game.m_particles=(new c_List2()).m_List_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<381>";
		this.m_game_state="tutorial";
		bb_std_lang.popErr();
	}
	c_JsonArray m_game_list=null;
	c_List3 m_game_select=null;
	public final int p_BuildGameListUI(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<286>";
		this.m_game_select=(new c_List3()).m_List_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<287>";
		int t_x=10;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<288>";
		int t_y=10;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<289>";
		for(int t_i=0;t_i<this.m_game_list.p_Length();t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<290>";
			c_JsonObject t_game_object=bb_std_lang.as(c_JsonObject.class,(this.m_game_list.p_Get3(t_i)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<291>";
			c_GameSelect t_game_ui=(new c_GameSelect()).m_GameSelect_new((float)(t_x),(float)(t_y),400.0f,50.0f,String.valueOf(t_game_object.p_GetInt("game_id",0)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<292>";
			this.m_game_select.p_AddLast3(t_game_ui);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<293>";
			t_y+=55;
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_UsePlayerStateToSetGameState(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<298>";
		if(this.m_game.m_player_state.compareTo("plan")==0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<299>";
			this.m_game_state="multiplayer";
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<300>";
			if(this.m_game.m_player_state.compareTo("finished")==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<301>";
				this.m_game_state="end_turn";
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<302>";
				if(this.m_game.m_player_state.compareTo("updated")==0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<303>";
					this.m_game_state="updated";
				}
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final void p_WinLoseOrContinue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<249>";
		int t_player_unit_count=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<250>";
		int t_opponent_unit_count=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<252>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<252>";
		c_KeyEnumerator t_=this.m_game.m_units.p_Keys().p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<252>";
		while(t_.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<252>";
			int t_key=t_.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<253>";
			c_Unit t_current_unit=this.m_game.m_units.p_Get3(t_key);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<254>";
			if(t_current_unit.m_armor>0 && (t_current_unit.m_player_id.compareTo(this.m_user.m_player_id)==0)){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<255>";
				t_player_unit_count+=1;
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<256>";
				if(t_current_unit.m_armor>0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<257>";
					t_opponent_unit_count+=1;
				}
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<261>";
		if(t_player_unit_count==0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<262>";
			this.m_game_state="loser";
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<263>";
			if(t_opponent_unit_count==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<264>";
				this.m_game_state="winner";
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<266>";
				this.m_game_state="multiplayer_ready";
			}
		}
		bb_std_lang.popErr();
	}
	public final void p_GetGameInfoFromServer(String t_game_id){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<276>";
		this.m_multiplayer_service.p_GetRequest("/game/"+t_game_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<277>";
		this.m_game_state="server";
		bb_std_lang.popErr();
	}
	public final void p_EndTurn(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<559>";
		String t_move_json=this.m_game.p_BuildMoveJson(this.m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<560>";
		this.m_multiplayer_service.p_PostJsonRequest("/end_turn/"+this.m_game.m_id,t_move_json);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<561>";
		this.m_game_state="multiplayer_server";
		bb_std_lang.popErr();
	}
	public final void p_DetermineGameState(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<210>";
		if(this.m_multiplayer_service.p_HasRequestFinished()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<211>";
			String t_action=this.m_multiplayer_service.m_response.p_GetString("action","");
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<212>";
			if(t_action.compareTo("Bad Server Response")==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<213>";
				this.m_game_state="menu";
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<214>";
				if(t_action.compareTo("Sign In")==0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<215>";
					this.m_user.m_player_id=this.m_multiplayer_service.m_response.p_GetString("player_id","");
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<216>";
					this.m_game_state="get_games";
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<217>";
					if(t_action.compareTo("Invalid Sign In")==0){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<218>";
						this.m_user=(new c_User()).m_User_new("","0","");
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<219>";
						this.m_game_state="setup";
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<220>";
						if(t_action.compareTo("List Games")==0){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<221>";
							this.m_game_list=bb_std_lang.as(c_JsonArray.class,(m_multiplayer_service.m_response.p_Get("games",null)));
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<222>";
							p_BuildGameListUI();
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<223>";
							this.m_game_state="list_games";
						}else{
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<224>";
							if(t_action.compareTo("Load Game")==0){
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<225>";
								this.m_game.p_LoadFromJson(this.m_multiplayer_service.m_response,this.m_user.m_player_id);
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<226>";
								p_UsePlayerStateToSetGameState();
							}else{
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<227>";
								if(t_action.compareTo("Turn Stop")==0){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<228>";
									this.m_game_state="multiplayer";
								}else{
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<229>";
									if((t_action.compareTo("Waiting")==0) || (t_action.compareTo("Turn Ended")==0)){
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<230>";
										this.m_timer_begin=(float)(bb_app.g_Millisecs());
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<231>";
										m_game_state="end_turn";
									}else{
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<232>";
										if(t_action.compareTo("Ready")==0){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<233>";
											this.m_moves=30;
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<234>";
											this.m_game.p_LoadServerMoves(this.m_multiplayer_service.m_response);
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<235>";
											p_WinLoseOrContinue();
										}else{
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<236>";
											if(t_action.compareTo("Update Waiting")==0){
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<237>";
												this.m_timer_begin=(float)(bb_app.g_Millisecs());
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<238>";
												m_game_state="updated";
											}else{
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<239>";
												if(t_action.compareTo("Update Ready")==0){
													bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<240>";
													p_GetGameInfoFromServer(this.m_game.m_id);
												}else{
													bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<241>";
													if(t_action.compareTo("Server Move Points")==0){
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<242>";
														this.m_game.p_SetUnitPathsToServerSimulation(this.m_multiplayer_service.m_response,this.m_user.m_player_id);
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<243>";
														p_EndTurn();
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		bb_std_lang.popErr();
	}
	public final void p_GetListOfActiveGames(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<271>";
		this.m_multiplayer_service.p_GetRequest("/games/"+this.m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<272>";
		this.m_game_state="server";
		bb_std_lang.popErr();
	}
	public final void p_JoinGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<281>";
		this.m_multiplayer_service.p_PostRequest("/join_game/"+m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<282>";
		this.m_game_state="server";
		bb_std_lang.popErr();
	}
	public final int p_LiveOpponentCount(c_List t_enemies){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<358>";
		int t_live_opponents=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<359>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<359>";
		c_Enumerator2 t_=t_enemies.p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<359>";
		while(t_.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<359>";
			c_Unit t_enemy=t_.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<360>";
			if(t_enemy.m_armor>0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<361>";
				t_live_opponents=t_live_opponents+1;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<364>";
		bb_std_lang.popErr();
		return t_live_opponents;
	}
	public final int p_MoveCamera(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<514>";
		if(bb_input.g_TouchX(0)>(float)(m_SCREEN_WIDTH-50)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<515>";
			this.m_game_cam.p_MoveRight();
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<516>";
			if(bb_input.g_TouchX(0)<50.0f){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<517>";
				this.m_game_cam.p_MoveLeft();
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<518>";
				if(bb_input.g_TouchY(0)<50.0f){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<519>";
					this.m_game_cam.p_MoveUp();
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<520>";
					if(bb_input.g_TouchY(0)>(float)(m_SCREEN_HEIGHT-50)){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<521>";
						this.m_game_cam.p_MoveDown();
					}
				}
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final void p_RunTutorial(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<395>";
		if(m_moves<1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<396>";
			if(this.m_tutorial_unit.m_armor<1){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<397>";
				this.m_game_state="loser";
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<398>";
				this.m_game_cam.p_Reset();
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<399>";
				if(p_LiveOpponentCount(this.m_game.m_opponents)==0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<400>";
					this.m_game_state="winner";
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<401>";
					this.m_game_cam.p_Reset();
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<402>";
					if((bb_input.g_TouchDown(0))!=0){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<403>";
						if((this.m_tutorial_unit.p_ControlSelected(bb_input.g_TouchX(0)-m_game_cam.m_position.m_x,bb_input.g_TouchY(0)-m_game_cam.m_position.m_y))!=0){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<404>";
							this.m_tutorial_unit.p_SetControl(bb_input.g_TouchX(0)-m_game_cam.m_position.m_x,bb_input.g_TouchY(0)-m_game_cam.m_position.m_y,640.0f,480.0f);
						}else{
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<405>";
							if(this.m_small_end_turn_button.p_Selected()){
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<406>";
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<406>";
								c_Enumerator2 t_=this.m_game.m_opponents.p_ObjectEnumerator();
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<406>";
								while(t_.p_HasNext()){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<406>";
									c_Unit t_enemy=t_.p_NextObject();
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<407>";
									int t_xrand=(int)(bb_random.g_Rnd2(-15.0f,15.0f));
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<408>";
									int t_yrand=(int)(bb_random.g_Rnd2(-15.0f,15.0f));
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<411>";
									t_enemy.p_SetControl(this.m_tutorial_unit.m_position.m_x+(float)(t_xrand),this.m_tutorial_unit.m_position.m_y+(float)(t_yrand),640.0f,480.0f);
								}
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<413>";
								m_moves=30;
							}else{
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<415>";
								p_MoveCamera();
							}
						}
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<418>";
						this.m_tutorial_unit.p_ControlReleased();
					}
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<421>";
			if((bb_input.g_KeyHit(13))!=0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<422>";
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<422>";
				c_Enumerator2 t_2=this.m_game.m_opponents.p_ObjectEnumerator();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<422>";
				while(t_2.p_HasNext()){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<422>";
					c_Unit t_enemy2=t_2.p_NextObject();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<423>";
					int t_xrand2=(int)(bb_random.g_Rnd2(-15.0f,15.0f));
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<424>";
					int t_yrand2=(int)(bb_random.g_Rnd2(-15.0f,15.0f));
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<425>";
					t_enemy2.p_SetControl(this.m_tutorial_unit.m_position.m_x+(float)(t_xrand2),this.m_tutorial_unit.m_position.m_y+(float)(t_yrand2),640.0f,480.0f);
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<427>";
				m_moves=30;
			}
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<430>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<430>";
			c_Enumerator2 t_3=this.m_game.m_opponents.p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<430>";
			while(t_3.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<430>";
				c_Unit t_enemy3=t_3.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<431>";
				if(t_enemy3.m_armor>0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<432>";
					t_enemy3.p_Update();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<433>";
					if(t_enemy3.m_currentEnergy==100.0f){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<434>";
						m_game.m_particles.p_AddLast2((new c_Particle()).m_Particle_new(t_enemy3.m_position,2.5f,1.0f,t_enemy3.m_heading,20.0f,t_enemy3.m_team,30));
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<435>";
						t_enemy3.p_FireWeapon();
					}
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<440>";
			if(this.m_tutorial_unit.m_armor>0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<441>";
				this.m_tutorial_unit.p_Update();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<442>";
				if(this.m_tutorial_unit.m_currentEnergy==100.0f){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<443>";
					this.m_game.m_particles.p_AddLast2((new c_Particle()).m_Particle_new(this.m_tutorial_unit.m_position,2.5f,1.0f,this.m_tutorial_unit.m_heading,20.0f,this.m_tutorial_unit.m_team,30));
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<444>";
					this.m_tutorial_unit.p_FireWeapon();
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<448>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<448>";
			c_Enumerator3 t_4=this.m_game.m_particles.p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<448>";
			while(t_4.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<448>";
				c_Particle t_particle=t_4.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<449>";
				t_particle.p_Update();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<450>";
				if((bb_dronetournament.g_Collided(t_particle,this.m_tutorial_unit))!=0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<451>";
					this.m_tutorial_unit.p_TakeDamage(1);
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<452>";
					this.m_game.m_particles.p_Remove(t_particle);
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<454>";
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<454>";
					c_Enumerator2 t_5=this.m_game.m_opponents.p_ObjectEnumerator();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<454>";
					while(t_5.p_HasNext()){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<454>";
						c_Unit t_opponent=t_5.p_NextObject();
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<455>";
						if((bb_dronetournament.g_Collided(t_particle,t_opponent))!=0){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<456>";
							t_opponent.p_TakeDamage(1);
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<457>";
							this.m_game.m_particles.p_Remove(t_particle);
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<458>";
							break;
						}
					}
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<463>";
				if(t_particle.m_lifetime<=0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<464>";
					this.m_game.m_particles.p_Remove(t_particle);
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<467>";
			m_moves-=1;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<468>";
			if(m_moves<1){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<469>";
				this.m_tutorial_unit.p_SetControl(this.m_tutorial_unit.m_position.m_x+this.m_tutorial_unit.m_velocity.m_x,this.m_tutorial_unit.m_position.m_y+this.m_tutorial_unit.m_velocity.m_y,640.0f,480.0f);
			}
		}
		bb_std_lang.popErr();
	}
	public final void p_GetServerMoves(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<553>";
		String t_move_json=this.m_game.p_BuildMoveJson(this.m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<554>";
		this.m_multiplayer_service.p_PostJsonRequest("/move_points/"+this.m_game.m_id+"/"+this.m_user.m_player_id+"/30",t_move_json);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<555>";
		this.m_game_state="multiplayer_server";
		bb_std_lang.popErr();
	}
	public final void p_UserPlanMoves(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<526>";
		if((bb_input.g_TouchDown(0))!=0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<527>";
			boolean t_making_move=false;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<528>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<528>";
			c_KeyEnumerator t_=this.m_game.m_units.p_Keys().p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<528>";
			while(t_.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<528>";
				int t_unit_id=t_.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<529>";
				c_Unit t_unit=this.m_game.m_units.p_Get3(t_unit_id);
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<530>";
				if((t_unit.m_player_id.compareTo(this.m_user.m_player_id)==0) && ((this.m_game.m_units.p_Get3(t_unit_id).p_ControlSelected(bb_input.g_TouchX(0),bb_input.g_TouchY(0)))!=0)){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<531>";
					t_making_move=true;
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<532>";
					this.m_game.m_units.p_Get3(t_unit_id).p_SetControl(bb_input.g_TouchX(0),bb_input.g_TouchY(0),640.0f,480.0f);
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<533>";
					break;
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<537>";
			if(this.m_small_end_turn_button.p_Selected() && !t_making_move){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<538>";
				p_GetServerMoves();
			}
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<541>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<541>";
			c_KeyEnumerator t_2=this.m_game.m_units.p_Keys().p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<541>";
			while(t_2.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<541>";
				int t_unit_id2=t_2.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<542>";
				this.m_game.m_units.p_Get3(t_unit_id2).p_ControlReleased();
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<546>";
		if((bb_input.g_KeyHit(13))!=0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<547>";
			p_GetServerMoves();
		}
		bb_std_lang.popErr();
	}
	public final void p_GetNextTurn(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<566>";
		this.m_multiplayer_service.p_GetRequest("/next_turn/"+this.m_game.m_id+"/"+this.m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<567>";
		this.m_game_state="multiplayer_server";
		bb_std_lang.popErr();
	}
	public final void p_RunMultiplayer(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<475>";
		if(this.m_moves>0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<477>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<477>";
			c_KeyEnumerator t_=this.m_game.m_units.p_Keys().p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<477>";
			while(t_.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<477>";
				int t_unit_id=t_.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<478>";
				c_Unit t_current_unit=this.m_game.m_units.p_Get3(t_unit_id);
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<479>";
				if(t_current_unit.m_armor>0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<480>";
					this.m_game.m_units.p_Get3(t_unit_id).p_Update();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<481>";
					t_current_unit=this.m_game.m_units.p_Get3(t_unit_id);
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<482>";
					if(t_current_unit.m_currentEnergy>=t_current_unit.m_unit_type.m_maxEnergy){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<483>";
						m_game.m_particles.p_AddLast2((new c_Particle()).m_Particle_new(t_current_unit.m_position,2.5f,1.0f,t_current_unit.m_heading,20.0f,t_current_unit.m_team,30));
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<484>";
						this.m_game.m_units.p_Get3(t_unit_id).p_FireWeapon();
					}
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<486>";
					if(m_moves==1){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<487>";
						this.m_game.m_units.p_Get3(t_unit_id).p_SetControl(t_current_unit.m_position.m_x+t_current_unit.m_velocity.m_x,t_current_unit.m_position.m_y+t_current_unit.m_velocity.m_y,640.0f,480.0f);
					}
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<492>";
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<492>";
			c_Enumerator3 t_2=this.m_game.m_particles.p_ObjectEnumerator();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<492>";
			while(t_2.p_HasNext()){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<492>";
				c_Particle t_particle=t_2.p_NextObject();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<493>";
				t_particle.p_Update();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<494>";
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<494>";
				c_KeyEnumerator t_3=this.m_game.m_units.p_Keys().p_ObjectEnumerator();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<494>";
				while(t_3.p_HasNext()){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<494>";
					int t_unit_id2=t_3.p_NextObject();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<495>";
					c_Unit t_current_unit2=this.m_game.m_units.p_Get3(t_unit_id2);
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<496>";
					if((bb_dronetournament.g_Collided(t_particle,t_current_unit2))!=0){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<497>";
						this.m_game.m_units.p_Get3(t_unit_id2).p_TakeDamage(1);
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<498>";
						this.m_game.m_particles.p_Remove(t_particle);
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<499>";
						break;
					}
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<503>";
				if(t_particle.m_lifetime<=0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<504>";
					this.m_game.m_particles.p_Remove(t_particle);
				}
			}
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<507>";
			this.m_moves-=1;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<509>";
			this.m_game_state="updated";
		}
		bb_std_lang.popErr();
	}
	public final void p_CheckIfAllPlayersUpdated(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<571>";
		this.m_multiplayer_service.p_GetRequest("/update_state/"+this.m_game.m_id+"/"+this.m_user.m_player_id);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<572>";
		this.m_game_state="multiplayer_server";
		bb_std_lang.popErr();
	}
	public final int p_OnUpdate(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<96>";
		if(m_game_state.compareTo("setup")==0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<97>";
			p_GetUsername();
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<98>";
			if(m_game_state.compareTo("get_password")==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<99>";
				p_GetPassword();
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<100>";
				if(m_game_state.compareTo("menu")==0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<101>";
					if(((bb_input.g_TouchDown(0))!=0) && (float)(bb_app.g_Millisecs())-this.m_timer_begin>1000.0f){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<102>";
						if(this.m_play_tutorial_button.p_Selected()){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<103>";
							p_SetupTutorial();
						}else{
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<104>";
							if(this.m_play_multiplayer_button.p_Selected()){
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<105>";
								if(this.m_user.m_player_id.compareTo("0")==0){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<106>";
									this.m_game_state="setup";
								}else{
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<108>";
									p_SignIn();
								}
							}
						}
					}
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<112>";
					if((this.m_game_state.compareTo("server")==0) || (this.m_game_state.compareTo("multiplayer_server")==0)){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<113>";
						p_DetermineGameState();
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<114>";
						if(m_game_state.compareTo("get_games")==0){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<115>";
							p_GetListOfActiveGames();
						}else{
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<116>";
							if(m_game_state.compareTo("list_games")==0){
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<117>";
								if((bb_input.g_TouchDown(0))!=0){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<118>";
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<118>";
									c_Enumerator t_=this.m_game_select.p_ObjectEnumerator();
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<118>";
									while(t_.p_HasNext()){
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<118>";
										c_GameSelect t_game_card=t_.p_NextObject();
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<119>";
										if(t_game_card.p_Selected()){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<120>";
											p_GetGameInfoFromServer(t_game_card.m_game_id);
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<121>";
											break;
										}
									}
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<125>";
									if(m_join_button.p_Selected()){
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<126>";
										p_JoinGame();
									}
								}
							}else{
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<129>";
								if(m_game_state.compareTo("tutorial")==0){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<130>";
									p_RunTutorial();
								}else{
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<131>";
									if(this.m_game_state.compareTo("multiplayer")==0){
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<132>";
										p_UserPlanMoves();
									}else{
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<133>";
										if(this.m_game_state.compareTo("end_turn")==0){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<134>";
											if((float)(bb_app.g_Millisecs())-this.m_timer_begin>3000.0f){
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<135>";
												this.m_timer_begin=(float)(bb_app.g_Millisecs());
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<136>";
												p_GetNextTurn();
											}
										}else{
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<138>";
											if(this.m_game_state.compareTo("multiplayer_ready")==0){
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<139>";
												p_RunMultiplayer();
											}else{
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<140>";
												if(this.m_game_state.compareTo("updated")==0){
													bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<141>";
													if((float)(bb_app.g_Millisecs())-this.m_timer_begin>3000.0f){
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<142>";
														this.m_timer_begin=(float)(bb_app.g_Millisecs());
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<143>";
														p_CheckIfAllPlayersUpdated();
													}
												}else{
													bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<145>";
													if(this.m_game_state.compareTo("loser")==0){
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<146>";
														if((bb_input.g_TouchDown(0))!=0){
															bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<147>";
															this.m_timer_begin=(float)(bb_app.g_Millisecs());
															bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<148>";
															this.m_game_state="menu";
														}
													}else{
														bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<150>";
														if(this.m_game_state.compareTo("winner")==0){
															bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<151>";
															if((bb_input.g_TouchDown(0))!=0){
																bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<152>";
																this.m_timer_begin=(float)(bb_app.g_Millisecs());
																bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<153>";
																this.m_game_state="menu";
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<156>";
		bb_asyncevent.g_UpdateAsyncEvents();
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnRender(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<308>";
		bb_graphics.g_Cls(100.0f,100.0f,100.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<310>";
		if(this.m_game_state.compareTo("setup")==0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<311>";
			bb_graphics.g_DrawText("Enter a username: "+m_user.m_username,50.0f,200.0f,0.0f,0.0f);
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<312>";
			if(this.m_game_state.compareTo("get_password")==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<313>";
				bb_graphics.g_DrawText("Enter a password: "+m_user.m_password,50.0f,200.0f,0.0f,0.0f);
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<314>";
				if(m_game_state.compareTo("menu")==0){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<315>";
					this.m_play_tutorial_button.p_Draw2();
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<316>";
					this.m_play_multiplayer_button.p_Draw2();
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<317>";
					if(m_game_state.compareTo("list_games")==0){
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<318>";
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<318>";
						c_Enumerator t_=this.m_game_select.p_ObjectEnumerator();
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<318>";
						while(t_.p_HasNext()){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<318>";
							c_GameSelect t_game_ui=t_.p_NextObject();
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<319>";
							t_game_ui.p_Draw2();
						}
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<321>";
						this.m_join_button.p_Draw2();
					}else{
						bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<326>";
						if((this.m_game_state.compareTo("multiplayer")==0) || (this.m_game_state.compareTo("multiplayer_ready")==0) || (this.m_game_state.compareTo("end_turn")==0) || (this.m_game_state.compareTo("updated")==0) || (this.m_game_state.compareTo("multiplayer_server")==0)){
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<329>";
							this.m_small_end_turn_button.p_Draw2();
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<330>";
							this.m_game.p_Draw(this.m_user.m_player_id,this.m_game_state);
						}else{
							bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<331>";
							if(this.m_game_state.compareTo("loser")==0){
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<332>";
								bb_graphics.g_DrawImage(m_lose_button,10.0f,100.0f,0);
							}else{
								bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<333>";
								if(this.m_game_state.compareTo("winner")==0){
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<334>";
									bb_graphics.g_DrawImage(m_win_button,10.0f,100.0f,0);
								}else{
									bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<335>";
									if(this.m_game_state.compareTo("tutorial")==0){
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<337>";
										this.m_small_end_turn_button.p_Draw2();
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<338>";
										bb_graphics.g_PushMatrix();
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<339>";
										bb_graphics.g_Translate(this.m_game_cam.m_position.m_x,this.m_game_cam.m_position.m_y);
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<340>";
										if(this.m_tutorial_unit.m_armor>0){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<341>";
											this.m_tutorial_unit.p_DrawStatic("1",this.m_game_state);
										}
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<344>";
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<344>";
										c_Enumerator2 t_2=this.m_game.m_opponents.p_ObjectEnumerator();
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<344>";
										while(t_2.p_HasNext()){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<344>";
											c_Unit t_enemy=t_2.p_NextObject();
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<345>";
											if(t_enemy.m_armor>0){
												bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<346>";
												t_enemy.p_DrawStatic("2",this.m_game_state);
											}
										}
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<350>";
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<350>";
										c_Enumerator3 t_3=this.m_game.m_particles.p_ObjectEnumerator();
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<350>";
										while(t_3.p_HasNext()){
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<350>";
											c_Particle t_part=t_3.p_NextObject();
											bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<351>";
											t_part.p_Draw2();
										}
										bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<353>";
										bb_graphics.g_PopMatrix();
									}
								}
							}
						}
					}
				}
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_OnResume(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return 0;
	}
}
class c_GameDelegate extends BBGameDelegate{
	public final c_GameDelegate m_GameDelegate_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<65>";
		bb_std_lang.popErr();
		return this;
	}
	gxtkGraphics m__graphics=null;
	gxtkAudio m__audio=null;
	c_InputDevice m__input=null;
	public final void StartGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<75>";
		m__graphics=(new gxtkGraphics());
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<76>";
		bb_graphics.g_SetGraphicsDevice(m__graphics);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<77>";
		bb_graphics.g_SetFont(null,32);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<79>";
		m__audio=(new gxtkAudio());
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<80>";
		bb_audio.g_SetAudioDevice(m__audio);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<82>";
		m__input=(new c_InputDevice()).m_InputDevice_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<83>";
		bb_input.g_SetInputDevice(m__input);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<85>";
		bb_app.g_ValidateDeviceWindow(false);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<87>";
		bb_app.g_EnumDisplayModes();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<89>";
		bb_app.g__app.p_OnCreate();
		bb_std_lang.popErr();
	}
	public final void SuspendGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<93>";
		bb_app.g__app.p_OnSuspend();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<94>";
		m__audio.Suspend();
		bb_std_lang.popErr();
	}
	public final void ResumeGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<98>";
		m__audio.Resume();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<99>";
		bb_app.g__app.p_OnResume();
		bb_std_lang.popErr();
	}
	public final void UpdateGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<103>";
		bb_app.g_ValidateDeviceWindow(true);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<104>";
		m__input.p_BeginUpdate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<105>";
		bb_app.g__app.p_OnUpdate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<106>";
		m__input.p_EndUpdate();
		bb_std_lang.popErr();
	}
	public final void RenderGame(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<110>";
		bb_app.g_ValidateDeviceWindow(true);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<111>";
		int t_mode=m__graphics.BeginRender();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<112>";
		if((t_mode)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<112>";
			bb_graphics.g_BeginRender();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<113>";
		if(t_mode==2){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<113>";
			bb_app.g__app.p_OnLoading();
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<113>";
			bb_app.g__app.p_OnRender();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<114>";
		if((t_mode)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<114>";
			bb_graphics.g_EndRender();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<115>";
		m__graphics.EndRender();
		bb_std_lang.popErr();
	}
	public final void KeyEvent(int t_event,int t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<119>";
		m__input.p_KeyEvent(t_event,t_data);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<120>";
		if(t_event!=1){
			bb_std_lang.popErr();
			return;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<121>";
		int t_1=t_data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<122>";
		if(t_1==432){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<123>";
			bb_app.g__app.p_OnClose();
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<124>";
			if(t_1==416){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<125>";
				bb_app.g__app.p_OnBack();
			}
		}
		bb_std_lang.popErr();
	}
	public final void MouseEvent(int t_event,int t_data,float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<130>";
		m__input.p_MouseEvent(t_event,t_data,t_x,t_y);
		bb_std_lang.popErr();
	}
	public final void TouchEvent(int t_event,int t_data,float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<134>";
		m__input.p_TouchEvent(t_event,t_data,t_x,t_y);
		bb_std_lang.popErr();
	}
	public final void MotionEvent(int t_event,int t_data,float t_x,float t_y,float t_z){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<138>";
		m__input.p_MotionEvent(t_event,t_data,t_x,t_y,t_z);
		bb_std_lang.popErr();
	}
	public final void DiscardGraphics(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<142>";
		m__graphics.DiscardGraphics();
		bb_std_lang.popErr();
	}
}
class c_Image extends Object{
	static int m_DefaultFlags;
	public final c_Image m_Image_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<70>";
		bb_std_lang.popErr();
		return this;
	}
	gxtkSurface m_surface=null;
	int m_width=0;
	int m_height=0;
	c_Frame[] m_frames=new c_Frame[0];
	int m_flags=0;
	float m_tx=.0f;
	float m_ty=.0f;
	public final int p_SetHandle(float t_tx,float t_ty){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<114>";
		this.m_tx=t_tx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<115>";
		this.m_ty=t_ty;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<116>";
		this.m_flags=this.m_flags&-2;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_ApplyFlags(int t_iflags){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<197>";
		m_flags=t_iflags;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<199>";
		if((m_flags&2)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
			c_Frame[] t_=m_frames;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
			int t_2=0;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
			while(t_2<bb_std_lang.length(t_)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
				c_Frame t_f=t_[t_2];
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<200>";
				t_2=t_2+1;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<201>";
				t_f.m_x+=1;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<203>";
			m_width-=2;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<206>";
		if((m_flags&4)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
			c_Frame[] t_3=m_frames;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
			int t_4=0;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
			while(t_4<bb_std_lang.length(t_3)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
				c_Frame t_f2=t_3[t_4];
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<207>";
				t_4=t_4+1;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<208>";
				t_f2.m_y+=1;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<210>";
			m_height-=2;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<213>";
		if((m_flags&1)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<214>";
			p_SetHandle((float)(m_width)/2.0f,(float)(m_height)/2.0f);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<217>";
		if(bb_std_lang.length(m_frames)==1 && m_frames[0].m_x==0 && m_frames[0].m_y==0 && m_width==m_surface.Width() && m_height==m_surface.Height()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<218>";
			m_flags|=65536;
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final c_Image p_Init(gxtkSurface t_surf,int t_nframes,int t_iflags){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<143>";
		if((m_surface)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<143>";
			bb_std_lang.error("Image already initialized");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<144>";
		m_surface=t_surf;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<146>";
		m_width=m_surface.Width()/t_nframes;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<147>";
		m_height=m_surface.Height();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<149>";
		m_frames=new c_Frame[t_nframes];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<150>";
		for(int t_i=0;t_i<t_nframes;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<151>";
			m_frames[t_i]=(new c_Frame()).m_Frame_new(t_i*m_width,0);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<154>";
		p_ApplyFlags(t_iflags);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<155>";
		bb_std_lang.popErr();
		return this;
	}
	c_Image m_source=null;
	public final c_Image p_Init2(gxtkSurface t_surf,int t_x,int t_y,int t_iwidth,int t_iheight,int t_nframes,int t_iflags,c_Image t_src,int t_srcx,int t_srcy,int t_srcw,int t_srch){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<159>";
		if((m_surface)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<159>";
			bb_std_lang.error("Image already initialized");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<160>";
		m_surface=t_surf;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<161>";
		m_source=t_src;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<163>";
		m_width=t_iwidth;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<164>";
		m_height=t_iheight;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<166>";
		m_frames=new c_Frame[t_nframes];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<168>";
		int t_ix=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<168>";
		int t_iy=t_y;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<170>";
		for(int t_i=0;t_i<t_nframes;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<171>";
			if(t_ix+m_width>t_srcw){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<172>";
				t_ix=0;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<173>";
				t_iy+=m_height;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<175>";
			if(t_ix+m_width>t_srcw || t_iy+m_height>t_srch){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<176>";
				bb_std_lang.error("Image frame outside surface");
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<178>";
			m_frames[t_i]=(new c_Frame()).m_Frame_new(t_ix+t_srcx,t_iy+t_srcy);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<179>";
			t_ix+=m_width;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<182>";
		p_ApplyFlags(t_iflags);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<183>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Width(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<81>";
		bb_std_lang.popErr();
		return m_width;
	}
	public final int p_Height(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<85>";
		bb_std_lang.popErr();
		return m_height;
	}
	public final int p_Frames(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<93>";
		int t_=bb_std_lang.length(m_frames);
		bb_std_lang.popErr();
		return t_;
	}
}
class c_GraphicsContext extends Object{
	public final c_GraphicsContext m_GraphicsContext_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<29>";
		bb_std_lang.popErr();
		return this;
	}
	c_Image m_defaultFont=null;
	c_Image m_font=null;
	int m_firstChar=0;
	int m_matrixSp=0;
	float m_ix=1.0f;
	float m_iy=.0f;
	float m_jx=.0f;
	float m_jy=1.0f;
	float m_tx=.0f;
	float m_ty=.0f;
	int m_tformed=0;
	int m_matDirty=0;
	float m_color_r=.0f;
	float m_color_g=.0f;
	float m_color_b=.0f;
	float m_alpha=.0f;
	int m_blend=0;
	float m_scissor_x=.0f;
	float m_scissor_y=.0f;
	float m_scissor_width=.0f;
	float m_scissor_height=.0f;
	public final int p_Validate(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<40>";
		if((m_matDirty)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<41>";
			bb_graphics.g_renderDevice.SetMatrix(bb_graphics.g_context.m_ix,bb_graphics.g_context.m_iy,bb_graphics.g_context.m_jx,bb_graphics.g_context.m_jy,bb_graphics.g_context.m_tx,bb_graphics.g_context.m_ty);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<42>";
			m_matDirty=0;
		}
		bb_std_lang.popErr();
		return 0;
	}
	float[] m_matrixStack=new float[192];
}
class c_Frame extends Object{
	int m_x=0;
	int m_y=0;
	public final c_Frame m_Frame_new(int t_x,int t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<23>";
		this.m_x=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<24>";
		this.m_y=t_y;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Frame m_Frame_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<18>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_InputDevice extends Object{
	c_JoyState[] m__joyStates=new c_JoyState[4];
	public final c_InputDevice m_InputDevice_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<26>";
		for(int t_i=0;t_i<4;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<27>";
			m__joyStates[t_i]=(new c_JoyState()).m_JoyState_new();
		}
		bb_std_lang.popErr();
		return this;
	}
	boolean[] m__keyDown=new boolean[512];
	int m__keyHitPut=0;
	int[] m__keyHitQueue=new int[33];
	int[] m__keyHit=new int[512];
	public final void p_PutKeyHit(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<241>";
		if(m__keyHitPut==bb_std_lang.length(m__keyHitQueue)){
			bb_std_lang.popErr();
			return;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<242>";
		m__keyHit[t_key]+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<243>";
		m__keyHitQueue[m__keyHitPut]=t_key;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<244>";
		m__keyHitPut+=1;
		bb_std_lang.popErr();
	}
	public final void p_BeginUpdate(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<193>";
		for(int t_i=0;t_i<4;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<194>";
			c_JoyState t_state=m__joyStates[t_i];
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<195>";
			if(!BBGame.Game().PollJoystick(t_i,t_state.m_joyx,t_state.m_joyy,t_state.m_joyz,t_state.m_buttons)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<195>";
				break;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<196>";
			for(int t_j=0;t_j<32;t_j=t_j+1){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<197>";
				int t_key=256+t_i*32+t_j;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<198>";
				if(t_state.m_buttons[t_j]){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<199>";
					if(!m__keyDown[t_key]){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<200>";
						m__keyDown[t_key]=true;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<201>";
						p_PutKeyHit(t_key);
					}
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<204>";
					m__keyDown[t_key]=false;
				}
			}
		}
		bb_std_lang.popErr();
	}
	int m__charGet=0;
	int m__charPut=0;
	public final void p_EndUpdate(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<211>";
		for(int t_i=0;t_i<m__keyHitPut;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<212>";
			m__keyHit[m__keyHitQueue[t_i]]=0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<214>";
		m__keyHitPut=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<215>";
		m__charGet=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<216>";
		m__charPut=0;
		bb_std_lang.popErr();
	}
	int[] m__charQueue=new int[32];
	public final void p_KeyEvent(int t_event,int t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<115>";
		int t_1=t_event;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<116>";
		if(t_1==1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<117>";
			if(!m__keyDown[t_data]){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<118>";
				m__keyDown[t_data]=true;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<119>";
				p_PutKeyHit(t_data);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<120>";
				if(t_data==1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<121>";
					m__keyDown[384]=true;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<122>";
					p_PutKeyHit(384);
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<123>";
					if(t_data==384){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<124>";
						m__keyDown[1]=true;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<125>";
						p_PutKeyHit(1);
					}
				}
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<128>";
			if(t_1==2){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<129>";
				if(m__keyDown[t_data]){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<130>";
					m__keyDown[t_data]=false;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<131>";
					if(t_data==1){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<132>";
						m__keyDown[384]=false;
					}else{
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<133>";
						if(t_data==384){
							bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<134>";
							m__keyDown[1]=false;
						}
					}
				}
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<137>";
				if(t_1==3){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<138>";
					if(m__charPut<bb_std_lang.length(m__charQueue)){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<139>";
						m__charQueue[m__charPut]=t_data;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<140>";
						m__charPut+=1;
					}
				}
			}
		}
		bb_std_lang.popErr();
	}
	float m__mouseX=.0f;
	float m__mouseY=.0f;
	float[] m__touchX=new float[32];
	float[] m__touchY=new float[32];
	public final void p_MouseEvent(int t_event,int t_data,float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<146>";
		int t_2=t_event;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<147>";
		if(t_2==4){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<148>";
			p_KeyEvent(1,1+t_data);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<149>";
			if(t_2==5){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<150>";
				p_KeyEvent(2,1+t_data);
				bb_std_lang.popErr();
				return;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<152>";
				if(t_2==6){
				}else{
					bb_std_lang.popErr();
					return;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<156>";
		m__mouseX=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<157>";
		m__mouseY=t_y;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<158>";
		m__touchX[0]=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<159>";
		m__touchY[0]=t_y;
		bb_std_lang.popErr();
	}
	public final void p_TouchEvent(int t_event,int t_data,float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<163>";
		int t_3=t_event;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<164>";
		if(t_3==7){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<165>";
			p_KeyEvent(1,384+t_data);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<166>";
			if(t_3==8){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<167>";
				p_KeyEvent(2,384+t_data);
				bb_std_lang.popErr();
				return;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<169>";
				if(t_3==9){
				}else{
					bb_std_lang.popErr();
					return;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<173>";
		m__touchX[t_data]=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<174>";
		m__touchY[t_data]=t_y;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<175>";
		if(t_data==0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<176>";
			m__mouseX=t_x;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<177>";
			m__mouseY=t_y;
		}
		bb_std_lang.popErr();
	}
	float m__accelX=.0f;
	float m__accelY=.0f;
	float m__accelZ=.0f;
	public final void p_MotionEvent(int t_event,int t_data,float t_x,float t_y,float t_z){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<182>";
		int t_4=t_event;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<183>";
		if(t_4==10){
		}else{
			bb_std_lang.popErr();
			return;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<187>";
		m__accelX=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<188>";
		m__accelY=t_y;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<189>";
		m__accelZ=t_z;
		bb_std_lang.popErr();
	}
	public final int p_GetChar(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<57>";
		if(m__charGet==m__charPut){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<57>";
			bb_std_lang.popErr();
			return 0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<58>";
		int t_chr=m__charQueue[m__charGet];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<59>";
		m__charGet+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<60>";
		bb_std_lang.popErr();
		return t_chr;
	}
	public final int p_SetKeyboardEnabled(boolean t_enabled){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<42>";
		BBGame.Game().SetKeyboardEnabled(t_enabled);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<43>";
		bb_std_lang.popErr();
		return 1;
	}
	public final boolean p_KeyDown(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<47>";
		if(t_key>0 && t_key<512){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<47>";
			bb_std_lang.popErr();
			return m__keyDown[t_key];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<48>";
		bb_std_lang.popErr();
		return false;
	}
	public final float p_TouchX(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<77>";
		if(t_index>=0 && t_index<32){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<77>";
			bb_std_lang.popErr();
			return m__touchX[t_index];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<78>";
		bb_std_lang.popErr();
		return 0.0f;
	}
	public final float p_TouchY(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<82>";
		if(t_index>=0 && t_index<32){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<82>";
			bb_std_lang.popErr();
			return m__touchY[t_index];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<83>";
		bb_std_lang.popErr();
		return 0.0f;
	}
	public final int p_KeyHit(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<52>";
		if(t_key>0 && t_key<512){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<52>";
			bb_std_lang.popErr();
			return m__keyHit[t_key];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<53>";
		bb_std_lang.popErr();
		return 0;
	}
}
class c_JoyState extends Object{
	public final c_JoyState m_JoyState_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/inputdevice.monkey<14>";
		bb_std_lang.popErr();
		return this;
	}
	float[] m_joyx=new float[2];
	float[] m_joyy=new float[2];
	float[] m_joyz=new float[2];
	boolean[] m_buttons=new boolean[32];
}
class c_DisplayMode extends Object{
	int m__width=0;
	int m__height=0;
	public final c_DisplayMode m_DisplayMode_new(int t_width,int t_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<192>";
		m__width=t_width;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<193>";
		m__height=t_height;
		bb_std_lang.popErr();
		return this;
	}
	public final c_DisplayMode m_DisplayMode_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<189>";
		bb_std_lang.popErr();
		return this;
	}
}
abstract class c_Map extends Object{
	public final c_Map m_Map_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<7>";
		bb_std_lang.popErr();
		return this;
	}
	c_Node m_root=null;
	public abstract int p_Compare(int t_lhs,int t_rhs);
	public final c_Node p_FindNode(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<157>";
		c_Node t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<159>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<160>";
			int t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<161>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<162>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<163>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<164>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<166>";
					bb_std_lang.popErr();
					return t_node;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<169>";
		bb_std_lang.popErr();
		return t_node;
	}
	public final boolean p_Contains(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<25>";
		boolean t_=p_FindNode(t_key)!=null;
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_RotateLeft(c_Node t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<251>";
		c_Node t_child=t_node.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<252>";
		t_node.m_right=t_child.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<253>";
		if((t_child.m_left)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<254>";
			t_child.m_left.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<256>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<257>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<258>";
			if(t_node==t_node.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<259>";
				t_node.m_parent.m_left=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<261>";
				t_node.m_parent.m_right=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<264>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<266>";
		t_child.m_left=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<267>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_RotateRight(c_Node t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<271>";
		c_Node t_child=t_node.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<272>";
		t_node.m_left=t_child.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<273>";
		if((t_child.m_right)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<274>";
			t_child.m_right.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<276>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<277>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<278>";
			if(t_node==t_node.m_parent.m_right){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<279>";
				t_node.m_parent.m_right=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<281>";
				t_node.m_parent.m_left=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<284>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<286>";
		t_child.m_right=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<287>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_InsertFixup(c_Node t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<212>";
		while(((t_node.m_parent)!=null) && t_node.m_parent.m_color==-1 && ((t_node.m_parent.m_parent)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<213>";
			if(t_node.m_parent==t_node.m_parent.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<214>";
				c_Node t_uncle=t_node.m_parent.m_parent.m_right;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<215>";
				if(((t_uncle)!=null) && t_uncle.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<216>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<217>";
					t_uncle.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<218>";
					t_uncle.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<219>";
					t_node=t_uncle.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<221>";
					if(t_node==t_node.m_parent.m_right){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<222>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<223>";
						p_RotateLeft(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<225>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<226>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<227>";
					p_RotateRight(t_node.m_parent.m_parent);
				}
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<230>";
				c_Node t_uncle2=t_node.m_parent.m_parent.m_left;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<231>";
				if(((t_uncle2)!=null) && t_uncle2.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<232>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<233>";
					t_uncle2.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<234>";
					t_uncle2.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<235>";
					t_node=t_uncle2.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<237>";
					if(t_node==t_node.m_parent.m_left){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<238>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<239>";
						p_RotateRight(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<241>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<242>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<243>";
					p_RotateLeft(t_node.m_parent.m_parent);
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<247>";
		m_root.m_color=1;
		bb_std_lang.popErr();
		return 0;
	}
	public final boolean p_Set(int t_key,c_DisplayMode t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<29>";
		c_Node t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		c_Node t_parent=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		int t_cmp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<32>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<33>";
			t_parent=t_node;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<34>";
			t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<35>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<36>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<37>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<38>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<40>";
					t_node.m_value=t_value;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<41>";
					bb_std_lang.popErr();
					return false;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<45>";
		t_node=(new c_Node()).m_Node_new(t_key,t_value,-1,t_parent);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<47>";
		if((t_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<48>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<49>";
				t_parent.m_right=t_node;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<51>";
				t_parent.m_left=t_node;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<53>";
			p_InsertFixup(t_node);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<55>";
			m_root=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<57>";
		bb_std_lang.popErr();
		return true;
	}
	public final boolean p_Insert(int t_key,c_DisplayMode t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<146>";
		boolean t_=p_Set(t_key,t_value);
		bb_std_lang.popErr();
		return t_;
	}
}
class c_IntMap extends c_Map{
	public final c_IntMap m_IntMap_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		super.m_Map_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Compare(int t_lhs,int t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<537>";
		int t_=t_lhs-t_rhs;
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Stack extends Object{
	public final c_Stack m_Stack_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_DisplayMode[] m_data=new c_DisplayMode[0];
	int m_length=0;
	public final c_Stack m_Stack_new2(c_DisplayMode[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<13>";
		this.m_data=((c_DisplayMode[])bb_std_lang.sliceArray(t_data,0));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<14>";
		this.m_length=bb_std_lang.length(t_data);
		bb_std_lang.popErr();
		return this;
	}
	public final void p_Push(c_DisplayMode t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<71>";
		if(m_length==bb_std_lang.length(m_data)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<72>";
			m_data=(c_DisplayMode[])bb_std_lang.resize(m_data,m_length*2+10,c_DisplayMode.class);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<74>";
		m_data[m_length]=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<75>";
		m_length+=1;
		bb_std_lang.popErr();
	}
	public final void p_Push2(c_DisplayMode[] t_values,int t_offset,int t_count){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<83>";
		for(int t_i=0;t_i<t_count;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<84>";
			p_Push(t_values[t_offset+t_i]);
		}
		bb_std_lang.popErr();
	}
	public final void p_Push3(c_DisplayMode[] t_values,int t_offset){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<79>";
		p_Push2(t_values,t_offset,bb_std_lang.length(t_values)-t_offset);
		bb_std_lang.popErr();
	}
	public final c_DisplayMode[] p_ToArray(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<18>";
		c_DisplayMode[] t_t=new c_DisplayMode[m_length];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<19>";
		for(int t_i=0;t_i<m_length;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<20>";
			t_t[t_i]=m_data[t_i];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<22>";
		bb_std_lang.popErr();
		return t_t;
	}
}
class c_Node extends Object{
	int m_key=0;
	c_Node m_right=null;
	c_Node m_left=null;
	c_DisplayMode m_value=null;
	int m_color=0;
	c_Node m_parent=null;
	public final c_Node m_Node_new(int t_key,c_DisplayMode t_value,int t_color,c_Node t_parent){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<364>";
		this.m_key=t_key;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<365>";
		this.m_value=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<366>";
		this.m_color=t_color;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<367>";
		this.m_parent=t_parent;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<361>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_BBGameEvent extends Object{
}
class c_User extends Object{
	String m_username="";
	String m_player_id="";
	String m_password="";
	public final c_User m_User_new(String t_username,String t_player_id,String t_password){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user.monkey<9>";
		this.m_username=t_username;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user.monkey<10>";
		this.m_player_id=t_player_id;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user.monkey<11>";
		this.m_password=t_password;
		bb_std_lang.popErr();
		return this;
	}
	public final c_User m_User_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user.monkey<2>";
		bb_std_lang.popErr();
		return this;
	}
}
interface c_IOnHttpRequestComplete{
	public void p_OnHttpRequestComplete(c_HttpRequest t_req);
}
class c_MultiplayerService extends Object implements c_IOnHttpRequestComplete{
	String m_server_url="";
	String m_http_status="";
	String m_raw_response="";
	c_JsonObject m_response=null;
	public final void p_ClearResponse(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<56>";
		this.m_http_status="";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<57>";
		this.m_raw_response="";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<58>";
		this.m_response=(new c_JsonObject()).m_JsonObject_new();
		bb_std_lang.popErr();
	}
	public final c_MultiplayerService m_MultiplayerService_new(String t_url){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<13>";
		this.m_server_url=t_url;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<14>";
		p_ClearResponse();
		bb_std_lang.popErr();
		return this;
	}
	public final c_MultiplayerService m_MultiplayerService_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<4>";
		bb_std_lang.popErr();
		return this;
	}
	c_HttpRequest m_server_request=null;
	public final void p_PostJsonRequest(String t_path,String t_json){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<30>";
		p_ClearResponse();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<31>";
		m_server_request=(new c_HttpRequest()).m_HttpRequest_new2("POST",m_server_url+t_path,(this));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<32>";
		m_server_request.p_Send2(t_json,"application/json","utf8");
		bb_std_lang.popErr();
	}
	public final boolean p_HasRequestFinished(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<48>";
		if(this.m_http_status.compareTo("")!=0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<49>";
			bb_std_lang.popErr();
			return true;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<51>";
			bb_std_lang.popErr();
			return false;
		}
	}
	public final void p_GetRequest(String t_path){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<18>";
		p_ClearResponse();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<19>";
		m_server_request=(new c_HttpRequest()).m_HttpRequest_new2("GET",m_server_url+t_path,(this));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<20>";
		m_server_request.p_Send();
		bb_std_lang.popErr();
	}
	public final void p_PostRequest(String t_path){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<24>";
		p_ClearResponse();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<25>";
		m_server_request=(new c_HttpRequest()).m_HttpRequest_new2("POST",m_server_url+t_path,(this));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<26>";
		m_server_request.p_Send();
		bb_std_lang.popErr();
	}
	public final void p_OnHttpRequestComplete(c_HttpRequest t_request){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<37>";
		this.m_http_status=String.valueOf(t_request.p_Status());
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<38>";
		this.m_raw_response=t_request.p_ResponseText();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<39>";
		try{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<40>";
			this.m_response=(new c_JsonObject()).m_JsonObject_new3(t_request.p_ResponseText());
		}catch(ThrowableObject t_json_error){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<42>";
			bb_std_lang.print("Server unreachable or gave a non JSON response");
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/server.monkey<43>";
			this.m_response=(new c_JsonObject()).m_JsonObject_new3("{ \"action\": \"Bad Server Response\" }");
		}
		bb_std_lang.popErr();
	}
}
abstract class c_JsonValue extends Object{
	public final c_JsonValue m_JsonValue_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<14>";
		bb_std_lang.popErr();
		return this;
	}
	public int p_IntValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<21>";
		bb_json.g_ThrowError();
		bb_std_lang.popErr();
		return 0;
	}
	public String p_StringValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<29>";
		bb_json.g_ThrowError();
		bb_std_lang.popErr();
		return "";
	}
	public float p_FloatValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<25>";
		bb_json.g_ThrowError();
		bb_std_lang.popErr();
		return 0;
	}
}
class c_JsonObject extends c_JsonValue{
	c_StringMap m__data=null;
	public final c_JsonObject m_JsonObject_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<46>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<47>";
		m__data=(new c_StringMap()).m_StringMap_new();
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonObject m_JsonObject_new2(c_StringMap t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<54>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<55>";
		m__data=t_data;
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonObject m_JsonObject_new3(String t_json){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<50>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<51>";
		m__data=((new c_JsonParser()).m_JsonParser_new(t_json)).p_ParseObject();
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonValue p_Get(String t_key,c_JsonValue t_defval){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<83>";
		if(!m__data.p_Contains2(t_key)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<83>";
			bb_std_lang.popErr();
			return t_defval;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<84>";
		c_JsonValue t_val=m__data.p_Get2(t_key);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<85>";
		if((t_val)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<85>";
			bb_std_lang.popErr();
			return t_val;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<86>";
		c_JsonValue t_=(c_JsonNull.m_Instance());
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_GetInt(String t_key,int t_defval){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<95>";
		if(!m__data.p_Contains2(t_key)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<95>";
			bb_std_lang.popErr();
			return t_defval;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<96>";
		int t_=p_Get(t_key,null).p_IntValue();
		bb_std_lang.popErr();
		return t_;
	}
	public final String p_GetString(String t_key,String t_defval){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<105>";
		if(!m__data.p_Contains2(t_key)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<105>";
			bb_std_lang.popErr();
			return t_defval;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<106>";
		String t_=p_Get(t_key,null).p_StringValue();
		bb_std_lang.popErr();
		return t_;
	}
	public final float p_GetFloat(String t_key,float t_defval){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<100>";
		if(!m__data.p_Contains2(t_key)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<100>";
			bb_std_lang.popErr();
			return t_defval;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<101>";
		float t_=p_Get(t_key,null).p_FloatValue();
		bb_std_lang.popErr();
		return t_;
	}
}
abstract class c_Map2 extends Object{
	public final c_Map2 m_Map_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<7>";
		bb_std_lang.popErr();
		return this;
	}
	c_Node2 m_root=null;
	public abstract int p_Compare2(String t_lhs,String t_rhs);
	public final int p_RotateLeft2(c_Node2 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<251>";
		c_Node2 t_child=t_node.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<252>";
		t_node.m_right=t_child.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<253>";
		if((t_child.m_left)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<254>";
			t_child.m_left.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<256>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<257>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<258>";
			if(t_node==t_node.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<259>";
				t_node.m_parent.m_left=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<261>";
				t_node.m_parent.m_right=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<264>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<266>";
		t_child.m_left=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<267>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_RotateRight2(c_Node2 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<271>";
		c_Node2 t_child=t_node.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<272>";
		t_node.m_left=t_child.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<273>";
		if((t_child.m_right)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<274>";
			t_child.m_right.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<276>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<277>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<278>";
			if(t_node==t_node.m_parent.m_right){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<279>";
				t_node.m_parent.m_right=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<281>";
				t_node.m_parent.m_left=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<284>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<286>";
		t_child.m_right=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<287>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_InsertFixup2(c_Node2 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<212>";
		while(((t_node.m_parent)!=null) && t_node.m_parent.m_color==-1 && ((t_node.m_parent.m_parent)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<213>";
			if(t_node.m_parent==t_node.m_parent.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<214>";
				c_Node2 t_uncle=t_node.m_parent.m_parent.m_right;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<215>";
				if(((t_uncle)!=null) && t_uncle.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<216>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<217>";
					t_uncle.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<218>";
					t_uncle.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<219>";
					t_node=t_uncle.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<221>";
					if(t_node==t_node.m_parent.m_right){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<222>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<223>";
						p_RotateLeft2(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<225>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<226>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<227>";
					p_RotateRight2(t_node.m_parent.m_parent);
				}
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<230>";
				c_Node2 t_uncle2=t_node.m_parent.m_parent.m_left;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<231>";
				if(((t_uncle2)!=null) && t_uncle2.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<232>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<233>";
					t_uncle2.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<234>";
					t_uncle2.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<235>";
					t_node=t_uncle2.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<237>";
					if(t_node==t_node.m_parent.m_left){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<238>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<239>";
						p_RotateRight2(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<241>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<242>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<243>";
					p_RotateLeft2(t_node.m_parent.m_parent);
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<247>";
		m_root.m_color=1;
		bb_std_lang.popErr();
		return 0;
	}
	public final boolean p_Set2(String t_key,c_JsonValue t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<29>";
		c_Node2 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		c_Node2 t_parent=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		int t_cmp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<32>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<33>";
			t_parent=t_node;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<34>";
			t_cmp=p_Compare2(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<35>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<36>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<37>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<38>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<40>";
					t_node.m_value=t_value;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<41>";
					bb_std_lang.popErr();
					return false;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<45>";
		t_node=(new c_Node2()).m_Node_new(t_key,t_value,-1,t_parent);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<47>";
		if((t_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<48>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<49>";
				t_parent.m_right=t_node;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<51>";
				t_parent.m_left=t_node;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<53>";
			p_InsertFixup2(t_node);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<55>";
			m_root=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<57>";
		bb_std_lang.popErr();
		return true;
	}
	public final c_Node2 p_FindNode2(String t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<157>";
		c_Node2 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<159>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<160>";
			int t_cmp=p_Compare2(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<161>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<162>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<163>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<164>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<166>";
					bb_std_lang.popErr();
					return t_node;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<169>";
		bb_std_lang.popErr();
		return t_node;
	}
	public final boolean p_Contains2(String t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<25>";
		boolean t_=p_FindNode2(t_key)!=null;
		bb_std_lang.popErr();
		return t_;
	}
	public final c_JsonValue p_Get2(String t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<101>";
		c_Node2 t_node=p_FindNode2(t_key);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
		if((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
			bb_std_lang.popErr();
			return t_node.m_value;
		}
		bb_std_lang.popErr();
		return null;
	}
}
class c_StringMap extends c_Map2{
	public final c_StringMap m_StringMap_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<551>";
		super.m_Map_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<551>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Compare2(String t_lhs,String t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<554>";
		int t_=t_lhs.compareTo(t_rhs);
		bb_std_lang.popErr();
		return t_;
	}
}
class c_JsonParser extends Object{
	String m__text="";
	int m__pos=0;
	String m__toke="";
	int m__type=0;
	public final int p_GetChar(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<345>";
		if(m__pos==m__text.length()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<345>";
			bb_json.g_ThrowError();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<346>";
		m__pos+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<347>";
		int t_=(int)m__text.charAt(m__pos-1);
		bb_std_lang.popErr();
		return t_;
	}
	public final boolean p_CParseDigits(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<367>";
		int t_p=m__pos;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<368>";
		while(m__pos<m__text.length() && (int)m__text.charAt(m__pos)>=48 && (int)m__text.charAt(m__pos)<=57){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<369>";
			m__pos+=1;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<371>";
		boolean t_=m__pos>t_p;
		bb_std_lang.popErr();
		return t_;
	}
	public final boolean p_CParseChar(int t_chr){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<361>";
		if(m__pos>=m__text.length() || (int)m__text.charAt(m__pos)!=t_chr){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<361>";
			bb_std_lang.popErr();
			return false;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<362>";
		m__pos+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<363>";
		bb_std_lang.popErr();
		return true;
	}
	public final int p_PeekChar(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<351>";
		if(m__pos==m__text.length()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<351>";
			bb_std_lang.popErr();
			return 0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<352>";
		bb_std_lang.popErr();
		return (int)m__text.charAt(m__pos);
	}
	public final String p_Bump(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<376>";
		while(m__pos<m__text.length() && (int)m__text.charAt(m__pos)<=32){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<377>";
			m__pos+=1;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<380>";
		if(m__pos==m__text.length()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<381>";
			m__toke="";
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<382>";
			m__type=0;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<383>";
			bb_std_lang.popErr();
			return m__toke;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<386>";
		int t_pos=m__pos;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<387>";
		int t_chr=p_GetChar();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<389>";
		if(t_chr==34){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<390>";
			do{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<391>";
				int t_chr2=p_GetChar();
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<392>";
				if(t_chr2==34){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<392>";
					break;
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<393>";
				if(t_chr2==92){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<393>";
					p_GetChar();
				}
			}while(!(false));
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<395>";
			m__type=1;
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<396>";
			if(t_chr==45 || t_chr>=48 && t_chr<=57){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<397>";
				if(t_chr==45){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<398>";
					t_chr=p_GetChar();
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<399>";
					if(t_chr<48 || t_chr>57){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<399>";
						bb_json.g_ThrowError();
					}
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<401>";
				if(t_chr!=48){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<402>";
					p_CParseDigits();
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<404>";
				if(p_CParseChar(46)){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<405>";
					p_CParseDigits();
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<407>";
				if(p_CParseChar(69) || p_CParseChar(101)){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<408>";
					if(p_PeekChar()==43 || p_PeekChar()==45){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<408>";
						p_GetChar();
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<409>";
					if(!p_CParseDigits()){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<409>";
						bb_json.g_ThrowError();
					}
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<411>";
				m__type=2;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<412>";
				if(t_chr>=65 && t_chr<91 || t_chr>=97 && t_chr<123){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<413>";
					t_chr=p_PeekChar();
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<414>";
					while(t_chr>=65 && t_chr<91 || t_chr>=97 && t_chr<123){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<415>";
						p_GetChar();
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<416>";
						t_chr=p_PeekChar();
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<418>";
					m__type=4;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<420>";
					m__type=3;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<422>";
		m__toke=bb_std_lang.slice(m__text,t_pos,m__pos);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<423>";
		bb_std_lang.popErr();
		return m__toke;
	}
	public final c_JsonParser m_JsonParser_new(String t_json){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<316>";
		m__text=t_json;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<317>";
		p_Bump();
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonParser m_JsonParser_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<313>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_CParse(String t_toke){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<435>";
		if(t_toke.compareTo(m__toke)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<435>";
			bb_std_lang.popErr();
			return false;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<436>";
		p_Bump();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<437>";
		bb_std_lang.popErr();
		return true;
	}
	public final void p_Parse(String t_toke){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<441>";
		if(!p_CParse(t_toke)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<441>";
			bb_json.g_ThrowError();
		}
		bb_std_lang.popErr();
	}
	public final int p_TokeType(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<431>";
		bb_std_lang.popErr();
		return m__type;
	}
	public final String p_Toke(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<427>";
		bb_std_lang.popErr();
		return m__toke;
	}
	public final String p_ParseString(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<471>";
		if(p_TokeType()!=1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<471>";
			bb_json.g_ThrowError();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<472>";
		String t_toke=bb_std_lang.slice(p_Toke(),1,-1);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<473>";
		int t_i=t_toke.indexOf("\\",0);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<474>";
		if(t_i!=-1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<475>";
			c_StringStack t_frags=(new c_StringStack()).m_StringStack_new2();
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<475>";
			int t_p=0;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<475>";
			String t_esc="";
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<476>";
			do{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<477>";
				if(t_i+1>=t_toke.length()){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<477>";
					bb_json.g_ThrowError();
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<478>";
				t_frags.p_Push4(bb_std_lang.slice(t_toke,t_p,t_i));
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<479>";
				int t_1=(int)t_toke.charAt(t_i+1);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<480>";
				if(t_1==34){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<480>";
					t_esc="\"";
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<481>";
					if(t_1==92){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<481>";
						t_esc="\\";
					}else{
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<482>";
						if(t_1==47){
							bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<482>";
							t_esc="/";
						}else{
							bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<483>";
							if(t_1==98){
								bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<483>";
								t_esc=String.valueOf((char)(8));
							}else{
								bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<484>";
								if(t_1==102){
									bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<484>";
									t_esc=String.valueOf((char)(12));
								}else{
									bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<485>";
									if(t_1==114){
										bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<485>";
										t_esc=String.valueOf((char)(13));
									}else{
										bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<486>";
										if(t_1==110){
											bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<486>";
											t_esc=String.valueOf((char)(10));
										}else{
											bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<487>";
											if(t_1==117){
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<488>";
												if(t_i+6>t_toke.length()){
													bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<488>";
													bb_json.g_ThrowError();
												}
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<489>";
												int t_val=0;
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<490>";
												for(int t_j=2;t_j<6;t_j=t_j+1){
													bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<491>";
													int t_chr=(int)t_toke.charAt(t_i+t_j);
													bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<492>";
													if(t_chr>=48 && t_chr<58){
														bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<493>";
														t_val=t_val<<4|t_chr-48;
													}else{
														bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<494>";
														if(t_chr>=65 && t_chr<123){
															bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<495>";
															t_chr&=31;
															bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<496>";
															if(t_chr<1 || t_chr>6){
																bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<496>";
																bb_json.g_ThrowError();
															}
															bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<497>";
															t_val=t_val<<4|t_chr+9;
														}else{
															bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<499>";
															bb_json.g_ThrowError();
														}
													}
												}
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<502>";
												t_esc=String.valueOf((char)(t_val));
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<503>";
												t_i+=4;
											}else{
												bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<505>";
												bb_json.g_ThrowError();
											}
										}
									}
								}
							}
						}
					}
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<507>";
				t_frags.p_Push4(t_esc);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<508>";
				t_p=t_i+2;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<509>";
				t_i=t_toke.indexOf("\\",t_p);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<510>";
				if(t_i!=-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<510>";
					continue;
				}
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<511>";
				t_frags.p_Push4(bb_std_lang.slice(t_toke,t_p));
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<512>";
				break;
			}while(!(false));
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<514>";
			t_toke=t_frags.p_Join("");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<516>";
		p_Bump();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<517>";
		bb_std_lang.popErr();
		return t_toke;
	}
	public final String p_ParseNumber(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<521>";
		if(p_TokeType()!=2){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<521>";
			bb_json.g_ThrowError();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<522>";
		String t_toke=p_Toke();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<523>";
		p_Bump();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<524>";
		bb_std_lang.popErr();
		return t_toke;
	}
	public final c_JsonValue[] p_ParseArray(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<459>";
		p_Parse("[");
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<460>";
		if(p_CParse("]")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<460>";
			bb_std_lang.popErr();
			return new c_JsonValue[0];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<461>";
		c_Stack3 t_stack=(new c_Stack3()).m_Stack_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<462>";
		do{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<463>";
			c_JsonValue t_value=p_ParseValue();
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<464>";
			t_stack.p_Push7(t_value);
		}while(!(!p_CParse(",")));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<466>";
		p_Parse("]");
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<467>";
		c_JsonValue[] t_=t_stack.p_ToArray();
		bb_std_lang.popErr();
		return t_;
	}
	public final c_JsonValue p_ParseValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<321>";
		if(p_TokeType()==1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<321>";
			c_JsonValue t_=(c_JsonString.m_Instance(p_ParseString()));
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<322>";
		if(p_TokeType()==2){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<322>";
			c_JsonValue t_2=(c_JsonNumber.m_Instance(p_ParseNumber()));
			bb_std_lang.popErr();
			return t_2;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<323>";
		if(p_Toke().compareTo("{")==0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<323>";
			c_JsonValue t_3=((new c_JsonObject()).m_JsonObject_new2(p_ParseObject()));
			bb_std_lang.popErr();
			return t_3;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<324>";
		if(p_Toke().compareTo("[")==0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<324>";
			c_JsonValue t_4=((new c_JsonArray()).m_JsonArray_new2(p_ParseArray()));
			bb_std_lang.popErr();
			return t_4;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<325>";
		if(p_CParse("true")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<325>";
			c_JsonValue t_5=(c_JsonBool.m_Instance(true));
			bb_std_lang.popErr();
			return t_5;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<326>";
		if(p_CParse("false")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<326>";
			c_JsonValue t_6=(c_JsonBool.m_Instance(false));
			bb_std_lang.popErr();
			return t_6;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<327>";
		if(p_CParse("null")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<327>";
			c_JsonValue t_7=(c_JsonNull.m_Instance());
			bb_std_lang.popErr();
			return t_7;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<328>";
		bb_json.g_ThrowError();
		bb_std_lang.popErr();
		return null;
	}
	public final c_StringMap p_ParseObject(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<445>";
		p_Parse("{");
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<446>";
		c_StringMap t_map=(new c_StringMap()).m_StringMap_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<447>";
		if(p_CParse("}")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<447>";
			bb_std_lang.popErr();
			return t_map;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<448>";
		do{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<449>";
			String t_name=p_ParseString();
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<450>";
			p_Parse(":");
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<451>";
			c_JsonValue t_value=p_ParseValue();
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<452>";
			t_map.p_Set2(t_name,t_value);
		}while(!(!p_CParse(",")));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<454>";
		p_Parse("}");
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<455>";
		bb_std_lang.popErr();
		return t_map;
	}
}
class c_JsonError extends ThrowableObject{
	public final c_JsonError m_JsonError_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<11>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_Stack2 extends Object{
	public final c_Stack2 m_Stack_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	String[] m_data=bb_std_lang.emptyStringArray;
	int m_length=0;
	public final c_Stack2 m_Stack_new2(String[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<13>";
		this.m_data=((String[])bb_std_lang.sliceArray(t_data,0));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<14>";
		this.m_length=bb_std_lang.length(t_data);
		bb_std_lang.popErr();
		return this;
	}
	public final void p_Push4(String t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<71>";
		if(m_length==bb_std_lang.length(m_data)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<72>";
			m_data=bb_std_lang.resize(m_data,m_length*2+10);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<74>";
		m_data[m_length]=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<75>";
		m_length+=1;
		bb_std_lang.popErr();
	}
	public final void p_Push5(String[] t_values,int t_offset,int t_count){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<83>";
		for(int t_i=0;t_i<t_count;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<84>";
			p_Push4(t_values[t_offset+t_i]);
		}
		bb_std_lang.popErr();
	}
	public final void p_Push6(String[] t_values,int t_offset){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<79>";
		p_Push5(t_values,t_offset,bb_std_lang.length(t_values)-t_offset);
		bb_std_lang.popErr();
	}
	public final String[] p_ToArray(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<18>";
		String[] t_t=bb_std_lang.stringArray(m_length);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<19>";
		for(int t_i=0;t_i<m_length;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<20>";
			t_t[t_i]=m_data[t_i];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<22>";
		bb_std_lang.popErr();
		return t_t;
	}
}
class c_StringStack extends c_Stack2{
	public final c_StringStack m_StringStack_new(String[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<355>";
		super.m_Stack_new2(t_data);
		bb_std_lang.popErr();
		return this;
	}
	public final c_StringStack m_StringStack_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<352>";
		super.m_Stack_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<352>";
		bb_std_lang.popErr();
		return this;
	}
	public final String p_Join(String t_separator){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<359>";
		String t_=bb_std_lang.join(t_separator,p_ToArray());
		bb_std_lang.popErr();
		return t_;
	}
}
class c_JsonString extends c_JsonValue{
	String m__value="";
	public final c_JsonString m_JsonString_new(String t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<257>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<258>";
		m__value=t_value;
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonString m_JsonString_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<255>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<255>";
		bb_std_lang.popErr();
		return this;
	}
	static c_JsonString m__null;
	public static c_JsonString m_Instance(String t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<270>";
		if((t_value).length()!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<270>";
			c_JsonString t_=(new c_JsonString()).m_JsonString_new(t_value);
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<271>";
		bb_std_lang.popErr();
		return m__null;
	}
	public final String p_StringValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<262>";
		bb_std_lang.popErr();
		return m__value;
	}
}
class c_JsonNumber extends c_JsonValue{
	String m__value="";
	public final c_JsonNumber m_JsonNumber_new(String t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<284>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<286>";
		m__value=t_value;
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonNumber m_JsonNumber_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<282>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<282>";
		bb_std_lang.popErr();
		return this;
	}
	static c_JsonNumber m__zero;
	public static c_JsonNumber m_Instance(String t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<302>";
		if(t_value.compareTo("0")!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<302>";
			c_JsonNumber t_=(new c_JsonNumber()).m_JsonNumber_new(t_value);
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<303>";
		bb_std_lang.popErr();
		return m__zero;
	}
	public final int p_IntValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<290>";
		int t_=LangUtil.parseInt((m__value).trim());
		bb_std_lang.popErr();
		return t_;
	}
	public final float p_FloatValue(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<294>";
		float t_=LangUtil.parseFloat((m__value).trim());
		bb_std_lang.popErr();
		return t_;
	}
}
class c_JsonArray extends c_JsonValue{
	c_JsonValue[] m__data=new c_JsonValue[0];
	public final c_JsonArray m_JsonArray_new(int t_length){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<133>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<134>";
		m__data=new c_JsonValue[t_length];
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonArray m_JsonArray_new2(c_JsonValue[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<137>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<138>";
		m__data=t_data;
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonArray m_JsonArray_new3(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<131>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<131>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Length(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<142>";
		int t_=bb_std_lang.length(m__data);
		bb_std_lang.popErr();
		return t_;
	}
	public final c_JsonValue p_Get3(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<167>";
		if(t_index<0 || t_index>=bb_std_lang.length(m__data)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<167>";
			bb_json.g_ThrowError();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<168>";
		c_JsonValue t_val=m__data[t_index];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<169>";
		if((t_val)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<169>";
			bb_std_lang.popErr();
			return t_val;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<170>";
		c_JsonValue t_=(c_JsonNull.m_Instance());
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Stack3 extends Object{
	public final c_Stack3 m_Stack_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_JsonValue[] m_data=new c_JsonValue[0];
	int m_length=0;
	public final c_Stack3 m_Stack_new2(c_JsonValue[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<13>";
		this.m_data=((c_JsonValue[])bb_std_lang.sliceArray(t_data,0));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<14>";
		this.m_length=bb_std_lang.length(t_data);
		bb_std_lang.popErr();
		return this;
	}
	public final void p_Push7(c_JsonValue t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<71>";
		if(m_length==bb_std_lang.length(m_data)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<72>";
			m_data=(c_JsonValue[])bb_std_lang.resize(m_data,m_length*2+10,c_JsonValue.class);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<74>";
		m_data[m_length]=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<75>";
		m_length+=1;
		bb_std_lang.popErr();
	}
	public final void p_Push8(c_JsonValue[] t_values,int t_offset,int t_count){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<83>";
		for(int t_i=0;t_i<t_count;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<84>";
			p_Push7(t_values[t_offset+t_i]);
		}
		bb_std_lang.popErr();
	}
	public final void p_Push9(c_JsonValue[] t_values,int t_offset){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<79>";
		p_Push8(t_values,t_offset,bb_std_lang.length(t_values)-t_offset);
		bb_std_lang.popErr();
	}
	public final c_JsonValue[] p_ToArray(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<18>";
		c_JsonValue[] t_t=new c_JsonValue[m_length];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<19>";
		for(int t_i=0;t_i<m_length;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<20>";
			t_t[t_i]=m_data[t_i];
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<22>";
		bb_std_lang.popErr();
		return t_t;
	}
}
class c_JsonBool extends c_JsonValue{
	boolean m__value=false;
	public final c_JsonBool m_JsonBool_new(boolean t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<228>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<229>";
		m__value=t_value;
		bb_std_lang.popErr();
		return this;
	}
	public final c_JsonBool m_JsonBool_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<226>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<226>";
		bb_std_lang.popErr();
		return this;
	}
	static c_JsonBool m__true;
	static c_JsonBool m__false;
	public static c_JsonBool m_Instance(boolean t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<242>";
		if(t_value){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<242>";
			bb_std_lang.popErr();
			return m__true;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<243>";
		bb_std_lang.popErr();
		return m__false;
	}
}
class c_JsonNull extends c_JsonValue{
	public final c_JsonNull m_JsonNull_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<210>";
		super.m_JsonValue_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<210>";
		bb_std_lang.popErr();
		return this;
	}
	static c_JsonNull m__instance;
	public static c_JsonNull m_Instance(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<217>";
		bb_std_lang.popErr();
		return m__instance;
	}
}
class c_Node2 extends Object{
	String m_key="";
	c_Node2 m_right=null;
	c_Node2 m_left=null;
	c_JsonValue m_value=null;
	int m_color=0;
	c_Node2 m_parent=null;
	public final c_Node2 m_Node_new(String t_key,c_JsonValue t_value,int t_color,c_Node2 t_parent){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<364>";
		this.m_key=t_key;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<365>";
		this.m_value=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<366>";
		this.m_color=t_color;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<367>";
		this.m_parent=t_parent;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node2 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<361>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_Game extends Object{
	c_IntMap2 m_units=null;
	c_List m_opponents=null;
	c_List2 m_particles=null;
	c_IntMap3 m_types=null;
	public final c_Game m_Game_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<17>";
		this.m_units=(new c_IntMap2()).m_IntMap_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<18>";
		this.m_opponents=(new c_List()).m_List_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<19>";
		this.m_particles=(new c_List2()).m_List_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<20>";
		this.m_types=(new c_IntMap3()).m_IntMap_new();
		bb_std_lang.popErr();
		return this;
	}
	String m_id="";
	String m_player_state="";
	public final int p_LoadFromJson(c_JsonObject t_game_json,String t_player_id){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<37>";
		this.m_units.p_Clear();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<38>";
		this.m_opponents.p_Clear();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<39>";
		this.m_particles.p_Clear();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<40>";
		this.m_types.p_Clear();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<42>";
		this.m_id=String.valueOf(t_game_json.p_GetInt("id",0));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<43>";
		c_JsonArray t_unit_list=bb_std_lang.as(c_JsonArray.class,(t_game_json.p_Get("units",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<44>";
		c_JsonArray t_types_list=bb_std_lang.as(c_JsonArray.class,(t_game_json.p_Get("types",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<45>";
		c_JsonArray t_player_list=bb_std_lang.as(c_JsonArray.class,(t_game_json.p_Get("players",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<46>";
		c_JsonArray t_particle_list=bb_std_lang.as(c_JsonArray.class,(t_game_json.p_Get("particles",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<48>";
		for(int t_i=0;t_i<t_types_list.p_Length();t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<49>";
			c_JsonObject t_type_json=bb_std_lang.as(c_JsonObject.class,(t_types_list.p_Get3(t_i)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<50>";
			c_UnitType t_new_type=(new c_UnitType()).m_UnitType_new(t_type_json);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<51>";
			this.m_types.p_Add2(t_new_type.m_id,t_new_type);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<54>";
		for(int t_i2=0;t_i2<t_unit_list.p_Length();t_i2=t_i2+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<55>";
			c_JsonObject t_unit_json=bb_std_lang.as(c_JsonObject.class,(t_unit_list.p_Get3(t_i2)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<56>";
			int t_type_id=t_unit_json.p_GetInt("unit_type_id",0);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<57>";
			c_UnitType t_unit_type=this.m_types.p_Get3(t_type_id);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<66>";
			c_Unit t_new_unit=(new c_Unit()).m_Unit_new(t_unit_json.p_GetInt("id",0),t_unit_json.p_GetFloat("x",0.0f),t_unit_json.p_GetFloat("y",0.0f),t_unit_json.p_GetFloat("heading",0.0f),t_unit_type,String.valueOf(t_unit_json.p_GetInt("player_id",0)),t_unit_json.p_GetInt("player_id",0),t_unit_json.p_GetFloat("energy",0.0f));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<67>";
			t_new_unit.m_armor=t_unit_json.p_GetInt("armor",0);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<68>";
			this.m_units.p_Add(t_new_unit.m_unit_id,t_new_unit);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<71>";
		for(int t_i3=0;t_i3<t_player_list.p_Length();t_i3=t_i3+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<72>";
			c_JsonObject t_player_json=bb_std_lang.as(c_JsonObject.class,(t_player_list.p_Get3(t_i3)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<73>";
			String t_current_player_id=String.valueOf(t_player_json.p_GetInt("player_id",0));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<74>";
			String t_current_player_state=t_player_json.p_GetString("player_state","");
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<75>";
			if(t_current_player_id.compareTo(t_player_id)==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<76>";
				this.m_player_state=t_current_player_state;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<77>";
				break;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<81>";
		for(int t_i4=0;t_i4<t_particle_list.p_Length();t_i4=t_i4+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<82>";
			c_JsonObject t_particle_json=bb_std_lang.as(c_JsonObject.class,(t_particle_list.p_Get3(t_i4)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<90>";
			c_Particle t_new_particle=(new c_Particle()).m_Particle_new((new c_Vec2D()).m_Vec2D_new(t_particle_json.p_GetFloat("x",0.0f),t_particle_json.p_GetFloat("y",0.0f),0.0f),2.5f,t_particle_json.p_GetFloat("power",0.0f),t_particle_json.p_GetFloat("heading",0.0f),t_particle_json.p_GetFloat("speed",0.0f),t_particle_json.p_GetInt("team",0),t_particle_json.p_GetInt("lifetime",0));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<92>";
			t_new_particle.m_past_position.p_Set4(t_new_particle.m_position.m_x-t_new_particle.m_speed*(float)Math.cos(t_new_particle.m_angle*0.017453292500000002f),t_new_particle.m_position.m_y-t_new_particle.m_speed*(float)Math.sin(t_new_particle.m_angle*0.017453292500000002f),0.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<93>";
			this.m_particles.p_AddLast2(t_new_particle);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_LoadServerMoves(c_JsonObject t_game_json){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<99>";
		c_JsonArray t_unit_list=bb_std_lang.as(c_JsonArray.class,(t_game_json.p_Get("units",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<100>";
		for(int t_i=0;t_i<t_unit_list.p_Length();t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<101>";
			c_JsonObject t_unit_json=bb_std_lang.as(c_JsonObject.class,(t_unit_list.p_Get3(t_i)));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<102>";
			c_Unit t_current_unit=this.m_units.p_Get3(t_unit_json.p_GetInt("id",0));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<103>";
			t_current_unit.m_position.p_Set4(t_unit_json.p_GetFloat("x",0.0f),t_unit_json.p_GetFloat("y",0.0f),0.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<104>";
			t_current_unit.m_heading=t_unit_json.p_GetFloat("heading",0.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<105>";
			t_current_unit.p_SetServerControl(t_unit_json.p_GetFloat("control_x",0.0f),t_unit_json.p_GetFloat("control_y",0.0f),640.0f,480.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<106>";
			t_current_unit.m_armor=t_unit_json.p_GetInt("armor",0);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<107>";
			this.m_units.p_Set3(t_unit_json.p_GetInt("id",0),t_current_unit);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_SetUnitPathsToServerSimulation(c_JsonObject t_server_json,String t_player_id){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<112>";
		c_JsonObject t_moves_json=bb_std_lang.as(c_JsonObject.class,(t_server_json.p_Get("move_points",null)));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<113>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<113>";
		c_KeyEnumerator t_=this.m_units.p_Keys().p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<113>";
		while(t_.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<113>";
			int t_key=t_.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<114>";
			if(this.m_units.p_Get3(t_key).m_player_id.compareTo(t_player_id)==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<115>";
				this.m_units.p_Get3(t_key).m_points=(new c_Deque()).m_Deque_new();
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<116>";
				c_JsonArray t_moves_array=bb_std_lang.as(c_JsonArray.class,(t_moves_json.p_Get(String.valueOf(t_key),null)));
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<117>";
				for(int t_i=0;t_i<t_moves_array.p_Length();t_i=t_i+1){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<118>";
					c_JsonObject t_move_json=bb_std_lang.as(c_JsonObject.class,(t_moves_array.p_Get3(t_i)));
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<119>";
					c_Vec2D t_move=(new c_Vec2D()).m_Vec2D_new(t_move_json.p_GetFloat("x",0.0f),t_move_json.p_GetFloat("y",0.0f),t_move_json.p_GetFloat("heading",0.0f));
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<121>";
					this.m_units.p_Get3(t_key).m_points.p_PushLast(t_move);
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<123>";
				c_Vec2D t_last_point=this.m_units.p_Get3(t_key).m_points.p_Get3(this.m_units.p_Get3(t_key).m_points.p_Length()-1);
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<124>";
				this.m_units.p_Get3(t_key).m_control.m_position.p_Set4(t_last_point.m_x,t_last_point.m_y,t_last_point.m_heading);
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final String p_BuildMoveJson(String t_player_id){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<130>";
		bb_std_lang.print("Prepping move json");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<131>";
		int t_first=1;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<132>";
		String t_move_json="{ \"data\" : { \"player_id\": "+t_player_id+", ";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<133>";
		t_move_json=t_move_json+"\"moves\" : [ ";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<135>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<135>";
		c_KeyEnumerator t_=this.m_units.p_Keys().p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<135>";
		while(t_.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<135>";
			int t_unit_id=t_.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<137>";
			if(this.m_units.p_Get3(t_unit_id).m_player_id.compareTo(t_player_id)==0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<138>";
				if(t_first==1){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<139>";
					t_first=0;
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<141>";
					t_move_json=t_move_json+", ";
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<143>";
				t_move_json=t_move_json+("{ \"unit_id\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_unit_id)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<144>";
				t_move_json=t_move_json+("\"x\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_position.m_x)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<145>";
				t_move_json=t_move_json+("\"y\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_position.m_y)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<146>";
				t_move_json=t_move_json+("\"control-x\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_control.m_position.m_x)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<147>";
				t_move_json=t_move_json+("\"control-y\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_control.m_position.m_y)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<148>";
				t_move_json=t_move_json+("\"control-heading\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_control.m_position.m_heading)+", ");
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<149>";
				t_move_json=t_move_json+("\"heading\": "+String.valueOf(this.m_units.p_Get3(t_unit_id).m_heading));
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<150>";
				t_move_json=t_move_json+" }";
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<153>";
		t_move_json=t_move_json+" ] } }";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<155>";
		bb_std_lang.popErr();
		return t_move_json;
	}
	public final int p_Draw(String t_player_id,String t_game_state){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<25>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<25>";
		c_KeyEnumerator t_=this.m_units.p_Keys().p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<25>";
		while(t_.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<25>";
			int t_key=t_.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<26>";
			c_Unit t_current_unit=this.m_units.p_Get3(t_key);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<27>";
			if(t_current_unit.m_armor>0){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<28>";
				t_current_unit.p_DrawStatic(t_player_id,t_game_state);
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<31>";
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<31>";
		c_Enumerator3 t_2=this.m_particles.p_ObjectEnumerator();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<31>";
		while(t_2.p_HasNext()){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<31>";
			c_Particle t_part=t_2.p_NextObject();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/game.monkey<32>";
			t_part.p_Draw2();
		}
		bb_std_lang.popErr();
		return 0;
	}
}
class c_Unit extends Object{
	int m_unit_id=0;
	String m_player_id="";
	c_UnitType m_unit_type=null;
	c_Vec2D m_position=null;
	c_ControlPoint m_control=null;
	float m_heading=.0f;
	c_Vec2D m_velocity=null;
	c_Deque m_points=null;
	public final int p_SetControl(float t_click_x,float t_click_y,float t_map_width,float t_map_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<105>";
		int t_goal_angle=(int)((float)(Math.atan2(t_click_y-this.m_position.m_y,t_click_x-this.m_position.m_x)*bb_std_lang.R2D));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<106>";
		int t_start_angle=(int)(this.m_heading);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<107>";
		c_Vec2D t_control_pos=(new c_Vec2D()).m_Vec2D_new(this.m_position.m_x,this.m_position.m_y,this.m_heading);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<108>";
		this.m_points=(new c_Deque()).m_Deque_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<110>";
		for(int t_i=0;t_i<30;t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<111>";
			t_control_pos=bb_dronetournament.g_NewPoint(t_control_pos,(float)(t_start_angle),(float)(t_goal_angle),this.m_unit_type.m_maxRotation,this.m_unit_type.m_maxVelocity/30.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<112>";
			t_start_angle=(int)(t_control_pos.m_heading);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<113>";
			t_goal_angle=(int)((float)(Math.atan2(t_click_y-t_control_pos.m_y,t_click_x-t_control_pos.m_x)*bb_std_lang.R2D));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<114>";
			this.m_points.p_PushLast(t_control_pos);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<117>";
		if(t_control_pos.m_x>t_map_width){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<118>";
			t_control_pos.m_x=t_map_width-10.0f;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<119>";
			t_control_pos.m_heading=180.0f;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<120>";
			if(t_control_pos.m_x<0.0f){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<121>";
				t_control_pos.m_x=10.0f;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<122>";
				t_control_pos.m_heading=0.0f;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<125>";
		if(t_control_pos.m_y>t_map_height){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<126>";
			t_control_pos.m_y=t_map_height-10.0f;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<127>";
			t_control_pos.m_heading=270.0f;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<128>";
			if(t_control_pos.m_y<0.0f){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<129>";
				t_control_pos.m_y=10.0f;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<130>";
				t_control_pos.m_heading=90.0f;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<133>";
		this.m_control.m_position.p_Set4(t_control_pos.m_x,t_control_pos.m_y,t_control_pos.m_heading);
		bb_std_lang.popErr();
		return 0;
	}
	int m_team=0;
	float m_currentEnergy=.0f;
	int m_armor=0;
	public final c_Unit m_Unit_new(int t_unit_id,float t_x,float t_y,float t_initial_heading,c_UnitType t_unit_type,String t_player_id,int t_team_number,float t_current_energy){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<23>";
		this.m_unit_id=t_unit_id;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<24>";
		this.m_player_id=t_player_id;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<25>";
		this.m_unit_type=t_unit_type;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<27>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(t_x,t_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<28>";
		this.m_control=(new c_ControlPoint()).m_ControlPoint_new(t_x+this.m_unit_type.m_maxVelocity,t_y,t_initial_heading,20.0f,20.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<30>";
		this.m_heading=t_initial_heading;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<31>";
		this.m_velocity=(new c_Vec2D()).m_Vec2D_new(this.m_unit_type.m_maxVelocity*(float)Math.cos(m_heading*0.017453292500000002f),this.m_unit_type.m_maxVelocity*(float)Math.sin(m_heading*0.017453292500000002f),0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<32>";
		this.p_SetControl(m_velocity.m_x,m_velocity.m_y,640.0f,480.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<33>";
		this.m_team=t_team_number;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<35>";
		this.m_currentEnergy=t_current_energy;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<37>";
		this.m_armor=this.m_unit_type.m_maxArmor;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Unit m_Unit_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<7>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_SetServerControl(float t_click_x,float t_click_y,float t_map_width,float t_map_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<137>";
		int t_goal_angle=(int)((float)(Math.atan2(t_click_y-this.m_position.m_y,t_click_x-this.m_position.m_x)*bb_std_lang.R2D));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<138>";
		float t_start_angle=this.m_heading;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<139>";
		c_Vec2D t_control_pos=(new c_Vec2D()).m_Vec2D_new(this.m_position.m_x,this.m_position.m_y,this.m_heading);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<140>";
		this.m_points=(new c_Deque()).m_Deque_new();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<142>";
		for(int t_i=0;t_i<30;t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<143>";
			t_control_pos=bb_dronetournament.g_NewPoint(t_control_pos,t_start_angle,(float)(t_goal_angle),this.m_unit_type.m_maxRotation,this.m_unit_type.m_maxVelocity/30.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<144>";
			t_start_angle=t_control_pos.m_heading;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<145>";
			t_goal_angle=(int)((float)(Math.atan2(t_click_y-t_control_pos.m_y,t_click_x-t_control_pos.m_x)*bb_std_lang.R2D));
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<146>";
			this.m_points.p_PushLast(t_control_pos);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<149>";
		this.m_control.m_position.p_Set4(t_control_pos.m_x,t_control_pos.m_y,t_control_pos.m_heading);
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_ControlSelected(float t_click_x,float t_click_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<87>";
		if(this.m_control.m_selected){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<88>";
			bb_std_lang.popErr();
			return 1;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<92>";
			if(t_click_x>=this.m_control.m_position.m_x-this.m_control.m_width && t_click_x<=this.m_control.m_position.m_x+this.m_control.m_width && t_click_y>=this.m_control.m_position.m_y-this.m_control.m_width && t_click_y<=this.m_control.m_position.m_y+this.m_control.m_height){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<93>";
				this.m_control.m_selected=true;
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<94>";
				bb_std_lang.popErr();
				return 1;
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<96>";
				bb_std_lang.popErr();
				return 0;
			}
		}
	}
	public final int p_ControlReleased(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<101>";
		this.m_control.m_selected=false;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_Update(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<77>";
		c_Vec2D t_next_point=this.m_points.p_PopFirst();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<79>";
		this.m_heading=(float)(Math.atan2(t_next_point.m_y-this.m_position.m_y,t_next_point.m_x-this.m_position.m_x)*bb_std_lang.R2D);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<80>";
		this.m_velocity.p_Set4(this.m_unit_type.m_maxVelocity*(float)Math.cos(m_heading*0.017453292500000002f),this.m_unit_type.m_maxVelocity*(float)Math.sin(this.m_heading*0.017453292500000002f),0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<81>";
		this.m_position=t_next_point;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<82>";
		this.m_currentEnergy=bb_math2.g_Min2(this.m_unit_type.m_maxEnergy,this.m_currentEnergy+this.m_unit_type.m_chargeEnergy);
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_FireWeapon(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<153>";
		this.m_currentEnergy=0.0f;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_TakeDamage(int t_power){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<157>";
		this.m_armor=this.m_armor-t_power;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_DrawStatic(String t_game_player_id,String t_game_state){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<41>";
		int t_R=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<42>";
		int t_G=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<43>";
		int t_B=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<44>";
		if(this.m_player_id.compareTo(t_game_player_id)==0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<45>";
			t_R=128;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<46>";
			t_G=255;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<47>";
			t_B=128;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<49>";
			t_R=255;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<50>";
			t_G=128;
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<51>";
			t_B=128;
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<54>";
		bb_graphics.g_SetColor((float)(t_R),(float)(t_G),(float)(t_B));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<55>";
		bb_graphics.g_DrawImage2(this.m_unit_type.m_image,this.m_position.m_x,this.m_position.m_y,-this.m_heading,1.0f,1.0f,0);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<56>";
		for(int t_i=0;t_i<this.m_armor-1;t_i=t_i+1){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<57>";
			bb_graphics.g_SetColor(0.0f,0.0f,255.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<58>";
			bb_graphics.g_DrawRect(this.m_position.m_x-20.0f+(float)(t_i*5),this.m_position.m_y+28.0f,5.0f,3.0f);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<60>";
		bb_graphics.g_SetColor((float)(t_R),(float)(t_G),(float)(t_B));
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<61>";
		if((this.m_player_id.compareTo(t_game_player_id)==0) && ((t_game_state.compareTo("multiplayer")==0) || (t_game_state.compareTo("tutorial")==0) || (t_game_state.compareTo("end_turn")==0))){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<62>";
			this.m_control.p_Draw2();
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<64>";
			for(int t_i2=0;t_i2<this.m_points.p_Length()-1;t_i2=t_i2+1){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<65>";
				if((this.m_currentEnergy+(float)(t_i2)*this.m_unit_type.m_chargeEnergy) % this.m_unit_type.m_maxEnergy==0.0f){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<66>";
					bb_graphics.g_SetColor(100.0f,100.0f,255.0f);
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<68>";
					bb_graphics.g_SetColor(255.0f,255.0f,255.0f);
				}
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<70>";
				c_Vec2D t_this_point=this.m_points.p_Get3(t_i2);
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit.monkey<71>";
				bb_graphics.g_DrawPoint(t_this_point.m_x,t_this_point.m_y);
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
}
abstract class c_Map3 extends Object{
	public final c_Map3 m_Map_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<7>";
		bb_std_lang.popErr();
		return this;
	}
	c_Node6 m_root=null;
	public final int p_Clear(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<13>";
		m_root=null;
		bb_std_lang.popErr();
		return 0;
	}
	public abstract int p_Compare(int t_lhs,int t_rhs);
	public final int p_RotateLeft3(c_Node6 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<251>";
		c_Node6 t_child=t_node.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<252>";
		t_node.m_right=t_child.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<253>";
		if((t_child.m_left)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<254>";
			t_child.m_left.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<256>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<257>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<258>";
			if(t_node==t_node.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<259>";
				t_node.m_parent.m_left=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<261>";
				t_node.m_parent.m_right=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<264>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<266>";
		t_child.m_left=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<267>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_RotateRight3(c_Node6 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<271>";
		c_Node6 t_child=t_node.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<272>";
		t_node.m_left=t_child.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<273>";
		if((t_child.m_right)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<274>";
			t_child.m_right.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<276>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<277>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<278>";
			if(t_node==t_node.m_parent.m_right){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<279>";
				t_node.m_parent.m_right=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<281>";
				t_node.m_parent.m_left=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<284>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<286>";
		t_child.m_right=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<287>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_InsertFixup3(c_Node6 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<212>";
		while(((t_node.m_parent)!=null) && t_node.m_parent.m_color==-1 && ((t_node.m_parent.m_parent)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<213>";
			if(t_node.m_parent==t_node.m_parent.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<214>";
				c_Node6 t_uncle=t_node.m_parent.m_parent.m_right;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<215>";
				if(((t_uncle)!=null) && t_uncle.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<216>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<217>";
					t_uncle.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<218>";
					t_uncle.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<219>";
					t_node=t_uncle.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<221>";
					if(t_node==t_node.m_parent.m_right){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<222>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<223>";
						p_RotateLeft3(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<225>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<226>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<227>";
					p_RotateRight3(t_node.m_parent.m_parent);
				}
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<230>";
				c_Node6 t_uncle2=t_node.m_parent.m_parent.m_left;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<231>";
				if(((t_uncle2)!=null) && t_uncle2.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<232>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<233>";
					t_uncle2.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<234>";
					t_uncle2.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<235>";
					t_node=t_uncle2.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<237>";
					if(t_node==t_node.m_parent.m_left){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<238>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<239>";
						p_RotateRight3(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<241>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<242>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<243>";
					p_RotateLeft3(t_node.m_parent.m_parent);
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<247>";
		m_root.m_color=1;
		bb_std_lang.popErr();
		return 0;
	}
	public final boolean p_Add(int t_key,c_Unit t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<61>";
		c_Node6 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<62>";
		c_Node6 t_parent=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<62>";
		int t_cmp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<64>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<65>";
			t_parent=t_node;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<66>";
			t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<67>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<68>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<69>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<70>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<72>";
					bb_std_lang.popErr();
					return false;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<76>";
		t_node=(new c_Node6()).m_Node_new(t_key,t_value,-1,t_parent);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<78>";
		if((t_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<79>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<80>";
				t_parent.m_right=t_node;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<82>";
				t_parent.m_left=t_node;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<84>";
			p_InsertFixup3(t_node);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<86>";
			m_root=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<88>";
		bb_std_lang.popErr();
		return true;
	}
	public final c_Node6 p_FindNode(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<157>";
		c_Node6 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<159>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<160>";
			int t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<161>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<162>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<163>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<164>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<166>";
					bb_std_lang.popErr();
					return t_node;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<169>";
		bb_std_lang.popErr();
		return t_node;
	}
	public final c_Unit p_Get3(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<101>";
		c_Node6 t_node=p_FindNode(t_key);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
		if((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
			bb_std_lang.popErr();
			return t_node.m_value;
		}
		bb_std_lang.popErr();
		return null;
	}
	public final boolean p_Set3(int t_key,c_Unit t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<29>";
		c_Node6 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		c_Node6 t_parent=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<30>";
		int t_cmp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<32>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<33>";
			t_parent=t_node;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<34>";
			t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<35>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<36>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<37>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<38>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<40>";
					t_node.m_value=t_value;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<41>";
					bb_std_lang.popErr();
					return false;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<45>";
		t_node=(new c_Node6()).m_Node_new(t_key,t_value,-1,t_parent);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<47>";
		if((t_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<48>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<49>";
				t_parent.m_right=t_node;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<51>";
				t_parent.m_left=t_node;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<53>";
			p_InsertFixup3(t_node);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<55>";
			m_root=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<57>";
		bb_std_lang.popErr();
		return true;
	}
	public final c_MapKeys p_Keys(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<113>";
		c_MapKeys t_=(new c_MapKeys()).m_MapKeys_new(this);
		bb_std_lang.popErr();
		return t_;
	}
	public final c_Node6 p_FirstNode(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<125>";
		if(!((m_root)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<125>";
			bb_std_lang.popErr();
			return null;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<127>";
		c_Node6 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<128>";
		while((t_node.m_left)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<129>";
			t_node=t_node.m_left;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<131>";
		bb_std_lang.popErr();
		return t_node;
	}
}
class c_IntMap2 extends c_Map3{
	public final c_IntMap2 m_IntMap_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		super.m_Map_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Compare(int t_lhs,int t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<537>";
		int t_=t_lhs-t_rhs;
		bb_std_lang.popErr();
		return t_;
	}
}
class c_List extends Object{
	public final c_List m_List_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_Node3 m__head=((new c_HeadNode()).m_HeadNode_new());
	public final c_Node3 p_AddLast(c_Unit t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<108>";
		c_Node3 t_=(new c_Node3()).m_Node_new(m__head,m__head.m__pred,t_data);
		bb_std_lang.popErr();
		return t_;
	}
	public final c_List m_List_new2(c_Unit[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		c_Unit[] t_=t_data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		int t_2=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		while(t_2<bb_std_lang.length(t_)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			c_Unit t_t=t_[t_2];
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			t_2=t_2+1;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<14>";
			p_AddLast(t_t);
		}
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Clear(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<36>";
		m__head.m__succ=m__head;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<37>";
		m__head.m__pred=m__head;
		bb_std_lang.popErr();
		return 0;
	}
	public final c_Enumerator2 p_ObjectEnumerator(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<186>";
		c_Enumerator2 t_=(new c_Enumerator2()).m_Enumerator_new(this);
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Node3 extends Object{
	c_Node3 m__succ=null;
	c_Node3 m__pred=null;
	c_Unit m__data=null;
	public final c_Node3 m_Node_new(c_Node3 t_succ,c_Node3 t_pred,c_Unit t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<261>";
		m__succ=t_succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<262>";
		m__pred=t_pred;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<263>";
		m__succ.m__pred=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<264>";
		m__pred.m__succ=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<265>";
		m__data=t_data;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node3 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<258>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_HeadNode extends c_Node3{
	public final c_HeadNode m_HeadNode_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<310>";
		super.m_Node_new2();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<311>";
		m__succ=(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<312>";
		m__pred=(this);
		bb_std_lang.popErr();
		return this;
	}
}
class c_Particle extends Object{
	c_Vec2D m_position=null;
	c_Vec2D m_past_position=null;
	float m_size=.0f;
	float m_power=.0f;
	float m_speed=.0f;
	float m_angle=.0f;
	int m_lifetime=0;
	int m_team=0;
	public final c_Particle m_Particle_new(c_Vec2D t_pos,float t_size,float t_power,float t_angle,float t_speed,int t_unit_team,int t_life_time){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<15>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(t_pos.m_x,t_pos.m_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<16>";
		this.m_past_position=(new c_Vec2D()).m_Vec2D_new(t_pos.m_x,t_pos.m_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<17>";
		this.m_size=t_size;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<18>";
		this.m_power=t_power;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<19>";
		this.m_speed=t_speed;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<20>";
		this.m_angle=t_angle;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<21>";
		this.m_lifetime=t_life_time;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<22>";
		this.m_team=t_unit_team;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Particle m_Particle_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<4>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Update(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<32>";
		float t_posx=m_position.m_x;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<33>";
		float t_posy=m_position.m_y;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<34>";
		m_past_position.p_Set4(t_posx,t_posy,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<35>";
		m_position.p_Set4(m_position.m_x+m_speed*(float)Math.cos(m_angle*0.017453292500000002f),m_position.m_y+m_speed*(float)Math.sin(m_angle*0.017453292500000002f),0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<36>";
		m_lifetime=m_lifetime-1;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_Draw2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<26>";
		bb_graphics.g_SetColor((float)(55*this.m_team),0.0f,255.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<27>";
		bb_graphics.g_DrawCircle(m_position.m_x-m_size,m_position.m_y-m_size,m_size);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/particle.monkey<28>";
		bb_graphics.g_DrawLine(m_past_position.m_x,m_past_position.m_y,m_position.m_x,m_position.m_y);
		bb_std_lang.popErr();
		return 0;
	}
}
class c_List2 extends Object{
	public final c_List2 m_List_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_Node4 m__head=((new c_HeadNode2()).m_HeadNode_new());
	public final c_Node4 p_AddLast2(c_Particle t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<108>";
		c_Node4 t_=(new c_Node4()).m_Node_new(m__head,m__head.m__pred,t_data);
		bb_std_lang.popErr();
		return t_;
	}
	public final c_List2 m_List_new2(c_Particle[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		c_Particle[] t_=t_data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		int t_2=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		while(t_2<bb_std_lang.length(t_)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			c_Particle t_t=t_[t_2];
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			t_2=t_2+1;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<14>";
			p_AddLast2(t_t);
		}
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Clear(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<36>";
		m__head.m__succ=m__head;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<37>";
		m__head.m__pred=m__head;
		bb_std_lang.popErr();
		return 0;
	}
	public final c_Enumerator3 p_ObjectEnumerator(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<186>";
		c_Enumerator3 t_=(new c_Enumerator3()).m_Enumerator_new(this);
		bb_std_lang.popErr();
		return t_;
	}
	public final boolean p_Equals(c_Particle t_lhs,c_Particle t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<28>";
		boolean t_=t_lhs==t_rhs;
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_RemoveEach(c_Particle t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<151>";
		c_Node4 t_node=m__head.m__succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<152>";
		while(t_node!=m__head){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<153>";
			c_Node4 t_succ=t_node.m__succ;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<154>";
			if(p_Equals(t_node.m__data,t_value)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<154>";
				t_node.p_Remove2();
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<155>";
			t_node=t_succ;
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final void p_Remove(c_Particle t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<137>";
		p_RemoveEach(t_value);
		bb_std_lang.popErr();
	}
}
class c_Node4 extends Object{
	c_Node4 m__succ=null;
	c_Node4 m__pred=null;
	c_Particle m__data=null;
	public final c_Node4 m_Node_new(c_Node4 t_succ,c_Node4 t_pred,c_Particle t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<261>";
		m__succ=t_succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<262>";
		m__pred=t_pred;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<263>";
		m__succ.m__pred=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<264>";
		m__pred.m__succ=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<265>";
		m__data=t_data;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node4 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<258>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Remove2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<274>";
		if(m__succ.m__pred!=this){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<274>";
			bb_std_lang.error("Illegal operation on removed node");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<276>";
		m__succ.m__pred=m__pred;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<277>";
		m__pred.m__succ=m__succ;
		bb_std_lang.popErr();
		return 0;
	}
}
class c_HeadNode2 extends c_Node4{
	public final c_HeadNode2 m_HeadNode_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<310>";
		super.m_Node_new2();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<311>";
		m__succ=(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<312>";
		m__pred=(this);
		bb_std_lang.popErr();
		return this;
	}
}
class c_UnitType extends Object{
	int m_id=0;
	String m_name="";
	float m_maxVelocity=.0f;
	float m_maxRotation=.0f;
	float m_maxEnergy=.0f;
	float m_chargeEnergy=.0f;
	int m_maxArmor=0;
	c_Image m_image=null;
	public final c_UnitType m_UnitType_new(c_JsonObject t_type_json){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<17>";
		this.m_id=t_type_json.p_GetInt("id",0);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<18>";
		this.m_name=t_type_json.p_GetString("name","");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<19>";
		this.m_maxVelocity=t_type_json.p_GetFloat("speed",0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<20>";
		this.m_maxRotation=t_type_json.p_GetFloat("turn",0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<21>";
		this.m_maxEnergy=t_type_json.p_GetFloat("full_energy",0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<22>";
		this.m_chargeEnergy=t_type_json.p_GetFloat("charge_energy",0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<23>";
		this.m_maxArmor=t_type_json.p_GetInt("armor",0);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<25>";
		String t_image_name=t_type_json.p_GetString("image_name","");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<26>";
		this.m_image=bb_graphics.g_LoadImage("images/"+t_image_name,1,1);
		bb_std_lang.popErr();
		return this;
	}
	public final c_UnitType m_UnitType_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/unit_type.monkey<5>";
		bb_std_lang.popErr();
		return this;
	}
}
abstract class c_Map4 extends Object{
	public final c_Map4 m_Map_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<7>";
		bb_std_lang.popErr();
		return this;
	}
	c_Node7 m_root=null;
	public final int p_Clear(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<13>";
		m_root=null;
		bb_std_lang.popErr();
		return 0;
	}
	public abstract int p_Compare(int t_lhs,int t_rhs);
	public final int p_RotateLeft4(c_Node7 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<251>";
		c_Node7 t_child=t_node.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<252>";
		t_node.m_right=t_child.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<253>";
		if((t_child.m_left)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<254>";
			t_child.m_left.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<256>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<257>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<258>";
			if(t_node==t_node.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<259>";
				t_node.m_parent.m_left=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<261>";
				t_node.m_parent.m_right=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<264>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<266>";
		t_child.m_left=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<267>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_RotateRight4(c_Node7 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<271>";
		c_Node7 t_child=t_node.m_left;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<272>";
		t_node.m_left=t_child.m_right;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<273>";
		if((t_child.m_right)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<274>";
			t_child.m_right.m_parent=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<276>";
		t_child.m_parent=t_node.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<277>";
		if((t_node.m_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<278>";
			if(t_node==t_node.m_parent.m_right){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<279>";
				t_node.m_parent.m_right=t_child;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<281>";
				t_node.m_parent.m_left=t_child;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<284>";
			m_root=t_child;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<286>";
		t_child.m_right=t_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<287>";
		t_node.m_parent=t_child;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_InsertFixup4(c_Node7 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<212>";
		while(((t_node.m_parent)!=null) && t_node.m_parent.m_color==-1 && ((t_node.m_parent.m_parent)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<213>";
			if(t_node.m_parent==t_node.m_parent.m_parent.m_left){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<214>";
				c_Node7 t_uncle=t_node.m_parent.m_parent.m_right;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<215>";
				if(((t_uncle)!=null) && t_uncle.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<216>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<217>";
					t_uncle.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<218>";
					t_uncle.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<219>";
					t_node=t_uncle.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<221>";
					if(t_node==t_node.m_parent.m_right){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<222>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<223>";
						p_RotateLeft4(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<225>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<226>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<227>";
					p_RotateRight4(t_node.m_parent.m_parent);
				}
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<230>";
				c_Node7 t_uncle2=t_node.m_parent.m_parent.m_left;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<231>";
				if(((t_uncle2)!=null) && t_uncle2.m_color==-1){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<232>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<233>";
					t_uncle2.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<234>";
					t_uncle2.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<235>";
					t_node=t_uncle2.m_parent;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<237>";
					if(t_node==t_node.m_parent.m_left){
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<238>";
						t_node=t_node.m_parent;
						bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<239>";
						p_RotateRight4(t_node);
					}
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<241>";
					t_node.m_parent.m_color=1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<242>";
					t_node.m_parent.m_parent.m_color=-1;
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<243>";
					p_RotateLeft4(t_node.m_parent.m_parent);
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<247>";
		m_root.m_color=1;
		bb_std_lang.popErr();
		return 0;
	}
	public final boolean p_Add2(int t_key,c_UnitType t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<61>";
		c_Node7 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<62>";
		c_Node7 t_parent=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<62>";
		int t_cmp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<64>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<65>";
			t_parent=t_node;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<66>";
			t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<67>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<68>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<69>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<70>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<72>";
					bb_std_lang.popErr();
					return false;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<76>";
		t_node=(new c_Node7()).m_Node_new(t_key,t_value,-1,t_parent);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<78>";
		if((t_parent)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<79>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<80>";
				t_parent.m_right=t_node;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<82>";
				t_parent.m_left=t_node;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<84>";
			p_InsertFixup4(t_node);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<86>";
			m_root=t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<88>";
		bb_std_lang.popErr();
		return true;
	}
	public final c_Node7 p_FindNode(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<157>";
		c_Node7 t_node=m_root;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<159>";
		while((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<160>";
			int t_cmp=p_Compare(t_key,t_node.m_key);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<161>";
			if(t_cmp>0){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<162>";
				t_node=t_node.m_right;
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<163>";
				if(t_cmp<0){
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<164>";
					t_node=t_node.m_left;
				}else{
					bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<166>";
					bb_std_lang.popErr();
					return t_node;
				}
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<169>";
		bb_std_lang.popErr();
		return t_node;
	}
	public final c_UnitType p_Get3(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<101>";
		c_Node7 t_node=p_FindNode(t_key);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
		if((t_node)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<102>";
			bb_std_lang.popErr();
			return t_node.m_value;
		}
		bb_std_lang.popErr();
		return null;
	}
}
class c_IntMap3 extends c_Map4{
	public final c_IntMap3 m_IntMap_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		super.m_Map_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<534>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Compare(int t_lhs,int t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<537>";
		int t_=t_lhs-t_rhs;
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Button extends Object{
	c_Vec2D m_position=null;
	c_Image m_image=null;
	c_TouchBox m_touch_box=null;
	public final c_Button m_Button_new(float t_x,float t_y,float t_hit_x,float t_hit_y,float t_width,float t_height,c_Image t_image){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<11>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(t_x,t_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<12>";
		this.m_image=t_image;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<13>";
		this.m_touch_box=(new c_TouchBox()).m_TouchBox_new(t_hit_x,t_hit_y,t_width,t_height);
		bb_std_lang.popErr();
		return this;
	}
	public final c_Button m_Button_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<4>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_Selected(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<17>";
		boolean t_=this.m_touch_box.p_Selected2(bb_input.g_TouchX(0),bb_input.g_TouchY(0));
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_Draw2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<21>";
		bb_graphics.g_DrawImage(this.m_image,this.m_position.m_x,this.m_position.m_y,0);
		bb_std_lang.popErr();
		return 0;
	}
}
class c_Vec2D extends Object{
	float m_x=.0f;
	float m_y=.0f;
	float m_heading=.0f;
	public final int p_Set4(float t_x,float t_y,float t_h){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<23>";
		this.m_x=t_x;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<24>";
		this.m_y=t_y;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<25>";
		this.m_heading=t_h;
		bb_std_lang.popErr();
		return 0;
	}
	public final c_Vec2D m_Vec2D_new(float t_x,float t_y,float t_h){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<19>";
		p_Set4(t_x,t_y,t_h);
		bb_std_lang.popErr();
		return this;
	}
}
class c_TouchBox extends Object{
	c_Vec2D m_position=null;
	float m_width=.0f;
	float m_height=.0f;
	public final c_TouchBox m_TouchBox_new(float t_x,float t_y,float t_width,float t_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<34>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(t_x,t_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<35>";
		this.m_width=t_width;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<36>";
		this.m_height=t_height;
		bb_std_lang.popErr();
		return this;
	}
	public final c_TouchBox m_TouchBox_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<28>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_Selected2(float t_touch_x,float t_touch_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<43>";
		if(t_touch_x>=this.m_position.m_x && t_touch_x<=this.m_position.m_x+this.m_width && t_touch_y>=this.m_position.m_y && t_touch_y<=this.m_position.m_y+this.m_height){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<44>";
			bb_std_lang.popErr();
			return true;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<46>";
			bb_std_lang.popErr();
			return false;
		}
	}
}
class c_Camera extends Object{
	float m_pan_distance=.0f;
	c_Vec2D m_position=null;
	int m_top_limit=0;
	int m_left_limit=0;
	int m_bottom_limit=0;
	int m_right_limit=0;
	public final c_Camera m_Camera_new(int t_game_width,int t_game_height,int t_screen_width,int t_screen_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<13>";
		this.m_pan_distance=10.0f;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<14>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(0.0f,0.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<16>";
		this.m_top_limit=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<17>";
		this.m_left_limit=0;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<18>";
		this.m_bottom_limit=-1*(t_game_height-t_screen_height);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<19>";
		this.m_right_limit=-1*(t_game_width-t_screen_width);
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Reset(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<23>";
		this.m_position.m_x=0.0f;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<24>";
		this.m_position.m_y=0.0f;
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_MoveRight(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<28>";
		bb_std_lang.print("Move Camera Right");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<29>";
		this.m_position.m_x-=m_pan_distance;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<30>";
		if(this.m_position.m_x<(float)(this.m_right_limit)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<31>";
			this.m_position.m_x=(float)(this.m_right_limit);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_MoveLeft(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<36>";
		bb_std_lang.print("Move Camera Left");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<37>";
		this.m_position.m_x+=m_pan_distance;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<38>";
		if(this.m_position.m_x>(float)(this.m_left_limit)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<39>";
			this.m_position.m_x=(float)(this.m_left_limit);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_MoveUp(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<44>";
		bb_std_lang.print("Move Camera Up");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<45>";
		this.m_position.m_y+=m_pan_distance;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<46>";
		if(this.m_position.m_y>(float)(this.m_top_limit)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<47>";
			this.m_position.m_y=(float)(this.m_top_limit);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public final int p_MoveDown(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<52>";
		bb_std_lang.print("Move Camera Down");
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<53>";
		this.m_position.m_y-=m_pan_distance;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<54>";
		if(this.m_position.m_y<(float)(this.m_bottom_limit)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/camera.monkey<55>";
			this.m_position.m_y=(float)(this.m_bottom_limit);
		}
		bb_std_lang.popErr();
		return 0;
	}
}
interface c_IAsyncEventSource{
	public void p_UpdateAsyncEvents();
}
class c_HttpRequest extends Object implements c_IAsyncEventSource{
	public final c_HttpRequest m_HttpRequest_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	BBHttpRequest m__req=null;
	c_IOnHttpRequestComplete m__onComplete=null;
	public final void p_Open(String t_req,String t_url,c_IOnHttpRequestComplete t_onComplete){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<38>";
		if(((m__req)!=null) && m__req.IsRunning()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<38>";
			bb_std_lang.error("HttpRequest in progress");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<40>";
		m__req=(new BBHttpRequest());
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<41>";
		m__onComplete=t_onComplete;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<43>";
		m__req.Open(t_req,t_url);
		bb_std_lang.popErr();
	}
	public final c_HttpRequest m_HttpRequest_new2(String t_req,String t_url,c_IOnHttpRequestComplete t_onComplete){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<34>";
		p_Open(t_req,t_url,t_onComplete);
		bb_std_lang.popErr();
		return this;
	}
	public final void p_Send(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<54>";
		if(!((m__req)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<54>";
			bb_std_lang.error("HttpRequest not open");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<55>";
		if(m__req.IsRunning()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<55>";
			bb_std_lang.error("HttpRequest in progress");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<57>";
		bb_asyncevent.g_AddAsyncEventSource(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<58>";
		m__req.Send();
		bb_std_lang.popErr();
	}
	public final void p_Send2(String t_data,String t_mimeType,String t_encoding){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<62>";
		if(!((m__req)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<62>";
			bb_std_lang.error("HttpRequest not open");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<63>";
		if(m__req.IsRunning()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<63>";
			bb_std_lang.error("HttpRequest in progress");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<65>";
		if((t_mimeType).length()!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<65>";
			m__req.SetHeader("Content-Type",t_mimeType);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<67>";
		bb_asyncevent.g_AddAsyncEventSource(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<68>";
		m__req.SendText(t_data,t_encoding);
		bb_std_lang.popErr();
	}
	public final void p_UpdateAsyncEvents(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<92>";
		if(m__req.IsRunning()){
			bb_std_lang.popErr();
			return;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<93>";
		bb_asyncevent.g_RemoveAsyncEventSource(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<94>";
		m__onComplete.p_OnHttpRequestComplete(this);
		bb_std_lang.popErr();
	}
	public final int p_Status(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<72>";
		if(!((m__req)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<72>";
			bb_std_lang.error("HttpRequest not open");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<73>";
		int t_=m__req.Status();
		bb_std_lang.popErr();
		return t_;
	}
	public final String p_ResponseText(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<77>";
		if(!((m__req)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<77>";
			bb_std_lang.error("HttpRequest not open");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/httprequest.monkey<78>";
		String t_=m__req.ResponseText();
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Stack4 extends Object{
	public final c_Stack4 m_Stack_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_IAsyncEventSource[] m_data=new c_IAsyncEventSource[0];
	int m_length=0;
	public final c_Stack4 m_Stack_new2(c_IAsyncEventSource[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<13>";
		this.m_data=((c_IAsyncEventSource[])bb_std_lang.sliceArray(t_data,0));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<14>";
		this.m_length=bb_std_lang.length(t_data);
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_Equals2(c_IAsyncEventSource t_lhs,c_IAsyncEventSource t_rhs){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<26>";
		boolean t_=t_lhs==t_rhs;
		bb_std_lang.popErr();
		return t_;
	}
	public final boolean p_Contains3(c_IAsyncEventSource t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<64>";
		for(int t_i=0;t_i<m_length;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<65>";
			if(p_Equals2(m_data[t_i],t_value)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<65>";
				bb_std_lang.popErr();
				return true;
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<67>";
		bb_std_lang.popErr();
		return false;
	}
	public final void p_Push10(c_IAsyncEventSource t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<71>";
		if(m_length==bb_std_lang.length(m_data)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<72>";
			m_data=(c_IAsyncEventSource[])bb_std_lang.resize(m_data,m_length*2+10,c_IAsyncEventSource.class);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<74>";
		m_data[m_length]=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<75>";
		m_length+=1;
		bb_std_lang.popErr();
	}
	public final void p_Push11(c_IAsyncEventSource[] t_values,int t_offset,int t_count){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<83>";
		for(int t_i=0;t_i<t_count;t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<84>";
			p_Push10(t_values[t_offset+t_i]);
		}
		bb_std_lang.popErr();
	}
	public final void p_Push12(c_IAsyncEventSource[] t_values,int t_offset){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<79>";
		p_Push11(t_values,t_offset,bb_std_lang.length(t_values)-t_offset);
		bb_std_lang.popErr();
	}
	static c_IAsyncEventSource m_NIL;
	public final void p_Length2(int t_newlength){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<45>";
		if(t_newlength<m_length){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<46>";
			for(int t_i=t_newlength;t_i<m_length;t_i=t_i+1){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<47>";
				m_data[t_i]=m_NIL;
			}
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<49>";
			if(t_newlength>bb_std_lang.length(m_data)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<50>";
				m_data=(c_IAsyncEventSource[])bb_std_lang.resize(m_data,bb_math2.g_Max(m_length*2+10,t_newlength),c_IAsyncEventSource.class);
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<52>";
		m_length=t_newlength;
		bb_std_lang.popErr();
	}
	public final int p_Length(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<56>";
		bb_std_lang.popErr();
		return m_length;
	}
	public final c_IAsyncEventSource p_Get3(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<104>";
		bb_std_lang.popErr();
		return m_data[t_index];
	}
	public final void p_RemoveEach2(c_IAsyncEventSource t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<155>";
		int t_i=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<155>";
		int t_j=m_length;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<156>";
		while(t_i<m_length){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<157>";
			if(!p_Equals2(m_data[t_i],t_value)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<158>";
				t_i+=1;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<159>";
				continue;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<161>";
			int t_b=t_i;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<161>";
			int t_e=t_i+1;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<162>";
			while(t_e<m_length && p_Equals2(m_data[t_e],t_value)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<163>";
				t_e+=1;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<165>";
			while(t_e<m_length){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<166>";
				m_data[t_b]=m_data[t_e];
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<167>";
				t_b+=1;
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<168>";
				t_e+=1;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<170>";
			m_length-=t_e-t_b;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<171>";
			t_i+=1;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<173>";
		t_i=m_length;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<174>";
		while(t_i<t_j){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<175>";
			m_data[t_i]=m_NIL;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/stack.monkey<176>";
			t_i+=1;
		}
		bb_std_lang.popErr();
	}
}
class c_ControlPoint extends Object{
	c_Vec2D m_position=null;
	float m_width=.0f;
	float m_height=.0f;
	boolean m_selected=false;
	float m_heading=.0f;
	c_Image m_image=null;
	public final c_ControlPoint m_ControlPoint_new(float t_x,float t_y,float t_heading,float t_width,float t_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<12>";
		this.m_position=(new c_Vec2D()).m_Vec2D_new(t_x,t_y,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<13>";
		this.m_width=t_width;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<14>";
		this.m_height=t_height;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<15>";
		this.m_selected=false;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<16>";
		this.m_heading=t_heading;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<17>";
		this.m_image=bb_graphics.g_LoadImage("images/control_point.png",1,1);
		bb_std_lang.popErr();
		return this;
	}
	public final c_ControlPoint m_ControlPoint_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<3>";
		bb_std_lang.popErr();
		return this;
	}
	public final int p_Draw2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<22>";
		if(this.m_selected){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<23>";
			bb_graphics.g_SetColor(255.0f,255.0f,128.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<24>";
			bb_graphics.g_DrawCircle(this.m_position.m_x,this.m_position.m_y,20.0f);
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<25>";
			bb_graphics.g_SetColor(255.0f,255.0f,255.0f);
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/control_point.monkey<27>";
		bb_graphics.g_DrawImage2(this.m_image,this.m_position.m_x,this.m_position.m_y,-this.m_position.m_heading,0.2f,0.2f,0);
		bb_std_lang.popErr();
		return 0;
	}
}
class c_Deque extends Object{
	public final c_Deque m_Deque_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_Vec2D[] m__data=new c_Vec2D[4];
	int m__capacity=0;
	int m__last=0;
	public final c_Deque m_Deque_new2(c_Vec2D[] t_arr){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<8>";
		m__data=((c_Vec2D[])bb_std_lang.sliceArray(t_arr,0));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<9>";
		m__capacity=bb_std_lang.length(m__data);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<10>";
		m__last=m__capacity;
		bb_std_lang.popErr();
		return this;
	}
	int m__first=0;
	public final int p_Length(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<31>";
		if(m__last>=m__first){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<31>";
			int t_=m__last-m__first;
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<32>";
		int t_2=m__capacity-m__first+m__last;
		bb_std_lang.popErr();
		return t_2;
	}
	public final void p_Grow(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<135>";
		c_Vec2D[] t_data=new c_Vec2D[m__capacity*2+10];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<136>";
		if(m__first<=m__last){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<137>";
			for(int t_i=m__first;t_i<m__last;t_i=t_i+1){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<138>";
				t_data[t_i-m__first]=m__data[t_i];
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<140>";
			m__last-=m__first;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<141>";
			m__first=0;
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<143>";
			int t_n=m__capacity-m__first;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<144>";
			for(int t_i2=0;t_i2<t_n;t_i2=t_i2+1){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<145>";
				t_data[t_i2]=m__data[m__first+t_i2];
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<147>";
			for(int t_i3=0;t_i3<m__last;t_i3=t_i3+1){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<148>";
				t_data[t_n+t_i3]=m__data[t_i3];
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<150>";
			m__last+=t_n;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<151>";
			m__first=0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<153>";
		m__capacity=bb_std_lang.length(t_data);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<154>";
		m__data=t_data;
		bb_std_lang.popErr();
	}
	public final void p_PushLast(c_Vec2D t_value){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<83>";
		if(p_Length()+1>=m__capacity){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<83>";
			p_Grow();
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<84>";
		m__data[m__last]=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<85>";
		m__last+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<86>";
		if(m__last==m__capacity){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<86>";
			m__last=0;
		}
		bb_std_lang.popErr();
	}
	public final c_Vec2D p_Get3(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<63>";
		if(t_index<0 || t_index>=p_Length()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<63>";
			bb_std_lang.error("Illegal deque index");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<65>";
		c_Vec2D t_=m__data[(t_index+m__first) % m__capacity];
		bb_std_lang.popErr();
		return t_;
	}
	public final boolean p_IsEmpty(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<36>";
		boolean t_=m__first==m__last;
		bb_std_lang.popErr();
		return t_;
	}
	static c_Vec2D m_NIL;
	public final c_Vec2D p_PopFirst(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<91>";
		if(p_IsEmpty()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<91>";
			bb_std_lang.error("Illegal operation on empty deque");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<93>";
		c_Vec2D t_v=m__data[m__first];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<94>";
		m__data[m__first]=m_NIL;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<95>";
		m__first+=1;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<96>";
		if(m__first==m__capacity){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<96>";
			m__first=0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/deque.monkey<97>";
		bb_std_lang.popErr();
		return t_v;
	}
}
class c_GameSelect extends Object{
	String m_game_id="";
	c_TouchBox m_touch_box=null;
	public final c_GameSelect m_GameSelect_new(float t_x,float t_y,float t_width,float t_height,String t_game_id){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<56>";
		this.m_game_id=t_game_id;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<57>";
		this.m_touch_box=(new c_TouchBox()).m_TouchBox_new(t_x,t_y,t_width,t_height);
		bb_std_lang.popErr();
		return this;
	}
	public final c_GameSelect m_GameSelect_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<51>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_Selected(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<61>";
		boolean t_=this.m_touch_box.p_Selected2(bb_input.g_TouchX(0),bb_input.g_TouchY(0));
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_Draw2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<65>";
		bb_graphics.g_SetColor(0.0f,200.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<66>";
		bb_graphics.g_DrawRect(this.m_touch_box.m_position.m_x,this.m_touch_box.m_position.m_y,this.m_touch_box.m_width,this.m_touch_box.m_height);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<67>";
		bb_graphics.g_SetColor(255.0f,255.0f,255.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/user_interface.monkey<68>";
		bb_graphics.g_DrawText(this.m_game_id,this.m_touch_box.m_position.m_x+5.0f,this.m_touch_box.m_position.m_y+5.0f,0.0f,0.0f);
		bb_std_lang.popErr();
		return 0;
	}
}
class c_List3 extends Object{
	public final c_List3 m_List_new(){
		bb_std_lang.pushErr();
		bb_std_lang.popErr();
		return this;
	}
	c_Node5 m__head=((new c_HeadNode3()).m_HeadNode_new());
	public final c_Node5 p_AddLast3(c_GameSelect t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<108>";
		c_Node5 t_=(new c_Node5()).m_Node_new(m__head,m__head.m__pred,t_data);
		bb_std_lang.popErr();
		return t_;
	}
	public final c_List3 m_List_new2(c_GameSelect[] t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		c_GameSelect[] t_=t_data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		int t_2=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
		while(t_2<bb_std_lang.length(t_)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			c_GameSelect t_t=t_[t_2];
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<13>";
			t_2=t_2+1;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<14>";
			p_AddLast3(t_t);
		}
		bb_std_lang.popErr();
		return this;
	}
	public final c_Enumerator p_ObjectEnumerator(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<186>";
		c_Enumerator t_=(new c_Enumerator()).m_Enumerator_new(this);
		bb_std_lang.popErr();
		return t_;
	}
}
class c_Node5 extends Object{
	c_Node5 m__succ=null;
	c_Node5 m__pred=null;
	c_GameSelect m__data=null;
	public final c_Node5 m_Node_new(c_Node5 t_succ,c_Node5 t_pred,c_GameSelect t_data){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<261>";
		m__succ=t_succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<262>";
		m__pred=t_pred;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<263>";
		m__succ.m__pred=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<264>";
		m__pred.m__succ=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<265>";
		m__data=t_data;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node5 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<258>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_HeadNode3 extends c_Node5{
	public final c_HeadNode3 m_HeadNode_new(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<310>";
		super.m_Node_new2();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<311>";
		m__succ=(this);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<312>";
		m__pred=(this);
		bb_std_lang.popErr();
		return this;
	}
}
class c_Node6 extends Object{
	int m_key=0;
	c_Node6 m_right=null;
	c_Node6 m_left=null;
	c_Unit m_value=null;
	int m_color=0;
	c_Node6 m_parent=null;
	public final c_Node6 m_Node_new(int t_key,c_Unit t_value,int t_color,c_Node6 t_parent){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<364>";
		this.m_key=t_key;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<365>";
		this.m_value=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<366>";
		this.m_color=t_color;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<367>";
		this.m_parent=t_parent;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node6 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<361>";
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node6 p_NextNode(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<385>";
		c_Node6 t_node=null;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<386>";
		if((m_right)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<387>";
			t_node=m_right;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<388>";
			while((t_node.m_left)!=null){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<389>";
				t_node=t_node.m_left;
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<391>";
			bb_std_lang.popErr();
			return t_node;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<393>";
		t_node=this;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<394>";
		c_Node6 t_parent=this.m_parent;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<395>";
		while(((t_parent)!=null) && t_node==t_parent.m_right){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<396>";
			t_node=t_parent;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<397>";
			t_parent=t_parent.m_parent;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<399>";
		bb_std_lang.popErr();
		return t_parent;
	}
}
class c_Node7 extends Object{
	int m_key=0;
	c_Node7 m_right=null;
	c_Node7 m_left=null;
	c_UnitType m_value=null;
	int m_color=0;
	c_Node7 m_parent=null;
	public final c_Node7 m_Node_new(int t_key,c_UnitType t_value,int t_color,c_Node7 t_parent){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<364>";
		this.m_key=t_key;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<365>";
		this.m_value=t_value;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<366>";
		this.m_color=t_color;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<367>";
		this.m_parent=t_parent;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Node7 m_Node_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<361>";
		bb_std_lang.popErr();
		return this;
	}
}
class c_MapKeys extends Object{
	c_Map3 m_map=null;
	public final c_MapKeys m_MapKeys_new(c_Map3 t_map){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<503>";
		this.m_map=t_map;
		bb_std_lang.popErr();
		return this;
	}
	public final c_MapKeys m_MapKeys_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<500>";
		bb_std_lang.popErr();
		return this;
	}
	public final c_KeyEnumerator p_ObjectEnumerator(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<507>";
		c_KeyEnumerator t_=(new c_KeyEnumerator()).m_KeyEnumerator_new(m_map.p_FirstNode());
		bb_std_lang.popErr();
		return t_;
	}
}
class c_KeyEnumerator extends Object{
	c_Node6 m_node=null;
	public final c_KeyEnumerator m_KeyEnumerator_new(c_Node6 t_node){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<459>";
		this.m_node=t_node;
		bb_std_lang.popErr();
		return this;
	}
	public final c_KeyEnumerator m_KeyEnumerator_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<456>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_HasNext(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<463>";
		boolean t_=m_node!=null;
		bb_std_lang.popErr();
		return t_;
	}
	public final int p_NextObject(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<467>";
		c_Node6 t_t=m_node;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<468>";
		m_node=m_node.p_NextNode();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/map.monkey<469>";
		bb_std_lang.popErr();
		return t_t.m_key;
	}
}
class c_Enumerator extends Object{
	c_List3 m__list=null;
	c_Node5 m__curr=null;
	public final c_Enumerator m_Enumerator_new(c_List3 t_list){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<326>";
		m__list=t_list;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<327>";
		m__curr=t_list.m__head.m__succ;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Enumerator m_Enumerator_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<323>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_HasNext(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<331>";
		while(m__curr.m__succ.m__pred!=m__curr){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<332>";
			m__curr=m__curr.m__succ;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<334>";
		boolean t_=m__curr!=m__list.m__head;
		bb_std_lang.popErr();
		return t_;
	}
	public final c_GameSelect p_NextObject(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<338>";
		c_GameSelect t_data=m__curr.m__data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<339>";
		m__curr=m__curr.m__succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<340>";
		bb_std_lang.popErr();
		return t_data;
	}
}
class c_Enumerator2 extends Object{
	c_List m__list=null;
	c_Node3 m__curr=null;
	public final c_Enumerator2 m_Enumerator_new(c_List t_list){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<326>";
		m__list=t_list;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<327>";
		m__curr=t_list.m__head.m__succ;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Enumerator2 m_Enumerator_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<323>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_HasNext(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<331>";
		while(m__curr.m__succ.m__pred!=m__curr){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<332>";
			m__curr=m__curr.m__succ;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<334>";
		boolean t_=m__curr!=m__list.m__head;
		bb_std_lang.popErr();
		return t_;
	}
	public final c_Unit p_NextObject(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<338>";
		c_Unit t_data=m__curr.m__data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<339>";
		m__curr=m__curr.m__succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<340>";
		bb_std_lang.popErr();
		return t_data;
	}
}
class c_Enumerator3 extends Object{
	c_List2 m__list=null;
	c_Node4 m__curr=null;
	public final c_Enumerator3 m_Enumerator_new(c_List2 t_list){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<326>";
		m__list=t_list;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<327>";
		m__curr=t_list.m__head.m__succ;
		bb_std_lang.popErr();
		return this;
	}
	public final c_Enumerator3 m_Enumerator_new2(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<323>";
		bb_std_lang.popErr();
		return this;
	}
	public final boolean p_HasNext(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<331>";
		while(m__curr.m__succ.m__pred!=m__curr){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<332>";
			m__curr=m__curr.m__succ;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<334>";
		boolean t_=m__curr!=m__list.m__head;
		bb_std_lang.popErr();
		return t_;
	}
	public final c_Particle p_NextObject(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<338>";
		c_Particle t_data=m__curr.m__data;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<339>";
		m__curr=m__curr.m__succ;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/list.monkey<340>";
		bb_std_lang.popErr();
		return t_data;
	}
}
class bb_Math{
}
class bb_Mojo{
}
class bb_asyncevent{
	static c_Stack4 g__sources;
	public static void g_AddAsyncEventSource(c_IAsyncEventSource t_source){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<14>";
		if(bb_asyncevent.g__sources.p_Contains3(t_source)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<14>";
			bb_std_lang.error("Async event source is already active");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<15>";
		bb_asyncevent.g__sources.p_Push10(t_source);
		bb_std_lang.popErr();
	}
	static c_IAsyncEventSource g__current;
	public static int g_UpdateAsyncEvents(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<24>";
		if((bb_asyncevent.g__current)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<24>";
			bb_std_lang.popErr();
			return 0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<25>";
		int t_i=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<26>";
		while(t_i<bb_asyncevent.g__sources.p_Length()){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<27>";
			bb_asyncevent.g__current=bb_asyncevent.g__sources.p_Get3(t_i);
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<28>";
			bb_asyncevent.g__current.p_UpdateAsyncEvents();
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<29>";
			if((bb_asyncevent.g__current)!=null){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<29>";
				t_i+=1;
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<31>";
		bb_asyncevent.g__current=null;
		bb_std_lang.popErr();
		return 0;
	}
	public static void g_RemoveAsyncEventSource(c_IAsyncEventSource t_source){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<19>";
		if(t_source==bb_asyncevent.g__current){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<19>";
			bb_asyncevent.g__current=null;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/asyncevent.monkey<20>";
		bb_asyncevent.g__sources.p_RemoveEach2(t_source);
		bb_std_lang.popErr();
	}
}
class bb_gametarget{
}
class bb_httprequest{
}
class bb_json{
	public static void g_ThrowError(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/brl/json.monkey<6>";
		throw (new c_JsonError()).m_JsonError_new();
	}
}
class bb_thread{
}
class bb_app{
	static c_App g__app;
	static c_GameDelegate g__delegate;
	static BBGame g__game;
	static int g__devWidth;
	static int g__devHeight;
	public static void g_ValidateDeviceWindow(boolean t_notifyApp){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<57>";
		int t_w=bb_app.g__game.GetDeviceWidth();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<58>";
		int t_h=bb_app.g__game.GetDeviceHeight();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<59>";
		if(t_w==bb_app.g__devWidth && t_h==bb_app.g__devHeight){
			bb_std_lang.popErr();
			return;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<60>";
		bb_app.g__devWidth=t_w;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<61>";
		bb_app.g__devHeight=t_h;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<62>";
		if(t_notifyApp){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<62>";
			bb_app.g__app.p_OnResize();
		}
		bb_std_lang.popErr();
	}
	static c_DisplayMode[] g__displayModes;
	static c_DisplayMode g__desktopMode;
	public static int g_DeviceWidth(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<263>";
		bb_std_lang.popErr();
		return bb_app.g__devWidth;
	}
	public static int g_DeviceHeight(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<267>";
		bb_std_lang.popErr();
		return bb_app.g__devHeight;
	}
	public static void g_EnumDisplayModes(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<33>";
		BBDisplayMode[] t_modes=bb_app.g__game.GetDisplayModes();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<34>";
		c_IntMap t_mmap=(new c_IntMap()).m_IntMap_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<35>";
		c_Stack t_mstack=(new c_Stack()).m_Stack_new();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<36>";
		for(int t_i=0;t_i<bb_std_lang.length(t_modes);t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<37>";
			int t_w=t_modes[t_i].width;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<38>";
			int t_h=t_modes[t_i].height;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<39>";
			int t_size=t_w<<16|t_h;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<40>";
			if(t_mmap.p_Contains(t_size)){
			}else{
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<42>";
				c_DisplayMode t_mode=(new c_DisplayMode()).m_DisplayMode_new(t_modes[t_i].width,t_modes[t_i].height);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<43>";
				t_mmap.p_Insert(t_size,t_mode);
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<44>";
				t_mstack.p_Push(t_mode);
			}
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<47>";
		bb_app.g__displayModes=t_mstack.p_ToArray();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<48>";
		BBDisplayMode t_mode2=bb_app.g__game.GetDesktopMode();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<49>";
		if((t_mode2)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<50>";
			bb_app.g__desktopMode=(new c_DisplayMode()).m_DisplayMode_new(t_mode2.width,t_mode2.height);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<52>";
			bb_app.g__desktopMode=(new c_DisplayMode()).m_DisplayMode_new(bb_app.g_DeviceWidth(),bb_app.g_DeviceHeight());
		}
		bb_std_lang.popErr();
	}
	public static void g_EndApp(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<259>";
		bb_std_lang.error("");
		bb_std_lang.popErr();
	}
	public static int g_Millisecs(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<233>";
		int t_=bb_app.g__game.Millisecs();
		bb_std_lang.popErr();
		return t_;
	}
	static int g__updateRate;
	public static void g_SetUpdateRate(int t_hertz){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<224>";
		bb_app.g__updateRate=t_hertz;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/app.monkey<225>";
		bb_app.g__game.SetUpdateRate(t_hertz);
		bb_std_lang.popErr();
	}
}
class bb_asyncimageloader{
}
class bb_asyncloaders{
}
class bb_asyncsoundloader{
}
class bb_audio{
	static gxtkAudio g_device;
	public static int g_SetAudioDevice(gxtkAudio t_dev){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/audio.monkey<22>";
		bb_audio.g_device=t_dev;
		bb_std_lang.popErr();
		return 0;
	}
}
class bb_audiodevice{
}
class bb_data{
	public static String g_FixDataPath(String t_path){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<7>";
		int t_i=t_path.indexOf(":/",0);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<8>";
		if(t_i!=-1 && t_path.indexOf("/",0)==t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<8>";
			bb_std_lang.popErr();
			return t_path;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<9>";
		if(t_path.startsWith("./") || t_path.startsWith("/")){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<9>";
			bb_std_lang.popErr();
			return t_path;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/data.monkey<10>";
		String t_="monkey://data/"+t_path;
		bb_std_lang.popErr();
		return t_;
	}
}
class bb_driver{
}
class bb_graphics{
	static gxtkGraphics g_device;
	public static int g_SetGraphicsDevice(gxtkGraphics t_dev){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<63>";
		bb_graphics.g_device=t_dev;
		bb_std_lang.popErr();
		return 0;
	}
	static c_GraphicsContext g_context;
	public static c_Image g_LoadImage(String t_path,int t_frameCount,int t_flags){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<239>";
		gxtkSurface t_surf=bb_graphics.g_device.LoadSurface(bb_data.g_FixDataPath(t_path));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<240>";
		if((t_surf)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<240>";
			c_Image t_=((new c_Image()).m_Image_new()).p_Init(t_surf,t_frameCount,t_flags);
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.popErr();
		return null;
	}
	public static c_Image g_LoadImage2(String t_path,int t_frameWidth,int t_frameHeight,int t_frameCount,int t_flags){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<244>";
		gxtkSurface t_surf=bb_graphics.g_device.LoadSurface(bb_data.g_FixDataPath(t_path));
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<245>";
		if((t_surf)!=null){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<245>";
			c_Image t_=((new c_Image()).m_Image_new()).p_Init2(t_surf,0,0,t_frameWidth,t_frameHeight,t_frameCount,t_flags,null,0,0,t_surf.Width(),t_surf.Height());
			bb_std_lang.popErr();
			return t_;
		}
		bb_std_lang.popErr();
		return null;
	}
	public static int g_SetFont(c_Image t_font,int t_firstChar){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<548>";
		if(!((t_font)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<549>";
			if(!((bb_graphics.g_context.m_defaultFont)!=null)){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<550>";
				bb_graphics.g_context.m_defaultFont=bb_graphics.g_LoadImage("mojo_font.png",96,2);
			}
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<552>";
			t_font=bb_graphics.g_context.m_defaultFont;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<553>";
			t_firstChar=32;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<555>";
		bb_graphics.g_context.m_font=t_font;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<556>";
		bb_graphics.g_context.m_firstChar=t_firstChar;
		bb_std_lang.popErr();
		return 0;
	}
	static gxtkGraphics g_renderDevice;
	public static int g_SetMatrix(float t_ix,float t_iy,float t_jx,float t_jy,float t_tx,float t_ty){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<312>";
		bb_graphics.g_context.m_ix=t_ix;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<313>";
		bb_graphics.g_context.m_iy=t_iy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<314>";
		bb_graphics.g_context.m_jx=t_jx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<315>";
		bb_graphics.g_context.m_jy=t_jy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<316>";
		bb_graphics.g_context.m_tx=t_tx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<317>";
		bb_graphics.g_context.m_ty=t_ty;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<318>";
		bb_graphics.g_context.m_tformed=((t_ix!=1.0f || t_iy!=0.0f || t_jx!=0.0f || t_jy!=1.0f || t_tx!=0.0f || t_ty!=0.0f)?1:0);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<319>";
		bb_graphics.g_context.m_matDirty=1;
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_SetMatrix2(float[] t_m){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<308>";
		bb_graphics.g_SetMatrix(t_m[0],t_m[1],t_m[2],t_m[3],t_m[4],t_m[5]);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_SetColor(float t_r,float t_g,float t_b){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<254>";
		bb_graphics.g_context.m_color_r=t_r;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<255>";
		bb_graphics.g_context.m_color_g=t_g;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<256>";
		bb_graphics.g_context.m_color_b=t_b;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<257>";
		bb_graphics.g_renderDevice.SetColor(t_r,t_g,t_b);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_SetAlpha(float t_alpha){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<271>";
		bb_graphics.g_context.m_alpha=t_alpha;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<272>";
		bb_graphics.g_renderDevice.SetAlpha(t_alpha);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_SetBlend(int t_blend){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<280>";
		bb_graphics.g_context.m_blend=t_blend;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<281>";
		bb_graphics.g_renderDevice.SetBlend(t_blend);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_SetScissor(float t_x,float t_y,float t_width,float t_height){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<289>";
		bb_graphics.g_context.m_scissor_x=t_x;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<290>";
		bb_graphics.g_context.m_scissor_y=t_y;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<291>";
		bb_graphics.g_context.m_scissor_width=t_width;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<292>";
		bb_graphics.g_context.m_scissor_height=t_height;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<293>";
		bb_graphics.g_renderDevice.SetScissor((int)(t_x),(int)(t_y),(int)(t_width),(int)(t_height));
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_BeginRender(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<225>";
		bb_graphics.g_renderDevice=bb_graphics.g_device;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<226>";
		bb_graphics.g_context.m_matrixSp=0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<227>";
		bb_graphics.g_SetMatrix(1.0f,0.0f,0.0f,1.0f,0.0f,0.0f);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<228>";
		bb_graphics.g_SetColor(255.0f,255.0f,255.0f);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<229>";
		bb_graphics.g_SetAlpha(1.0f);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<230>";
		bb_graphics.g_SetBlend(0);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<231>";
		bb_graphics.g_SetScissor(0.0f,0.0f,(float)(bb_app.g_DeviceWidth()),(float)(bb_app.g_DeviceHeight()));
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_EndRender(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<235>";
		bb_graphics.g_renderDevice=null;
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DebugRenderDevice(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<53>";
		if(!((bb_graphics.g_renderDevice)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<53>";
			bb_std_lang.error("Rendering operations can only be performed inside OnRender");
		}
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Cls(float t_r,float t_g,float t_b){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<378>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<380>";
		bb_graphics.g_renderDevice.Cls(t_r,t_g,t_b);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawImage(c_Image t_image,float t_x,float t_y,int t_frame){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<452>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<453>";
		if(t_frame<0 || t_frame>=bb_std_lang.length(t_image.m_frames)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<453>";
			bb_std_lang.error("Invalid image frame");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<456>";
		c_Frame t_f=t_image.m_frames[t_frame];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<458>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<460>";
		if((t_image.m_flags&65536)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<461>";
			bb_graphics.g_renderDevice.DrawSurface(t_image.m_surface,t_x-t_image.m_tx,t_y-t_image.m_ty);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<463>";
			bb_graphics.g_renderDevice.DrawSurface2(t_image.m_surface,t_x-t_image.m_tx,t_y-t_image.m_ty,t_f.m_x,t_f.m_y,t_image.m_width,t_image.m_height);
		}
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_PushMatrix(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<333>";
		int t_sp=bb_graphics.g_context.m_matrixSp;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<334>";
		if(t_sp==bb_std_lang.length(bb_graphics.g_context.m_matrixStack)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<334>";
			bb_graphics.g_context.m_matrixStack=(float[])bb_std_lang.resize(bb_graphics.g_context.m_matrixStack,t_sp*2,float.class);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<335>";
		bb_graphics.g_context.m_matrixStack[t_sp+0]=bb_graphics.g_context.m_ix;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<336>";
		bb_graphics.g_context.m_matrixStack[t_sp+1]=bb_graphics.g_context.m_iy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<337>";
		bb_graphics.g_context.m_matrixStack[t_sp+2]=bb_graphics.g_context.m_jx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<338>";
		bb_graphics.g_context.m_matrixStack[t_sp+3]=bb_graphics.g_context.m_jy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<339>";
		bb_graphics.g_context.m_matrixStack[t_sp+4]=bb_graphics.g_context.m_tx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<340>";
		bb_graphics.g_context.m_matrixStack[t_sp+5]=bb_graphics.g_context.m_ty;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<341>";
		bb_graphics.g_context.m_matrixSp=t_sp+6;
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Transform(float t_ix,float t_iy,float t_jx,float t_jy,float t_tx,float t_ty){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<355>";
		float t_ix2=t_ix*bb_graphics.g_context.m_ix+t_iy*bb_graphics.g_context.m_jx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<356>";
		float t_iy2=t_ix*bb_graphics.g_context.m_iy+t_iy*bb_graphics.g_context.m_jy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<357>";
		float t_jx2=t_jx*bb_graphics.g_context.m_ix+t_jy*bb_graphics.g_context.m_jx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<358>";
		float t_jy2=t_jx*bb_graphics.g_context.m_iy+t_jy*bb_graphics.g_context.m_jy;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<359>";
		float t_tx2=t_tx*bb_graphics.g_context.m_ix+t_ty*bb_graphics.g_context.m_jx+bb_graphics.g_context.m_tx;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<360>";
		float t_ty2=t_tx*bb_graphics.g_context.m_iy+t_ty*bb_graphics.g_context.m_jy+bb_graphics.g_context.m_ty;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<361>";
		bb_graphics.g_SetMatrix(t_ix2,t_iy2,t_jx2,t_jy2,t_tx2,t_ty2);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Transform2(float[] t_m){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<351>";
		bb_graphics.g_Transform(t_m[0],t_m[1],t_m[2],t_m[3],t_m[4],t_m[5]);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Translate(float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<365>";
		bb_graphics.g_Transform(1.0f,0.0f,0.0f,1.0f,t_x,t_y);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Rotate(float t_angle){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<373>";
		bb_graphics.g_Transform((float)Math.cos((t_angle)*bb_std_lang.D2R),-(float)Math.sin((t_angle)*bb_std_lang.D2R),(float)Math.sin((t_angle)*bb_std_lang.D2R),(float)Math.cos((t_angle)*bb_std_lang.D2R),0.0f,0.0f);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_Scale(float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<369>";
		bb_graphics.g_Transform(t_x,0.0f,0.0f,t_y,0.0f,0.0f);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_PopMatrix(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<345>";
		int t_sp=bb_graphics.g_context.m_matrixSp-6;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<346>";
		bb_graphics.g_SetMatrix(bb_graphics.g_context.m_matrixStack[t_sp+0],bb_graphics.g_context.m_matrixStack[t_sp+1],bb_graphics.g_context.m_matrixStack[t_sp+2],bb_graphics.g_context.m_matrixStack[t_sp+3],bb_graphics.g_context.m_matrixStack[t_sp+4],bb_graphics.g_context.m_matrixStack[t_sp+5]);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<347>";
		bb_graphics.g_context.m_matrixSp=t_sp;
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawImage2(c_Image t_image,float t_x,float t_y,float t_rotation,float t_scaleX,float t_scaleY,int t_frame){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<470>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<471>";
		if(t_frame<0 || t_frame>=bb_std_lang.length(t_image.m_frames)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<471>";
			bb_std_lang.error("Invalid image frame");
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<474>";
		c_Frame t_f=t_image.m_frames[t_frame];
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<476>";
		bb_graphics.g_PushMatrix();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<478>";
		bb_graphics.g_Translate(t_x,t_y);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<479>";
		bb_graphics.g_Rotate(t_rotation);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<480>";
		bb_graphics.g_Scale(t_scaleX,t_scaleY);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<482>";
		bb_graphics.g_Translate(-t_image.m_tx,-t_image.m_ty);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<484>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<486>";
		if((t_image.m_flags&65536)!=0){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<487>";
			bb_graphics.g_renderDevice.DrawSurface(t_image.m_surface,0.0f,0.0f);
		}else{
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<489>";
			bb_graphics.g_renderDevice.DrawSurface2(t_image.m_surface,0.0f,0.0f,t_f.m_x,t_f.m_y,t_image.m_width,t_image.m_height);
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<492>";
		bb_graphics.g_PopMatrix();
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawText(String t_text,float t_x,float t_y,float t_xalign,float t_yalign){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<577>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<579>";
		if(!((bb_graphics.g_context.m_font)!=null)){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<579>";
			bb_std_lang.popErr();
			return 0;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<581>";
		int t_w=bb_graphics.g_context.m_font.p_Width();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<582>";
		int t_h=bb_graphics.g_context.m_font.p_Height();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<584>";
		t_x-=(float)Math.floor((float)(t_w*t_text.length())*t_xalign);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<585>";
		t_y-=(float)Math.floor((float)(t_h)*t_yalign);
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<587>";
		for(int t_i=0;t_i<t_text.length();t_i=t_i+1){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<588>";
			int t_ch=(int)t_text.charAt(t_i)-bb_graphics.g_context.m_firstChar;
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<589>";
			if(t_ch>=0 && t_ch<bb_graphics.g_context.m_font.p_Frames()){
				bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<590>";
				bb_graphics.g_DrawImage(bb_graphics.g_context.m_font,t_x+(float)(t_i*t_w),t_y,t_ch);
			}
		}
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawRect(float t_x,float t_y,float t_w,float t_h){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<393>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<395>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<396>";
		bb_graphics.g_renderDevice.DrawRect(t_x,t_y,t_w,t_h);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawCircle(float t_x,float t_y,float t_r){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<417>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<419>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<420>";
		bb_graphics.g_renderDevice.DrawOval(t_x-t_r,t_y-t_r,t_r*2.0f,t_r*2.0f);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawPoint(float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<385>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<387>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<388>";
		bb_graphics.g_renderDevice.DrawPoint(t_x,t_y);
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_DrawLine(float t_x1,float t_y1,float t_x2,float t_y2){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<401>";
		bb_graphics.g_DebugRenderDevice();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<403>";
		bb_graphics.g_context.p_Validate();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/graphics.monkey<404>";
		bb_graphics.g_renderDevice.DrawLine(t_x1,t_y1,t_x2,t_y2);
		bb_std_lang.popErr();
		return 0;
	}
}
class bb_graphicsdevice{
}
class bb_input{
	static c_InputDevice g_device;
	public static int g_SetInputDevice(c_InputDevice t_dev){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<22>";
		bb_input.g_device=t_dev;
		bb_std_lang.popErr();
		return 0;
	}
	public static int g_GetChar(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<48>";
		int t_=bb_input.g_device.p_GetChar();
		bb_std_lang.popErr();
		return t_;
	}
	public static int g_DisableKeyboard(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<36>";
		int t_=bb_input.g_device.p_SetKeyboardEnabled(false);
		bb_std_lang.popErr();
		return t_;
	}
	public static int g_EnableKeyboard(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<32>";
		int t_=bb_input.g_device.p_SetKeyboardEnabled(true);
		bb_std_lang.popErr();
		return t_;
	}
	public static int g_TouchDown(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<84>";
		int t_=((bb_input.g_device.p_KeyDown(384+t_index))?1:0);
		bb_std_lang.popErr();
		return t_;
	}
	public static float g_TouchX(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<76>";
		float t_=bb_input.g_device.p_TouchX(t_index);
		bb_std_lang.popErr();
		return t_;
	}
	public static float g_TouchY(int t_index){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<80>";
		float t_=bb_input.g_device.p_TouchY(t_index);
		bb_std_lang.popErr();
		return t_;
	}
	public static int g_KeyHit(int t_key){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/mojo/input.monkey<44>";
		int t_=bb_input.g_device.p_KeyHit(t_key);
		bb_std_lang.popErr();
		return t_;
	}
}
class bb_inputdevice{
}
class bb_keycodes{
}
class bb_boxes{
}
class bb_deque{
}
class bb_lang{
}
class bb_list{
}
class bb_map{
}
class bb_math2{
	public static int g_Min(int t_x,int t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<51>";
		if(t_x<t_y){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<51>";
			bb_std_lang.popErr();
			return t_x;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<52>";
		bb_std_lang.popErr();
		return t_y;
	}
	public static float g_Min2(float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<78>";
		if(t_x<t_y){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<78>";
			bb_std_lang.popErr();
			return t_x;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<79>";
		bb_std_lang.popErr();
		return t_y;
	}
	public static int g_Max(int t_x,int t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<56>";
		if(t_x>t_y){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<56>";
			bb_std_lang.popErr();
			return t_x;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<57>";
		bb_std_lang.popErr();
		return t_y;
	}
	public static float g_Max2(float t_x,float t_y){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<83>";
		if(t_x>t_y){
			bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<83>";
			bb_std_lang.popErr();
			return t_x;
		}
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/math.monkey<84>";
		bb_std_lang.popErr();
		return t_y;
	}
}
class bb_monkey{
}
class bb_random{
	static int g_Seed;
	public static float g_Rnd(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/random.monkey<21>";
		bb_random.g_Seed=bb_random.g_Seed*1664525+1013904223|0;
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/random.monkey<22>";
		float t_=(float)(bb_random.g_Seed>>8&16777215)/16777216.0f;
		bb_std_lang.popErr();
		return t_;
	}
	public static float g_Rnd2(float t_low,float t_high){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/random.monkey<30>";
		float t_=bb_random.g_Rnd3(t_high-t_low)+t_low;
		bb_std_lang.popErr();
		return t_;
	}
	public static float g_Rnd3(float t_range){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Applications/MonkeyXPro86e/modules/monkey/random.monkey<26>";
		float t_=bb_random.g_Rnd()*t_range;
		bb_std_lang.popErr();
		return t_;
	}
}
class bb_set{
}
class bb_stack{
}
class bb_monkeytarget{
}
class bb_camera{
}
class bb_control_point{
}
class bb_dronetournament{
	public static c_Vec2D g_NewPoint(c_Vec2D t_start_point,float t_start_angle,float t_goal_angle,float t_max_angle_change,float t_distance){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<31>";
		float t_new_angle=0.0f;
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<33>";
		if(t_start_angle<0.0f){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<34>";
			t_start_angle=t_start_angle % 360.0f+360.0f;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<36>";
			t_start_angle=t_start_angle % 360.0f;
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<39>";
		if(t_goal_angle<0.0f){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<40>";
			t_goal_angle=t_goal_angle % 360.0f+360.0f;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<42>";
			t_goal_angle=t_goal_angle % 360.0f;
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<46>";
		if(t_start_angle>t_goal_angle){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<47>";
			if(t_start_angle-t_goal_angle<180.0f){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<48>";
				t_new_angle=t_start_angle-bb_math2.g_Min2(t_start_angle-t_goal_angle,t_max_angle_change);
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<50>";
				t_new_angle=t_start_angle+t_max_angle_change;
			}
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<52>";
			if(t_start_angle<t_goal_angle){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<53>";
				if(t_goal_angle-t_start_angle<180.0f){
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<54>";
					t_new_angle=t_start_angle+bb_math2.g_Min2(t_goal_angle-t_start_angle,t_max_angle_change);
				}else{
					bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<56>";
					t_new_angle=t_start_angle-t_max_angle_change;
				}
			}else{
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<59>";
				t_new_angle=t_start_angle;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<62>";
		c_Vec2D t_=(new c_Vec2D()).m_Vec2D_new(t_start_point.m_x+t_distance*(float)Math.cos(t_new_angle*0.017453292500000002f),t_start_point.m_y+t_distance*(float)Math.sin(t_new_angle*0.017453292500000002f),t_new_angle);
		bb_std_lang.popErr();
		return t_;
	}
	public static boolean g_CounterClockwise(c_Vec2D t_pointOne,c_Vec2D t_pointTwo,c_Vec2D t_pointThree){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<67>";
		boolean t_=(t_pointThree.m_y-t_pointOne.m_y)*(t_pointTwo.m_x-t_pointOne.m_x)>(t_pointTwo.m_y-t_pointOne.m_y)*(t_pointThree.m_x-t_pointOne.m_x);
		bb_std_lang.popErr();
		return t_;
	}
	public static boolean g_LinesIntersect(c_Vec2D t_pointA,c_Vec2D t_pointB,c_Vec2D t_pointC,c_Vec2D t_pointD){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<73>";
		boolean t_abc=bb_dronetournament.g_CounterClockwise(t_pointA,t_pointB,t_pointC);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<74>";
		boolean t_abd=bb_dronetournament.g_CounterClockwise(t_pointA,t_pointB,t_pointD);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<75>";
		boolean t_cda=bb_dronetournament.g_CounterClockwise(t_pointC,t_pointD,t_pointA);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<76>";
		boolean t_cdb=bb_dronetournament.g_CounterClockwise(t_pointC,t_pointD,t_pointB);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<78>";
		boolean t_=t_abc!=t_abd && t_cda!=t_cdb;
		bb_std_lang.popErr();
		return t_;
	}
	public static int g_Collided(c_Particle t_particle,c_Unit t_unit){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<83>";
		if(t_unit.m_armor<=0){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<84>";
			bb_std_lang.popErr();
			return 0;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<85>";
			if(t_particle.m_team==t_unit.m_team){
				bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<86>";
				bb_std_lang.popErr();
				return 0;
			}
		}
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<89>";
		c_Vec2D t_top_left=(new c_Vec2D()).m_Vec2D_new(t_unit.m_position.m_x-10.0f,t_unit.m_position.m_y-10.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<90>";
		c_Vec2D t_top_right=(new c_Vec2D()).m_Vec2D_new(t_unit.m_position.m_x+10.0f,t_unit.m_position.m_y-10.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<91>";
		c_Vec2D t_bottom_left=(new c_Vec2D()).m_Vec2D_new(t_unit.m_position.m_x-10.0f,t_unit.m_position.m_y+10.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<92>";
		c_Vec2D t_bottom_right=(new c_Vec2D()).m_Vec2D_new(t_unit.m_position.m_x+10.0f,t_unit.m_position.m_y+10.0f,0.0f);
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<96>";
		if(bb_dronetournament.g_LinesIntersect(t_particle.m_past_position,t_particle.m_position,t_top_left,t_top_right) || bb_dronetournament.g_LinesIntersect(t_particle.m_past_position,t_particle.m_position,t_top_left,t_bottom_left) || bb_dronetournament.g_LinesIntersect(t_particle.m_past_position,t_particle.m_position,t_top_right,t_bottom_right) || bb_dronetournament.g_LinesIntersect(t_particle.m_past_position,t_particle.m_position,t_bottom_right,t_bottom_left)){
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<98>";
			bb_std_lang.popErr();
			return 1;
		}else{
			bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/dronetournament.monkey<100>";
			bb_std_lang.popErr();
			return 0;
		}
	}
}
class bb_game{
}
class bb_{
	public static int bbMain(){
		bb_std_lang.pushErr();
		bb_std_lang.errInfo="/Users/tcooper/projects/monkeygames/dronetournament/main.monkey<586>";
		(new c_DroneTournamentGame()).m_DroneTournamentGame_new();
		bb_std_lang.popErr();
		return 0;
	}
	public static int bbInit(){
		bb_app.g__app=null;
		bb_app.g__delegate=null;
		bb_app.g__game=BBGame.Game();
		bb_graphics.g_device=null;
		bb_graphics.g_context=(new c_GraphicsContext()).m_GraphicsContext_new();
		c_Image.m_DefaultFlags=0;
		bb_audio.g_device=null;
		bb_input.g_device=null;
		bb_app.g__devWidth=0;
		bb_app.g__devHeight=0;
		bb_app.g__displayModes=new c_DisplayMode[0];
		bb_app.g__desktopMode=null;
		bb_graphics.g_renderDevice=null;
		bb_app.g__updateRate=0;
		c_JsonString.m__null=(new c_JsonString()).m_JsonString_new("");
		c_JsonNumber.m__zero=(new c_JsonNumber()).m_JsonNumber_new("0");
		c_JsonBool.m__true=(new c_JsonBool()).m_JsonBool_new(true);
		c_JsonBool.m__false=(new c_JsonBool()).m_JsonBool_new(false);
		c_JsonNull.m__instance=(new c_JsonNull()).m_JsonNull_new();
		bb_asyncevent.g__sources=(new c_Stack4()).m_Stack_new();
		bb_random.g_Seed=1234;
		c_Deque.m_NIL=null;
		bb_asyncevent.g__current=null;
		c_Stack4.m_NIL=null;
		return 0;
	}
}
class bb_particle{
}
class bb_server{
}
class bb_unit{
}
class bb_unit_type{
}
class bb_user{
}
class bb_user_interface{
}
//${TRANSCODE_END}
