package cse.fau.edu.group3.news;

import java.util.ArrayList;
import java.util.List;
import cse.fau.edu.group3.news.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class PhotoGallery extends Activity {
	
    private static final String LOG_TAG = PhotoGallery.class.getSimpleName();
    private Context context;
    public static final String TOP_PHOTO_ID_NAME = "topPhoto";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        context = this;
        // force landscape orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // connect to the gallery layout
        setContentView(R.layout.gallery);
        Gallery g = (Gallery) findViewById(R.id.gallery);
        //image adapter
        ImageAdapter imageAdapter = new ImageAdapter(this);
        g.setAdapter(imageAdapter);
        // set up the on click listener when pressed one of the picture
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(@SuppressWarnings("rawtypes") AdapterView parent, View v, int position, long id) {
            	// Starting the camera preview activity
            	Intent startPreviewCameraIntent = new Intent(context, PreviewCamera.class);
            	startPreviewCameraIntent.putExtra(TOP_PHOTO_ID_NAME, id);
            	context.startActivity(startPreviewCameraIntent);
            }
        });
    }
 
    public class ImageAdapter extends BaseAdapter {
	    int mGalleryItemBackground;
	    private Context mContext;
	    private List<Integer>mImageIds;
	    //set up the list of images
	    public ImageAdapter(Context c) {
	        mContext = c;
	        TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);
	        mGalleryItemBackground = a.getResourceId(
	                R.styleable.HelloGallery_android_galleryItemBackground, 0);
	        a.recycle();
	        //array for the images and its position
	        mImageIds = new ArrayList<Integer>();
	        //images here
        	mImageIds.add(R.drawable.frame1);
        	mImageIds.add(R.drawable.frame2);
        	mImageIds.add(R.drawable.frame3);
        	mImageIds.add(R.drawable.frame4);
        	mImageIds.add(R.drawable.frame5);

	    }
	    public int getCount() {
	        return mImageIds.size();
	    }
	    public Object getItem(int position) {
	        return mImageIds.get(position);
	    }
	    public long getItemId(int position) {
	        return mImageIds.get(position);
	    }
	    //set of the view for the images
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView i = new ImageView(mContext);
	        Log.v(LOG_TAG, "Getting position " + position);
		        i.setImageResource(mImageIds.get(position));
		        i.setLayoutParams(new Gallery.LayoutParams(900, 600));
		        i.setScaleType(ImageView.ScaleType.FIT_XY);
		        i.setBackgroundResource(mGalleryItemBackground);
	        return i;
	    }
	}
}