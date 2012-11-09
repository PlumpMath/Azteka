package com.logosstudios.azteka;
import android.graphics.Canvas;


public interface PowerUp {
	public boolean collide(Particle p);
	public void draw(Canvas c);
	public int getY();
}
