package cse.fau.edu.group8.news;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.media.MediaScannerConnection;

public class PreviewCamera extends Activity implements OnClickListener{
	
	private static final String LOG_TAG = PreviewCamera.class.getSimpleName();
    private Bitmap bitmap;
    private Preview mPreview;
    private Long topPhotoLong;
    int camera_id = 0;
    
    //created
    @SuppressLint("UseValueOf")
	@Override 
    public void onCreate(Bundle savedInstanceState) { 
          super.onCreate(savedInstanceState); 
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          // Get the intent
          Intent startedIntent = getIntent();
          // Get the pictures
          topPhotoLong = startedIntent.getLongExtra(PhotoGallery.TOP_PHOTO_ID_NAME, new Long(-1));
          if (topPhotoLong > -1) {
        	  Log.v(LOG_TAG, "The photoId is " + topPhotoLong);
              bitmap = BitmapFactory.decodeResource(getResources(), topPhotoLong.intValue());
          } else {
        	  Log.w(LOG_TAG, "Photo id not found");
          }
          FrameLayout NewsStudioFrame = new FrameLayout(this);
          mPreview = new Preview(this); 
          //by default place the picture on the top
          DrawOnTop mDraw = new DrawOnTop(this);
          LinearLayout PhotoStudioWidgets = new LinearLayout (this);
          // set button to take and save picture
          Button CameraButton = new Button(this);
          Button TakePhotoButton = new Button(this);
         // CameraButton.setWidth(400);
         // TakePhotoButton.setWidth(800);
          CameraButton.setText("Go Back");
          TakePhotoButton.setText("Take and Save photo");
          // add the news studio frame
          PhotoStudioWidgets.addView(TakePhotoButton);
          PhotoStudioWidgets.addView(CameraButton);
          // display the frame on the camera
          NewsStudioFrame.addView(mPreview);
          NewsStudioFrame.addView(PhotoStudioWidgets);
          // set the content view with the preview frame
          setContentView(NewsStudioFrame);
          addContentView(mDraw, new LayoutParams (LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT)); 
          TakePhotoButton.setOnClickListener(this);
          
          CameraButton.setOnClickListener(new OnClickListener() {
  	              @Override
   	              public void onClick(View view) {
        	                  Intent back = new Intent(PreviewCamera.this, PhotoGallery.class); 
        	                  startActivity(back);
        	                  PreviewCamera.this.finish();
        	      }
          });

          // Landscape only
          this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     
   } 
   class DrawOnTop extends View { 
       public DrawOnTop(Context context) { 
           super(context); 
   } 
   @Override 
   protected void onDraw(Canvas canvas) {
	   
	   Bitmap b = Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), false);
	   if (bitmap != null) {
		   canvas.drawBitmap(b, 0, 0, null);
	   }
       super.onDraw(canvas); 
   } 
} 
   public void onClick(View v) {
	   Log.v(LOG_TAG, "Taking picture");
	   mPreview.takePicture();
	   Intent StopCamActivity = new Intent(PreviewCamera.this, NewsRoomMenu.class);
 	   startActivity(StopCamActivity);
 	   this.finish();
 	   
 	  
   }
   
class Preview extends SurfaceView implements SurfaceHolder.Callback { 
   SurfaceHolder mHolder; 
   Camera mCamera; 

   @SuppressWarnings("deprecation")
Preview(Context context) { 
       super(context); 
       // Install a SurfaceHolder.Callback so we get notified when the 
       // underlying surface is created and destroyed. 
       mHolder = getHolder(); 
       mHolder.addCallback(this); 
       mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
   } 

   public void surfaceCreated(SurfaceHolder holder) { 
       // The Surface has been created, acquire the camera and tell it where 
       // to draw. 
	   
       mCamera = Camera.open(camera_id);
        try {
        	mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
        	e.printStackTrace();
        }
   }

   public void surfaceDestroyed(SurfaceHolder holder) { 
      //Stop preview when surface is destroyed
      mCamera.stopPreview(); 
      mCamera = null; 
   }
   
   public void takePicture() {
	   mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));

   }

   public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { 
       //setup camera
       Camera.Parameters parameters = mCamera.getParameters(); 
       
       List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
       Camera.Size previewSize = previewSizes.get(previewSizes.size()-1);
       //pick the largest preview size
       
       parameters.setPreviewSize(previewSize.width, previewSize.height); 
       mCamera.setParameters(parameters); 
       mCamera.startPreview(); 
  } 
} 
	public class PhotoHandler implements PictureCallback {
	
	    private final Context context;
	
	    public PhotoHandler(Context context) {
	        this.context = context;
	        Toast.makeText(context, "Saving the image, Please wait...",
	                Toast.LENGTH_LONG).show();
	    }
	    
	    @SuppressLint("SimpleDateFormat")
		@Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	
	        File sdDir = Environment
	                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	        // save picture
	        File pictureFileDir =  new File(sdDir, "/NewsRoom_Photos");
	        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
	
	            Log.d(LOG_TAG, "Can't create directory to save image.");
	            Toast.makeText(context, "Can't create directory to save image.",
	                    Toast.LENGTH_LONG).show();
	            return;
	
	        }
	        //format the picture name
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	        String date = dateFormat.format(new Date());
	        String photoFile = "NewsRoom_" + date + ".jpg";
	
	        String filename = pictureFileDir.getPath() + File.separator + photoFile;
	
	        File pictureFile = new File(filename);

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	            Toast.makeText(context, "New Image saved:" + photoFile,
	                    Toast.LENGTH_LONG).show();
	            MediaScannerConnection.scanFile(context, new String[] { pictureFile.getPath() }, 
	            								new String[] { "image/jpeg" }, null);
	            // TODO: Merge the photo
	            try {
	                Bitmap bottomImage = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
	
	                bitmap = Bitmap.createBitmap(bottomImage.getWidth(), bottomImage.getHeight(), Bitmap.Config.ARGB_8888);
	                Canvas c = new Canvas(bitmap);
	                Resources res = getResources();
	
	                Bitmap topImage = BitmapFactory.decodeResource(res, topPhotoLong.intValue());
	                @SuppressWarnings("deprecation")
					Drawable drawable1 = new BitmapDrawable(bottomImage);
	                @SuppressWarnings("deprecation")
					Drawable drawable2 = new BitmapDrawable(topImage);
	
	
	                drawable1.setBounds(0, 0, bottomImage.getWidth(), bottomImage.getHeight());
	                drawable2.setBounds(0, 0, bottomImage.getWidth(), bottomImage.getHeight());
	                drawable1.draw(c);
	                drawable2.draw(c);
	                
	
	
	            } catch (Exception e) {
	            }
	            
	            // To write the file out to the SDCard:
	            OutputStream os = null;
	            try {
	                os = new FileOutputStream(filename);
	                bitmap.compress(Bitmap.CompressFormat.PNG, 50, os);
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	            Toast.makeText(context, "Image has been saved in your local storage.",
	                   Toast.LENGTH_LONG).show();
	           
	        } catch (Exception error) {
	            Log.d(LOG_TAG, "File" + filename + "not saved: "
	                    + error.getMessage());
	            Toast.makeText(context, "Image could not be saved.",
	                    Toast.LENGTH_LONG).show();
	        }			   
	    }

	}
	
}