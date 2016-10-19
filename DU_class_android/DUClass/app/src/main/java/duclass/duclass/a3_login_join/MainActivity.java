package duclass.duclass.a3_login_join;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.BackPressCloseHandler;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a4_mainpage.MainPageActivity;


public class MainActivity extends Activity {
	int check = 0;
	CheckBox cb1;
	CSharedPreferences sharedPreferences;
	EditText editId;
	EditText editPW;
	String id;
	String pw;
	SendMsgTask sendMsgTask;
	boolean exit = true;
	byte[] cipher;
	private BackPressCloseHandler backPressCloseHandler;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		backPressCloseHandler.onBackPressed();
		//moveTaskToBack(true);
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (exit) {
			android.os.Process.killProcess(android.os.Process.myPid());// 완전종료
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

		backPressCloseHandler = new BackPressCloseHandler(this);

		editId = (EditText) findViewById(R.id.text1);
		editPW = (EditText) findViewById(R.id.text2);
		sharedPreferences = new CSharedPreferences(this);
		
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb1.setChecked(true);
	}


	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.Btn1:// 로그인 버튼
			id = editId.getText().toString();
			pw = editPW.getText().toString();

			
			if (id.compareTo("") == 0) {
				Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
				break;
			}
			if (pw.compareTo("") == 0) {
				Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
				break;
			}
			id = id.replace("\n","");
			pw = pw.replace("\n","");
			
			new SendMsgTask().execute("1");
			serverCheck.showLoading(this);
			break;

		case R.id.Btn2:// 회원가입
			Intent intent2 = new Intent(this, Join_Us.class);
			startActivity(intent2);
			// finish();//이 화면 종료
			break;
		}
	}

	private String SendLoginMsg() {

		// 여기에 서버로 id 비번 보내고 로그인 가능한지 받는다
		String strMsg = "LOGIN" + SC._del + id + SC._del + pw + SC._del;

		try {
			AndroidSocket.socket.SendMessage(strMsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 로그인 정보 전송
		strMsg = "";
		
		while (AndroidSocket.socket.HasMessage()){
			if(duclass.duclass.a2_static_class.serverCheck.canNotGet){
				duclass.duclass.a2_static_class.serverCheck.canNotGet=false;
				return "9";
			}
		}
		serverCheck.hideLoading();
		strMsg = AndroidSocket.socket.GetMessage();// 서버로부터 받기
		Log.i("test: SendLoginMsg",""+strMsg);

		return strMsg;
	}

	public class SendMsgTask extends AsyncTask<String, Void, String> {
		String msg = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			AndroidSocket.socket = AndroidSocket.shared();
			String type = params[0];
			if (type.compareTo("1") == 0) {
				msg = SendLoginMsg();
				if(msg.compareTo("9") == 0)
					return "9";
				else
					return "1";
			}


			else if(type.compareTo("2") == 0){
				
				id = sharedPreferences.getPreferences("id");
				pw = "1";
				
				// 아이디를 가져옴. 내부 DB에서
				// 다음 엑티비티에 보낸다

				AndroidSocket.socket.id = id;
				CUserInfo.id = id;
				
				msg = SendLoginMsg();
				
				
				return "2";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			String arrMsg[] = msg.split(SC._del);
			if (result.compareTo("1") == 0) {


				Log.i("test: msg",""+msg);
				Log.i("test: arrMsg[1]",""+arrMsg[1]);
				//LOGINÈ1ÈasdddÈ3ÈÈÊ
				AndroidSocket.socket.CloseSocket();
				if (arrMsg[1].compareTo("1") == 0)// 1을 받으면 로그인 성공, 아니면 실패
				{

					CUserInfo.commentNum = Integer.parseInt(arrMsg[3]);
					if (cb1.isChecked())// 내부 디비에 넣는다.
					{
						sharedPreferences.savePreferences("id", id);
						sharedPreferences.savePreferences("isLogin", "1");
					}

					AndroidSocket.socket.id = id;
					CUserInfo.id = id;
					CUserInfo.name = arrMsg[2];
					// 다음 엑티비티에 보낸다
					Intent intent5 = new Intent(MainActivity.this, MainPageActivity.class);
					// intent5.putExtra("id", id);
					startActivity(intent5);
					exit = false;
					finish();// 이 화면 종료

				}
				else if (arrMsg[1].compareTo("2") == 0) {
					Toast.makeText(MainActivity.this, "없는 아이디 입니다.",
							Toast.LENGTH_SHORT).show();
				} else if (arrMsg[1].compareTo("4") == 0) {
					Toast.makeText(MainActivity.this, "이미 접속한 회원 입니다.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "틀린 비밀번호 입니다.",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (result.compareTo("2") == 0) {

				CUserInfo.commentNum = Integer.parseInt(arrMsg[3]);
				Intent intent5 = new Intent(MainActivity.this, MainPageActivity.class);
				startActivity(intent5);
				exit = false;
				finish();// 이 화면 종료
				
				
			}

		}

	}

}