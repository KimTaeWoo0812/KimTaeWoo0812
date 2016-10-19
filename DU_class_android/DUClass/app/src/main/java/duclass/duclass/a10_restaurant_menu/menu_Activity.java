package duclass.duclass.a10_restaurant_menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import duclass.duclass.R;
import duclass.duclass.a10_restaurant_menu.search.KoreanTextMatch;
import duclass.duclass.a10_restaurant_menu.search.KoreanTextMatcher;
import duclass.duclass.a11_restaurant_review.restaurant_review_Activity;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a9_restaurant_list.CRestaurant_Info;

/**
 * Created by kimtaewoo on 2016-08-18.
 */
public class menu_Activity  extends Activity {

    RatingBar ratingBar;
    TextView restaurant;
    ListView listView;
    EditText search;
    Context mContext = this;
    String stringRating = "";
    String menuNum = "";
    private ArrayList<menu_Adapter> mNoticeData = new ArrayList<menu_Adapter>();
    private ArrayList<menu_Adapter> adapter_data = new ArrayList<menu_Adapter>();
    private ArrayList<menu_Adapter> search_data = new ArrayList<menu_Adapter>();
    CSharedPreferences sharedPreferences;
    KoreanTextMatch match;
    View upSideLayout;
    boolean isSearch= false;
    private Random mRandom = new Random();
    private final int N_RECOMMEND_STANDARD = 3;
    //private View stickyViewSpacer;
    //EditText stickyView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidSocket.socket.CloseSocket();
    }
    @Override
    protected void onResume() {
        super.onResume();
        AndroidSocket.socket = AndroidSocket.shared();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_menu);

        AndroidSocket.socket = AndroidSocket.shared();
        //ViewDynamicChange();
        sharedPreferences = new CSharedPreferences(this);

        upSideLayout = findViewById(R.id.upSideLayout);
        search = (EditText) findViewById(R.id.search);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        restaurant = (TextView) findViewById(R.id.restaurant);
        listView = (ListView) findViewById(R.id.listView2);
        ratingBar.setRating(Float.valueOf(CRestaurant_Info.grade).floatValue());
        restaurant.setText(CRestaurant_Info.name);

        //stickyView = (EditText) findViewById(R.id.search);

         /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.school_list_list_header, null);
        //stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        listView.addHeaderView(listHeader);

        /* Handle list View scroll events */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();

                    }

                    //int heroTopY = stickyViewSpacer.getTop();
                    //stickyView.setY(Math.max(0, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                    upSideLayout.setY(topY * 0.9f);
//                    Log.e("###","y알기"+topY * 0.9f);
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때

                //listView.setSelection(300);
//                int index = listView.getFirstVisiblePosition();
//                listView.smoothScrollToPosition(index);
                Log.e("##", "포지션: " +listView.getFirstVisiblePosition());
                Log.e("##",""+listView.getCheckedItemPosition());



//                Log.i("###",""+s.toString());
                if(s.toString().equals("")){
                    Log.i("###","널");
                    isSearch=false;
                    ListAdapter adapter = null;
                    adapter = new ListAdapter(mContext, 0,adapter_data);
                    listView.setAdapter(adapter);

                    return;
                }

                String text;
                String pattern;
                search_data.clear();
//                Log.i("###", "adapter_data.size(): "+adapter_data.size());

                for(int i=0;i<adapter_data.size();i++) {
                    text = adapter_data.get(i).getName(); //리스트를 하나씩 비교
                    pattern = s.toString();
//                    Log.i("###",""+text+"   "+pattern);

                    match = KoreanTextMatcher.match(text, pattern);
                    if (match.success()) {
//                        Log.i("###","match!");

                        //여기서 리스트뷰 갱신
                        search_data.add(new menu_Adapter(adapter_data.get(i).getNum(),adapter_data.get(i).getName(),adapter_data.get(i).getGrade(), Integer.valueOf(adapter_data.get(i).getRank())));

                        isSearch=true;
//                        System.out.format("%s: %s[%d]에서 시작, 길이 %d\n",
//                                match.value(), text, match.index(), match.length());
                    }
                }

                ListAdapter adapter = null;
                adapter = new ListAdapter(mContext, 0,search_data);
                listView.setAdapter(adapter);

                Log.i("###", "리스트뷰 갱신!!");
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                Log.i("###", "afterTextChanged");
//                listView.smoothScrollToPosition(4);
                int index=listView.getFirstVisiblePosition()+1;

                listView.setSelection(index);




                upSideLayout.setY(-597);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        position--;
                        Log.e("###  ", "position   " + position);
                        Log.e("###  ", "isSearch   " + isSearch);
                        {
                            if (isSearch) {
                                Log.e("###  ", "position   " + search_data.get(position).getName());
                                Log.e("###  ", "position   " + search_data.get(position).getGrade());

                            }
                            else if(!isSearch){
                                Log.e("###  ", "position2   " + adapter_data.get(position).getName());
                                Log.e("###  ", "position2   " + adapter_data.get(position).getGrade());

                            }
                        }





                        menuNum = mNoticeData.get(position).getNum();

                        Log.e("###", "" + sharedPreferences.IsFirst(CRestaurant_Info.num + menuNum));

                        if (sharedPreferences.IsFirst(CRestaurant_Info.num + menuNum)) //이전에 한적이 없으면
                        {
                            click();
                        } else {
                            int beforeDate = Integer.parseInt(sharedPreferences.getPreferences(CRestaurant_Info.num + menuNum));
                            int nowDate = Integer.parseInt(getDate());

//                            Log.e("###  ", "beforeDate   " + beforeDate + "  " + nowDate);
//                            Log.e("###  ", "beforeDate -  " + Math.abs(nowDate - beforeDate));
                            if (Math.abs(nowDate - beforeDate) != 0) {
                                //작성한지 한달이 넘었으면
//                        Toast.makeText(menu_Activity.this, "한달이 넘었네", Toast.LENGTH_SHORT).show();

                                click();
                            } else {
                                Toast.makeText(menu_Activity.this, "같은 메뉴 평가는 한달에 한번 가능합니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        new SetList().execute("1");
        serverCheck.showLoading(mContext);
    }





    public void onClick(View view) {// 버튼 눌렀을때
        switch (view.getId()) {
            case R.id.btn_recommend:// 메뉴 추천
                int adapter_size = adapter_data.size()-1;
                //mRandom
                int n_random = mRandom.nextInt(adapter_size);

                for(;;){
                    //3
                    if(N_RECOMMEND_STANDARD < Double.valueOf(adapter_data.get(n_random).getGrade()).doubleValue()){
                        break;
                    }
                    n_random = mRandom.nextInt(adapter_size);
                }

                //다이얼로그 뛰우기
                Dialog_for_recommend(adapter_data.get(n_random).getName(), adapter_data.get(n_random).getGrade());

                break;
            case R.id.review:// 식당 후기
                Intent intent = new Intent(menu_Activity.this, restaurant_review_Activity.class);
                startActivity(intent);

                break;
        }
    }
    private void Dialog_for_recommend(String str, String grade){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("지금 이 순간, 당신에게\n'"+str+"'을(를) 추천합니다.\n메뉴 별점: "+grade).setCancelable(
                false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("추천메뉴");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.icon_50);
        alert.show();
    }


    private void click() {
        final Dialog ratingDialog = new Dialog(menu_Activity.this, R.style.Base_Theme_AppCompat_Light_Dialog);
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
                stringRating = String.valueOf(ratingBar4.getRating());
                new SetList().execute("2");


                new SetList().execute("1");
                serverCheck.showLoading(mContext);

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


        return ;
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
            if(type.equals("1")) {
                adapter_data.clear();
                String strTemp2 = "";
                strTemp2 = "SET_MENU" + SC._del + CRestaurant_Info.num + SC._del;

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

                Log.i("#받음 랭킹: ", "#" + strTemp);
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

//               1 #num
//               2 #name
//               3 #grade
//               4 #joinPeople


                    adapter_data.add(new menu_Adapter(Msg[0], Msg[1], Msg[2], k));
                    k++;
                    Log.i("#받음23: ", "#" + msg[i] + "   " + Msg.length);

                }
                return "1";
            }
            else if(type.equals("2")) {
                String strTemp2 = "";
                strTemp2 = "SAVE_GRADE" + SC._del + CRestaurant_Info.num + SC._del + menuNum + SC._del + stringRating + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(strTemp2);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                sharedPreferences.savePreferences(CRestaurant_Info.num + menuNum, getDate()); //했던거 저장

                return "2";
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                if (k != 0) {
                    android.widget.ListAdapter adapter = null;

                    adapter = new ListAdapter(mContext, 0,adapter_data);

                    listView.setAdapter(adapter);
                }
            }
            else if (result.compareTo("2") == 0) {
                Toast.makeText(menu_Activity.this,"소중한 의견 감사합니다.",Toast.LENGTH_SHORT);

            }
        }

    }

    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("MM");
        return f.format(new Date(time));
    }

    private class ListAdapter extends ArrayAdapter<menu_Adapter> {
        public ListAdapter(Context context, int resource,
                           ArrayList<menu_Adapter> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.menu_adapter, null,
                    true);
            TextView ranking = (TextView) rowView.findViewById(R.id.ranking);
            TextView menu = (TextView) rowView.findViewById(R.id.menu);
            TextView avr = (TextView) rowView.findViewById(R.id.avr);
            RatingBar rating = (RatingBar) rowView.findViewById((R.id.ratingBar));

            if (mNoticeData.get(position).rank.compareTo("1") == 0) {
                ranking.setTextColor(Color.RED);
                menu.setTextColor(Color.RED);
                avr.setTextColor(Color.RED);
            } else if (mNoticeData.get(position).rank.compareTo("2") == 0) {
                ranking.setTextColor(Color.BLUE);
                menu.setTextColor(Color.BLUE);
                avr.setTextColor(Color.BLUE);
            } else if (mNoticeData.get(position).rank.compareTo("3") == 0) {
                ranking.setTextColor(Color.BLUE);
                menu.setTextColor(Color.BLUE);
                avr.setTextColor(Color.BLUE);
            }
            ranking.setText(mNoticeData.get(position).rank + "등");
            menu.setText(mNoticeData.get(position).getName());
            rating.setRating(Float.valueOf(mNoticeData.get(position).getGrade()).floatValue());

            avr.setText(mNoticeData.get(position).getGrade()+"  ");
            return rowView;
            // return super.getView(position, convertView, parent);
        }
    }
}
