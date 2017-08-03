package eu.sigrlami.sim;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RNSimModule extends ReactContextBaseJavaModule {

    ReactApplicationContext reactContext;

    public RNSimModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNSimInfo";
    }

    @Override
    public
    @Nullable
    Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        TelephonyManager telManager = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            SubscriptionManager manager = (SubscriptionManager) this.reactContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                int activeSubscriptionInfoCount = manager.getActiveSubscriptionInfoCount();
                int activeSubscriptionInfoCountMax = manager.getActiveSubscriptionInfoCountMax();

                List<SubscriptionInfo> subscriptionInfos = manager.getActiveSubscriptionInfoList();

                int sub = 0;
                for (SubscriptionInfo subInfo : subscriptionInfos) {
                    CharSequence carrierName = subInfo.getCarrierName();
                    String countryIso = subInfo.getCountryIso();
                    int dataRoaming = subInfo.getDataRoaming();  // 1 is enabled ; 0 is disabled
                    CharSequence displayName = subInfo.getDisplayName();
                    String iccId = subInfo.getIccId();
                    int mcc = subInfo.getMcc();
                    int mnc = subInfo.getMnc();
                    String number = subInfo.getNumber();
                    int simSlotIndex = subInfo.getSimSlotIndex();
                    int subscriptionId = subInfo.getSubscriptionId();
                    boolean networkRoaming = telManager.isNetworkRoaming();
                    String deviceId = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        deviceId = telManager.getDeviceId(simSlotIndex);
                    }

                    constants.put("carrierName" + sub, carrierName.toString());
                    constants.put("displayName" + sub, displayName.toString());
                    constants.put("countryCode" + sub, countryIso);
                    constants.put("mcc" + sub, mcc);
                    constants.put("mnc" + sub, mnc);
                    constants.put("isNetworkRoaming" + sub, networkRoaming);
                    constants.put("isDataRoaming" + sub, (dataRoaming == 1));
                    constants.put("simSlotIndex" + sub, simSlotIndex);
                    constants.put("phoneNumber" + sub, number);
                    constants.put("deviceId" + sub, deviceId);
                    constants.put("simSerialNumber" + sub, iccId);
                    constants.put("subscriptionId" + sub, subscriptionId);
                    sub++;
                }
            }
            else{
                constants.put("phoneNumber0", telManager.getLine1Number());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return constants;
    }
}