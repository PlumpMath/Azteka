package com.logosstudios.azteka;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.logostudios.azteka.R;

public class GameNight extends Activity implements GameInterface
{
	private Panel panel;
	protected static int width, height;
	private long timeAllowed;
	private Particle p;
	private Hoop h;
	private int mouseX, mouseY;
	private long timeLeft;
	private long timeStarted;
	private ArrayList<PowerUp> powerUps;
	private boolean wings;
	private long wingStart;
	private boolean hoops;
	private long hoopsStart;
	private boolean slows;
	private long slowStart;
	private boolean endScreen;
	private int ballSpeed;
	private int powerUpChance;
	private int powerUpPick;
	private Bitmap background;
	private boolean check = true;
	private int taps;
	private long timePaused;
	private boolean paused;

	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = getWindowManager().getDefaultDisplay();
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_night);
        width = display.getWidth();
        height = display.getHeight();
        
        p = new Particle(getResources(), this, (int)getAdjustedDimension(10));
		h = new Hoop((int)getAdjustedDimension(41), (int)getAdjustedDimension(51), this, true);
		endScreen = false;
		powerUps = new ArrayList<PowerUp>();
		timeStarted = System.currentTimeMillis();
		timeAllowed = 46000;
		paused = false;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
          super.onActivityResult(requestCode, resultCode, data);
    }
    protected void onPause()
    {
    	timePaused = System.currentTimeMillis();
    	super.onPause();
    	paused = true;
    }
    protected void onResume()
    {
    	if(paused)
    	{
    		timeStarted += System.currentTimeMillis() - timePaused;
    		paused = false;
    	}
    	super.onResume();
    	h.setNight(true);
    	panel = new Panel(this);
        panel.requestFocus();
        panel.setFocusableInTouchMode(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(panel);
    }
    protected void onStop()
    {
    	super.onStop();
    	panel.thread.setRunning(false);
    }
    public int getGameHeight()
    {
    	return height;
    }
    public int getGameWidth()
    {
    	return width;
    }
    public void increaseTimeAllowed(int q)
    {
    	timeAllowed += q;
    }
    public double getAdjustedDimension(float value){
    	Display display = getWindowManager().getDefaultDisplay(); 
    	int width = display.getWidth();  // deprecated
    	float adjusted = value * (width/480) * 1.6f;
		return adjusted;
    }
    
    public class Panel extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener, OnKeyListener
    {
        RunningThread thread;
        Bitmap bufferImage;

        public Panel(Context context)
        {
            super(context);
            getHolder().addCallback(this);
            setOnKeyListener(this);
            bufferImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            //thread = new RunningThread(getHolder(), this);
            setOnTouchListener(this);
        }

        public void onDraw(Canvas canvas)
        {
        	Canvas buffer = new Canvas(bufferImage);
        	buffer.drawColor(Color.BLACK);
        	canvas.drawColor(Color.BLACK);
        	Rect src = new Rect(0, 0, background.getWidth(), background.getHeight());
    		Rect dest = new Rect(0, 0, width, height);
            buffer.drawBitmap(background, src, dest, null);
			for(int i = 0; i < powerUps.size(); i++)
			{
				powerUps.get(i).draw(buffer);
			}
			h.drawBack(buffer);
			p.draw(buffer);
			h.drawFront(buffer);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setTextSize((int)getAdjustedDimension(20));
			buffer.drawText(String.format("%1$tM:%1$tS", timeLeft), width/2-(int)getAdjustedDimension(20), height/2, paint);
    		canvas.drawBitmap(bufferImage, -((int)(p.getX() + p.getSize()/2) - width/2), -((int)(p.getY() + p.getSize()/2) - height/2), null);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        	
        }

        public void surfaceCreated(SurfaceHolder holder)
        {
        	thread = new RunningThread(getHolder(), this); 
            thread.setRunning(true);
            thread.start();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            thread.setRunning(false);
            while (retry)
            {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {}
            }
        }

		
		public boolean onTouch(View v, MotionEvent ev)
		{
			if(ev.getAction() == MotionEvent.ACTION_DOWN)
			{
				float dX = ev.getX() - width/2;
				float dY = ev.getY() - height/2;
				p.setXVelocity(dX/ballSpeed);
				p.setYVelocity(-dY/ballSpeed);
				taps++;
			}
			mouseX = (int) ev.getX();
			mouseY = (int) ev.getY();
			return true;
		}
		
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			return false;
		}
    }
    class RunningThread extends Thread
    {
        private SurfaceHolder surfaceHolder;
        private Panel panel;
        private boolean running = false;
        private long frameRate = (int)(1.0/60 * 1000);

        public RunningThread(SurfaceHolder surfaceHolder, Panel panel)
        {
            this.surfaceHolder = surfaceHolder;
            this.panel = panel;
        }

        public void setRunning(boolean r)
        {
            this.running = r;
        }

        public void run()
        {
            Canvas canvas;
            while (running)
            {
            	long startTime = System.currentTimeMillis();
            	
            	logic();
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        panel.onDraw(canvas);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                
                long endTime = System.currentTimeMillis();
                try {
					sleep(Math.max(frameRate - (endTime-startTime), 0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
        public void logic()
    	{
			p.move();
			h.collide(p);
			timeLeft = timeAllowed - (System.currentTimeMillis() - timeStarted);
			if(check&&timeLeft<0)
			{
				endScreen = true;
			}
			if(endScreen)
			{
				//endScreen = true;
				Intent intent = new Intent(getBaseContext(), EndScreen.class);
	    		intent.putExtra("score", h.getScore());
	    		intent.putExtra("taps", taps);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    		startActivityForResult(intent, 0);
	    		finish();
	    		overridePendingTransition(0, 0);
	    		endScreen = false;
	    		check = false;
	    		running = false;
				//System.exit(0);
	    	}

			powerUpChance = (int)(Math.random() * 1001); //chance of power up spawning
			if(powerUpChance > 999)
			{
				powerUpPick = (int)(Math.random() * 3); //which power up to spawn
				if(powerUpPick == 0)
				{
					powerUps.add(new Wings((int)(Math.random() * (width-17)), 10, (int)getAdjustedDimension(25), (int)getAdjustedDimension(17), getResources()));
				}
				else if(powerUpPick == 1)
				{
					powerUps.add(new BigHoops((int)(Math.random() * (width-17)), 10, (int)getAdjustedDimension(25), (int)getAdjustedDimension(17), getResources()));
				}
				else
				{
					powerUps.add(new SlowDown((int)(Math.random() * (width-17)), 10, (int)getAdjustedDimension(25), (int)getAdjustedDimension(17), getResources()));
				}
			}
			for(int i = 0; i < powerUps.size(); i++)
			{
				if(powerUps.get(i).collide(p))
				{
					if(powerUps.get(i).collide(p))
					{
						if(powerUps.get(i) instanceof Wings)
						{
							wings = true;
							wingStart = System.currentTimeMillis();
							
						}
						if(powerUps.get(i) instanceof BigHoops)
						{
							hoops = true;
							hoopsStart = System.currentTimeMillis();
							h.setHeight((int) (h.getStartHeight()*1.5));
							h.setWidth((int) (h.getStartWidth()*1.5));
							h.reset();
						}
						if(powerUps.get(i) instanceof SlowDown)
						{
							slows = true;
							slowStart = System.currentTimeMillis();
						}
						powerUps.remove(i);
						continue;
					}
					if(powerUps.get(i).getY() > height)
					{
						powerUps.remove(i);
						continue;
					}
				}
			}
			if(System.currentTimeMillis()-wingStart>10000)
			{
				wings = false;
			}
			if(wings)
			{
				float dX = mouseX - width/2;
				float dY = mouseY - height/2;
				p.setXVelocity(dX/ballSpeed);
				p.setYVelocity(-dY/ballSpeed);
			}
			if(System.currentTimeMillis()-hoopsStart>10000)
			{
				hoops = false;
			}
			if(hoops)
			{
				h.setHeight((int) (h.getStartHeight()*1.5));
				h.setWidth((int) (h.getStartWidth()*1.5));
			}
			else
			{
				h.setHeight(h.getStartHeight());
				h.setWidth(h.getStartWidth());
			}
			if(System.currentTimeMillis()-slowStart>10000)
			{
				slows = false;
			}
			if(slows)
			{
				ballSpeed = (int) 17;
				p.setGravity((float) -6f/3);
			}
			else
			{
				ballSpeed = (int) 7;
				p.setGravity((float) -6f);
			}
    	}
    }
}
