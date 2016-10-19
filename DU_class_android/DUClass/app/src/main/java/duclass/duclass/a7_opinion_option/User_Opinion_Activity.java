package duclass.duclass.a7_opinion_option;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;


public class User_Opinion_Activity extends Activity {

	EditText text;
	String msg = "";
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_opinion);
		
		
		text = (EditText) findViewById(R.id.Text1);

		//text.setText("");
	}
	
	public void onClick(View view) {// ��ư ��������
		switch (view.getId()) {
		case R.id.Btn1:// ����
			msg= text.getText().toString();
			new SendMsgTask().execute( );
		}
	}
	

	
	
	public class SendMsgTask extends AsyncTask<String, Void, String> {
		String strTemp = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			AndroidSocket.socket = AndroidSocket.shared();
			strTemp = "OPINOION" + SC._del + CUserInfo.id + SC._del + CUserInfo.name + SC._del + msg + SC._del;
			
			
			try {
				AndroidSocket.socket.SendMessage(strTemp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			text.setText("");
			Toast.makeText(User_Opinion_Activity.this, "전송 완료!",Toast.LENGTH_SHORT).show();
		}

	}
	
}
