package io.itch.nickjconnors.object.entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.itch.nickjconnors.AsteroidArcadeGame;
import io.itch.nickjconnors.object.GameObject;
import io.itch.nickjconnors.object.entities.Entity;

public class Player extends Entity {
	private Weapon weapon;
	private GameObject shield;
	
	public float timer;
	private float shieldHealth, maxShieldHealth;
	
	public boolean tripleShot = false;
	public float tripleShotTimer = 0f;
	
	public boolean superMode = false;
	public float superModeTimer = 0f;
	
	public boolean magnetMode = false;
	public float magnetTimer = 0f;
	
	public boolean atomBomb = false;
	
	private boolean hpRegen = false;
	public int score = 0;
	
	private float speed = 1f;
	private float roll;
	
	public Player() {
		this.setTeam(Team.GOOD);
	}
	
	@Override
	public void damage() {
		if(this.maxShieldHealth > 0 && shieldHealth > 0) {
			if(this.shieldHealth > 0) shieldHealth--;
			if(shieldHealth <= 0) {
				this.shield.setVisibility(false);
			}
		}
		else this.setHealth(this.getHealth() - 1f);
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public GameObject addShield(float maxHP, Texture shieldTexture, float scale) {
		this.shieldHealth = maxHP;
		this.maxShieldHealth = maxHP;
		
		GameObject shield = new GameObject().setTexture(scale, shieldTexture).setRotation(0f, 180f);
		shield.setParent(this, new Vector2(0,0), false);
		this.shield = shield;
		
		return shield;
	}
	
	public void setShieldHealth(float hp) {
		this.shieldHealth = hp;
		if(hp != 0)	this.shield.setVisibility(true);
		else this.shield.setVisibility(false);
	}
	
	public float getShieldHealth() {
		return this.shieldHealth;
	}
	
	public float getMaxShieldHealth() {
		return this.maxShieldHealth;
	}
	
	public void setWeapon(Weapon.Type type, float e, Texture wt) {
		this.weapon = new Weapon(this, type, e, wt);
	}
	
	public void setWeapon(Weapon.Type type, float e, Texture... wt) {
		this.weapon = new Weapon(this, type, e, wt);
	}
	
	public void setSpeed(float f) {
		this.speed = f;
	}
	
	public void addRegen() {
		this.hpRegen = true;
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		this.weapon.update(dt);
		
		timer += dt;
		
		if(this.hpRegen) {
			float hp = this.getHealth() + 0.5f * dt;
			if(hp > this.getMaxHealth()) hp = this.getMaxHealth();
			this.setHealth(hp);
		}
		
		float currentSpeed = (superMode ? speed * 1.5f : speed);
		
		this.getVelocity().y = (float) Math.sin(6 * timer) * 16f;
		this.getVelocity().x += this.roll * 1280f * currentSpeed * currentSpeed * dt;
		
		if(this.getVelocity().x > 128f * currentSpeed) this.getVelocity().x = 128f * currentSpeed;
		if(this.getVelocity().x < -128f * currentSpeed) this.getVelocity().x = -128f * currentSpeed;
		
		if(this.getPosition().x > AsteroidArcadeGame.WIDTH - this.getWidth()) {
			this.getPosition().x = AsteroidArcadeGame.WIDTH - this.getWidth();
			this.getVelocity().x = 0;
		} else if(this.getPosition().x < 0) {
			this.getPosition().x = 0;
			this.getVelocity().x = 0;
		}
		
		//Power ups
		if(tripleShot) {
			tripleShotTimer -= dt;
			if(tripleShotTimer < 0) {
				tripleShotTimer = 0f;
				tripleShot = false;
			}
		}
		
		if(superMode) {
			superModeTimer -= dt;
			if(superModeTimer < 0) {
				superModeTimer = 0f;
				superMode = false;
			}
		}
		
		if(magnetMode) {
			magnetTimer -= dt;
			if(magnetTimer < 0) {
				magnetTimer = 0f;
				magnetMode = false;
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
	}
	
	public Weapon getWeapon() {
		return this.weapon;
	}
	
	public void setShieldToMax() {
		if(this.shield != null) this.setShieldHealth(this.getMaxShieldHealth());
	}
}
