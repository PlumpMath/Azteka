package com.logosstudios.azteka;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.logostudios.azteka.R;

public class SlowDown implements PowerUp {
	private int x, y;
	private Bitmap slow;
	
	public SlowDown(int x, int y, int width, int height, Resources r)
	{
		this.x = x;
		this.y = y;
		slow = BitmapFactory.decodeResource(r, R.drawable.slow);
		slow = Bitmap.createScaledBitmap(slow, width, height, false);
	}
	public boolean collide(Particle p) 
	{
		y++;
		if(p.getX() + 10 > x && p.getX() < x + slow.getWidth() && p.getY()+10>y && p.getY() < y+slow.getHeight())
		{
			return true;
		}
		return false;
	}
	public void draw(Canvas c)
	{
		c.drawBitmap(slow, x, y, null);
	}
	public int getY()
	{
		return y;
	}
}
