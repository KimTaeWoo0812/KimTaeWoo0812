package duclass.duclass.a3_login_join;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;

import duclass.duclass.R;
import duclass.duclass.a1_sub_class.AndroidSocket;
import duclass.duclass.a1_sub_class.CSharedPreferences;
import duclass.duclass.a2_static_class.CUserInfo;
import duclass.duclass.a2_static_class.SC;
import duclass.duclass.a2_static_class.serverCheck;
import duclass.duclass.a4_mainpage.MainPageActivity;

/**
 * Created by kimtaewoo on 2016-10-03.
 */
public class Join_new_Activity extends Activity {

    CheckBox checkBox_1;
    CheckBox checkBox_2;
    EditText input_name;
    String name = "";
    Context context = this;
    CSharedPreferences sharedPreferences;
    String phoneNum = "";
    String d_id = "";
    String d_pw = "";

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
            setContentView(R.layout.activity_join_new);


        checkBox_1 = (CheckBox)findViewById(R.id.checkBox_1);
        checkBox_2 = (CheckBox)findViewById(R.id.checkBox_2);
        input_name = (EditText)findViewById(R.id.input_name);
        sharedPreferences = new CSharedPreferences(this);

            Log.i("login", "Join_new_Activity");

        TextView textView = (TextView)findViewById(R.id.textView_10);
        SpannableString content = new SpannableString("이전에 가입한 회원정보 연동");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);


    }

    public void onClick(View view) {// 버튼 눌렀을때
        switch (view.getId()) {
            case R.id.btn_submit:// 가입

                name=input_name.getText().toString();

                if(!checkBox_1.isChecked()) {
                    Toast.makeText(this, "이 서비스는 대구대 학생만 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!checkBox_2.isChecked()) {
                    Toast.makeText(this, "핸드폰 번호 사용에 동의하셔야 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(name.length()<2)
                {
                    Toast.makeText(this, "이름은 2자 이상 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }

                boolean permission = CheckPermission();
                Log.i("GetPermission","permission:  "+permission);

                if(!permission)
                    return;

                DialogSimple();

                break;
            case R.id.btn_2:// 이전에 가입한 정보
//                Intent intent5 = new Intent(Join_new_Activity.this, MainPageActivity.class);
//                startActivity(intent5);

                final Dialog ratingDialog = new Dialog(Join_new_Activity.this,R.style.Base_Theme_AppCompat_Light_Dialog);
                ratingDialog.setContentView(R.layout.dialog_useing_before_info);
                ratingDialog.setCancelable(true);

                final EditText input_id = (EditText) ratingDialog.findViewById(R.id.input_id);
                final EditText input_pw = (EditText) ratingDialog.findViewById(R.id.input_pw);


                Button btn_1 = (Button) ratingDialog
                        .findViewById(R.id.btn_1);
                btn_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.i("###", "###" + input_id.getText().toString());
                        d_id = input_id.getText().toString();
                        d_pw = input_pw.getText().toString();


                        if(d_id.length()==0) {
                            Toast.makeText(Join_new_Activity.this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(d_pw.length()==0){
                            Toast.makeText(Join_new_Activity.this, "PW를 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean permission = CheckPermission();

                        if(!permission)
                            return;

                        new SendMsgTask().execute("2");
                        serverCheck.showLoading(Join_new_Activity.this);
                        ratingDialog.dismiss();
                    }
                });

                Button btn_2 = (Button) ratingDialog
                        .findViewById(R.id.btn_2);
                btn_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratingDialog.cancel();
                    }
                });
                ratingDialog.show();


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

                TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
                phoneNum = telManager.getLine1Number();

                msg = "NEW_JOIN_US" + SC._del + phoneNum + SC._del + name+SC._del;

                try {
                    AndroidSocket.socket.SendMessage(msg);
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
                msg = AndroidSocket.socket.GetMessage();// 서버로부터 받기

                return "1";
            }
            else if (type.compareTo("2") == 0) {

                TelephonyManager telManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
                phoneNum = telManager.getLine1Number();

                msg = "NEW_ID_CHECK" + SC._del + phoneNum + SC._del + d_id + SC._del + d_pw + SC._del;

                try {
                    AndroidSocket.socket.SendMessage(msg);
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
                msg = AndroidSocket.socket.GetMessage();// 서버로부터 받기

                return "2";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AndroidSocket.socket.CloseSocket();

            if (result.compareTo("1") == 0) {
                String arrMsg2[] = msg.split(SC._del);
                if (arrMsg2[1].compareTo("1") == 0) {

                    CUserInfo.name = name;
                    CUserInfo.id = phoneNum;

                    sharedPreferences.savePreferences("isLogin","1");
                    sharedPreferences.savePreferences("id",phoneNum);
                    sharedPreferences.savePreferences("name",name);
                    CUserInfo.commentNum = 0;


                    Toast.makeText(Join_new_Activity.this, "회원가입 성공!\n별명은 "+name+" 입니다.", Toast.LENGTH_SHORT).show();
//                    long saveTime = System.currentTimeMillis();
//                    long currTime = 0;
//                    while (currTime - saveTime > 2000) {
//                        currTime = System.currentTimeMillis();
//                    }
//                    finish();// 이 화면 종료


                    Intent intent5 = new Intent(Join_new_Activity.this, MainPageActivity.class);
                    startActivity(intent5);
                    finish();// 이 화면 종료


                    return;
                }
                else if(arrMsg2[1].compareTo("2") == 0){

                    CUserInfo.name = name;
                    CUserInfo.id = phoneNum;

                    sharedPreferences.savePreferences("isLogin","1");
                    sharedPreferences.savePreferences("id",phoneNum);
                    sharedPreferences.savePreferences("name",name);
                    CUserInfo.commentNum = Integer.parseInt(arrMsg2[2]);


                    Toast.makeText(Join_new_Activity.this, "기존 회원 정보 연동!\n별명은 "+name+" 입니다.", Toast.LENGTH_SHORT).show();
//                    long saveTime = System.currentTimeMillis();
//                    long currTime = 0;
//                    while (currTime - saveTime > 2000) {
//                        currTime = System.currentTimeMillis();
//                    }


                    Intent intent5 = new Intent(Join_new_Activity.this, MainPageActivity.class);
                    startActivity(intent5);
                    finish();// 이 화면 종료


                    return;
                }
                else {
                    Toast.makeText(Join_new_Activity.this, "회원가입 실패! 다시 시도해 주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else if (result.compareTo("2") == 0) {
                String arrMsg2[] = msg.split(SC._del);
                if (arrMsg2[1].compareTo("9999999") == 0) {
                    Toast.makeText(Join_new_Activity.this, "정보가 틀리거나 없는 회원입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;

                }
                CUserInfo.name = arrMsg2[2];
                CUserInfo.id = phoneNum;

                sharedPreferences.savePreferences("isLogin", "1");
                sharedPreferences.savePreferences("id", phoneNum);
                CUserInfo.commentNum = Integer.parseInt(arrMsg2[1]);
                sharedPreferences.savePreferences("name", arrMsg2[2]);


                Toast.makeText(Join_new_Activity.this, "회원가입 성공!\n별명은 " + arrMsg2[2] + " 입니다.", Toast.LENGTH_SHORT).show();
//                    long saveTime = System.currentTimeMillis();
//                    long currTime = 0;
//                    while (currTime - saveTime > 2000) {
//                        currTime = System.currentTimeMillis();
//                    }
//                    finish();// 이 화면 종료


                Intent intent5 = new Intent(Join_new_Activity.this, MainPageActivity.class);
                startActivity(intent5);
                finish();


                return;
            }
        }

    }




    private void DialogSimple(){
        String clause = "1. 개인정보의 취급목적\n" +
                "\n" +
                "DU여론은 회원가입 시 서비스 이용을 위해 필요한 최소한의 개인정보만을 수집합니다. 귀하가 DU여론의 서비스를 이용하기 위해서는 회원가입 시 (핸드폰 번호, 별명)을 필수적으로 입력하셔야 합니다.\n" +
                "\n" +
                "개인정보 항목별 구체적인 수집목적 및 이용목적은 다음과 같습니다.\n" +
                "핸드폰 번호, 별명 :회원제 서비스 이용에 따른 본인 식별 절차에 이용.\n" +
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
                        name = name.replace("\n","");

                        new SendMsgTask().execute("1");
                        serverCheck.showLoading(Join_new_Activity.this);
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

    private boolean CheckPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            Log.i("GetPermission","권한 없음");
            // 권한 없음
            GetPermission();

            return false;

        }else{
            Log.i("GetPermission","권한 있음");
            // 권한 있음
            return true;
        }
    }
    private void GetPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(Join_new_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Log.i("GetPermission", "onPermissionGranted");

                DialogSimple();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Log.i("GetPermission", "onPermissionDenied");
                //Toast.makeText(Join_new_Activity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("회원 고유 식별자로 사용할 핸드폰 번호를 받아오는데 권한이 필요합니다.")
                .setDeniedMessage("회원 고유 식별자로 사용할 핸드폰 번호를 받아오는데 권한이 필요합니다.\n\n[설정] > [권한]에서 해당 권한을 활성화해주세요")
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();

    }

}
