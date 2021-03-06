package au.com.codeka.warworlds.game.wormhole;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.ITextureAtlas.ITextureAtlasStateListener;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import au.com.codeka.BackgroundRunner;
import au.com.codeka.common.Log;
import au.com.codeka.warworlds.BaseGlFragment;
import au.com.codeka.warworlds.game.starfield.RadarIndicatorEntity;
import au.com.codeka.warworlds.model.Sector;

public class WormholeSceneManager {
    private static final Log log = new Log("SectorSceneManager");
    private Scene mScene;
    private BaseGlFragment mFragment;
    private String mWormholeStarKey;
    private boolean mWasStopped;

    private BitmapTextureAtlas mWormholeTextureAtlas;
    private TiledTextureRegion mWormholeTextureRegion;

    private BitmapTextureAtlas mBackgroundGasTextureAtlas;
    private TiledTextureRegion mBackgroundGasTextureRegion;
    private BitmapTextureAtlas mBackgroundStarsTextureAtlas;
    private TiledTextureRegion mBackgroundStarsTextureRegion;

    public WormholeSceneManager(BaseGlFragment fragment, int starID) {
        mFragment = fragment;
        mWormholeStarKey = Integer.toString(starID);
    }

    public BaseGlFragment getFragment() {
        return mFragment;
    }

    public void onLoadResources() {
        mWormholeTextureAtlas = new BitmapTextureAtlas(mFragment.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mWormholeTextureAtlas.setTextureAtlasStateListener(new ITextureAtlasStateListener.DebugTextureAtlasStateListener<IBitmapTextureAtlasSource>());

        mWormholeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                mWormholeTextureAtlas, mFragment.getActivity(), "stars/wormhole_big.png", 0, 0, 2, 2);

        mBackgroundGasTextureAtlas = new BitmapTextureAtlas(mFragment.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mBackgroundGasTextureAtlas.setTextureAtlasStateListener(new ITextureAtlasStateListener.DebugTextureAtlasStateListener<IBitmapTextureAtlasSource>());

        mBackgroundGasTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                mBackgroundGasTextureAtlas, mFragment.getActivity(), "decoration/gas.png", 0, 0, 4, 4);
        mBackgroundStarsTextureAtlas = new BitmapTextureAtlas(mFragment.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mBackgroundStarsTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                mBackgroundStarsTextureAtlas, mFragment.getActivity(), "decoration/starfield.png", 0, 0, 4, 4);

        mFragment.getShaderProgramManager().loadShaderProgram(RadarIndicatorEntity.getShaderProgram());
        mFragment.getTextureManager().loadTexture(mWormholeTextureAtlas);
        mFragment.getTextureManager().loadTexture(mBackgroundGasTextureAtlas);
        mFragment.getTextureManager().loadTexture(mBackgroundStarsTextureAtlas);
    }

    public void onStart() {
        if (mWasStopped) {
            log.debug("We were stopped, refreshing the scene...");
            refreshScene();
        }
    }

    public void onStop() {
        mWasStopped = true;
    }

    protected void refreshScene() {
        new BackgroundRunner<Scene>() {
            @Override
            protected Scene doInBackground() {
                try {
                    return createScene();
                } catch(Exception e) {
                    return null;
                }
            }

            @Override
            protected void onComplete(final Scene scene) {
                if (scene == null) {
                    return;
                }

                final Engine engine = mFragment.getEngine();
                if (engine != null) {
                    engine.runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            engine.setScene(scene);
                        }
                    });
                }
            }
        }.execute();
    }


    public Scene createScene() {
        if (mFragment.getEngine() == null) {
            // if the engine hasn't been created yet, schedule a refresh for later...
            mFragment.runOnUpdateThread(new Runnable() {
                @Override
                public void run() {
                    refreshScene();
                }
            });
            return null;
        }

        mScene = new Scene();
        mScene.setBackground(new Background(0.0f, 0.0f, 0.0f));

        drawBackground(mScene);

        WormholeEntity entity = new WormholeEntity(this, mWormholeTextureRegion, mFragment.getVertexBufferObjectManager());
        mScene.attachChild(entity);

        return mScene;
    }

    private void drawBackground(Scene scene) {
        Random r = new Random(mWormholeStarKey.hashCode());
        final int STAR_SIZE = 256;
        for (int y = 0; y < Sector.SECTOR_SIZE / STAR_SIZE; y++) {
            for (int x = 0; x < Sector.SECTOR_SIZE / STAR_SIZE; x++) {
                Sprite bgSprite = new Sprite(
                        (float) x * STAR_SIZE, (float) y * STAR_SIZE, STAR_SIZE, STAR_SIZE,
                        mBackgroundStarsTextureRegion.getTextureRegion(r.nextInt(16)),
                        mFragment.getVertexBufferObjectManager());
                scene.attachChild(bgSprite);
            }
        }

        final int GAS_SIZE = 512;
        for (int i = 0; i < 10; i++) {
            float x = r.nextInt(Sector.SECTOR_SIZE + (GAS_SIZE / 4)) - (GAS_SIZE / 8);
            float y = r.nextInt(Sector.SECTOR_SIZE + (GAS_SIZE / 4)) - (GAS_SIZE / 8);

            Sprite bgSprite = new Sprite(
                    x - (GAS_SIZE / 2.0f), y - (GAS_SIZE / 2.0f), GAS_SIZE, GAS_SIZE,
                    mBackgroundGasTextureRegion.getTextureRegion(r.nextInt(14)),
                    mFragment.getVertexBufferObjectManager());
            scene.attachChild(bgSprite);
        }
    }

}
