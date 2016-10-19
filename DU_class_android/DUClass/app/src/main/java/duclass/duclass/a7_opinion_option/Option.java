package duclass.duclass.a7_opinion_option;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;


public class Option extends Activity {
//	CheckBox cb1;
	CSharedPreferences sharedPreferences;
	String id;
	TextView editId;
	EditText name;
	String newName="";
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);
        sharedPreferences = new CSharedPreferences(this);
//        cb1=(CheckBox)findViewById(R.id.checkBox1);
        Intent intent=getIntent();
        id= CUserInfo.id;

		editId = (TextView) findViewById(R.id.textView1);
		name = (EditText) findViewById(R.id.name2);
		editId.setText(id);
		name.setText(CUserInfo.name);
        

//        int isLogin=sharedPreferences.IsLogin();
//        if(isLogin==1)
//        	cb1.setChecked(true);
	}
	
	@Override
    public void onDestroy()
    {
//		if(cb1.isChecked())
//		{
//			sharedPreferences.savePreferences("id",id);
//			sharedPreferences.savePreferences("isLogin","1");
//		}
//		else if(!cb1.isChecked())
//		{
//			sharedPreferences.savePreferences("isLogin","");
//		}
		
		super.onDestroy();
    }
	
	
	
	 public void onClick(View view) {//��ư ��������
	        switch (view.getId()) {
	        	case R.id.Btn1://�����ڿ��� �ǰ� ���� �ϱ�
	        		Intent intent = new Intent(this, User_Opinion_Activity.class);
	        		startActivity(intent);
	        		break;
				case R.id.Btn2:

					newName = name.getText().toString();

					if(newName.equals("")){
						Toast.makeText(Option.this,"별명을 입력하세요.",Toast.LENGTH_SHORT).show();
						break;
					}
					if(newName.length()<2){
						Toast.makeText(Option.this,"별명은 2자 이상입니다.",Toast.LENGTH_SHORT).show();
						break;
					}

					(new SetList()).execute(new String[]{"1"});


					break;
	        		
	        	case R.id.checkBox1://�ڵ��α��� ���
	        		
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

				CUserInfo.name = newName;

				String strTemp2 = "CHANGE_NAME" + SC._del +CUserInfo.id+ SC._del + newName+ SC._del;

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

				return "1";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			if (result.compareTo("1") == 0) {

				name.setText(CUserInfo.name);
				Toast.makeText(Option.this,"별명이 "+CUserInfo.name+"으로 변경되었습니다.",Toast.LENGTH_SHORT).show();
			}

		}

	}
}
