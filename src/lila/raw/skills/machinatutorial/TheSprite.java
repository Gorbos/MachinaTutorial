package lila.raw.skills.machinatutorial;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class TheSprite {
	// direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
	private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
	private int x = 0; 
	private int y = 0;
    private int xSpeed = 5;
    private int ySpeed = 5;
    private TheGame tg;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;
   
    public TheSprite(TheGame tg, Bitmap bmp) {
          this.tg=tg;
          this.bmp=bmp;
          this.width = bmp.getWidth() / BMP_COLUMNS;
          this.height = bmp.getHeight() / BMP_ROWS;
          
          Random rnd = new Random(System.currentTimeMillis());
          x = rnd.nextInt(tg.getWidth() - width);
          y = rnd.nextInt(tg.getHeight() - height);
          xSpeed = rnd.nextInt(10)-5;
          ySpeed = rnd.nextInt(10)-5;
    }

    private void update() {
//          if (x > tg.getWidth() - bmp.getWidth() - xSpeed) {
//                 xSpeed = -5;
//          }
//          if (x + xSpeed< 0) {
//                 xSpeed = 5;
//          }
//          x = x + xSpeed;
    	  if (x > tg.getWidth() - width - xSpeed || x + xSpeed < 0) {
	             xSpeed = -xSpeed;
	      }
	      x = x + xSpeed;
	      if (y > tg.getHeight() - height - ySpeed || y + ySpeed < 0) {
	             ySpeed = -ySpeed;
	      }
	      y = y + ySpeed;
          currentFrame = ++currentFrame % BMP_COLUMNS;
    }
   
    public void onDraw(Canvas canvas) {
//          update();
//          canvas.drawBitmap(bmp, x , 10, null);
    	update();
        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
          double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
          int direction = (int) Math.round(dirDouble) % BMP_ROWS;
          return DIRECTION_TO_ANIMATION_MAP[direction];
    }
}
