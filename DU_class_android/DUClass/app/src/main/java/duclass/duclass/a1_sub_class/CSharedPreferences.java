package duclass.duclass.a1_sub_class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CSharedPreferences{
	Context context;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	public CSharedPreferences(Context _context) {
		context = _context;
		pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
		editor = pref.edit();
	}
	//�ҷ�����
	public String getPreferences(String  key){
		
		String id=pref.getString(key, "");
		
		return id;
    }
     
    // �� �����ϱ�
    public void savePreferences(String  key,String data){
    	editor.putString(key, data);
        editor.commit();
    }
     
    // ��(Key Data) �����ϱ�
    public void removePreferences(String  key){
        editor.remove(key);
        editor.commit();
    }

	public boolean IsFirst(String key){
		String temp="";
		temp=pref.getString(key, "");

		if(temp.equals("") || temp==null)
			return true;
		else
			return false;
	}

    public int IsLogin(){
    	String temp="";
    	temp=pref.getString("isLogin", "");
    	if(temp.equals("") || temp==null)
    		return 0;//�α��� �ȵ�
    	else
    		return 1;//�̹� �α��� ��
    }
}
