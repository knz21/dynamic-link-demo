package jp.wellnote.dynamiclinkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDeepLink();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(AppInvite.API).build();

        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, false)
                .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(@NonNull AppInviteInvitationResult result) {
                        Log.v(TAG, "onResult");
                        if (result.getStatus().isSuccess()) {
                            Log.v(TAG, "result is success");
                            Intent intent = result.getInvitationIntent();
                            String deepLink = AppInviteReferral.getDeepLink(intent);
                            ((TextView) findViewById(R.id.text_deep_link)).setText(deepLink);
                        }
                    }
                });
    }

    private void setDeepLink() {
        if (getIntent() != null) {
            String deepLink = getIntent().getStringExtra("deep_link");
            if (deepLink != null) {
                Log.v(TAG, "setDeepLink from intent");
                ((TextView) findViewById(R.id.text_deep_link)).setText(deepLink);
            }
        }
    }
}
