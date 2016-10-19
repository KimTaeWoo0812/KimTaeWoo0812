package duclass.duclass.a10_restaurant_menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import duclass.duclass.R;
import duclass.duclass.a9_restaurant_list.CRestaurant_Info;

/**
 * Created by kimtaewoo on 2016-08-19.
 */
public class menu_for_personnel_Activity extends Activity {
    Context mContext = this;
    ListView listView;
    TextView restaurantName;
    private ArrayList<menu_for_personnel_Adapter> mNoticeData = new ArrayList<menu_for_personnel_Adapter>();
    private ArrayList<menu_for_personnel_Adapter> adapter_data = new ArrayList<menu_for_personnel_Adapter>();
    private serverCheck ServerCheck = new serverCheck();
    private String strToday;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_menu_for_personnel);

        listView = (ListView) findViewById(R.id.listView2);
        restaurantName = (TextView)findViewById(R.id.restaurant);


        strToday = doDayOfWeek();

        restaurantName.setText(CRestaurant_Info.name);
        new SetList().execute("1");
        ServerCheck.showLoading(this);
    }

    private String doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        String strWeek = null;

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1) {
            strWeek = "일";
        } else if (nWeek == 2) {
            strWeek = "월";
        } else if (nWeek == 3) {
            strWeek = "화";
        } else if (nWeek == 4) {
            strWeek = "수";
        } else if (nWeek == 5) {
            strWeek = "목";
        } else if (nWeek == 6) {
            strWeek = "금";
        } else if (nWeek == 7) {
            strWeek = "토";
        }

        return strWeek;
    }



    public class SetList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("http://www.daegu.ac.kr/web/sub_title/student/sub4_1.asp?mm_num="+ CRestaurant_Info.num).get();
                org.jsoup.select.Elements contents = doc.select("table.table4 tbody tr td");
                String text;

                for(int i=1;i<contents.size()-1;i++){
                    text = contents.get(i).text().replace(" ","\n");
//                System.out.println(text.replace(" ",", "));

                    adapter_data.add(new menu_for_personnel_Adapter(DefineDay(i), text));

//				System.out.println(contents.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "1";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ServerCheck.hideLoading();
            if (result.compareTo("1") == 0) {
                ListAdapter adapter = null;
                adapter = new ListAdapter(mContext, 0,adapter_data);

                listView.setAdapter(adapter);
            }
        }

    }

    private String DefineDay(int i){

        switch (i){
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            default:
                return "토";
        }
    }

    private class ListAdapter extends ArrayAdapter<menu_for_personnel_Adapter> {
        public ListAdapter(Context context, int resource,
                           ArrayList<menu_for_personnel_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.adapter_menu_for_personnel, null,
                    true);

            TextView num = (TextView) rowView.findViewById(R.id.num);
            TextView name = (TextView) rowView.findViewById(R.id.name);

            if(strToday.equals(mNoticeData.get(position).getNum())){
                num.setTextColor(Color.rgb(255, 64, 129));
                name.setTextColor(Color.rgb(255, 64, 129));
            }




            num.setText(mNoticeData.get(position).getNum());
            name.setText(mNoticeData.get(position).getName());

            return rowView;
            // return super.getView(position, convertView, parent);
        }
    }


    private class serverCheck {

        private Handler mHandler;
        private Runnable mRunnable;
        private ProgressDialog m_loadingDialog = null;
        Context baseContext;

        public boolean flag_forServer = false;

        public void showLoading(final Context context) {
            baseContext = context;
            if (m_loadingDialog == null) {

                Log.e("test", "con: " + context);
                m_loadingDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);

                m_loadingDialog.setIndeterminate(true);
                m_loadingDialog.setCancelable(false);
                m_loadingDialog.setMessage("데이터 수신 중...");

            }
            flag_forServer = true;
            m_loadingDialog.show();
            currentActivity(context);

        }

        public void hideLoading() {

            if (m_loadingDialog != null) {
                m_loadingDialog.dismiss();
                m_loadingDialog = null;
            }
        }

        public void currentActivity(final Context mContext) {
            mRunnable = new Runnable() {
                @Override
                public void run() {

                    if (m_loadingDialog != null) {
                        if (flag_forServer) {
                            Toast toast = Toast.makeText(mContext,
                                    "네트워크 상태가 원활하지 않습니다.", Toast.LENGTH_SHORT);
                            toast.show();

                            m_loadingDialog.dismiss();
                            m_loadingDialog = null;
                        }
                    }
                }
            };

            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, 8000);
        }

    }
}
