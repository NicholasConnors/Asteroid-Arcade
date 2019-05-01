package njc.asteroids.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import njc.asteroids.Game;
import njc.asteroids.object.entities.Entity;
import njc.asteroids.object.entities.player.Player;

public class PowerUp extends Entity {
	public enum Type {
		HEART,
		ENERGY,
		COIN, 
		TRIPLE_SHOT,
		SUPER_MODE,
		ATOM
	}
	private Type type;
	public PowerUp(float y, Texture t, Type type, float scale) {
		this.type = type;
		
		this.setTexture(scale, t);
		float x = ((int) ((Math.random() * (Game.WIDTH - this.getWidth())) / this.getWidth())) * this.getWidth();
		this.setPosition(new Vector2(x, y));
		this.setVelocity(new Vector2(0, 200f));
		this.setTeam(Team.POWERUP);
	}
	
	public PowerUp(float x, float y, Texture t, Type type, float scale) {
		this(y, t, type, scale);
		this.getPosition().x = x;
	}
	
	public void onCollide(Entity e) {
		if(e instanceof Player) {
			Player p = (Player) e;
			switch(type) {
				case COIN: p.score++; break;
				case HEART: 
					float hp = p.getHealth() + 8f;
					if(hp > p.getMaxHealth()) hp = p.getMaxHealth();
					p.setHealth(hp);
					break;
				case ENERGY:
					p.weapon.setEnergy(p.weapon.getMaxEnergy());
					if(p.shield != null) p.setShieldHealth(p.getMaxShieldHealth());
					break;
				case TRIPLE_SHOT: 
					p.tripleShot = true; 
					p.tripleShotTimer = 20f; 
					break;
				case SUPER_MODE: 
					if(p.shield != null) p.setShieldHealth(p.getMaxShieldHealth());
					p.setHealth(p.getMaxHealth());
					p.superMode = true;
					p.superModeTimer = 20f; 
					break;
				case ATOM: 
					p.atomBomb = true; 
					break;
			}
			this.markForRemoval = true;
		}
		if(e instanceof PowerUp && !this.markForRemoval) {
			e.markForRemoval = true;
		}
	}
}
