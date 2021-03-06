package io.itch.nickjconnors.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.itch.nickjconnors.AsteroidArcadeGame;

public class GameObject implements Cloneable {
	private Vector2 position, velocity;
	
	private float theta, omega;
	protected float width, height;
	
	private Texture texture;
	
	private boolean visible = true;
	private float opacity = 1f;
	
	//Animation
	protected boolean isAnimated = false;
	private Texture[] animationFrames;
	private float animationTimer, frameTime;
	private int animationIndex;
	
	//Parent
	private GameObject parent;
	private boolean hasParent = false;
	private boolean copyParentRotation = false;
	private Vector2 offset;
	
	protected boolean markForRemoval = false;
	
	public GameObject() {
		position = new Vector2();
		velocity = new Vector2();
	}
	
	public GameObject setTexture(float scale, Texture t) {
		this.isAnimated = false;
		
		this.texture = t;
		this.width = t.getWidth() * scale;
		this.height = t.getHeight() * scale;
		
		return this;
	}
	
	public GameObject setTexture(float scale, float frameTime, Texture... textures) {
		this.isAnimated = true;
		this.animationIndex = 0;
		this.animationFrames = textures;
		this.frameTime = frameTime;
		
		this.texture = this.animationFrames[0];
		this.width = texture.getWidth() * scale;
		this.height = texture.getHeight() * scale;
		
		return this;
	}
	
	public GameObject setPosition(Vector2 position) {
		this.position = position;
		return this;
	}
	
	public void setParent(GameObject p, Vector2 o, boolean r) {
		this.parent = p;
		this.offset = o;
		this.copyParentRotation = r;
		
		this.hasParent = true;
	}
	
	public void setOpacity(float o) {
		this.opacity = o;
	}
	
	public boolean getVisibility() {
		return this.visible;
	}
	
	public GameObject setRotation(float theta, float omega) {
		this.theta = theta;
		this.omega = omega;
		return this;
	}
	
	public GameObject setVelocity(Vector2 velocity) {
		this.velocity = velocity;
		return this;
	}
	
	public void setHeight(float h) {
		this.height = h;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public Vector2 getVelocity() {
		return this.velocity;
	}
	
	public void changeFrame() {
		if(++this.animationIndex >= animationFrames.length) {
			this.animationIndex = 0;
		}
		this.texture = animationFrames[animationIndex];
	}
	
	public void setVisibility(boolean v) {
		this.visible = v;
	}
	
	public GameObject centerX() {
		this.getPosition().x = (AsteroidArcadeGame.WIDTH - this.width) / 2f; 
		return this;
	}
	
	public GameObject centerY() {
		this.getPosition().y = (AsteroidArcadeGame.HEIGHT - this.height) / 2f; 
		return this;
	}
	
	public void update(float dt) {
		if(this.isAnimated) {
			this.animationTimer += dt;
			if(this.frameTime != -1f && this.animationTimer > frameTime) {
				this.animationTimer = 0f;
				changeFrame();
			}
		}
		
		if(!this.hasParent) this.position.mulAdd(this.velocity, dt);
		else {
			if(this.parent == null || this.parent.markForRemoval) 
				this.markForRemoval = true;
			else {
				float x, y;
				if(copyParentRotation) {
					this.setRotation(parent.getRotation(), 0f);
					
					float angle = (float) Math.toRadians(parent.getRotation());
					
					float s = (float) Math.sin(angle);
					float c = (float) Math.cos(angle);
					
					x = (c * this.offset.x) - (s * this.offset.y);
					y = (s * this.offset.x) + (c * this.offset.y);
				} else {
					x = this.offset.x;
					y = this.offset.y;
				}
				this.position = new Vector2(
						x + parent.getPosition().x + (parent.getWidth() - this.getWidth()) / 2f,
						y + parent.getPosition().y + (parent.getHeight() - this.getHeight()) / 2f
						);
			}
		}
				
		this.theta += this.omega * dt;
		
		if(this.position.y > AsteroidArcadeGame.HEIGHT * 1.5f) markForRemoval = true;
		if(this.position.y < -AsteroidArcadeGame.HEIGHT / 2f) markForRemoval = true;
		if(this.position.x < -AsteroidArcadeGame.WIDTH / 2f) markForRemoval = true;
		if(this.position.x > AsteroidArcadeGame.WIDTH * 1.5f) markForRemoval = true;
	}
	
	public GameObject getParent() {
		if(!this.hasParent) return null;
		else return this.parent;
	}
	
	public void render(SpriteBatch batch) {
		if(!this.visible) return;
		if(this.opacity != 1) batch.setColor(1f, 1f, 1f, this.opacity);
		
		batch.draw(
				new TextureRegion(this.texture),
				this.position.x, this.position.y,
				this.width / 2f, this.height / 2f,
				this.width, this.height,
				1f, 1f, this.theta);
		
		if(this.opacity != 1) batch.setColor(1f, 1f, 1f, 1f);
	}
	
	public boolean isOnScreen() {
		if(this.position.x > -this.getWidth() && this.position.x < AsteroidArcadeGame.WIDTH ) {
			if(this.position.y > -this.getHeight() && this.position.y < AsteroidArcadeGame.HEIGHT + this.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public float getRotation() {
		return this.theta;
	}
	
	public float getAngularVelocity() {
		return this.omega;
	}
	
	public boolean isTouched(Vector2 pos) {
		if(pos.x > this.getPosition().x && pos.x < this.getPosition().x + this.getWidth()) {
			if(pos.y > this.getPosition().y && pos.y < this.getPosition().y + this.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	public void invert() {
		this.position.y = (this.position.y - AsteroidArcadeGame.HEIGHT) * -1 - this.getHeight();
		this.velocity.y *= -1f;
	}
	
	public boolean isMarkedForRemoval() {
		return this.markForRemoval;
	}
	
	public void setMarkedForRemoval(boolean remove) {
		this.markForRemoval = remove;
	}
	
	public boolean collisionCheck(GameObject b) {
		float dy = (this.getPosition().y + this.getHeight() / 2f) - (b.getPosition().y + b.getHeight() / 2f);
		if(Math.abs(dy) / 0.8 < (this.getHeight() + b.getHeight()) / 2f) {
			float dx = (this.getPosition().x + this.getWidth() / 2f) - (b.getPosition().x + b.getWidth() / 2f);
			if(Math.abs(dx) / 0.8 < (this.getWidth() + b.getWidth()) / 2f) {				
				return true;
			}
		}
		return false;
	}
}
