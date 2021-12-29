import { NativeModules } from 'react-native';
import type { Card } from './types';

const AdyenPayment = NativeModules.AdyenPayment;

export const encryptCard = (card: Card, publicToken: string) => {
  const { number, cvc, expiryMonth, expiryYear } = card;
  return AdyenPayment.encrypt(
    number,
    cvc,
    expiryMonth,
    expiryYear,
    publicToken
  );
};

export default {
  encryptCard,
};
