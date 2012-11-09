package com.logosstudios.azteka;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;


public class Hoop 
{
	private int xPos, yPos;
	private Integer score;
	private int hoopHeight, hoopWidth, startWidth, startHeight;
	private GameInterface game;
	private boolean night;
	
	public Hoop(int w, int h, GameInterface g, boolean night)
	{
		startWidth = w;
		startHeight = h;
		hoopHeight = startHeight;
		hoopWidth = startWidth;
		game = g;
		reset();
		score = 0;
		this.night = night;
	}
	
	public void drawBack(Canvas c)
	{
		/*
		g.drawOval(xPos, yPos, 20, 40);
		g.drawOval(xPos, yPos, 21, 41);
		g.drawOval(xPos, yPos, 19, 39);
		*/
		RectF r1 = new RectF(xPos, yPos, xPos+hoopWidth, yPos+hoopHeight);
		RectF r2 = new RectF(xPos, yPos, xPos+hoopWidth-1, yPos+hoopHeight-1);
		RectF r3 = new RectF(xPos, yPos, xPos+hoopWidth-2, yPos+hoopHeight-2);
		Paint p = new Paint();
		if(night)
		{
			p.setColor(Color.YELLOW);
		}
		else
		{
			p.setColor(Color.BLACK);
		}
		p.setStyle(Paint.Style.STROKE);
		c.drawArc(r2, 90, 180, false, p);
		c.drawArc(r1, 90, 180, false, p);
		c.drawArc(r3, 90, 180, false, p);
	}
	public void drawFront(Canvas c)
	{
		RectF r1 = new RectF(xPos, yPos, xPos+hoopWidth, yPos+hoopHeight);
		RectF r2 = new RectF(xPos, yPos, xPos+hoopWidth-1, yPos+hoopHeight-1);
		RectF r3 = new RectF(xPos, yPos, xPos+hoopWidth-2, yPos+hoopHeight-2);
		Paint p = new Paint();
		if(night)
		{
			p.setColor(Color.YELLOW);
		}
		else
		{
			p.setColor(Color.BLACK);
		}
		p.setStyle(Paint.Style.STROKE);
		c.drawArc(r2, -90, 180, false, p);
		c.drawArc(r1, -90, 180, false, p);
		c.drawArc(r3, -90, 180, false, p);
	}
	public int getX()
	{
		return xPos;
	}
	public int getY()
	{
		return yPos;
	}
	public int getStartHeight()
	{
		return startHeight;
	}
	public int getStartWidth()
	{
		return startWidth;
	}
	public void setHeight(int h)
	{
		hoopHeight = h;
	}
	public void setWidth(int w)
	{
		hoopWidth = w;
	}
	public void setNight(boolean b)
	{
		night = b;
	}
	public void reset()
	{
		xPos = (int)((Math.random() * (game.getGameWidth()-hoopWidth)));
		yPos = (int)((Math.random() * (game.getGameHeight()-hoopHeight)));
	}
	public void score()
	{
		score++;
		game.increaseTimeAllowed(1500);
		reset();
	}
	public Integer getScore()
	{
		return score;
	}
	public void collide(Particle p)
	{
		//Scoring box
		int delta = p.deltaX();
		if(delta>0)
		{
			if(p.getX() <= xPos + (hoopWidth + 4) && p.getX() + p.getSize() >= xPos + hoopWidth-3)
			{
				if(p.getY() + p.getSize() >= yPos && p.getY() <=yPos + (hoopHeight))
				{
					score();
					return;
				}
			}
		}
		else if(delta<0)
		{
			if(p.getX() <= xPos + 4 && p.getX() + p.getSize() >= xPos - 3)
			{
				if(p.getY() + p.getSize() >= yPos && p.getY() <= yPos + (hoopHeight))
				{
					score();
					return;
				}
			}
		}
		
		//Upper box
		if((p.getX()+p.getSize() > xPos + (delta>0?hoopWidth/4:0)  && p.getX() < xPos + (delta>0?hoopWidth:hoopWidth*0.75)) && (p.getY()+p.getSize() > yPos && p.getY() < yPos+p.getSize()/2))
		{
			if((p.getPreviousX()+p.getSize() > xPos + (delta>0?hoopWidth/4:0)  && p.getPreviousX() < xPos + (delta>0?hoopWidth:hoopWidth*0.75)))
			{
				if(p.deltaY()>0)
				{
					p.setY(yPos - (p.getSize()+1));
					p.setYVelocity(-p.getYVelocity()/2.5f);
					return;
				}
				else
				{
					p.setY(yPos + p.getSize()/2);
					p.setYVelocity(-p.getYVelocity()/2.5f);
					return;
				}
			}
			if((p.getPreviousY()+p.getSize() > yPos + hoopHeight - p.getSize()/2 && p.getPreviousY() < yPos + hoopHeight))
			{
				if(p.deltaX()>0)
				{
					p.setX(xPos-p.getSize());
					p.setXVelocity(0);
					return;
				}
				else
				{
					p.setX(xPos+hoopWidth);
					p.setXVelocity(0);
					return;
				}
			}
			return;
		}
		//Lower Box
		if((p.getX()+p.getSize() > xPos + (delta>0?hoopWidth/4:0)  && p.getX() < xPos + (delta>0?hoopWidth:hoopWidth*0.75)) && (p.getY()+p.getSize() > yPos + hoopHeight - p.getSize()/2 && p.getY() < yPos + hoopHeight))
		{
			if((p.getPreviousX()+p.getSize() > xPos + (delta>0?hoopWidth/4:0)  && p.getPreviousX() < xPos + (delta>0?hoopWidth:hoopWidth*0.75)))
			{
				if(p.deltaY()>0)
				{
					p.setY(yPos + hoopHeight - (p.getSize()+5));
					p.setYVelocity(-p.getYVelocity()/2.5f);
					return;
				}
				else
				{
					p.setY(yPos + hoopHeight);
					p.setYVelocity(-p.getYVelocity()/2.5f);
					return;
				}
			}
			if((p.getPreviousY()+p.getSize() > yPos + hoopHeight - p.getSize()/2 && p.getPreviousY() < yPos + hoopHeight))
			{
				if(p.deltaX()>0)
				{
					p.setX(xPos-p.getSize());
					p.setXVelocity(0);
					return;
				}
				else
				{
					p.setX(xPos+hoopWidth);
					p.setXVelocity(0);
					return;
				}
			}
			return;
		}
	}
}
