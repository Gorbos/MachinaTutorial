package lila.raw.skills.machinatutorial;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressLint("WrongCall")
public class TheGame extends SurfaceView {
	private Bitmap bmp;
	private SurfaceHolder holder;	
	private TheThread tt;
	private int x = 0;
	private int xSpeed = 1;
	private TheSprite ts;
	private List<TheSprite> leSprites = new ArrayList<TheSprite>();
	Paint paint = new Paint();	
	private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpBlood;
    private Bitmap bg;
	
	TheSprite spirit;
	public float myX;
	public float myY;
	public boolean spiritIsDrawn = false;
	
	int screenHeight;
	int screenWidth;
	TheGame tg;
	
	public TheGame(Context context) {
         super(context);
         tt = new TheThread(this);
         bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
         tg = this;
         holder = this.getHolder();
         holder.addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                	boolean retry = true;
                    tt.setRunning(false);
                    while (retry) {
                           try {
                                 tt.join();
                                 retry = false;
                           } catch (InterruptedException e) {
                           }
                    }
                }	

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                	screenHeight = tg.getHeight();
                    screenWidth = tg.getWidth();
                    bg = scaleImage(R.drawable.bg1);
                	spirit = createSprite(R.drawable.good1);
                	createManySprites();
                	tt.setRunning(true);
                    tt.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                              int width, int height) {
                }
         });
//         bmp = BitmapFactory.decodeResource(getResources(), R.drawable.angel);
//         ts = new TheSprite(this, bmp);	
   }
	
	
//	private void createSprites() {
//        leSprites.add(spirit);
//        leSprites.add(spirit);
//        leSprites.add(spirit);
//        leSprites.add(spirit);
//        	
//  }
	
	private void createManySprites(){
		leSprites.add( createSprite(R.drawable.good1));
		leSprites.add( createSprite(R.drawable.good2));
		leSprites.add( createSprite(R.drawable.good3));
		leSprites.add( createSprite(R.drawable.bad1));
		leSprites.add( createSprite(R.drawable.bad2));
		leSprites.add( createSprite(R.drawable.bad3));
		
	}
 
  private TheSprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new TheSprite(this,bmp);
  }
	
   @Override
   protected void onDraw(Canvas canvas) {
	   paint = new Paint();
	   paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	   canvas.drawPaint(paint);
	   doDrawRunning(canvas);
	   paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
//	   canvas.drawColor(Color.BLACK);
//       canvas.drawBitmap(bmp, x, 10, null);
//       ts.onDraw(canvas);
	   for (int i = temps.size() - 1; i >= 0; i--) {
           temps.get(i).onDraw(canvas);
	   	}
	   for (TheSprite sprite : leSprites) {
           sprite.onDraw(canvas);
    }
       
   }
   
   @Override
   public boolean onTouchEvent(MotionEvent event) {
	   
	   Integer ix;
	   Integer iy;
	   
         if (System.currentTimeMillis() - lastClick > 500) {
        	 lastClick = System.currentTimeMillis();
             float x = event.getX();
             float y = event.getY();
                synchronized (getHolder()) {
                    for (int i = leSprites.size() - 1; i >= 0; i--) {
                        TheSprite sprite = leSprites.get(i);
                        if (sprite.isCollition(event.getX(), event.getY())) {
                              leSprites.remove(sprite);
                              temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                              break;
                        }
                    }
//                	if(!spiritIsDrawn){
//                		createSprites();
//                		spiritIsDrawn = true;
//                	}
//                	
//                	spirit.userX = (int) event.getX();
//                	spirit.userY = (int) event.getY();
                }
         }
         return true;
   }
   
   private int mBGFarMoveX = 0;
   private int mBGNearMoveX = 0;

   private void doDrawRunning(Canvas canvas) {
    // decrement the far background
    mBGFarMoveX = mBGFarMoveX - 10;
    // decrement the near background
    mBGNearMoveX = mBGNearMoveX - 4;
    // calculate the wrap factor for matching image draw
    int newFarX = bg.getWidth() - (-mBGFarMoveX);
    // if we have scrolled all the way, reset to start
    if (newFarX <= 0) {
     mBGFarMoveX = 0;
     // only need one draw
     canvas.drawBitmap(bg, mBGFarMoveX, 0, null);
     //canvas.drawBitmap(bg, newFarX, 0, null);
    } else {
     // need to draw original and wrap
     canvas.drawBitmap(bg, mBGFarMoveX, 0, null);
     canvas.drawBitmap(bg, newFarX, 0, null);
    }
    }
   
   public Bitmap prepBG(int resource){
	   	
		
		int shortHeight = (int)(screenHeight * 0.4);
		int shortWidth = (int)(screenWidth * 0.4);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		Bitmap temp = BitmapFactory.decodeResource(getResources(), resource);
		
		 // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, shortWidth, shortHeight);
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	   return BitmapFactory.decodeResource(getResources(), resource, options);
	   //return null;
   }
   
   private Bitmap scaleImage(int resource){
	   Bitmap bp = BitmapFactory.decodeResource(getResources(), resource);
	   Bitmap bg = Bitmap.createScaledBitmap(bp, screenWidth, screenHeight, false);
	   return bg;
   }
   
   public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
}
