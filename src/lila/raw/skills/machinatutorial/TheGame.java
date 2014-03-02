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
	
	public TheGame(Context context) {
         super(context);
         tt = new TheThread(this);
         holder = getHolder();
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
                	createSprites();
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
	
	
	private void createSprites() {
        leSprites.add(createSprite(R.drawable.bad1));
        leSprites.add(createSprite(R.drawable.bad2));
        leSprites.add(createSprite(R.drawable.bad3));
        leSprites.add(createSprite(R.drawable.good1));
        leSprites.add(createSprite(R.drawable.good2));
        leSprites.add(createSprite(R.drawable.good3));
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
	   paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
//	   canvas.drawColor(Color.BLACK);
//       canvas.drawBitmap(bmp, x, 10, null);
//       ts.onDraw(canvas);
	   for (TheSprite sprite : leSprites) {
           sprite.onDraw(canvas);
    }
       
   }
}
