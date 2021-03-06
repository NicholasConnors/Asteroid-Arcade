package io.itch.nickjconnors.object;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextObject extends GameObject {
	private float scale;
	private BitmapFont font;
	private GlyphLayout layout;
	public TextObject(String msg, float scale, BitmapFont font) {
		this.scale = scale;
		this.font = font;

		this.setMsg(msg);
	}
	
	public void setMsg(String msg, float scale) {
		this.scale = scale;
		setMsg(msg);
	}
	
	public void setMsg(String msg) {		
		this.layout = new GlyphLayout();
		this.font.getData().setScale(this.scale);
		layout.setText(this.font, msg);
		
		this.width = this.layout.width;
		this.height = this.layout.height;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!this.getVisibility()) return;
		
		font.getData().setScale(this.scale);
		this.font.draw(batch, layout, this.getPosition().x, this.getPosition().y + this.height);
	}
}
