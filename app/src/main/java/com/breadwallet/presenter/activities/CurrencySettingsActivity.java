package com.breadwallet.presenter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.breadwallet.R;
import com.breadwallet.presenter.activities.util.BRActivity;
import com.breadwallet.presenter.customviews.BRDialogView;
import com.breadwallet.presenter.customviews.BRText;
import com.breadwallet.tools.animation.BRAnimator;
import com.breadwallet.tools.animation.BRDialog;
import com.breadwallet.tools.manager.BRSharedPrefs;
import com.breadwallet.tools.threads.executor.BRExecutor;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;

/**
 * Created by byfieldj on 2/5/18.
 */

public class CurrencySettingsActivity extends BRActivity {

    private BRText mTitle;
    private ImageButton mBackButton;
    private RelativeLayout mRescanBlockchainRow;
    private RelativeLayout mRedeemPrivateKeyRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_settings);

        mTitle = findViewById(R.id.title);
        mBackButton = findViewById(R.id.back_button);
        mRescanBlockchainRow = findViewById(R.id.rescan_row);
        mRedeemPrivateKeyRow = findViewById(R.id.redeem_private_key_row);

        mRedeemPrivateKeyRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRAnimator.openScanner(CurrencySettingsActivity.this, BRConstants.SCANNER_REQUEST);
            }
        });

        final Activity app = this;
        final BaseWalletManager wm = WalletsMaster.getInstance(app).getCurrentWallet(app);

        mRescanBlockchainRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BRAnimator.isClickAllowed()) return;
                Log.d("CurrencySettings", "Rescan tapped!");
                BRDialog.showCustomDialog(CurrencySettingsActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        BRSharedPrefs.putStartHeight(CurrencySettingsActivity.this, wm.getIso(app), 0);
                                        BRSharedPrefs.putAllowSpend(CurrencySettingsActivity.this, wm.getIso(app), false);
                                        WalletsMaster.getInstance(CurrencySettingsActivity.this).getWalletByIso(CurrencySettingsActivity.this, wm.getIso(app)).getPeerManager().rescan();
                                        BRAnimator.startBreadActivity(CurrencySettingsActivity.this, false);

                                    }
                                });
                            }
                        }, new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                            }
                        }, null, 0);
            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (!BRAnimator.isClickAllowed()) return;
                Log.d("CurrencySettings", "Rescan tapped!");
                BRDialog.showCustomDialog(CurrencySettingsActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        BRSharedPrefs.putStartHeight(CurrencySettingsActivity.this, BRSharedPrefs.getCurrentWalletIso(CurrencySettingsActivity.this), 0);
                                        BRSharedPrefs.putAllowSpend(CurrencySettingsActivity.this, BRSharedPrefs.getCurrentWalletIso(CurrencySettingsActivity.this), false);
                                        WalletsMaster.getInstance(CurrencySettingsActivity.this).getCurrentWallet(CurrencySettingsActivity.this).getPeerManager().rescan();
                                        BRAnimator.startBreadActivity(CurrencySettingsActivity.this, false);

                                    }
                                });
                            }
                        }, new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                            }
                        }, null, 0);
            }
        });


        mBackButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mTitle.setText(String.format("%s Settings", wm.getName(this)));
    }
}