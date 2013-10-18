package example.animation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class MainActivity extends Activity implements OnClickListener{
	
	private boolean jump = true;
	private boolean alpha = false;
	private boolean trans = false;
	private boolean rotate = false;
	
	private View iv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		iv1 = findViewById(R.id.imageView1);
		
		View btn1 = findViewById(R.id.button1);
		btn1.setBackgroundResource(R.drawable.droid);
		btn1.setOnClickListener(this);

		View menu = findViewById(R.id.menu);
		menu.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	  public void onClick(View v) {
	    switch(v.getId()){
	    case R.id.button1:
	    	animation(iv1);
	    	break;
	    case R.id.menu:
	    	show_dialog();
	    	break;
	    default:
	    	break;
	    }
	  }


  void show_dialog(){
		// チェック表示用のアラートダイアログ
      final CharSequence[] chkItems = {"jump", "alpha", "trans", "rotate"};
      final boolean[] chkSts = {jump, alpha, trans, rotate};
      AlertDialog.Builder checkDlg = new AlertDialog.Builder(this);
      checkDlg.setTitle("Action");
      checkDlg.setMultiChoiceItems(
          chkItems,
          chkSts,
          new DialogInterface.OnMultiChoiceClickListener() {
              public void onClick(DialogInterface dialog,
                                  int which, boolean flag) {
              	chkSts[which] = flag;
              }
          });
      checkDlg.setPositiveButton(
          "OK",
          new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
              	for (int i = 0; i < chkSts.length; i++){
              		switch(i){
              		case 0:
              			jump = chkSts[i];
              			break;
              		case 1:
              			alpha = chkSts[i];
              			break;
              		case 2:
              			trans = chkSts[i];
              			break;
              		case 3:
              			rotate = chkSts[i];
              			break;
              		default:
              			break;
              		}
                  }
              }
          });
      // 表示
      checkDlg.create().show();
  }


  void animation(View v){

  	iv1.setBackgroundResource(0);

  	//jump
  	if(jump == true){
  		iv1.setBackgroundResource( R.drawable.jumping );
  		AnimationDrawable anim = (AnimationDrawable)v.getBackground();
        // アニメーション開始
        anim.start();
  	}else{
  		iv1.setBackgroundResource(R.drawable.fig01);
  	}

  	AnimationSet set = new AnimationSet(false);

  	//alpha
  	if(alpha == true){
  		AlphaAnimation alpha_anim = new AlphaAnimation(0.0f, 1.0f);
  		alpha_anim.setDuration(4000);
  		set.addAnimation(alpha_anim);
  	}

  	//trans
  	if(trans == true){
  		TranslateAnimation trans_anim = new TranslateAnimation(
  				Animation.RELATIVE_TO_SELF, 2,
  				Animation.RELATIVE_TO_SELF, 0,
  				Animation.RELATIVE_TO_SELF, 0,
  				Animation.RELATIVE_TO_SELF, 0);
  		trans_anim.setDuration(4000);
  		set.addAnimation(trans_anim);
  	}

  	//rotate
  	if(rotate == true){
  		RotateAnimation rotate_anim = new RotateAnimation(
  				0, 720f,
  				Animation.RELATIVE_TO_SELF, 0.5f,
  				Animation.RELATIVE_TO_SELF, 0.5f);
  		rotate_anim.setDuration(4000);
  		set.addAnimation(rotate_anim);
  	}

  	//start animation
  	set.setFillEnabled(true);
  	set.setFillAfter(true);
  	v.startAnimation(set);

  }

}
