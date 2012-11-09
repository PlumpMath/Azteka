package com.logosstudios.azteka;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.logostudios.azteka.R;

public class Wings implements PowerUp {
	private int x, y;
	private Bitmap wings;
	
	public Wings(int x, int y, int width, int height, Resources r)
	{
		this.x = x;
		this.y = y;
		wings = BitmapFactory.decodeResource(r, R.drawable.wings);
		wings = Bitmap.createScaledBitmap(wings, width, height, false);
	}
	public boolean collide(Particle p) 
	{
		y++;
		if(p.getX() + 10 > x && p.getX() < x + wings.getWidth() && p.getY()+10>y && p.getY() < y+wings.getHeight())
		{
			return true;
		}
		return false;
	}
	public void draw(Canvas c)
	{
		c.drawBitmap(wings, x, y, null);
	}
	public int getY()
	{
		return y;
	}
}
