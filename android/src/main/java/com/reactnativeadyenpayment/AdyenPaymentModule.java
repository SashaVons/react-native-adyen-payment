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
    public void encrypt(String holderName, String number, String cvc, String expiryMonth, String expiryYear,
                        String publicKey, Promise promise) {

        Card card = new Card.Builder()
                .setHolderName(holderName)
                .setCvc(cvc)
                .setExpiryMonth(expiryMonth)
                .setExpiryYear(expiryYear)
                .setGenerationTime(new Date())
                .setNumber(number)
                .build();

        try {
            String encryptedCard = card.serialize(publicKey);
            promise.resolve(encryptedCard);
        } catch (EncrypterException e) {
            promise.reject("RN_ADYEN_CSE_ERROR", e);
        }
    }
}
