/*package com.example.viewtoimage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainAc extends Activity  implements OnClickListener {
	
	Button btntakephoto, btnsave, btnshare;
	ImageView ivdisplayphoto;
	SeekBar sbSeekBar; 
	
	private ColorMatrix colorMatrix;  
    private ColorMatrixColorFilter filter;  
    private Paint paint;
    private Canvas cv; 
 
    
    String fotoname;
    int progress;
 
	private File photofile, file;
	private int TAKENPHOTO = 0;
	Bitmap photo, canvasBitmap;
	private Button mButton;
    private View mView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 mView = findViewById(R.id.f_view);
		  mButton = (Button) findViewById(R.id.button1);
	      mButton.setOnClickListener(this);

	        mView.setDrawingCacheEnabled(true);
	        mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
	        mView.buildDrawingCache(true);
		 
		 
		 
		btntakephoto = (Button)findViewById(R.id.btn_takephoto);
		btnsave = (Button)findViewById(R.id.btn_save);
		btnshare = (Button)findViewById(R.id.btn_share);
		ivdisplayphoto = (ImageView)findViewById(R.id.iv_displayphoto);
		
			sbSeekBar = (SeekBar) findViewById(R.id.skbarChangeColor);  
	        sbSeekBar.setMax(100);  
	        sbSeekBar.setKeyProgressIncrement(1);  
	        sbSeekBar.setProgress(50); 
			sbSeekBar.setVisibility(View.GONE);
		
	    colorMatrix = new ColorMatrix();  
	    filter = new ColorMatrixColorFilter(this.colorMatrix);
        paint = new Paint();  
        paint.setColorFilter(filter);
		
        sbSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
 
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
			
				applyColorFilter(progress);
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
 
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        	
     
		
		btntakephoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				photofile = new File(photostorage, (System.currentTimeMillis()) + ".jpg");
				
				
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //intent to start camera
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
				startActivityForResult(i, TAKENPHOTO);
			}
		});
		
		btnsave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
  
			        ivdisplayphoto.setDrawingCacheEnabled(true);
	                Bitmap bitmap = ivdisplayphoto.getDrawingCache();
	                
	                String root = Environment.getExternalStorageDirectory().toString();
	                File newDir = new File(root + "/saved_images");    
	                newDir.mkdirs();
	                Random gen = new Random();
	                int n = 10000;
	                n = gen.nextInt(n);
	                String fotoname = "photo-"+ n +".jpg";
	                File file = new File (newDir, fotoname);
	                if (file.exists ()) file.delete (); 
	                	try {
	                       FileOutputStream out = new FileOutputStream(file);
	                       bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	                       out.flush();
	                       out.close();
	                       Toast.makeText(getApplicationContext(), "safed to your folder", Toast.LENGTH_SHORT ).show();
 
		                } catch (Exception e) {
		                       
		                }
	                 
	                
	             }
		});
		
		btnshare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
	            
			
		        BitmapDrawable bitmapDrawable = (BitmapDrawable)ivdisplayphoto.getDrawable();
		        Bitmap bitmap = bitmapDrawable.getBitmap();
 
		        // Save this bitmap to a file.
		        File cache = getApplicationContext().getExternalCacheDir();
		        File sharefile = new File(cache, "toshare.png");
		        try {
		            FileOutputStream out = new FileOutputStream(sharefile);
		            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		            out.flush();
		            out.close();
		        } catch (IOException e) {
		          
		        }
 
		        // Now send it out to share
		        Intent share = new Intent(android.content.Intent.ACTION_SEND);
		        share.setType("image/*");
		        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
		        try {
		            startActivity(Intent.createChooser(share, "Share photo"));
		        } catch (Exception e) {
		            
		        }
		     }
		});
		
	}
	
	 @Override
	    public void onClick(View v) {

	        if (v.getId() == R.id.button1) {
	            Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
	            mView.setDrawingCacheEnabled(false);
	            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

	            // Save this bitmap to a file.
		        File cache = getApplicationContext().getExternalCacheDir();
		        File sharefile = new File(cache, "toshare.png");
		        try {
		            FileOutputStream out = new FileOutputStream(sharefile);
		            b.compress(Bitmap.CompressFormat.PNG, 100, out);
		            out.flush();
		            out.close();
		        } catch (IOException e) {
		          
		        }
 
		        // Now send it out to share
		        Intent share = new Intent(android.content.Intent.ACTION_SEND);
		        share.setType("image/*");
		        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
		        try {
		            startActivity(Intent.createChooser(share, "Share photo"));
		        } catch (Exception e) {
		            
		        }
		     }
	        }
	    
 
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAKENPHOTO){
			
			try{
			
			photo = (Bitmap) data.getExtras().get("data");
			}	
			catch(NullPointerException ex){
				 photo = BitmapFactory.decodeFile(photofile.getAbsolutePath());
			}
			
			if(photo != null){
				ivdisplayphoto.setImageBitmap(photo);
				sbSeekBar.setVisibility(View.VISIBLE);
 
				
			}
			else{
				 
				Toast.makeText(this, "Oops,can't get the photo from your gallery", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	public void applyColorFilter(int progress){
		
        colorMatrix.setSaturation(progress/(float)40);  
        filter = new ColorMatrixColorFilter(colorMatrix);  
        paint.setColorFilter(filter);  
        canvasBitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);  
        cv = new Canvas(canvasBitmap);
        cv.drawBitmap(photo, 0, 0, paint);  
        ivdisplayphoto.setImageBitmap(canvasBitmap);
	
	}
	
		
	}
*/