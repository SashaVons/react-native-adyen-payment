import { NativeModules } from 'react-native';
import type { Card } from './types';

const AdyenPayment = NativeModules.AdyenPayment;

export const encryptCard = (card: Card, publicToken: string) => {
  const { cardNumber, cvc, expiryMonth, expiryYear } = card;
  return AdyenPayment.encrypt(
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
