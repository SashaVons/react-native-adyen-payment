package com.reactnativeadyenpayment;

import androidx.annotation.NonNull;
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
