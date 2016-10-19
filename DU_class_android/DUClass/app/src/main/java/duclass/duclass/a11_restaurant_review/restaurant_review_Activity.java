package duclass.duclass.a11_restaurant_review;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a9_restaurant_list.CRestaurant_Info;

/**
 * Created by kimtaewoo on 2016-10-01.
 */
public class restaurant_review_Activity extends Activity {


    Context mContext = this;
    private ArrayList<review_Adapter> adapter_data = new ArrayList<review_Adapter>();

    ListView listView;
    String grade = "0";
    EditText text_comment;
    String comment="";
    CSharedPreferences sharedPreferences;
    String commendNumForDelete = "";


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
        setContentView(R.layout.activity_review);


        AndroidSocket.socket = AndroidSocket.shared();
        sharedPreferences = new CSharedPreferences(this);


        listView = (ListView) findViewById(R.id.listView);
        text_comment = (EditText) findViewById(R.id.editText3);

        new SetList().execute("1");
        serverCheck.showLoading(mContext);
    }

    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("MM");
        return f.format(new Date(time));
    }

    public void onClick(View view) {// 버튼 눌렀을때
        switch (view.getId()) {
            case R.id.button4:// 작성

                boolean canido = false;

                Log.e("###", "" + sharedPreferences.IsFirst("review" + CRestaurant_Info.num));
            {
                if (sharedPreferences.IsFirst("review" + CRestaurant_Info.num)) //이전에 한적이 없으면
                {
                    canido = true;
                } else {
                    int beforeDate = Integer.parseInt(sharedPreferences.getPreferences("review" + CRestaurant_Info.num));
                    int nowDate = Integer.parseInt(getDate());

//                            Log.e("###  ", "beforeDate   " + beforeDate + "  " + nowDate);
//                            Log.e("###  ", "beforeDate -  " + Math.abs(nowDate - beforeDate));
                    if (Math.abs(nowDate - beforeDate) != 0) {
                        //작성한지 한달이 넘었으면
//                        Toast.makeText(menu_Activity.this, "한달이 넘었네", Toast.LENGTH_SHORT).show();

                        canido = true;
                    } else {
                        Toast.makeText(restaurant_review_Activity.this, "같은 식당 후기는 한달에 한번 가능합니다", Toast.LENGTH_SHORT).show();
                        canido = false;
                    }
                }
            }

            if (canido) {
                final Dialog ratingDialog = new Dialog(restaurant_review_Activity.this, R.style.Base_Theme_AppCompat_Light_Dialog);
                ratingDialog.setContentView(R.layout.dialog_for_menu);
                ratingDialog.setCancelable(true);
                final RatingBar ratingBar4 = (RatingBar) ratingDialog
                        .findViewById(R.id.ratingBar4);

                ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {

                    }
                });

                Button button7 = (Button) ratingDialog
                        .findViewById(R.id.button7); // 확인 버튼
                button7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment = text_comment.getText().toString();
                        grade = String.valueOf(ratingBar4.getRating());
                        new SetList().execute("2");

                        serverCheck.showLoading(mContext);
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        new SetList().execute("1");
                        text_comment.setText("");
                        ratingDialog.dismiss();
                    }
                });

                Button button9 = (Button) ratingDialog
                        .findViewById(R.id.button9); // 취소 버튼
                button9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratingDialog.cancel();
                    }
                });
                ratingDialog.show();


            }
        }
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
            if(type.equals("1")) {
                adapter_data.clear();
                String strTemp2 = "";
                strTemp2 = "SET_REVIEW" + SC._del + CRestaurant_Info.num + SC._del;

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

                Log.i("#받음 후기: ", "#" + strTemp);
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

//                    public String num = "";
//                    public String id = "";
//                    public String nickname = "";
//                    public String comment = "";
//                    public String gradesAvr = "";
//                    public String date = "";

                    check = true;
                    adapter_data.add(new review_Adapter(Msg[0], Msg[1], Msg[2], Msg[3], Msg[4], Msg[5]));
                    Log.i("#받음23: ", "#" + msg[i] + "   " + Msg.length);

                }
                return "1";
            }
            else if(type.equals("2")) {
                sharedPreferences.savePreferences("review" + CRestaurant_Info.num, getDate()); //했던거 저장

                comment = comment.replace("\n"," ");

                String strTemp = "SAVE_REVIEW" + SC._del +
                        CRestaurant_Info.num + SC._del +
                        CUserInfo.id + SC._del +
                        CUserInfo.name + SC._del +
                        comment + SC._del +
                        grade + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(strTemp);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return "2";
            }else if(type.equals("3")) {

               String strTemp = "DELETE_REVIEW" + SC._del + commendNumForDelete + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(strTemp);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return "3";
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                    ListAdapter adapter = null;

                    adapter = new ListAdapter(mContext, 0,adapter_data);
                    listView.setAdapter(adapter);

            }
            else if (result.compareTo("2") == 0) {
                Toast.makeText(restaurant_review_Activity.this,"소중한 의견 감사합니다.",Toast.LENGTH_SHORT);

            }
            else if (result.compareTo("3") == 0) {
                Toast.makeText(restaurant_review_Activity.this,"삭제 완료",Toast.LENGTH_SHORT);

            }
        }

    }



    private class ListAdapter extends ArrayAdapter<review_Adapter> {

        private ArrayList<review_Adapter> mNoticeData;

        public ListAdapter(Context context, int resource,
                           ArrayList<review_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.adapter_review,
                    null, true);
            TextView txtNickname = (TextView) rowView
                    .findViewById(R.id.nickname);
            TextView txtComment = (TextView) rowView
                    .findViewById(R.id.commenttext);
            TextView date = (TextView) rowView
                    .findViewById(R.id.date);
            RatingBar ratingBar3 = (RatingBar) rowView
                    .findViewById(R.id.ratingBar3);
            LinearLayout cancel = (LinearLayout) rowView.findViewById(R.id.cancel);


//            Log.i("#","#asdasdasdasdasd   "+position)
//            Log.i("#test getNickname","#"+mNoticeData.get(position).num);
//            Log.i("#test getNickname","#"+mNoticeData.get(position).id);
//            Log.i("#test getNickname","#"+mNoticeData.get(position).getNickname());
//            Log.i("#test getComment","#"+mNoticeData.get(position).getComment());
//            Log.i("#test getGradesAvr","#"+mNoticeData.get(position).getGradesAvr());
//            Log.i("#test getDate","#"+mNoticeData.get(position).getDate());

            {
                if (!mNoticeData.get(position).getId().equals(CUserInfo.id)) {
                    cancel.setVisibility(View.INVISIBLE);
                }
                else{
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //삭제 눌렀을때

                            commendNumForDelete = mNoticeData.get(position).getNum();
                            Dialog_for_delete_comment();

                        }
                    });
                }

            }
            txtNickname.setText(mNoticeData.get(position).getNickname());
            txtComment.setText(mNoticeData.get(position).getComment());
            date.setText(mNoticeData.get(position).getDate());
            ratingBar3.setRating(Float.valueOf(
                    mNoticeData.get(position).getGradesAvr()).floatValue());

            return rowView;
            // return super.getView(position, convertView, parent);
        }
    }
    private void Dialog_for_delete_comment() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("삭제하시겠습니까?").setCancelable(false)
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new SetList().execute("3");
                        serverCheck.showLoading(mContext);
                        try {
                            Thread.sleep(35);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        new SetList().execute("1");

                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("");
        // Icon for AlertDialog
        alert.show();

    }
}
