package koigame.sdk.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import koigame.sdk.api.KServiceException;
import koigame.sdk.api.KWebApiImpl;
import koigame.sdk.bean.user.KUserInfo;
import koigame.sdk.bean.user.KUserSession;
import koigame.sdk.util.JSONUtils;

public class KGameProxy {

    private static KGameProxy instance = new KGameProxy();

    private static Map<String, KGamePrice> gamePriceMap = new HashMap<String, KGamePrice>();

    private static String gamePriceStr = "";

    private KGameProxy() {

    }

    public static KGameProxy getInstance() {
        return instance;
    }

    /**
     * 加载配方包
     */
    public void loadPackages() {
        KUserInfo userInfo = KUserSession.instance().getUserInfo();

        try {
            JSONObject props = KWebApiImpl.instance().getGamePrices(userInfo.getAccessToken());
            gamePriceStr = JSONUtils.getString(props, "data");
            formGamePriceMap();
        } catch (KServiceException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, KGamePrice> getGamePriceMap() {
        return gamePriceMap;
    }

    public static String getGamePriceStr() {
        return gamePriceStr;
    }

    /**
     * 建立配方映射map
     *
     * @throws JSONException
     */
    private void formGamePriceMap() {
        try {
            JSONArray jsonArray = new JSONArray(gamePriceStr);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                Object value = jsonArray.get(i);
                if (value == null) {
                    continue;
                } else {
                    JSONObject jso = new JSONObject(value.toString());
                    String productId = JSONUtils.getString(jso, "goodsCode");

                    KGamePrice gamePrice = new KGamePrice();
                    gamePrice.setGoodsCode(productId);
                    gamePrice.setGoodsName(JSONUtils.getString(jso, "goodsName"));
                    gamePrice.setRelatedGoodsId(JSONUtils.getString(jso, "relatedGoodsId"));
                    gamePrice.setShopType(JSONUtils.getInt(jso, "shopType"));
                    gamePrice.setGoodsAmount(JSONUtils.getInt(jso, "goodsAmount"));
                    gamePrice.setDiscount(JSONUtils.getDouble(jso, "discount"));
                    gamePrice.setGoodsCount(JSONUtils.getInt(jso, "goodsCount"));

                    gamePrice.setGoodsItem(JSONUtils.getString(jso, "itemsPackage"));
                    gamePrice.setGoodsDesc(JSONUtils.getString(jso, "goodsDesc"));
                    gamePrice.setGoodsMarkTime(JSONUtils.getString(jso, "goodsMarkTime"));
                    gamePriceMap.put(productId, gamePrice);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
