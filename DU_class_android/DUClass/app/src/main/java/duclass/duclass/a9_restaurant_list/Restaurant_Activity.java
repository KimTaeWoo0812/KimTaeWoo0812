package duclass.duclass.a9_restaurant_list;

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
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import duclass.duclass.R;
import duclass.duclass.a10_restaurant_menu.menu_Activity;
import duclass.duclass.a10_restaurant_menu.menu_for_personnel_Activity;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;

/**
 * Created by kimtaewoo on 2016-08-17.
 */
public class Restaurant_Activity extends Activity {
    private ArrayList<Restaurant_Adapter> mNoticeData = new ArrayList<Restaurant_Adapter>();
    private ArrayList<Restaurant_Adapter> adapter_data = new ArrayList<Restaurant_Adapter>();


    private ArrayList<for_personnel_Adapter> mNoticeData2 = new ArrayList<for_personnel_Adapter>();
    private ArrayList<for_personnel_Adapter> adapter_data2 = new ArrayList<for_personnel_Adapter>();
    private Context mContext = this;
    private ListView listView;
    private boolean rankCase = true;// true = 전체, false = 교양

    Button btn1;
    Button btn2;

    duclass.duclass.a2_static_class.serverCheck serverCheck = new serverCheck();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_restaurant);

        listView = (ListView) findViewById(R.id.listView);

        btn1 = (Button) findViewById(R.id.Btn1);
        btn2 = (Button) findViewById(R.id.Btn2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                if(rankCase) { //학생 식당
                    CRestaurant_Info.num = mNoticeData.get(position).getNum();
                    CRestaurant_Info.name = mNoticeData.get(position).getName();
                    CRestaurant_Info.grade = mNoticeData.get(position).getGrade();
                    CRestaurant_Info.rank = mNoticeData.get(position).getRank();

                    Intent intent1 = new Intent(Restaurant_Activity.this,
                            menu_Activity.class);
                    startActivity(intent1);

                }
                else{ //교직원 식당
                    CRestaurant_Info.num = mNoticeData2.get(position).getNum();
                    CRestaurant_Info.name = mNoticeData2.get(position).getName();

                    Intent intent1 = new Intent(Restaurant_Activity.this,
                            menu_for_personnel_Activity.class);
                    startActivity(intent1);

                }

            }
        });

        btn2.setBackgroundColor(Color.WHITE);
        btn2.setTextColor(Color.YELLOW);

        btn1.setBackgroundColor(Color.parseColor("#00BAFF"));
        btn1.setTextColor(Color.parseColor("#ffffff"));

        new SetList().execute("1");
        serverCheck.showLoading(this);
    }


    public void onClick(View view) {// 버튼 눌렀을때
        switch (view.getId()) {
            case R.id.Btn1:// 학생 식당

                if(rankCase)
                    break;
                btn2.setBackgroundColor(Color.WHITE);
                btn2.setTextColor(Color.YELLOW);

                btn1.setBackgroundColor(Color.parseColor("#00BAFF"));
                btn1.setTextColor(Color.parseColor("#ffffff"));
                rankCase = !rankCase;

                if(adapter_data.size()>1){
                    ListAdapter adapter = new ListAdapter(mContext, 0,adapter_data);
                    listView.setAdapter(adapter);
                    break;
                }

                new SetList().execute("1");
                serverCheck.showLoading(this);
                break;

            case R.id.Btn2:// 교직원 식당
                if(!rankCase)
                    break;

                btn1.setBackgroundColor(Color.WHITE);
                btn1.setTextColor(Color.YELLOW);

                btn2.setBackgroundColor(Color.parseColor("#00BAFF"));
                btn2.setTextColor(Color.parseColor("#ffffff"));
                rankCase = !rankCase;

                adapter_data2.clear();

                adapter_data2.add(new for_personnel_Adapter("1","\t\t\t\t\t\t\t학생회관"));
                adapter_data2.add(new for_personnel_Adapter("4","\t\t\t\t\t\t\t성산홀"));
                adapter_data2.add(new for_personnel_Adapter("3","\t\t\t\t\t\t\t공대"));
//                adapter_data2.add(new for_personnel_Adapter("11","\t\t\t\t\t\t\t기숙사식당"));

                ListAdapter2 adapter = new ListAdapter2(mContext, 0, adapter_data2);
                listView.setAdapter(adapter);



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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            AndroidSocket.socket = AndroidSocket.shared();
            String strTemp2 = "";
            strTemp2 = "SET_RESTAURANT" + SC._del;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                    AndroidSocket.socket.SendMessage(strTemp2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            adapter_data.clear();

            int out = 0;
            while (AndroidSocket.socket.HasMessage()) {
                if (!serverCheck.canNotGet) {
                    serverCheck.canNotGet = false;
                    return "9";
                }
            }
            strTemp = AndroidSocket.socket.GetMessage();

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

//               1 #num
//               2 #name
//               3 #grade
//               4 #joinPeople


                adapter_data.add(new Restaurant_Adapter(Msg[0], Msg[1], Msg[2], k));
                k++;
                Log.i("#받음23: ", "#" + msg[i]+"   "+Msg.length);

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

                    adapter = new ListAdapter(mContext, 0,adapter_data);

                    listView.setAdapter(adapter);
                }
            }
        }

    }

    private class ListAdapter extends ArrayAdapter<Restaurant_Adapter> {
        public ListAdapter(Context context, int resource,
                           ArrayList<Restaurant_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.restaurant_adapter, null,
                    true);
            TextView ranking = (TextView) rowView.findViewById(R.id.ranking);
            TextView restaurant = (TextView) rowView.findViewById(R.id.restaurant);
            TextView avr = (TextView) rowView.findViewById(R.id.avr);
            RatingBar rating = (RatingBar) rowView.findViewById((R.id.ratingBar));

            if (mNoticeData.get(position).rank.compareTo("1") == 0) {
                ranking.setTextColor(Color.RED);
                restaurant.setTextColor(Color.RED);
                avr.setTextColor(Color.RED);
            } else if (mNoticeData.get(position).rank.compareTo("2") == 0) {
                ranking.setTextColor(Color.BLUE);
                restaurant.setTextColor(Color.BLUE);
                avr.setTextColor(Color.BLUE);
            } else if (mNoticeData.get(position).rank.compareTo("3") == 0) {
                ranking.setTextColor(Color.BLUE);
                restaurant.setTextColor(Color.BLUE);
                avr.setTextColor(Color.BLUE);
            }
            ranking.setText(mNoticeData.get(position).rank + "등");
            restaurant.setText(mNoticeData.get(position).getName());

            avr.setText(mNoticeData.get(position).getGrade() + "  ");

            rating.setRating(Float.valueOf(mNoticeData.get(position).getGrade()).floatValue());

            return rowView;
            // return super.getView(position, convertView, parent);
        }
    }



    private class ListAdapter2 extends ArrayAdapter<for_personnel_Adapter> {
        public ListAdapter2(Context context, int resource,
                           ArrayList<for_personnel_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData2 = noticeData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.for_personnel_adapter, null,
                    true);
            TextView name = (TextView) rowView.findViewById(R.id.name);

            name.setText(mNoticeData2.get(position).getName());

            return rowView;
            // return super.getView(position, convertView, parent);
        }
    }
}