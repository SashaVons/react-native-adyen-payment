package com.reactnativeadyenpayment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.adyen.checkout.base.ActionComponentData;
import com.adyen.checkout.base.model.payments.response.Action;
import com.adyen.checkout.core.api.Environment;
import com.adyen.checkout.redirect.RedirectComponent;
import com.adyen.checkout.redirect.RedirectConfiguration;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.adyen.checkout.cse.Card;
import com.adyen.checkout.cse.CardEncryptor;
import com.adyen.checkout.cse.EncryptedCard;
import com.adyen.checkout.cse.internal.CardEncryptorImpl;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import adyen.com.adyencse.encrypter.exception.EncrypterException;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.json.JSONObject;

import java.util.Locale;

@ReactModule(name = AdyenPaymentModule.NAME)
public class AdyenPaymentModule extends ReactContextBaseJavaModule {
  public static final String NAME = "AdyenPayment";
  public Context context;

  public AdyenPaymentModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.context = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

   @ReactMethod void openRedirect(String publicKey, JSONObject paymentResponse) {
     ActionComponentData action = ActionComponentData.SERIALIZER.deserialize(paymentResponse);
     RedirectConfiguration redirectConfiguration = new RedirectConfiguration.Builder(Locale.ENGLISH, Environment.EUROPE)
       .setClientKey(publicKey)
       .build();


     RedirectComponent redirectComponent = RedirectComponent.PROVIDER.get((FragmentActivity) this.context, redirectConfiguration);

     redirectComponent.handleAction(this.getCurrentActivity(), action);
   }

  @ReactMethod
  public void encrypt(String number, String cvc, String expiryMonth, String expiryYear,
                      String publicKey, Promise promise) {
    CardEncryptor encryptor = new CardEncryptorImpl();
    Card card = new Card.Builder()
      .setNumber(number)
      .setSecurityCode(cvc)
      .setExpiryDate(Integer.parseInt(expiryMonth), Integer.parseInt(expiryYear))
      .build();

    EncryptedCard encryptedCard = encryptor.encryptFields(card, publicKey);

    WritableNativeMap encryptedCardMap = new WritableNativeMap();
    encryptedCardMap.putString("encryptedCardNumber", encryptedCard.getEncryptedNumber());
    encryptedCardMap.putString("encryptedSecurityCode", encryptedCard.getEncryptedSecurityCode());
    encryptedCardMap.putString("encryptedExpiryMonth", encryptedCard.getEncryptedExpiryMonth());
    encryptedCardMap.putString("encryptedExpiryYear", encryptedCard.getEncryptedExpiryYear());
    promise.resolve(encryptedCardMap);
  }
}
