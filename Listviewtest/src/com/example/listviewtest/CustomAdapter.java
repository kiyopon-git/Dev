package com.example.listviewtest;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter  extends ArrayAdapter<UserData> {
	 private LayoutInflater layoutInflater_;
	 
	 public CustomAdapter(Context context, int textViewResourceId, ArrayList<UserData> objects) {
	 super(context, textViewResourceId, objects);
	 	layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	 
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	 // 特定の行(position)のデータを得る
	 UserData item = (UserData)getItem(position);
	 
	 // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
	 if (null == convertView) {
		 convertView = layoutInflater_.inflate(R.layout.custom_layout, null);
	 }
	 
	 // UserdataのデータをViewの各Widgetにセットする
	 ImageView imageView;
	 imageView = (ImageView)convertView.findViewById(R.id.image);
	 imageView.setImageBitmap(item.getImage());
	 
	 TextView Name;
	 Name = (TextView)convertView.findViewById(R.id.name);
	 Name.setText(item.getName());
	 
	 TextView Date;
	 Date = (TextView)convertView.findViewById(R.id.date);
	 Date.setText(item.getDate());
	 
	 TextView Introduction;
	 Introduction = (TextView)convertView.findViewById(R.id.introduction);
	 Introduction.setText(item.getIntroduction());
	 
	 return convertView;
	 }
}