package duclass.duclass.a5_search_make;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.ClassData;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;


/**
 * Created by HoChan on 2015-11-18.
 */
public class MakePageActivity extends Activity {
	EditText editText2;
	EditText editText3;
	String major = "";
	String title;
	String professor;
	Spinner mSpinner;
	Spinner mSpinner2;
	ArrayAdapter adapter2;
	String str = "";
	ClassData data = new ClassData();
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_makepage);
		
		
		editText2 = (EditText) findViewById(R.id.Text2);
		editText3 = (EditText) findViewById(R.id.Text3);
		
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mSpinner2 = (Spinner) findViewById(R.id.spinner2);

		
		
		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.data);
		

		mSpinner.setAdapter(adapter);
			
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			
			@Override		
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				major = data.data[position]; // major ���� ����
				
				Toast.makeText(getApplicationContext(), data.data[position], Toast.LENGTH_SHORT).show();
				
				switch (position) {
				case 0:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s0);
					break;
				case 1:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s1);
					break;
				case 2:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s2);
					break;
				case 3:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s3);
					break;
				case 4:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s4);
					break;
				case 5:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s5);
					break;
				case 6:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s6);
					break;
				case 7:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s7);
					break;
				case 8:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s8);
					break;
				case 9:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s9);
					break;
				case 10:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s10);
					break;
				case 11:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s11);
					break;
				case 12:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s12);
					break;
				case 13:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s13);
					break;
				case 14:
					adapter2 = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data.s14);
					break;

				}
				mSpinner2.setAdapter(adapter2);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		
		mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override		
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				str = (String) mSpinner2.getSelectedItem();
				if (str.compareTo("선택 없음") == 0) {
					str = "";
				}
				//Toast.makeText(getApplicationContext(), data[position], Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	public void onClick(View view) {// ��ư ��������
		switch (view.getId()) {
		case R.id.Btn1:// ����
			title = editText2.getText().toString();
			professor = editText3.getText().toString();

			if(title.length()<2) {
				Toast.makeText(this, "과목명은 2자 이상 입니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			if(professor.length()<2) {
				Toast.makeText(this, "교수명은 2자 이상 입니다.", Toast.LENGTH_SHORT).show();
				return;
			}
			title = title.replace("\n","");
			professor = professor.replace("\n","");

			new SendMsgTask().execute();
			break;
			
		case R.id.Btn2:// ���
			finish();// �� ȭ�� ����
			break;
			
		}
		
	}
	
	
	public class SendMsgTask extends AsyncTask<String, Void, String> {
		String msg = "";
		String strTemp = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {



			AndroidSocket.socket = AndroidSocket.shared();
			strTemp = "MAKE_CLASS" + SC._del + major+" "+str + SC._del + title
					+ SC._del + professor + SC._del;

			try {
				AndroidSocket.socket.SendMessage(strTemp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (AndroidSocket.socket.HasMessage()){
				if(serverCheck.canNotGet){
					serverCheck.canNotGet=false;
					return "9";
				}
			}
			strTemp = AndroidSocket.socket.GetMessage();// �����κ��� �ޱ�

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			String arrMsg2[] = strTemp.split(SC._del);
			if (arrMsg2[1].compareTo("1") == 0) {
				SC.isMake = title;
				Toast.makeText(MakePageActivity.this, "과목 생성 성공!",
						Toast.LENGTH_SHORT).show();
				long saveTime = System.currentTimeMillis();
				long currTime = 0;
				while (currTime - saveTime > 2000) {
					currTime = System.currentTimeMillis();
				}
				finish();// �� ȭ�� ����
				return;
			} else if (arrMsg2[1].compareTo("0") == 0) {
				Toast.makeText(MakePageActivity.this, "과목 생성 실패!",
						Toast.LENGTH_SHORT).show();
				long saveTime = System.currentTimeMillis();
				long currTime = 0;
				while (currTime - saveTime > 2000) {
					currTime = System.currentTimeMillis();
				}
				finish();// �� ȭ�� ����
				return;
			} else {
				SC.isMake = title;
				Toast.makeText(MakePageActivity.this, "이미 있는 과목입니다.",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
