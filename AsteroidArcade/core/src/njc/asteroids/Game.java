package njc.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import njc.asteroids.graphics.Font;
import njc.asteroids.managers.MusicHandler;
import njc.asteroids.managers.SceneManager;
import njc.asteroids.scene.LoadScene;

public class Game extends ApplicationAdapter {
	public static int WIDTH = 370, HEIGHT = 640;
	public static final String TITLE = "Generic Asteroid Game";
	
	public static float masterVolume, musicVolume;
	
	private SpriteBatch _batch;
	private SceneManager _sceneManager;
	private AssetManager _assetManager;
	private Font[] _fonts; 
	
	private OrthographicCamera _camera;
	private FitViewport _viewport;
	
	@Override
	public void create() {
		//Init
		_batch = new SpriteBatch();
		_assetManager = new AssetManager();
		
		_camera = new OrthographicCamera(WIDTH, HEIGHT);
		_camera.setToOrtho(false, WIDTH, HEIGHT);		
	    
		_camera.position.set(_camera.viewportWidth/2,_camera.viewportHeight/2,0);
		_camera.update();
		
		_viewport = new FitViewport(WIDTH, HEIGHT, _camera);
		_viewport.apply();

		//Set background to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		//Load music
		loadVolume();
		loadMusic("Arcadia.wav");
		loadMusic("Salty_Ditty.wav");
		_assetManager.finishLoading();
		
		//Set up sceneManager
		_fonts = new Font[2];
		_fonts[0] = new Font(Font.yellow);
		_fonts[1] = new Font(Font.white);
		_sceneManager = new SceneManager(new MusicHandler(_assetManager), _fonts);
		
		//Load resources for load scene
		loadTexture("textures/asteroid.png");
		loadTexture("textures/star.png");
		loadTexture("textures/fade.png");
		_assetManager.finishLoading();
		
		//Push load scene
		_sceneManager.push(new LoadScene(_sceneManager, _assetManager, _fonts));
		
		loadTexture("textures/boom.png");
		loadTexture("textures/dish.png");
		loadTexture("textures/flame_1.png");
		loadTexture("textures/flame_2.png");
		loadTexture("textures/junk.png");
		
		//Pickups
		loadTexture("textures/coin.png");
		loadTexture("textures/energy.png");
		loadTexture("textures/halfheart.png");
		loadTexture("textures/heart.png");
		loadTexture("textures/atom.png");
		loadTexture("textures/superstar.png");
		loadTexture("textures/tripleshot.png");
		
		//Weapons
		loadTexture("textures/weapons/laser.png");
		loadTexture("textures/weapons/missile.png");
		loadTexture("textures/weapons/photon.png");
		
		loadTexture("textures/mine.png");
		loadTexture("textures/panel.png");

		loadTexture("textures/satellite.png");
		loadTexture("textures/shield.png");
		loadTexture("textures/shieldheart.png");
		loadTexture("textures/smallflame.png");
		loadTexture("textures/smoke.png");
		loadTexture("gui/unknown.png");

		loadTexture("textures/startrail.png");
		loadTexture("textures/orangetrail.png");
		
		loadTexture("textures/wormhole.png");
		
		//Ships
		loadTexture("textures/ships/rocket.png");
		loadTexture("textures/ships/shuttle.png");
		loadTexture("textures/ships/ufo.png");
		loadTexture("textures/ships/laser_ship.png");
		loadTexture("textures/ships/whale_1.png");
		loadTexture("textures/ships/whale_2.png");
		loadTexture("textures/ships/eagle.png");
		
		loadTexture("textures/ships/boss_laser.png");
		loadTexture("textures/ships/boss_laser_spikes.png");
		
		loadTexture("textures/ships/boss_ufo.png");
		loadTexture("textures/ships/boss_ufo_spikes.png");
		loadTexture("textures/ships/boss_turret.png");
		
		loadTexture("textures/ships/swarm_ufo.png");
		loadTexture("textures/ships/swarm_ufo_2.png");
		
		//GUI
		loadTexture("gui/music.png");
		loadTexture("gui/mute.png");
		loadTexture("gui/pause.png");
		loadTexture("gui/play.png");
		loadTexture("gui/sound.png");
		loadTexture("gui/soundMute.png");
		loadTexture("gui/button.png");
		loadTexture("gui/options.png");
		loadTexture("gui/optionsLarge.png");
		loadTexture("gui/arrowLeft.png");
		loadTexture("gui/arrowRight.png");
		
		loadAudio("blip.wav");
		loadAudio("bloop.wav");
		loadAudio("death.wav");
		loadAudio("explosion1.wav");
		loadAudio("explosion2.wav");
		loadAudio("explosion3.wav");
		loadAudio("laser1.wav");
		loadAudio("laser2.wav");
		loadAudio("laser3.wav");
		loadAudio("pickup1.wav");
		loadAudio("pickup2.wav");
		loadAudio("pickup3.wav");
	}
	
	private void loadTexture(String s) {
		this._assetManager.load(s, Texture.class);
	}
	
	private void loadAudio(String s) {
		this._assetManager.load("audio/" + s, Sound.class);
	}
	
	private void loadMusic(String s) {
		this._assetManager.load("audio/music/" + s, Music.class);
	}
	
	public void update(float dt) {
		_sceneManager.peek().handleInput(dt, this.getInput(), this.getRoll());
		_sceneManager.peek().update(dt);
	}

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());
			
		_camera.update();	
		_batch.setProjectionMatrix(_camera.combined);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		_batch.begin();
		_sceneManager.peek().render(_batch);
		_batch.end();
	}
	
	@Override
	public void dispose() {
		_batch.dispose();
		_assetManager.dispose();
		for(Font f : _fonts) {
			f.dispose();
		}
	}
	
   @Override
   public void resize(int width, int height){
      _viewport.update(width,height);
      _camera.position.set(_camera.viewportWidth/2,_camera.viewportHeight/2,0);
   }
	
	public void loadVolume() {
		masterVolume = Gdx.app.getPreferences("AsteroidArcadePrefs").getBoolean("soundMute", false) ? 0f : 0.5f;
		musicVolume = Gdx.app.getPreferences("AsteroidArcadePrefs").getBoolean("musicMute", false) ? 0f : 0.5f;
	}
	
	private Vector2 getInput() {
		float x = (Gdx.input.getX() - _viewport.getScreenX()) * (WIDTH / (float)_viewport.getScreenWidth());
		float y = HEIGHT - (Gdx.input.getY() - _viewport.getScreenY()) * (HEIGHT / (float)_viewport.getScreenHeight());
		
		return new Vector2(x, y);
	}
	
	private float getRoll() {
		float angle = Gdx.input.getRoll();
		
		if(angle > 30) angle = 30;
		if(angle < -30) angle = -30;
		
		return (float) Math.sin(Math.toRadians(angle));
	}
}
