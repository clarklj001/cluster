package com.kylin.tankwar.core;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.kylin.tankwar.Direction;
import com.kylin.tankwar.TankFrame;
import com.kylin.tankwar.Tank_;

public class Tank {
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	private static final Logger logger = Logger.getLogger(Tank.class);
	
	private static Random r = new Random();
	
	private boolean bL = false;
	private boolean bU = false;
	private boolean bR = false;
	private boolean bD = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	static {
		tankImages = new Image[] {
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank_.class.getClassLoader().getResource("images/tankLD.gif"))
		};
		
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);
		
	}
	
	private String id;
	
	private boolean isGood;
	
	private boolean isLive ;
	
	private int life ;
	
	private int x;
	
	private int y;
	
	private Direction dir;
	
	private Direction ptDir;

	public Tank(String id, boolean isGood, boolean isLive, int life, int x, int y, Direction dir, Direction ptDir) {
		super();
		this.id = id;
		this.isGood = isGood;
		this.isLive = isLive;
		this.life = life;
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.ptDir = ptDir;
	}
	
	public Tank(TankView view) {
		updateTank(view);
	}
	
	private int oldX, oldY;
	
	private int step = r.nextInt(12) + 3;
	
	public TankView getView() {
		return new TankView(id, isGood, isLive, life, x, y, dir, ptDir);
	}
	
	public String getId() {
		return id;
	}

	/**
	 * Update Tank status after either synchronous or asychronous session replication finished
	 * @param tankView
	 */
	public void updateTank(TankView tankView){
		
		this.id = tankView.getId();
		this.isGood = tankView.isGood();
		this.isLive = tankView.isLive();
		this.life = tankView.getLife();
		this.x = tankView.getX();
		this.y = tankView.getY();
		this.dir = tankView.getDir();
		this.ptDir = tankView.getPtDir();
	}

	public boolean isGood() {
		return isGood;
	}

	public void setGood(boolean isGood) {
		this.isGood = isGood;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public void draw(Graphics g) {
		
		if(!isLive) {
			return;
		}
		
		switch(ptDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
		
//		move();
	}
	
	private void move() {
		
		this.oldX = x;
		this.oldY = y;
		
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank_.WIDTH > TankFrame.GAME_WIDTH) x = TankFrame.GAME_WIDTH - Tank_.WIDTH;
		if(y + Tank_.HEIGHT > TankFrame.GAME_HEIGHT) y = TankFrame.GAME_HEIGHT - Tank_.HEIGHT;
		
		if(!isGood) {
			Direction[] dirs = Direction.values();
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}			
			step --;
			
		}		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		
		locateDirection();
	}
	
	private void locateDirection() {
		
		logger.debug("locate direction [bL= " + bL + ", bU= " + bU + ", bR= " + bR + ", bD= " + bD + "]");
		
		if(bL && !bU && !bR && !bD) {
			dir = Direction.L;
		} else if(bL && bU && !bR && !bD) {
			dir = Direction.LU;
		} else if(!bL && bU && !bR && !bD) {
			dir = Direction.U;
		} else if(!bL && bU && bR && !bD) {
			dir = Direction.RU;
		} else if(!bL && !bU && bR && !bD) {
			dir = Direction.R;
		} else if(!bL && !bU && bR && bD) {
			dir = Direction.RD;
		} else if(!bL && !bU && !bR && bD) {
			dir = Direction.D;
		} else if(bL && !bU && !bR && bD) {
			dir = Direction.LD;
		} else if(!bL && !bU && !bR && !bD) {
			dir = Direction.STOP;
		}
		
		logger.debug("Tank direction: " + dir);
	}

}