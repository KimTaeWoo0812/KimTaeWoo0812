package duclass.duclass.a8_ranking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a6_lecture.LectureActivity;


public class RankingActivity extends Activity {
	private ArrayList<Ranking_Adapter> mNoticeData;
	private ArrayList<Ranking_Adapter> databases_forAll = new ArrayList<Ranking_Adapter>();
	private ArrayList<Ranking_Adapter> databases_forArt = new ArrayList<Ranking_Adapter>();
	private Context mContext = this;
	private ListView listView;
	private int i = 0, j = 1;
	private boolean rankCase = true;// true = 전체, false = 교양

	Button btn1;
	Button btn2;

	duclass.duclass.a2_static_class.serverCheck serverCheck = new serverCheck();

	public void onClick(View view) {// 버튼 눌렀을때
		switch (view.getId()) {
		case R.id.Btn1:// 전체

			if(rankCase)
				break;
			btn2.setBackgroundColor(Color.WHITE);
			btn2.setTextColor(Color.YELLOW);

			btn1.setBackgroundColor(Color.parseColor("#00BAFF"));
			btn1.setTextColor(Color.parseColor("#ffffff"));
			rankCase = !rankCase;

			if(databases_forAll.size()>1){
				ListAdapter adapter = new ListAdapter(mContext, 0,databases_forAll);
				listView.setAdapter(adapter);
				break;
			}

			new SetList().execute("1");
			serverCheck.showLoading(this);
			break;

		case R.id.Btn2:// 교양
			if(!rankCase)
				break;

			btn1.setBackgroundColor(Color.WHITE);
			btn1.setTextColor(Color.YELLOW);

			btn2.setBackgroundColor(Color.parseColor("#00BAFF"));
			btn2.setTextColor(Color.parseColor("#ffffff"));
			rankCase = !rankCase;

			if(databases_forArt.size()>1){
				ListAdapter adapter = new ListAdapter(mContext, 0,databases_forArt);
				listView.setAdapter(adapter);
				break;
			}

			new SetList().execute("2");
			serverCheck.showLoading(this);
			break;
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);

		setContentView(R.layout.activity_ranking);

		listView = (ListView) findViewById(R.id.listView);

		databases_forAll.clear();
		databases_forArt.clear();

		btn1 = (Button) findViewById(R.id.Btn1);
		btn2 = (Button) findViewById(R.id.Btn2);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CUserInfo.classNum = mNoticeData.get(position).getNum();
				CUserInfo.major = mNoticeData.get(position).getMajor();
				CUserInfo.title = mNoticeData.get(position).getTitle();
				CUserInfo.professor = mNoticeData.get(position).getProfessor();
				CUserInfo.gradesAvr = mNoticeData.get(position).getAvr();
				CUserInfo.joinPeople = mNoticeData.get(position)
						.getJoinPeople();

				Intent intent1 = new Intent(RankingActivity.this,
						LectureActivity.class);
				startActivity(intent1);

			}
		});

		btn2.setBackgroundColor(Color.WHITE);
		btn2.setTextColor(Color.YELLOW);

		btn1.setBackgroundColor(Color.parseColor("#00BAFF"));
		btn1.setTextColor(Color.parseColor("#ffffff"));
		
		new SetList().execute("1");
		serverCheck.showLoading(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	public class SetList extends AsyncTask<String, Void, String> {
		String strTemp = "";
		int k = 1;
		boolean case_ = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {

			String type = params[0];
			AndroidSocket.socket = AndroidSocket.shared();
			String strTemp2 = "";
			if (type.compareTo("1") == 0) {
				strTemp2 = "RANKINGFORALL" + SC._del;
				case_ = true; //전체
			}
			if (type.compareTo("2") == 0) {
				strTemp2 = "RANKINGFORART" + SC._del;
				case_ = false; //교양
			}
			try {
				AndroidSocket.socket.SendMessage(strTemp2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			int out = 0;
			while (AndroidSocket.socket.HasMessage()) {
				if (!serverCheck.canNotGet) {
					serverCheck.canNotGet = false;
					return "9";
				}
			}
			strTemp = AndroidSocket.socket.GetMessage();// ?��버로�???????�� 받기
			Log.i("#받음 랭킹: ", "#" + strTemp);
			serverCheck.hideLoading();
			if(strTemp.equals(TCP_SC.CANNOTGET)){
				return "1000";
			}

			String msg[] = strTemp.split(SC._endDel);
			for (int i = 0; i < msg.length; i++) {
				String Msg[] = msg[i].split(SC._del);

				Log.i("#받음2: ", "#" + msg[i] + "   " + Msg.length);
				if (Msg[0].equals("0")) {
					break;
				}

				if (Msg[4].length() > 6)
					Msg[4] = Msg[4].substring(0, 5);

				Log.i("#", "#" + Msg[0] + Msg[1] + Msg[2]);
				Log.i("#받음21: ", "#" + msg[i]+"   "+Msg.length);
				if (Msg[1].length() > 12) {
					Msg[1].substring(0, 12);
//					Msg[1] += "build/intermediates/exploded-aar/com.android.support/support-vector-drawable/23.3.0";
					Msg[1] += "...";
				}
				if (Msg[2].length() > 12) {
					Msg[2].substring(0, 12);
//					Msg[2] += "build/intermediates/exploded-aar/com.android.support/support-vector-drawable/23.3.0";
					Msg[2] += "...";
				}
				Log.i("#받음22: ", "#" + msg[i] + "   " + Msg.length);

				if(case_) //전체
					databases_forAll.add(new Ranking_Adapter(Msg[0], Msg[1], Msg[2],Msg[3], Msg[4], Msg[5], k));
				if(!case_) //교양
					databases_forArt.add(new Ranking_Adapter(Msg[0], Msg[1], Msg[2],Msg[3], Msg[4], Msg[5], k));

				k++;
				Log.i("#받음23: ", "#" + msg[i]+"   "+Msg.length);
//					check =true;
//					databases.add(new SearchDatabase(Msg[0], Msg[1],
//							Msg[2], Msg[3], Msg[4], Msg[5]));


			}
			duclass.duclass.a2_static_class.serverCheck.hideLoading();
			return "1";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			AndroidSocket.socket.CloseSocket();
			if (result.compareTo("1") == 0) {
				if (k != 0) {
					ListAdapter adapter = null;
					if(case_) //전체
						adapter = new ListAdapter(mContext, 0,databases_forAll);
					if(!case_) //교양
						adapter = new ListAdapter(mContext, 0,databases_forArt);
					listView.setAdapter(adapter);
				}
			}
		}

	}

	private class ListAdapter extends ArrayAdapter<Ranking_Adapter> { // 諛곗뿴��??????
																		// �엳�뒗
																		// 媛�
																		// 蹂댁뿬二?���??????

		public ListAdapter(Context context, int resource,
				ArrayList<Ranking_Adapter> noticeData) {
			super(context, resource, noticeData);
			mNoticeData = noticeData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.ranking_adapter, null,
					true);
			TextView ranking = (TextView) rowView.findViewById(R.id.ranking);
			TextView major = (TextView) rowView.findViewById(R.id.major);
			TextView avr = (TextView) rowView.findViewById(R.id.avr);
			if (mNoticeData.get(position).rank.compareTo("1") == 0) {
				ranking.setTextColor(Color.RED);
				major.setTextColor(Color.RED);
				avr.setTextColor(Color.RED);
			} else if (mNoticeData.get(position).rank.compareTo("2") == 0) {
				ranking.setTextColor(Color.BLUE);
				major.setTextColor(Color.BLUE);
				avr.setTextColor(Color.BLUE);
			} else if (mNoticeData.get(position).rank.compareTo("3") == 0) {
				ranking.setTextColor(Color.BLUE);
				major.setTextColor(Color.BLUE);
				avr.setTextColor(Color.BLUE);
			}
			ranking.setText(mNoticeData.get(position).rank + "등");
			major.setText(mNoticeData.get(position).getTitle() + "\n"
					+ mNoticeData.get(position).getProfessor() + " 교수님");

			Log.i("###랭킹",""+mNoticeData.get(position).getTitle()+"   "+position);
			// score.setText();
			avr.setText("\n평균 평점: " + mNoticeData.get(position).getAvr());
			return rowView;
			// return super.getView(position, convertView, parent);
		}
	}

}