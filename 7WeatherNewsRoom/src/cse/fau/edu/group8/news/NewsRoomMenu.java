package cse.fau.edu.group8.news;

import cse.fau.edu.group8.news.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewsRoomMenu extends Activity implements OnClickListener {
	Button Info, Start, Exit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsroom_menu);
		initializeVars();// initialization
	}

	private void initializeVars() {
		// TODO Auto-generated method stub
		// find
		Info = (Button) findViewById(R.id.b_about);
		Start = (Button) findViewById(R.id.b_start);
		Exit = (Button) findViewById(R.id.b_quit);
		// set when clicks
		Info.setOnClickListener(this);// clicks
		Start.setOnClickListener(this);// clicks
		Exit.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	public void onClick(View view) {
		//when buttons click
		switch (view.getId()) {
	      case R.id.b_about:
	    	  AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	    	  alertDialog.setTitle("About");
	    	  alertDialog.setMessage("7 weather newsroom app allows you to put your own face into" +
	    	  		" different frame scenarios, so you can see yourself delivering important news " +
	    	  		"to the viewers as a weather cast reporter. " +
	    	  		"For better experience you can take pictures while visiting " +
	    	  		"the newsroom at the Museum of Discovery and Science, then " +
	    	  		"this app will give the gallery where you can select and put together a scenario" +
	    	  		" where it looks like you are in a real news studio. " +
	    	  		"There is something for everyone. Save and share your MODS souvenir via " +
	    	  		"email or Facebook using the default features of your smartphone.");
	    	  alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    	  public void onClick(final DialogInterface dialog, final int which) {
	    	  // here you can add functions
	    	  }
	    	  });
	    	  alertDialog.setIcon(R.drawable.ic_launcher);
	    	  alertDialog.show();
	         break;
	      case R.id.b_start:
	    	  Intent StartCamActivity = new Intent(NewsRoomMenu.this, PhotoGallery.class);
	  		startActivity(StartCamActivity);
	         break;
	      case R.id.b_quit:
	    	  super.finish();
	         break;
		}
	}
	public class NewsroomReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Intent intent = new Intent(arg0, NewsRoomMenu.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			arg0.startActivity(intent);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_room_menu, menu);
		return true;
	}

}
