package com.logosstudios.azteka;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.logostudios.azteka.R;

public class BigHoops implements PowerUp {
	private int x, y;
	private Bitmap bigHoops;
	
	public BigHoops(int x, int y, int width, int height, Resources r)
	{
		this.x = x;
		this.y = y;
		bigHoops = BitmapFactory.decodeResource(r, R.drawable.bighoops);
		bigHoops = Bitmap.createScaledBitmap(bigHoops, width, height, false);
	}
	public boolean collide(Particle p) 
	{
		y++;
		if(p.getX() + 10 > x && p.getX() < x + bigHoops.getWidth() && p.getY()+10>y && p.getY() < y+bigHoops.getHeight())
		{
			return true;
		}
		return false;
	}
	public void draw(Canvas c)
	{
		c.drawBitmap(bigHoops, x, y, null);
	}
	public int getY()
	{
		return y;
	}
}
