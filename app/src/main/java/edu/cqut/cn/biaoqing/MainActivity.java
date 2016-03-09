package edu.cqut.cn.biaoqing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "main";
    ImageView mImageView;
    EditText mEditText;
    Button btn_save;
    Button btn_create;

    Bitmap bitmap;
    Bitmap newBitmap;
    Canvas mCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);

        findViews();
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.draw);
        mImageView.setImageBitmap(bitmap);

    }

    private void findViews() {
        mImageView = (ImageView) findViewById(R.id.image_view);
        mEditText = (EditText) findViewById(R.id.name);
        btn_create = (Button) findViewById(R.id.btn_create);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
        btn_create.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_create : create();break;
            case R.id.btn_save : save();break;
        }
    }

    private void save() {
        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/dun");
        if(!f.exists()){
            f.mkdir();
        }
        File image = new File(f.getPath(), SystemClock.uptimeMillis()+".png");
        try {
            FileOutputStream fos = new FileOutputStream(image);
            if(newBitmap!=null){
                newBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                fos.flush();
                fos.close();
                Toast.makeText(MainActivity.this,"保存在"+image.getPath()+"成功啦",Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void create() {
        String name = mEditText.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "不输入名字我做你个头的图啊~", Toast.LENGTH_SHORT).show();
            return;
        }

        newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(newBitmap);
        mCanvas.drawBitmap(bitmap,0,0,null);

        Paint p = new Paint();

        String familyName = "新宋体";
        Typeface font = Typeface.create(familyName,Typeface.BOLD);
        p.setColor(Color.BLACK);
        p.setTypeface(font);
        p.setTextSize(sp2px(5));

        if(name.length()==2){
            Log.i(TAG, "create: "+dip2px(10)+"   "+dip2px(26));

            mCanvas.drawText(name.substring(0, 1), dip2px(12), dip2px(26), p);
            mCanvas.drawText(name.substring(1,2),dip2px(12),dip2px(33),p);
        }else if(name.length() == 3){
            p.setTextSize(sp2px(4));
            mCanvas.drawText(name.substring(0,1),dip2px(12),dip2px(25),p);
            mCanvas.drawText(name.substring(1,2),dip2px(12),dip2px(30),p);
            mCanvas.drawText(name.substring(2,3),dip2px(12),dip2px(35),p);
        }else{
            Toast.makeText(this,"只支持2~3个字哦",Toast.LENGTH_SHORT).show();
        }


        mImageView.setImageBitmap(newBitmap);
    }

    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public int sp2px(float spValue) {
        final float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
