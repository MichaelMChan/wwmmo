package au.com.codeka.warworlds.game.starfield;

import java.io.IOException;

import org.andengine.entity.scene.Scene;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import au.com.codeka.warworlds.BaseGlActivity;
import au.com.codeka.warworlds.R;
import au.com.codeka.warworlds.model.EmpireShieldManager;

/** This is the base activity for activities that show a starfield. */
@SuppressLint("Registered") // it's a base class
public abstract class BaseStarfieldActivity extends BaseGlActivity {
    protected StarfieldSceneManager mStarfield;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStarfield = new StarfieldSceneManager(this);
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.starfield;
    }

    @Override
    protected void onCreateResources() throws IOException {
        mStarfield.onLoadResources();
    }

    @Override
    protected Scene onCreateScene() throws IOException {
        // return an empty scene, but queue up a refresh.
        mStarfield.queueRefreshScene();
        return new Scene();
    }

    @Override
    public void onStart() {
        super.onStart();
        mStarfield.onStart();
    }

    @Override
    public void onResume() {
        super.onResume(); 
        EmpireShieldManager.i.clearTextureCache();
    }

    @Override
    public void onStop() {
        super.onStop();
        mStarfield.onStop();
    }
}
