import { NativeModules } from 'react-native';
import type { Card } from './types';

const AdyenPayment = NativeModules.AdyenPayment;

export const encryptCard = (card: Card, publicToken: string) => {
  const { holderName, cardNumber, cvc, expiryMonth, expiryYear } = card;
  return AdyenPayment.encrypt(
    holderName,
    cardNumber,
    cvc,
    expiryMonth,
    expiryYear,
    publicToken
  );
};

export default {
  encryptCard,
};
