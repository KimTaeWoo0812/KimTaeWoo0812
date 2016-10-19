package duclass.duclass.a4_mainpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a12_chatting.chatting_Activity;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.BackPressCloseHandler;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a5_search_make.SearchPageActivity;
import duclass.duclass.a7_opinion_option.Option;
import duclass.duclass.a8_ranking.RankingActivity;
import duclass.duclass.a9_restaurant_list.Restaurant_Activity;


public class MainPageActivity extends Activity {
	CSharedPreferences sharedPreferences;
	private BackPressCloseHandler backPressCloseHandler;
	
	@Override
	protected void onResume() {
		super.onResume();

		if(CUserInfo.commentNum < SC.commentNum_Limt)
			Dialog_for_commentNum();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());//�������� �̤�
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_page);
		backPressCloseHandler = new BackPressCloseHandler(this);

		sharedPreferences = new CSharedPreferences(this);

		Toast.makeText(this, "친구와 공유할수록 이 앱의 가치는 높아집니다", Toast.LENGTH_SHORT).show();

		(new SetList()).execute(new String[]{"1"});//���� �ޱ�
		serverCheck.showLoading(this);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		backPressCloseHandler.onBackPressed();
		//moveTaskToBack(true);
	}
	

	public void onClick(View view) {

		if(SC._del.equals("")){
			SC.SetDel();
		}


		Intent intent;
		switch (view.getId()) {
		case R.id.Btn1://강의 조회
			intent = new Intent(MainPageActivity.this, SearchPageActivity.class);
			startActivity(intent);
			break;
		case R.id.Btn_restaurant: //식당 평가
			intent = new Intent(MainPageActivity.this, Restaurant_Activity.class);
			startActivity(intent);
			break;
		case R.id.Btn2: //강의 랭킹
			intent = new Intent(MainPageActivity.this, RankingActivity.class);
			startActivity(intent);
			break;

			case R.id.btn_review: //채팅
				intent = new Intent(MainPageActivity.this, chatting_Activity.class);
				startActivity(intent);

				break;

		case R.id.Btn3: //설정
			intent = new Intent(MainPageActivity.this, Option.class);
			startActivity(intent);
			
			break;
		}
		
	}
	
	
	
	
	
	
	public class SetList extends AsyncTask<String, Void, String> {
		String strTemp = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			AndroidSocket.socket = AndroidSocket.shared();
			String type = params[0];
			if (type.compareTo("1") == 0) {

	        	String check = sharedPreferences.getPreferences("order");
	        	if(check.compareTo("1") == 0)
	        		return "9";//�ƹ��͵� ���ϱ�
				
				String strTemp2 = "NOTICE" + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while (AndroidSocket.socket.HasMessage()){
					if(serverCheck.canNotGet)
						return "9";
				}
				serverCheck.hideLoading();
				strTemp = AndroidSocket.socket.GetMessage();// �����κ��� �ޱ�
			
				return "1";
			}
			
			

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			if (result.compareTo("1") == 0) {
				String temp[] = strTemp.split(SC._del);
				Log.i("#","#"+temp[0]);
				Log.i("#","#"+temp[1]);
				if (temp[1].compareTo("1") == 0) {
				
					//temp[2]�� ������ ����ִ�. ���⼭ �ٿ�� �ȴ�.
					//DialogSimple(temp[2]);
					new AlertDialog.Builder(MainPageActivity.this).
					setTitle("공지사항").setMessage(temp[2]).setNeutralButton("닫기", null).show();
				}
			}
			
		}

	}
	
	//���̾�α�
	private void DialogSimple(String msg){
	    AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
	    alt_bld.setMessage(msg).setCancelable(
	        false).setPositiveButton("닫기",
	        new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            // do something
	            dialog.cancel();
	        }
	        }).setNegativeButton("다시보지않기",
	        new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            
	        	
	        	sharedPreferences.savePreferences("order", "1");
	        	
	        	
	        	
	            dialog.cancel();
	        }
	        });
	    AlertDialog alert = alt_bld.create();
	    // Title for AlertDialog
	    alert.setTitle("공지사항");
	    // Icon for AlertDialog
	    alert.setIcon(R.drawable.ic_launcher);
	    alert.show();
	}
	private void Dialog_for_commentNum(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("강의 후기를 3개 이상 작성해야 다른사람의 후기를 볼 수 있습니다. 현재 " + CUserInfo.commentNum+ "개 작성하셨습니다.").setCancelable(
				false).setPositiveButton("확인",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do something
						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("알림");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon_50);
		alert.show();
	}


}