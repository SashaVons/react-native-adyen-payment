package com.reactnativeadyenpayment;

import android.app.Application;

import androidx.annotation.NonNull;

import com.adyen.checkout.redirect.RedirectComponent;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import adyen.com.adyencse.encrypter.exception.EncrypterException;

import adyen.com.adyencse.pojo.Card;
import java.util.Date;

import com.adyen.checkout.core.api.Environment;
import com.adyen.checkout.redirect.RedirectConfiguration;

@ReactModule(name = AdyenPaymentModule.NAME)
public class AdyenPaymentModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AdyenPayment";

    public AdyenPaymentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    // @ReactMethod void openRedirect(String publicKey) {
    //   RedirectConfiguration redirectConfiguration = RedirectConfiguration.Builder(context, publicKey)
    //         .setEnvironment(Environment.TEST)
    //         .build();
    //   Application application = new AdyenPaymentModule();
    //   RedirectComponent redirectComponent = RedirectComponent.PROVIDER.get(this.getCurrentActivity(), application, redirectConfiguration);
    // }

    @ReactMethod
    public void encryptCard(String cardNumber, String expiryMonth, String expiryYear, String securityCode, String publicKey, final Promise promise) {
        Card.Builder cardBuilder = new Card.Builder();
        int month = Integer.parseInt(expiryMonth);
        int year = Integer.parseInt(expiryYear);
        cardBuilder.setNumber(cardNumber).setExpiryDate(month, year);
        cardBuilder.setSecurityCode(securityCode);
        Card card = cardBuilder.build();
        final EncryptedCard encryptedCard = Encryptor.INSTANCE.encryptFields(card, publicKey);
        WritableMap resultMap = new WritableNativeMap();
        resultMap.putString("encryptedCardNumber", encryptedCard.getEncryptedNumber());
        resultMap.putString("encryptedExpiryMonth", encryptedCard.getEncryptedExpiryMonth());
        resultMap.putString("encryptedExpiryYear", encryptedCard.getEncryptedExpiryYear());
        resultMap.putString("encryptedSecurityCode", encryptedCard.getEncryptedSecurityCode());
        promise.resolve(resultMap);
    }
}
