package duclass.duclass.a5_search_make;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a6_lecture.LectureActivity;


public class SearchPageActivity extends Activity {
	ArrayList<SearchDatabase> mNoticeData;
	String searchText = "";
	ArrayList<SearchDatabase> databases = new ArrayList<SearchDatabase>();
	Context mContext = this;
	EditText editText;
	ListView listView;
	int i = 0;
	CSharedPreferences sharedPreferences;
	RelativeLayout layout;
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);

		setContentView(R.layout.activity_search);
		sharedPreferences = new CSharedPreferences(this);
		Button button = (Button) findViewById(R.id.button4);
		Button button2 = (Button) findViewById(R.id.button3);
		editText = (EditText) findViewById(R.id.editText3);

		listView = (ListView) findViewById(R.id.listView);
		layout = (RelativeLayout) this.findViewById(R.id.hi);

		editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// listView.setTextFilterEnabled(true);
		// ?��?�뒪�듃?��곗뿉 ��?��?���듃�븘�꽣留� 湲곕?���씠 媛��뒫�븯寃� �븳�떎.

		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(actionId == EditorInfo.IME_ACTION_DONE){ // IME_ACTION_SEARCH , IME_ACTION_GO
					// Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
					searchText = editText.getText().toString(); // ?��몄옄�뿴 ���옣

					if (searchText.compareTo("") == 0) {
						Toast.makeText(SearchPageActivity.this, "검색어를 입력하세요.",
								Toast.LENGTH_SHORT).show();
						return false;
					}
					if (searchText.length()<2) {
						Toast.makeText(SearchPageActivity.this, "2자 이상 입력하세요.",
								Toast.LENGTH_SHORT).show();
						return false;
					}

					searchText = searchText.replace("\n","");

					i = 0;
					(new SetList()).execute(new String[] { "2" });
					serverCheck.showLoading(mContext);
					// listView.get(position).getFileName();


				}
				return false;
			}
		});


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CUserInfo.classNum = mNoticeData.get(position).getNum();
				CUserInfo.major = mNoticeData.get(position).getMajor();
				CUserInfo.title = mNoticeData.get(position).getTitle();
				CUserInfo.professor = mNoticeData.get(position).getProfessor();
				CUserInfo.gradesAvr = mNoticeData.get(position).getGradesAvr();
				CUserInfo.joinPeople = mNoticeData.get(position)
						.getJoinPeople();

				Intent intent1 = new Intent(SearchPageActivity.this,
						LectureActivity.class);
				startActivity(intent1);

			}
		});

		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(SearchPageActivity.this,
						MakePageActivity.class);
				startActivity(intent2);
			}
		});

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				searchText = editText.getText().toString(); // ?��몄옄�뿴 ���옣

				if (searchText.compareTo("") == 0) {
					Toast.makeText(SearchPageActivity.this, "검색어를 입력하세요.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (searchText.length()<2) {
					Toast.makeText(SearchPageActivity.this, "2자 이상 입력하세요.",
							Toast.LENGTH_SHORT).show();
					return;
				}

				searchText = searchText.replace("\n","");

				i = 0;
				(new SetList()).execute(new String[] { "2" });
				serverCheck.showLoading(mContext);
				// listView.get(position).getFileName();
			}
		});

	}

	@Override
	protected void onResume() {
		if(!(SC.isMake.compareTo("") == 0)){
			
			editText.setText(SC.isMake);
			SC.isMake = "";
			searchText = editText.getText().toString(); // ?��몄옄�뿴 ���옣
			(new SetList()).execute(new String[] { "2" });
			serverCheck.showLoading(mContext);
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	public class SetList extends AsyncTask<String, Void, String> {
		String strTemp = "";
	boolean check = false;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			String type = params[0];
			AndroidSocket.socket = AndroidSocket.shared();
			if (type.compareTo("1") == 0) {

	        	String check = sharedPreferences.getPreferences("order");
	        	if(check.compareTo("1") == 0)
	        		return "9";//?��무것?�� ?��?���???????
				
				String strTemp2 = "NOTICE" + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				while (AndroidSocket.socket.HasMessage())
					;
				strTemp = AndroidSocket.socket.GetMessage();// ?��버로�????????�� 받기
			
				return "1";
			}
			if (type.compareTo("2") == 0) {

				String strTemp2 = "SET_CLASS" + SC._del + searchText + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				databases.clear();

				while (AndroidSocket.socket.HasMessage()) {
					if (serverCheck.canNotGet) {
						serverCheck.canNotGet = false;
						return "9";
					}
				}

				Log.i("###",""+strTemp);

				serverCheck.hideLoading();
				if(strTemp.equals(TCP_SC.CANNOTGET)){
					return "1000";
				}

				strTemp = AndroidSocket.socket.GetMessage();// ?��버로�????????�� 받기


				String msg[] = strTemp.split(SC._endDel);
				for (int i = 0; i < msg.length; i++) {
					String Msg[] = msg[i].split(SC._del);

					if (Msg[0].equals("0")) {
						break;
					}
					check =true;
					databases.add(new SearchDatabase(Msg[0], Msg[1],
							Msg[2], Msg[3], Msg[4], Msg[5]));
				}

				return "2";
			}

			return null;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			if (result.compareTo("1") == 0) {
				String temp[] = strTemp.split(SC._del);
				Log.i("#","#"+temp[0]);
				Log.i("#","#"+temp[1]);
				if (temp[1].compareTo("1") == 0) {
				
					//temp[2]?�� 공�?�??????? ?��?��?��?��. ?��기서 ?��?���??????? ?��?��.
					DialogSimple(temp[2]);
					
				}
			}
			if (result.compareTo("2") == 0) {
				if (check) {
					layout.setBackgroundResource(0);
					ListAdapter adapter = new ListAdapter(mContext, 0,
							databases);
					listView.setAdapter(adapter);
				}
				else{
					layout.setBackgroundResource(R.drawable.none_search2);
				}
			}
		}

	}
	
	//?��?��?��로그
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
	private class ListAdapter extends ArrayAdapter<SearchDatabase> { // 諛곗뿴��???????
																		// �엳�뒗
																		// 媛�
																		// 蹂댁뿬二?���???????

		public ListAdapter(Context context, int resource,
				ArrayList<SearchDatabase> noticeData) {
			super(context, resource, noticeData);
			mNoticeData = noticeData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.search_adapter,
					null, true);
			TextView txtMajor = (TextView) rowView.findViewById(R.id.major);
			TextView txtLecture = (TextView) rowView.findViewById(R.id.lecture);
			TextView txtProfessor = (TextView) rowView
					.findViewById(R.id.professor);
			RatingBar ratingBar = (RatingBar) rowView
					.findViewById(R.id.ratingBar);
			TextView txtRatingScore = (TextView) rowView
					.findViewById(R.id.ratingScore);

			txtMajor.setText("\n"+mNoticeData.get(position).getMajor());
			txtLecture.setText(mNoticeData.get(position).getTitle());
			txtProfessor.setText(mNoticeData.get(position).getProfessor());
			ratingBar.setRating(Float.valueOf(
					mNoticeData.get(position).getGradesAvr()).floatValue());
			txtRatingScore.setText(mNoticeData.get(position).getJoinPeople());

			return rowView;
			// return super.getView(position, convertView, parent);
		}
	}

}
