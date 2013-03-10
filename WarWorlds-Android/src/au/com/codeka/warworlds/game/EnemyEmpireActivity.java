package au.com.codeka.warworlds.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.codeka.warworlds.BaseActivity;
import au.com.codeka.warworlds.R;
import au.com.codeka.warworlds.ServerGreeter;
import au.com.codeka.warworlds.WarWorldsActivity;
import au.com.codeka.warworlds.ServerGreeter.ServerGreeting;
import au.com.codeka.warworlds.model.Empire;
import au.com.codeka.warworlds.model.EmpireManager;

public class EnemyEmpireActivity extends BaseActivity
                                 implements EmpireManager.EmpireFetchedHandler {
    private Context mContext = this;
    private Empire mEmpire;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.enemy_empire);
    }

    @Override
    public void onResume() {
        super.onResume();

        ServerGreeter.waitForHello(this, new ServerGreeter.HelloCompleteHandler() {
            @Override
            public void onHelloComplete(boolean success, ServerGreeting greeting) {
                if (!success) {
                    startActivity(new Intent(EnemyEmpireActivity.this, WarWorldsActivity.class));
                } else {
                    String empireKey = getIntent().getExtras().getString("au.com.codeka.warworlds.EmpireKey");
                    EmpireManager.getInstance().fetchEmpire(mContext, empireKey, EnemyEmpireActivity.this);
                }
            }
        });
    }

    @Override
    public void onEmpireFetched(Empire empire) {
        mEmpire = empire;

        TextView empireName = (TextView) findViewById(R.id.empire_name);
        ImageView empireIcon = (ImageView) findViewById(R.id.empire_icon);

        empireName.setText(mEmpire.getDisplayName());
        empireIcon.setImageBitmap(mEmpire.getShield(mContext));
    }
}
