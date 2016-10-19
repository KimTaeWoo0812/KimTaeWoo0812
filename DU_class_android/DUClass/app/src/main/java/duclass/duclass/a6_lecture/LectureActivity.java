package duclass.duclass.a6_lecture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;


public class LectureActivity extends Activity{

	private ArrayList<CommentDatabase> databases = new ArrayList<CommentDatabase>();


	RatingBar ratingBar;
	TextView txtMajor;
	TextView txtTitle;
	TextView txtProfessor;
	//TextView txtJoinPeople;
	TextView getMyText;
	static TextView g_b_text;
	ListView listView;
	Context mContext = this;
	String classNum = "";
	Button button4;
	Temp CTemp = new Temp();
	String strTempNum = "";
	boolean btnBool = true;
	String commentText = "";
	String stringRating = "";
	int i=0;
	String wordPosition;
	ForSendMsgThread forSendMsgThread;
//	ListView listView2;
	int g_b_num=0;
	boolean NOT_VIEW = false;
	@Override
	protected void onDestroy() {
		super.onDestroy();
        AndroidSocket.socket.CloseSocket();
	}

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		
		setContentView(R.layout.activity_lecture);

				//ViewDynamicChange();

        AndroidSocket.socket = AndroidSocket.shared();

		ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
		button4 = (Button) findViewById(R.id.commentbutton);
		// 리스?���????? ?��?��?�� �??????��?���?????
		txtMajor = (TextView) findViewById(R.id.textMajor);
		txtTitle = (TextView) findViewById(R.id.textLecture);
		txtProfessor = (TextView) findViewById(R.id.textProfessor);
		//txtJoinPeople = (TextView) findViewById(R.id.joinPeople);
		listView = (ListView) findViewById(R.id.listView2);
//		listView2 = (ListView) findViewById(R.id.listView2);
		ratingBar.setRating(Float.valueOf(CUserInfo.gradesAvr).floatValue());
		
		
		txtMajor.setText(CUserInfo.major);
		txtTitle.setText(CUserInfo.title);
		txtProfessor.setText(CUserInfo.professor);
		//txtJoinPeople.setText(CUserInfo.joinPeople);
		classNum = CUserInfo.classNum;

		
		getMyText = (TextView) findViewById(R.id.textView19);
		new SetList().execute("1");
		serverCheck.showLoading(mContext);

//		ListAdapter adapter = new ListAdapter(mContext, 0, databases); // 리스?���?????
//																		// 보이�?????
//		listView2.setAdapter(adapter);

	}
	public void onClick(View view) {// 버튼 ?��???��?��
		switch (view.getId()) {
		case R.id.commentbutton://댓글작성
			final Dialog ratingDialog = new Dialog(LectureActivity.this,R.style.Base_Theme_AppCompat_Light_Dialog);
			ratingDialog.setContentView(R.layout.dialog_ratingbar);
			ratingDialog.setCancelable(true);
			final RatingBar ratingBar4 = (RatingBar) ratingDialog
					.findViewById(R.id.ratingBar4);

			final EditText editCom = (EditText) ratingDialog.findViewById(R.id.Text1);
			
			ratingBar4
					.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
						@Override
						public void onRatingChanged(RatingBar ratingBar,
								float rating, boolean fromUser) {

						}
					});

			Button button7 = (Button) ratingDialog
					.findViewById(R.id.button7); // 댓글 쓰기
			button7.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View v) {
					
					Log.i("###","###"+editCom.getText().toString());
					commentText = editCom.getText().toString();
					stringRating = String.valueOf(ratingBar4.getRating());

					//databases.add(new CommentDatabase("0", commentText, CUserInfo.id,
					//		CUserInfo.name, stringRating, "0", "0"));

					Log.i("###", "###" + CUserInfo.name);


					if(commentText.length()<2) {
						Toast.makeText(LectureActivity.this, "3자 이상 입력하세요", Toast.LENGTH_SHORT).show();
						return;
					}
					if(ratingBar4.getRating()==0){
						Toast.makeText(LectureActivity.this, "별점을 입력하세요", Toast.LENGTH_SHORT).show();
						return;
					}


					//개행 없애기
					commentText = commentText.replace("\n"," ");

					Log.i("###commentText",""+commentText);
					CTemp.comNum = "0";
					CTemp.nickname = CUserInfo.name;
					CTemp.comment = commentText;
					CTemp.gradesAvr = stringRating;
					CTemp.like = "0";
					CTemp.unlike = "0";
					
					editCom.setText("");
					{
						//?���???? ?��?��?���????, ?��?��?���???? 구분
						if (btnBool)
							new SetList().execute("4");
						else
							new SetList().execute("5");
						
						//serverCheck.showLoading(mContext);
					}

					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}


					//?���????�???? ?���???? 추�??���???? ?���????말고, ?��로고침으�???? 바꿔?�� ?��?��.
//					ListAdapter adapter = new ListAdapter(mContext, 0,
//							databases); // 리스?���????? 보이�?????
//					listView2.setAdapter(adapter);
					new SetList().execute("1");
					serverCheck.showLoading(mContext);

					ratingDialog.dismiss();
				}
			});

			Button button9 = (Button) ratingDialog
					.findViewById(R.id.button9);
			button9.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ratingDialog.cancel();
				}
			});
			ratingDialog.show();
			
			
			break;
		}
	}
	
	
	
	public void ViewDynamicChange(final String com) {
		getMyText.setText("나의 댓글: "+com);
		button4.setText("댓글 수정");
		btnBool = false;

	}

	class Temp {
		public String comNum = "";
		public String nickname = "";
		public String comment = "";
		public String gradesAvr = "";
		public String like = "";
		public String unlike = "";
	}


	public class SetList extends AsyncTask<String, Void, String> {
		String strTemp = "";
		boolean no = true;
		boolean hasCom = false;
		String hasStr= "";
		int count = 0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			String type = params[0];
			i = 0;

			{
				if (type.compareTo("1") == 0 && CUserInfo.commentNum < SC.commentNum_Limt) {

					NOT_VIEW = true;
				}
			}

			if (type.compareTo("1") == 0) {





				String strTemp = "SET_COMMENT" + SC._del + classNum + SC._del
						+ CUserInfo.id + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				databases.clear();

				int out = 0;
				for (;;) {
					while (AndroidSocket.socket.HasMessage()){
						if(!serverCheck.canNotGet){
							Log.i("###canNotGet",""+serverCheck.canNotGet);
							serverCheck.hideLoading();
							serverCheck.canNotGet=false;
							return "9";
						}
					}
					
					strTemp = AndroidSocket.socket.GetMessage();// ?��버로�??????�� 받기


					String msg[] = strTemp.split(SC._endDel);
					Log.i("#받음: ", "#" + strTemp);
					serverCheck.hideLoading();
					if(strTemp.equals(TCP_SC.CANNOTGET)){
						return "1000";
					}


					for (int i = 0; i < msg.length; i++,count++) {
						String Msg[] = msg[i].split(SC._del);

						Log.i("#받음2: ", "#" + msg[i] + "  " + Msg.length);
						if (Msg[0].equals("0")) {
							out=1;
							break;
						}
						databases.add(new CommentDatabase(Msg[0], Msg[1],
								Msg[2], Msg[3], Msg[4], Msg[5], Msg[6], Msg[7]));
						Log.i("#받음3: ", "#" + Msg[2] + "  " +CUserInfo.id);

						if (CUserInfo.id.equals(Msg[2])) {
							hasCom = true;
							hasStr = Msg[1];
						}
					}
					if (out == 1)
						break;
				}




				return "1";
			} else if (type.compareTo("2") == 0) {
				String strTemp = "COMMENT_GOOD" + SC._del + CUserInfo.id + SC._del + wordPosition + SC._del+ g_b_num + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (AndroidSocket.socket.HasMessage()){
					if(!serverCheck.canNotGet){
						serverCheck.canNotGet=false;
						return "9";
					}
				}
				serverCheck.hideLoading();
				if(strTemp.equals(TCP_SC.CANNOTGET)){
					return "1000";
				}
				strTemp = AndroidSocket.socket.GetMessage();// ?��버로�??????�� 받기

				String temp[] = strTemp.split(SC._del);
				
				if(temp[1].compareTo("0")==0)
						no=false;
				
				
				return "2";
			} else if (type.compareTo("3") == 0) {
				String strTemp = "COMMENT_BAD" + SC._del + CUserInfo.id + SC._del + wordPosition + SC._del+ g_b_num + SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while (AndroidSocket.socket.HasMessage()){
					if(!serverCheck.canNotGet){
						serverCheck.canNotGet=false;
						return "9";
					}
				}
				serverCheck.hideLoading();
				if(strTemp.equals(TCP_SC.CANNOTGET)){
					return "1000";
				}
				strTemp = AndroidSocket.socket.GetMessage();// ?��버로�??????�� 받기

				String temp[] = strTemp.split(SC._del);
				
				if(temp[1].compareTo("0")==0)
						no=false;
				return "3";
			} else if (type.compareTo("4") == 0) {
				String strTemp = "SAVE_COMMENT" + SC._del + CUserInfo.id
						+ SC._del + CUserInfo.classNum + SC._del
						+ CTemp.comment + SC._del + CUserInfo.name + SC._del
						+ CTemp.gradesAvr + SC._del;//!@

				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (AndroidSocket.socket.HasMessage()){
					if(!serverCheck.canNotGet){
						serverCheck.canNotGet=false;
						return "9";
					}
				}
//				serverCheck.hideLoading();
				if(strTemp.equals(TCP_SC.CANNOTGET)){
					return "1000";
				}
				strTemp = AndroidSocket.socket.GetMessage();// ?��버로�??????�� 받기

				String Msg[] = strTemp.split(SC._del);

				
				if(Msg[1].compareTo("1") == 0){
//					new SetList().execute("1");
					//servercheck.showLoading(mContext);
					return "4";
				}
				return "44";
			} else if (type.compareTo("5") == 0) {
				String strTemp = "REVISE_MY_COMMENT" + SC._del + CUserInfo.id+ SC._del + CUserInfo.classNum
						+ SC._del + CTemp.comment + SC._del + CTemp.gradesAvr
						+ SC._del;

				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			new SetList().execute("1");
			//servercheck.showLoading(mContext);
			return "0";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.i("###NOT_VIEW", "" + NOT_VIEW);
			Log.i("###onPostExecute hasCom", "" + hasCom);
			Log.i("###onPostExecute i", "" + count);



			if (result.compareTo("1") == 0) {
				if(hasCom){
					ViewDynamicChange(hasStr);
				}
				if (count != 0) {

					if(NOT_VIEW){
						Dialog_for_commentNum();
					}
					else {
						Log.i("###NOT_VIEW22222", "" + NOT_VIEW);
						ListAdapter adapter = new ListAdapter(mContext, 0,
								databases);

						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						listView.setAdapter(adapter);
					}
				}
				Log.i("test", "" + "onPostExecute");
			}
			else if (result.compareTo("4") == 0) {
				CUserInfo.commentNum++;
				Toast.makeText(LectureActivity.this, "게시 성공!",
						Toast.LENGTH_SHORT).show();

			} else if (result.compareTo("44") == 0) {
				Toast.makeText(LectureActivity.this, "게시  실패!",
						Toast.LENGTH_SHORT).show();

			} else if (result.compareTo("2") == 0) {
				if(no){
					g_b_text.setText(Integer.toString(g_b_num));
					Toast.makeText(LectureActivity.this, "증가!",
							Toast.LENGTH_SHORT).show();
				} else{
					DialogSimple();
					//Toast.makeText(LectureActivity.this, "?���???? ?��?��?��?��!",Toast.LENGTH_SHORT).show();
				}

			} else if (result.compareTo("3") == 0) {
				if(no){
					g_b_text.setText(Integer.toString(g_b_num));
					Toast.makeText(LectureActivity.this, "증가!",
							Toast.LENGTH_SHORT).show();
				} else{
					DialogSimple();
					//Toast.makeText(LectureActivity.this, "?���???? ?��?��?��?��!",Toast.LENGTH_SHORT).show();
				}

			}
		}

	}

	private class ListAdapter extends ArrayAdapter<CommentDatabase> {

		private ArrayList<CommentDatabase> mNoticeData;

		public ListAdapter(Context context, int resource,
				ArrayList<CommentDatabase> noticeData) {
			super(context, resource, noticeData);
			mNoticeData = noticeData;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {


			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.lecture_adapter,
					null, true);
			TextView txtNickname = (TextView) rowView
					.findViewById(R.id.nickname);
			TextView txtComment = (TextView) rowView
					.findViewById(R.id.commenttext);
			TextView date = (TextView) rowView
					.findViewById(R.id.date);
			final TextView txtLike = (TextView) rowView.findViewById(R.id.like);
			final TextView txtUnlike = (TextView) rowView
					.findViewById(R.id.unlike);
			RatingBar ratingBar3 = (RatingBar) rowView
					.findViewById(R.id.ratingBar3);
			ImageButton imageLike = (ImageButton) rowView
					.findViewById(R.id.imageButton);
			ImageButton imageUnlike = (ImageButton) rowView
					.findViewById(R.id.imageButton2);
			
			LinearLayout layout1 =  (LinearLayout) rowView
					.findViewById(R.id.layout1);
			LinearLayout layout2 =  (LinearLayout) rowView
					.findViewById(R.id.layout2);
			Log.i("#","#asdasdasdasdasd   "+position);
			Log.i("#","#"+mNoticeData.get(position).getNickname());
			Log.i("#","#"+mNoticeData.get(position).getComment());
			Log.i("#","#"+mNoticeData.get(position).getGood());
			Log.i("#","#"+mNoticeData.get(position).getBad());

			txtNickname.setText(mNoticeData.get(position).getNickname());
			txtComment.setText(mNoticeData.get(position).getComment());
			date.setText(mNoticeData.get(position).getDate());
			txtLike.setText(mNoticeData.get(position).getGood());
			txtUnlike.setText(mNoticeData.get(position).getBad());
			ratingBar3.setRating(Float.valueOf(
					mNoticeData.get(position).getGradesAvr()).floatValue());

			layout1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					// 좋아?�� ?���????
					if (true)// 중복?��?��경우
					{
						int like = Integer.parseInt(mNoticeData.get(position).getGood())+1;
						//txtLike.setText(Integer.toString(like));
						g_b_text = txtLike;
						g_b_num = like;
						wordPosition = mNoticeData.get(position).getComNum();
						new SetList().execute("2");
					} else { // ?���???? ?��?��경우
						Toast.makeText(LectureActivity.this, "?���????? ?��?��?��???��?��?��!",
								Toast.LENGTH_SHORT).show();
						
					}

				}
			});
			layout2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					// 좋아?�� ?���????
					if (true)// 중복?��?��경우
					{
						int bad = Integer.parseInt(mNoticeData.get(position).getBad())+1;
						//txtUnlike.setText(Integer.toString(bad));
						g_b_text = txtUnlike;
						g_b_num = bad;
						wordPosition = mNoticeData.get(position).getComNum();
						new SetList().execute("3");
					} else { // ?���???? ?��?��경우
						Toast.makeText(LectureActivity.this, "?���????? ?��?��?��???��?��?��!",
								Toast.LENGTH_SHORT).show();
					}
			
				}
			});
			return rowView;
			// return super.getView(position, convertView, parent);
		}
	}
	//?��?��?��로그
	private void DialogSimple() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("이미 했습니다. \n취소 하시겠습니까?").setCancelable(false)
				.setPositiveButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				})
				.setNegativeButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// do something
						//g_b_text.setText(Integer.toString(g_b_num));
						forSendMsgThread = new ForSendMsgThread();
						forSendMsgThread.start();

					}
				});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("");
		// Icon for AlertDialog
		alert.show();
	}
		
		public class ForSendMsgThread extends Thread {
			
			public ForSendMsgThread(){
				
			}
			
			@Override
			public void run() {
				String strTemp = "DELETE_G_B" + SC._del + CUserInfo.id + SC._del + wordPosition + SC._del;

				AndroidSocket.socket = AndroidSocket.shared();
				try {
					AndroidSocket.socket.SendMessage(strTemp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//AndroidSocket.socket.CloseSocket();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				new SetList().execute("1");
				
				
			}
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
//final Handler handler = new Handler();
//handler.post(new Runnable() {
//	public void run() {
//		g_b_text.setText(Integer.toString(g_b_num));
//	}
//});