package duclass.duclass.a0_SplashPackage;

/**
 * Created by Karthik's on 27-02-2016.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.IOException;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a1_sub_class.TCP_SC;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a3_login_join.MainActivity;
import duclass.duclass.a3_login_join.RoadingSplash;
import duclass.duclass.a4_mainpage.MainPageActivity;

public class SplashActivity extends Activity {
    public AndroidSocket socket;
    CSharedPreferences sharedPreferences;
    String id = "";
    String pw = "";
    int isLogin ;
    Context mContext = this;
    private static int SPLASH_TIME_OUT = 3000;
    private KenBurnsView mKenBurns;

    @Override
    protected void onPause() {
        super.onPause();

        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setAnimation();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        mKenBurns.setImageResource(R.drawable.splash_background2);


        SC._del+=(char)200;
        SC._endDel+=(char)201;
        SC._endSendDel+=(char)202;
        TCP_SC.SetData();

        sharedPreferences = new CSharedPreferences(this);
        socket = AndroidSocket.shared();


        isLogin = sharedPreferences.IsLogin();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new SendMsgTask().execute("1");
                serverCheck.showLoading(mContext);
            }
        }, SPLASH_TIME_OUT);

    }

    private void setAnimation() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(2000);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(2000);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(100);
        animatorSet.start();

        findViewById(R.id.imagelogo).setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        findViewById(R.id.imagelogo).startAnimation(anim);
    }




    private String SendLoginMsg() {

        String strMsg = "LOGIN" + SC._del + id + SC._del + pw + SC._del;

        try {
            AndroidSocket.socket.SendMessage(strMsg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (socket.HasMessage()){
            if(serverCheck.canNotGet){
                serverCheck.canNotGet=false;
                return "9";
            }
        }
        serverCheck.hideLoading();
        strMsg = socket.GetMessage();// 서버로부터 받기
        String strTemp[]=strMsg.split(SC._del);

        if(!strTemp[1].equals("2"))
            CUserInfo.name=strTemp[2];

        return strMsg;
    }
    /**
     * 리턴 1은 정상, 2는 실패
     * @return
     */
    private int RoadingMsg() {
        String strMsg = "ROADING" + SC._del;

        try {
            AndroidSocket.socket.SendMessage(strMsg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (AndroidSocket.socket.HasMessage()){
            if(serverCheck.canNotGet){
                serverCheck.canNotGet=false;
                return 0;
            }
        }
        serverCheck.hideLoading();
        strMsg = AndroidSocket.socket.GetMessage();// 서버로부터 받기




        return 1;
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
            if (isLogin == 1)// 이미 로그인 했다면 다음 엑티비티로 넘어간다.
            {
                id = sharedPreferences.getPreferences("id");
                pw = "1";


                socket.id = id;
                CUserInfo.id = id;

                String msg[] = SendLoginMsg().split(SC._del);

                Log.i("###", "" + msg[1]);
                if(msg[1].equals("2")){
                    Log.i("###22", ""+msg[1]);
                    return "2";
                }
                if(msg[1].compareTo("9") == 0)
                    return "9";//연결실패
                else
                    return "1";//이미로그인
            }



            int temp = RoadingMsg();
            {
                if(temp==1)
                    return "2";//로그인 페이지로
                else
                    return "9";//연결 실패
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            AndroidSocket.socket.CloseSocket();
            Log.i("###3", "" + result);
            if (result.compareTo("9") == 0)//연결 실패
            {
                DialogSimple();
            }

            else if (isLogin == 1 && !result.equals("2"))// 이미 로그인 했다면 다음 엑티비티로 넘어간다.
            {

                // servercheck.showLoading(this);

                Intent intent5 = new Intent(SplashActivity.this,
                        MainPageActivity.class);
                startActivity(intent5);
                finish();// 이 화면 종료

                // new SendMsgTask().execute("2");

            } else {
                Intent intent5 = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent5);
                finish();// 이 화면 종료

            }
        }

    }
    //다이얼로그
    private void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("다시 시도하시겠습니까?").setCancelable(
                false).setPositiveButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something
                        finish();// 이 화면 종료
                        dialog.cancel();
                    }
                }).setNegativeButton("다시 시도",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent5 = new Intent(SplashActivity.this,
                                RoadingSplash.class);
                        startActivity(intent5);
                        finish();// 이 화면 종료

                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("서버와 연결 실패");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }
}