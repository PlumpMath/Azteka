package com.logosstudios.azteka;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.logostudios.azteka.R;

public class Particle
{
	int xPos, yPos, previousX, previousY, size;
	float yVelocity, xVelocity, gravity, max;
	boolean groundFriction;
	private Bitmap draw;
	private Canvas graphics;
	private Bitmap image;
	private GameInterface game;
	private float angle, previousAngle;

	public Particle(Resources r, GameInterface game, int size)
	{
		xPos = (int)(Math.random() * game.getGameWidth());
		yPos = (int)(Math.random() * game.getGameHeight());
		this.size = size;
		yVelocity = 10f;
		xVelocity = 0;
		gravity = -5.8f;
		groundFriction = false;
		image = BitmapFactory.decodeResource(r, R.drawable.ball);
		image = Bitmap.createScaledBitmap(image, size, size, false);
		draw = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		graphics = new Canvas(draw);
		this.game = game;
		max = 20.0f;
	}

	public void move()
	{
		if(groundFriction)
		{
			xVelocity *= 0.8;
		}
		previousX = xPos;
		previousY = yPos;

		yPos -= yVelocity;
		xPos += xVelocity;

		previousAngle = angle;

		angle += xVelocity;
		if(yPos+size> game.getGameHeight())
		{
			yVelocity = -yVelocity/1.5f;
			yPos = game.getGameHeight()-size;
			if(yVelocity < 4f)
			{
				yVelocity -= yVelocity;
				groundFriction = true;
			}
		}
		if(yPos+size < game.getGameHeight())
		{
			yVelocity = yVelocity + (gravity/10);
			groundFriction = false;
		}
		if(yPos < 0)
		{
			yPos = 0;
			yVelocity = -yVelocity;
		}
		if(xPos < 0)
		{
			xVelocity = -xVelocity/1.5f;
			xPos = 0;
		}
		if(xPos+size > game.getGameWidth())
		{
			xVelocity = -xVelocity/1.5f;
			xPos = game.getGameWidth()-size;
		}
	}
	public int getX()
	{
		return xPos;
	}
	public int getY()
	{
		return yPos;
	}
	public int getPreviousX()
	{
		return previousX;
	}
	public int getPreviousY()
	{
		return previousY;
	}
	public float getXVelocity()
	{
		return xVelocity;
	}
	public float getYVelocity()
	{
		return yVelocity;
	}
	public int getSize()
	{
		return size;
	}
	public int deltaX()
	{
		return xPos-previousX;
	}
	public int deltaY()
	{
		return yPos-previousY;
	}
	public void stop()
	{
		xVelocity = 0;
		yVelocity = 0;
	}
	public void setXVelocity(float x)
	{
		if(xVelocity < max)
		{
			xVelocity = x;
		}
		else
		{
			xVelocity = max;
		}
	}
	public void setYVelocity(float y)
	{
		if(yVelocity < max)
		{
			yVelocity = y;
		}
		else
		{
			yVelocity = max;
		}
	}
	public void setX(int x)
	{
		xPos = x;
	}
	public void setY(int y)
	{
		yPos = y;
	}
	public void setGravity(float g)
	{
		gravity = g;
	}
	public void draw(Canvas c)
	{
		//graphics = draw.createGraphics();
		//graphics.clearRect(0, 0, 10, 10);
		graphics.rotate(angle-previousAngle, size/2.0f, size/2.0f);
		graphics.drawBitmap(image, 0, 0, null);
		c.drawBitmap(draw, xPos, yPos, null);
	}
}
