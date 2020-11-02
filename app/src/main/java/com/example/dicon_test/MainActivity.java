package com.example.dicon_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TessBaseAPI tess;
    String datapath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Item> data = new ArrayList<>();
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"플라스틱","플라스틱은 말이여,,,"));
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"종이","종이는 말이여,,,"));
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"비닐","비닐은 말이여,,,"));
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"캔","캔은 말이여,,,"));
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"스티로폼","스티로폼은 말이여,,,"));
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"페트병","페트병은 말이여,,,"));

//        RecyclerView recyclerView =findViewById(R.id.recycling);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ReAdapter reAdapter = new ReAdapter(data);
//        recyclerView.setAdapter(reAdapter);

        ImageView img =findViewById(R.id.product_img);
        img.setImageResource(R.drawable.teeee);

        //문자 인식
        //data경로
        datapath=getFilesDir()+"/tesseract/";
        //한글 데이터 체크
        checkFile(new File(datapath+"tessdata/"),"kor");
        checkFile(new File(datapath+"tessdata/"),"eng");
        //문자인식 객체 생성
        String lang = "kor+eng";
        tess= new TessBaseAPI();
        tess.init(datapath,lang);


        //문자인식
        processImg(BitmapFactory.decodeResource(getResources(),R.drawable.teeee));

    }




    public void processImg(Bitmap bitmap){
        Toast.makeText(getApplicationContext(),"기다려",Toast.LENGTH_LONG).show();
        String OCRr = null;
        tess.setImage(bitmap);
        OCRr=tess.getUTF8Text();
        TextView OCRt=findViewById(R.id.test);

        OCRt.setText(OCRr);


    }

    private void checkFile(File dir, String Language) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(Language);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if (dir.exists()) {
            String datafilepath = datapath + "tessdata/" + Language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(Language);
            }
        }
    }

    //copy file to device
    private void copyFiles(String Language) {
        try {
            String filepath = datapath + "/tessdata/" + Language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/"+Language+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Item addItem(Drawable img, String title, String way)
    {
        Item item = new Item();
        item.setRecycle_img(img);
        item.setRecycle_class(title);
        item.setRecycle_how(way);
        return item;
    }


}