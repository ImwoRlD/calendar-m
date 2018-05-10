package cc.yy.calendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.yy.calendar.R;
import cc.yy.calendar.db.DBOper;

public class PasswordManage extends Activity{
    private EditText et1;
    private EditText et2;
    private Button button1;
//    private Button button2;
    private DBOper db;
    private int CHANGE_PWD_SUCCESS = 100;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        db = new DBOper(this);
        setContentView(R.layout.activity_password_manage);
        et1 = (EditText)findViewById(R.id.new_password);
        et2 = (EditText)findViewById(R.id.repeat_password);
        button1 = (Button)findViewById(R.id.save_password);

        button1.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {
                String new_pwd = et1.getText().toString();
                String repeat_pwd = et2.getText().toString();
                if(new_pwd==null||new_pwd.equals("")){
                    Toast.makeText(PasswordManage.this, "密码为空", Toast.LENGTH_LONG).show();
                }else{
                    if(new_pwd.equals(repeat_pwd)){
                        if(db.getPwd().equals("")){
                            db.insertPwd(new_pwd);
                            Toast.makeText(PasswordManage.this, "密码设置成功", Toast.LENGTH_LONG).show();
                            toNoteList();
                        }else{
                            db.updatePwd(new_pwd);
                            Toast.makeText(PasswordManage.this, "密码修改成功", Toast.LENGTH_LONG).show();
                            PasswordManage.this.setResult(CHANGE_PWD_SUCCESS);
                            PasswordManage.this.finish();
                            toNoteList();
                        }
                    }else{
                        Toast.makeText(PasswordManage.this, "密码不相符", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void toNoteList(){
        Toast.makeText(PasswordManage.this, "密码设置成功", Toast.LENGTH_LONG);
        Intent intent = new Intent(PasswordManage.this,EditNoteActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
