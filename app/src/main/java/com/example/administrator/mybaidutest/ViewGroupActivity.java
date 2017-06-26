package com.example.administrator.mybaidutest;

import com.example.administrator.mybaidutest.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewGroupActivity extends Activity {
MyViewGroup myViewGroup;
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_view_group_layout);
	myViewGroup= (MyViewGroup) findViewById(R.id.myView);
		TextView textView=new TextView(this);
		textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setText("  这是中文字幕捱三顶五群多群无多群无七味都气丸的 ");
		TextView textView2=new TextView(this);
		textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView2.setText(" 3fdgdfg33333333333333333333333333333333333333333 ");
		TextView textView3=new TextView(this);
		textView3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView3.setText("  11发大水见客dfgfdgdfg户1   ");
		TextView textView4=new TextView(this);
		textView4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView4.setText("   heheheheasd     ");
		myViewGroup.addView(textView);
		myViewGroup.addView(textView2);
		myViewGroup.addView(textView3);
		myViewGroup.addView(textView4);

	}
}
