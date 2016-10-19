package duclass.duclass.a12_chatting;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;

/**
 * Created by kimtaewoo on 2016-10-01.
 */
public class chatting_Activity extends Activity {


    Context mContext = this;
    private ArrayList<chatting_Adapter> adapter_data = new ArrayList<chatting_Adapter>();
    private ArrayList<chatting_Adapter> adapter_temp = new ArrayList<chatting_Adapter>();

    ListView listView;
    String grade = "0";
    EditText text_comment;
    String comment="";

    private final int ARR_MAX_SIZE = 300;
    private final int ARR_ADD_SIZE = 35;
    private int arrStart = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chatting);


        listView = (ListView) findViewById(R.id.listView);
        text_comment = (EditText) findViewById(R.id.text);


        new SetList().execute("1");
        serverCheck.showLoading(mContext);

    }



    public void onClick(View view) {// 버튼 눌렀을때
        switch (view.getId()) {
            case R.id.btn_send:// 전송

                comment=text_comment.getText().toString();
                new SetList().execute("2");


                break;
        }

    }


    static String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("MM-dd hh:mm");
        return f.format(new Date(time));
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
            if(type.equals("1")) {
                adapter_data.clear();
                String strTemp2 = "";
                strTemp2 = "SET_CHAT" + SC._del + arrStart + SC._del + ARR_ADD_SIZE + SC._del;


                if(ARR_MAX_SIZE > arrStart + ARR_ADD_SIZE)
                    adapter_data.add(new chatting_Adapter("0","0","더보기","0", false));

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
                strTemp = AndroidSocket.socket.GetMessage();

                serverCheck.hideLoading();
                if (strTemp.equals(TCP_SC.CANNOTGET)) {
                    return "1000";
                }

                String msg[] = strTemp.split(SC._endDel);
                for (int i = 0; i < msg.length; i++) {
                    String Msg[] = msg[i].split(SC._del);

                    Log.i("#받음2: ", "#" + msg[i] + "   " + Msg.length);
                    if (Msg[0].equals("0")) {
                        break;
                    }

                    check = true;
                    boolean temp = false;
                    if(Msg[0].equals(CUserInfo.id))
                        temp=true;

                    adapter_data.add(new chatting_Adapter(Msg[0], Msg[1], Msg[2], Msg[3], temp));
                    Log.i("#받음23: ", "#" + msg[i] + "   " + Msg.length);

                }
                return "1";
            }
            else if(type.equals("2")) {
                arrStart++;

                comment = comment.replace("\n"," ");

                String strTemp = "SAVE_CHAT" + SC._del +
                        CUserInfo.id + SC._del +
                        CUserInfo.name + SC._del +
                        comment + SC._del+
                        getDate() + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(strTemp);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return "2";
            }else if(type.equals("3")) {
                arrStart += ARR_ADD_SIZE;


                String strTemp2 = "";
                strTemp2 = "SET_CHAT" + SC._del + arrStart + SC._del + ARR_ADD_SIZE + SC._del;


                adapter_temp.clear();
                adapter_data.remove(0);
                adapter_temp.addAll(adapter_data);

                adapter_data.clear();

                if(ARR_MAX_SIZE > arrStart + ARR_ADD_SIZE)
                    adapter_data.add(new chatting_Adapter("0","0","더보기","0", false));

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
                strTemp = AndroidSocket.socket.GetMessage();

                serverCheck.hideLoading();
                if (strTemp.equals(TCP_SC.CANNOTGET)) {
                    return "1000";
                }

                String msg[] = strTemp.split(SC._endDel);
                for (int i = 0; i < msg.length; i++) {
                    String Msg[] = msg[i].split(SC._del);

                    Log.i("#받음2: ", "#" + msg[i] + "   " + Msg.length);
                    if (Msg[0].equals("0")) {
                        break;
                    }

                    check = true;
                    boolean temp = false;
                    if(Msg[0].equals(CUserInfo.id))
                        temp=true;

                    adapter_data.add(new chatting_Adapter(Msg[0], Msg[1], Msg[2], Msg[3], temp));
                    Log.i("#받음23: ", "#" + msg[i] + "   " + Msg.length);

                }


//                for(int i=0;i<adapter_data.size();i++){
//                    Log.i("testt","adapter_data: "+adapter_data.get(i).getComment());
//                }
//                for(int i=0;i<adapter_temp.size();i++){
//                    Log.i("testt","adapter_temp: "+adapter_temp.get(i).getComment());
//                }


                adapter_data.addAll(adapter_temp);

                return "3";
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AndroidSocket.socket.CloseSocket();
            if (result.compareTo("1") == 0) {
                ListAdapter adapter = null;

                adapter = new ListAdapter(mContext, 0, adapter_data);
                listView.setAdapter(adapter);

                listView.setSelection(adapter_data.size() - 1);
//                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


            }
            else if (result.compareTo("2") == 0) {
                adapter_data.add(new chatting_Adapter(CUserInfo.id, CUserInfo.name, comment, getDate(), true));
                ListAdapter adapter = null;
                adapter = new ListAdapter(mContext, 0,adapter_data);
                listView.setAdapter(adapter);

                listView.setSelection(adapter_data.size() - 1);
                text_comment.setText("");
            }
            else if (result.compareTo("3") == 0) {
                if(!check){
                    adapter_data.remove(0);
                    Toast.makeText(chatting_Activity.this, "내용이 더 없습니다.", Toast.LENGTH_SHORT).show();

                }
                ListAdapter adapter = null;

                adapter = new ListAdapter(mContext, 0, adapter_data);
                listView.setAdapter(adapter);

                listView.setSelection(adapter_data.size() - ARR_ADD_SIZE);
//                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


            }

        }

    }


    //안드로이드 화면에 뛰우는거 마저 짜고
    //서버 짜야된다.
    //버전은 올렸다


    private class ListAdapter extends ArrayAdapter<chatting_Adapter> {

        private ArrayList<chatting_Adapter> mNoticeData;

        public ListAdapter(Context context, int resource,
                           ArrayList<chatting_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = null;
            {
                if (mNoticeData.get(position).getComment().equals("더보기")) {
                    rowView = inflater.inflate(R.layout.adapter_chatting_add,null, true);

                    LinearLayout linearLayout = (LinearLayout)rowView.findViewById(R.id.linearLayout);

                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //더보기 눌렀을 때


                            new SetList().execute("3");
                            serverCheck.showLoading(mContext);

                        }
                    });




                    return rowView;
                }
                if (mNoticeData.get(position).getIsitme()) {
                    rowView = inflater.inflate(R.layout.adapter_chatting_right,null, true);
                }
                else {
                    rowView = inflater.inflate(R.layout.adapter_chatting_left,null, true);
                }

            }
            //좌우
//            LinearLayout baseLayout = (LinearLayout) rowView.findViewById(R.id.layout_2);

            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView comment = (TextView) rowView.findViewById(R.id.comment);
            TextView date = (TextView) rowView.findViewById(R.id.date);


//            Log.i("test:getIsitme() ", "" + mNoticeData.get(position).getIsitme());
//            Log.i("test:getId ",""+mNoticeData.get(position).getId());
//            Log.i("test:getNickname ", "" + mNoticeData.get(position).getNickname());
//            Log.i("test:getComment ", "" + mNoticeData.get(position).getComment());
//            Log.i("test:getDate ", "" + mNoticeData.get(position).getDate());


            name.setText(mNoticeData.get(position).getNickname());
            comment.setText(mNoticeData.get(position).getComment());
            date.setText(mNoticeData.get(position).getDate());

//            comment.setTextColor(Color.parseColor("#000000"));
//            {
//                if (mNoticeData.get(position).getIsitme()) {
//                    comment.setBackground(this.getContext().getResources().getDrawable(R.drawable.talk_balloon_right));
//                } else {
//                    comment.setBackground(this.getContext().getResources().getDrawable(R.drawable.talk_balloon_left));
//                }
//            }


            return rowView;
        }
    }


}
