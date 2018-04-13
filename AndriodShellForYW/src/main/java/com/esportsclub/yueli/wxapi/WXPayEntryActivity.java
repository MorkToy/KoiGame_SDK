package com.esportsclub.yueli.wxapi;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import koigame.sdk.util.AndroidUtils;

/**
 * Created by wudi .
 * on 2018/4/12.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private String TAG = "WXPayEntryActivity";

    private IWXAPI api;


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "wx resp : " + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.i("WXPayEntryActivity", "onResp COMMAND_PAY_BY_WX start");
            AndroidUtils.showToast(this, baseResp.errCode + "", 1);
        }
    }
}
