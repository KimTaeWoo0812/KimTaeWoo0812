package duclass.duclass.a3_login_join;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a4_mainpage.MainPageActivity;

public class RoadingSplash extends Activity {
	CSharedPreferences sharedPreferences;
	int isLogin ;
	Context mContext = this;
	@Override
	protected void onResume() {
		super.onResume();


	}

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_roading);

		SC._del+=(char)200;
		SC._endDel+=(char)201;
		SC._endSendDel+=(char)202;
		TCP_SC.SetData();
        CUserInfo.commentNum = 100; //터짐 방지

		sharedPreferences = new CSharedPreferences(this);


		isLogin = sharedPreferences.IsLogin();


        Log.i("login","test1");
		Handler hd = new Handler();
		hd.postDelayed(new Runnable() {

			@Override
			public void run() {
                Log.i("login", "isLogin " + isLogin);
				new SendMsgTask().execute("1");
                if(isLogin == 1)
				    serverCheck.showLoading(mContext);
                Log.i("login", "test3");
			}
		}, 1200);

	}

	public class SendMsgTask extends AsyncTask<String, Void, String> {
		String msg = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {


			if (isLogin == 1)// 이미 로그인 했다면 다음 엑티비티로 넘어간다.
			{
                AndroidSocket.socket = AndroidSocket.shared();
                Log.i("login","test");
                CUserInfo.id = sharedPreferences.getPreferences("id");
                CUserInfo.name = sharedPreferences.getPreferences("name");


                AndroidSocket.id = CUserInfo.id;

                msg = "NEW_LOGIn" + SC._del + CUserInfo.id + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (AndroidSocket.socket.HasMessage()){
                    if(serverCheck.canNotGet){
                        serverCheck.canNotGet=false;
                        return "9";
                    }
                }
                serverCheck.hideLoading();
                msg = AndroidSocket.socket.GetMessage();// 서버로부터 받기

                return "1";
			}
            else{
                Log.i("login", "두번째");
                Log.i("login", "두번째2");
                return "2";
            }
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
            Log.i("login", "onPostExecute");
            Log.i("login", "onPostExecute2: "+result);

            if (result.compareTo("1") == 0) {
                AndroidSocket.socket.CloseSocket();
                String arrMsg[] = msg.split(SC._del);


                if(arrMsg[1].compareTo("9999999") == 0){ // 로그인 실패
                    Intent intent2 = new Intent(RoadingSplash.this,Join_new_Activity.class);
                    startActivity(intent2);
                    finish();// 이 화면 종료
                }

                CUserInfo.commentNum = Integer.parseInt(arrMsg[1]);

                Intent intent5 = new Intent(RoadingSplash.this,MainPageActivity.class);
                startActivity(intent5);
                finish();// 이 화면 종료

            }
            else  if (result.compareTo("2") == 0){
                Log.i("login", "result2: "+result);
                Intent intent2 = new Intent(RoadingSplash.this,Join_new_Activity.class);
                startActivity(intent2);
                finish();// 이 화면 종료
            }
			else if (result.compareTo("9") == 0)//연결 실패
			{
                AndroidSocket.socket.CloseSocket();
				DialogSimple();
			}
		}

	}
	//다이얼로그
	private void DialogSimple(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("다시 시도하시겠습니까?").setCancelable(
				false).setPositiveButton("취소",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do something
						finish();// 이 화면 종료
						dialog.cancel();
					}
				}).setNegativeButton("다시 시도",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Intent intent5 = new Intent(RoadingSplash.this,
								RoadingSplash.class);
						startActivity(intent5);
						finish();// 이 화면 종료

						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("서버와 연결 실패");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.ic_launcher);
		alert.show();
	}
}




//
//public class RoadingSplash extends Activity {
//	public AndroidSocket socket;
//	CSharedPreferences sharedPreferences;
//	String id = "";
//	String pw = "";
//	int isLogin ;
//	Context mContext = this;
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//
//	}
//
//	@Override
//	protected void onCreate(Bundle saveInstanceState) {
//		super.onCreate(saveInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_roading);
//
//		SC._del+=(char)200;
//		SC._endDel+=(char)201;
//		SC._endSendDel+=(char)202;
//		TCP_SC.SetData();
//
//		sharedPreferences = new CSharedPreferences(this);
//		socket = AndroidSocket.shared();
//
//
//		isLogin = sharedPreferences.IsLogin();
//
//
//		//servercheck.showLoading(this);
//		 Handler hd = new Handler();
//		 hd.postDelayed(new Runnable() {
//
//		 @Override
//		 public void run() {
//			 new SendMsgTask().execute("1");
//			 serverCheck.showLoading(mContext);
//		 }
//		 }, 1200);
//
//	}
//	private String SendLoginMsg() {
//
//		String strMsg = "LOGIN" + SC._del + id + SC._del + pw + SC._del;
//
//		try {
//			AndroidSocket.socket.SendMessage(strMsg);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		while (socket.HasMessage()){
//			if(serverCheck.canNotGet){
//				serverCheck.canNotGet=false;
//				return "9";
//			}
//		}
//		//servercheck.hideLoading();
//		// servercheck.hideLoading();
//		serverCheck.hideLoading();
//		strMsg = socket.GetMessage();// 서버로부터 받기
//		String strTemp[]=strMsg.split(SC._del);
//
//		if(!strTemp[1].equals("2"))
//			CUserInfo.name=strTemp[2];
//
//		return strMsg;
//	}
//	/**
//	 * 리턴 1은 정상, 2는 실패
//	 * @return
//	 */
//	private int RoadingMsg() {
//		String strMsg = "ROADING" + SC._del;
//
//		try {
//			AndroidSocket.socket.SendMessage(strMsg);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		while (AndroidSocket.socket.HasMessage()){
//			if(serverCheck.canNotGet){
//				serverCheck.canNotGet=false;
//				return 0;
//			}
//		}
//		serverCheck.hideLoading();
//		strMsg = AndroidSocket.socket.GetMessage();// 서버로부터 받기
//
//
//
//
//		return 1;
//	}
//
//	public class SendMsgTask extends AsyncTask<String, Void, String> {
//		String msg = "";
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			AndroidSocket.socket = AndroidSocket.shared();
//			if (isLogin == 1)// 이미 로그인 했다면 다음 엑티비티로 넘어간다.
//			{
//				id = sharedPreferences.getPreferences("id");
//				pw = "1";
//
//
//				socket.id = id;
//				CUserInfo.id = id;
//
//				String msg[] = SendLoginMsg().split(SC._del);
//
//				CUserInfo.commentNum = Integer.parseInt(msg[3]);
//
//				Log.i("###", ""+msg[1]);
//				if(msg[1].equals("2")){
//					Log.i("###22", ""+msg[1]);
//					return "2";
//				}
//				if(msg[1].compareTo("9") == 0)
//					return "9";//연결실패
//				else
//					return "1";//이미로그인
//			}
//
//
//
//			int temp = RoadingMsg();
//			{
//				if(temp==1)
//					return "2";//로그인 페이지로
//				else
//					return "9";//연결 실패
//			}
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			AndroidSocket.socket.CloseSocket();
//			Log.i("###3", "" + result);
//			if (result.compareTo("9") == 0)//연결 실패
//			{
//				DialogSimple();
//			}
//
//			else if (isLogin == 1 && !result.equals("2"))// 이미 로그인 했다면 다음 엑티비티로 넘어간다.
//			{
//
//				// servercheck.showLoading(this);
//
//
//
//
//				Intent intent5 = new Intent(RoadingSplash.this,
//						MainPageActivity.class);
//				startActivity(intent5);
//				finish();// 이 화면 종료
//
//				// new SendMsgTask().execute("2");
//
//			} else {
//				Intent intent5 = new Intent(RoadingSplash.this,
//						MainActivity.class);
//				startActivity(intent5);
//				finish();// 이 화면 종료
//
//			}
//		}
//
//	}
//	//다이얼로그
//		private void DialogSimple(){
//		    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
//		    alt_bld.setMessage("다시 시도하시겠습니까?").setCancelable(
//		        false).setPositiveButton("취소",
//		        new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int id) {
//		            // do something
//		        	finish();// 이 화면 종료
//		            dialog.cancel();
//		        }
//		        }).setNegativeButton("다시 시도",
//		        new DialogInterface.OnClickListener() {
//		        public void onClick(DialogInterface dialog, int id) {
//
//		        	Intent intent5 = new Intent(RoadingSplash.this,
//		        			RoadingSplash.class);
//					startActivity(intent5);
//					finish();// 이 화면 종료
//
//		            dialog.cancel();
//		        }
//		        });
//		    AlertDialog alert = alt_bld.create();
//		    // Title for AlertDialog
//		    alert.setTitle("서버와 연결 실패");
//		    // Icon for AlertDialog
//		    alert.setIcon(R.drawable.ic_launcher);
//		    alert.show();
//		}
//}
