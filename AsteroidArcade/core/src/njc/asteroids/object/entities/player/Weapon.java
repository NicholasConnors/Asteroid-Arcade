package njc.asteroids.object.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import njc.asteroids.object.entities.Entity;

public class Weapon {
	public enum Type {
		LASER,
		MISSILE,
		PHOTON,
		BURST_LASER;
	}
	private Entity parent;
	public Type type;
	private float cooldown;
	private float energy, maxEnergy;
	private int offsetX = 0;
	
	private Texture texture;
	
	public Weapon(Entity p, Type t, float e, Texture tex) {
		this.parent = p;
		this.type = t;
		this.maxEnergy = e;
		this.energy = e;
		
		this.texture = tex;
	}
	
	public void setOffset(int o) {
		this.offsetX = o;
	}
	
	public void setEnergy(float e) {
		if(e > this.maxEnergy) this.energy = this.maxEnergy;
		else this.energy = e;
	}
	
	public float getEnergy() {
		return this.energy;
	}
	
	public float getMaxEnergy() {
		return this.maxEnergy;
	}

	public Entity fire() {
		return fire(90);
	}
	
	public Entity fire(float dir) {
		return fire(dir, false);
	}
	
	public Entity fire(float dir, boolean ignoreEnergy) {
		if(cooldown > 0 || (this.parent instanceof Player && energy < 0))
			return null;
		
		Entity e = new Entity();
		e.setRotation(dir - 90f, 0);
		float vel = 300f;
		float hp = 4f;
		
		switch(type) {
			case LASER:
				e.setTexture(3f, this.texture);
				e.canExplode = false;
				vel = 1000f;
				e.setHeight(48f);
				hp = 2f;
				if(!ignoreEnergy) {
					cooldown -= 0.02f;
					if(this.parent instanceof Player) {
						energy -= 0.1f;
						if(energy <= 0) cooldown += 1.0f;
					}
				}
				break;
			case PHOTON:
				e.setTexture(3f, this.texture);
				e.canExplode = false;
				e.setRotation((float) Math.random() * 90, (float) Math.random() * 180f - 90f); 
				hp = 4f;
				if(!ignoreEnergy) {
					cooldown += 0.15f;
					if(this.parent instanceof Player) energy -= 0.75f;
				}
				break;
			case MISSILE:
				e.setTexture(2f, this.texture);
				vel = 600f;
				e.explodeOnDeath = true;
				if(!ignoreEnergy) cooldown += 0.5f;
				hp = 1f;
				break;
			case BURST_LASER:
				e.canExplode = false;
				e.setTexture(3f, this.texture);
				vel = 1000f;
				e.setHeight(48f);
				hp = 2f;
				if(!ignoreEnergy) cooldown += 0.10f;
				if(!ignoreEnergy && this.parent instanceof Player) {
					energy -= 0.50f;
				}
				break;
			default:
				return null;
		}

		float x = parent.getPosition().x + (parent.getWidth() - e.getWidth()) / 2f + (float) offsetX;
		float y = parent.getPosition().y + (parent.getHeight() - e.getHeight()) / 2f;
		
		e.setPosition(new Vector2(x, y));
		e.setVelocity(new Vector2(vel * (float) Math.cos(Math.toRadians(dir)), vel * (float) Math.sin(Math.toRadians(dir))));
		
		e.setHealth(hp);
		e.setTeam(parent.getTeam());
		
		return e;
	}
	
	public void update(float dt) {
		if(cooldown > 0) cooldown -= dt;
		if(cooldown < 0) cooldown = 0;
		
		if(energy < maxEnergy) {
			this.energy += 2f * dt;
		}
		if(energy > maxEnergy) {
			this.energy = maxEnergy;
		}
	}
}
