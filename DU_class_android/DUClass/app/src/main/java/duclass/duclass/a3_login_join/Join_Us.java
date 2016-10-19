package duclass.duclass.a3_login_join;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.ClassData;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;


public class Join_Us extends Activity {
	int isChecked = 0;
	char[] message = new char[128];
	String strTemp;
	int i;
	EditText checkId;
	EditText editPW;
	EditText editName;
	String id;

	String PW;
	String name;
	EditText editPWckeck;
	Spinner mSpinner;
	Spinner mSpinner2;
	ArrayAdapter adapter2;
	ClassData data = new ClassData();
	byte[] cipher;
	String str = "";
	String major = "";
	String[] data2 = {" ","인문대","법과대","행정대","경상대","사회과학대"
			,"자연과학대","공과대","정보통신대","생명환경대","조형예술대"
			,"사범대","재활과학대","기초교육대","간호보건학부"};
	boolean isSpinnerChecked = false;

	
	@Override
	protected void onResume() {
		super.onResume();
		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.page_spinner, R.id.text_spinner, data2);
		

		mSpinner.setAdapter(adapter);
			
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				major = data.data[position]; // major 받음 새로

				if (position != 0)
					isSpinnerChecked = true;

//				Toast.makeText(getApplicationContext(), data.data[position], Toast.LENGTH_SHORT).show();

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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_join_us);

		checkId = (EditText) findViewById(R.id.id);
		editPW = (EditText) findViewById(R.id.pw);
		mSpinner = (Spinner) findViewById(R.id.spinner1);
		mSpinner2 = (Spinner) findViewById(R.id.spinner2);

	}


	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.Btn1:// 중복확인
			id = checkId.getText().toString();
			// soccket.SendMessage("아이디 길이   "+checkId.length());//

				if (checkId.length() < 4 || editPW.length() > 16) {
					Toast.makeText(this, "아이디는 네자 이상, 16자 미만입니다!",
							Toast.LENGTH_SHORT).show();

					break;
				}
			id = id.replace("\n","");
			new SendMsgTask().execute("1");
			serverCheck.showLoading(this);
			
			break;

		case R.id.Btn2:// 가입
			PW = editPW.getText().toString();
			editPWckeck = (EditText) findViewById(R.id.Text3);
			editName = (EditText) findViewById(R.id.Text5);
			String PWcheck = editPWckeck.getText().toString();
			
			if(!isSpinnerChecked){
				Toast.makeText(this, "학과를 선택하세요!", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if (isChecked == 0) {
				Toast.makeText(this, "아이디 중복확인을 해주세요!", Toast.LENGTH_SHORT).show();
				break;
			}
			if (editPW.length() < 4 || editPW.length() > 16) {
				Toast.makeText(this, "비밀번호는 네자 이상, 16자 미만입니다!",
						Toast.LENGTH_SHORT).show();

				break;
			}
			if (!PW.equals(PWcheck)) {
				Toast.makeText(this, "입력한 비밀번호가 같지 않습니다!", Toast.LENGTH_SHORT)
						.show();

				break;
			}
			name=editName.getText().toString();
			
			for(int i=0;i<name.length();i++)
			{
				if(name.charAt(i)==200){
					Toast.makeText(this, "이런 이상한 문자는 사용 못합니다.", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			if(name.length()<2)
			{
				Toast.makeText(this, "이름은 2자 이상 입니다.", Toast.LENGTH_SHORT).show();
				break;
			}

			DialogSimple();
			
			break;

		case R.id.Btn3:// 취소
			finish();// 이 화면 종료
			break;
		}
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
				strTemp = "IDCHECK" + SC._del + id + SC._del;
				try {
					AndroidSocket.socket.SendMessage(strTemp);
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
				strTemp = AndroidSocket.socket.GetMessage();// 서버로부터 받기
				
				
				
				return "1";
			}
			
			if (type.compareTo("2") == 0) {
				strTemp = "JOIN_US" + SC._del + id + SC._del + PW + SC._del+name+SC._del+major+" "+str +SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp);
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
				strTemp = AndroidSocket.socket.GetMessage();// 서버로부터 받기
			return "2";
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			if (result.compareTo("1") == 0) {
				String arrMsg[] = strTemp.split(SC._del);
				
				if (arrMsg[1].compareTo("1") == 0) {
					isChecked = 1;
					Toast.makeText(Join_Us.this, "아이디 사용 가능!", Toast.LENGTH_SHORT).show();

				} else {
					isChecked = 0;
					Toast.makeText(Join_Us.this, "이미 있는 아이디 입니다!", Toast.LENGTH_SHORT)
							.show();
				}
			}
			
			else if (result.compareTo("2") == 0) {
				String arrMsg2[] = strTemp.split(SC._del);
				if (arrMsg2[1].compareTo("1") == 0) {
					Toast.makeText(Join_Us.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
					long saveTime = System.currentTimeMillis();
					long currTime = 0;
					while (currTime - saveTime > 2000) {
						currTime = System.currentTimeMillis();
					}
					finish();// 이 화면 종료

					return;
				}
				else {
					Toast.makeText(Join_Us.this, "회원가입 실패! 다시 시도해 주세요.",
							Toast.LENGTH_SHORT).show();
					checkId = null;
					editPW = null;
					editPWckeck = null;
					isChecked = 0;
				}
			}
		}

	}
	private void DialogSimple(){
		String clause = "1. 개인정보의 취급목적\n" +
				"\n" +
				"DU여론은 회원가입 시 서비스 이용을 위해 필요한 최소한의 개인정보만을 수집합니다. 귀하가 DU여론의 서비스를 이용하기 위해서는 회원가입 시 (ID, 비밀번호, 별명, 학과)를 필수적으로 입력하셔야 합니다.\n" +
				"\n" +
				"개인정보 항목별 구체적인 수집목적 및 이용목적은 다음과 같습니다.\n" +
				"ID, 비밀번호, 별명, 학과 :회원제 서비스 이용에 따른 본인 식별 절차에 이용.\n" +
				"본 서비스는 만 14세 미만의 아동에 대한 개인정보를 수집하고 있지 않으며, 어플리케이션에 아동에게 유해한 정보를 게시하거나 제공하고 있지 않습니다.\n" +
				"\n" +
				"2. 개인정보의 취급 및 보유기간 \n" +
				"\n" +
				"DU여론은 수집된 개인정보의 보유기간은 회원가입 하신 후 해지(탈퇴신청등)시까지 입니다. 또한 해지시 DU여론은 회원님의 개인정보를 재생이 불가능한 방법으로 즉시 파기하며 (개인정보가 제3자에게 제공된 경우에는 제3자에게도 파기하도록 지시합니다.) 다만 다음 각호의 경우에는 각 호에 명시한 기간동안 개인정보를 보유합니다.\n" +
				"① 상법 등 법령의 규정에 의하여 보존할 필요성이 있는 경우에는 법령에서 규정한 보존기간 동안 거래내역과 최소한의 기본정보를 보유함 \n" +
				"② 보유기간을 회원님에게 미리 고지하고 그 보유기간이 경과하지 아니한 경우와 개별적으로 회원님의 동의를 받을 경우에는 약속한 보유기간 동안 보유함 \n" +
				"\n" +
				"3. 취급하는 개인정보 항목 \n" +
				"\n" +
				"(일반 회원가입 시)\n" +
				"필수 개인정보 항목 :ID, 비밀번호, 별명, 학과 \n" +
				"\n" +
				"정보주체는 개인정보의 수집 이용목적에 대한 동의를 거부할 수 있으며, 동의 거부시 DU여론에 회원가입이 되지 않으며, DU여론에서 제공하는 서비스를 이용할 수 없습니다.\n" +
				"\n" +
				"귀하께서는 DU여론을 이용하시며 발생하는 모든 개인정보보호 관련 민원을 개인정보관리책임자 혹은 담당부서로 신고하실 수 있습니다. 담당자는 이용자들의 신고사항에 대해 신속하게 충분한 답변을 드릴 것입니다. \n" +
				"\n" +
				"4. 동의 거부 권리 및 동의 거부 시 불이익 내용 \n" +
				"\n" +
				"귀하는 개인정보의 수집목적 및 이용목적에 대한 동의를 거부할 수 있으며, 동의 거부시 DU여론에 회원가입이 되지 않으며, DU여론에서 제공하는 모든 서비스를 이용할 수 없습니다.";

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(clause).setCancelable(
				false).setPositiveButton("취소",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id_) {
						// do something
						dialog.cancel();
					}
				}).setNegativeButton("동의",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id_) {
						id = id.replace("\n","");
						PW = PW.replace("\n","");
						name = name.replace("\n","");

						new SendMsgTask().execute("2");
						serverCheck.showLoading(Join_Us.this);
						dialog.cancel();
					}
				});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("이용 약관");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon_50);
		alert.show();

		TextView msgView = (TextView) alert.findViewById(android.R.id.message);
		msgView.setTextSize(12);

	}
}
