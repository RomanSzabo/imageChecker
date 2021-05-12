package at.ac.univie.imagechecker.util;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

/**
 * Implementing ContractGasProvider to provide custom gas price and max gas values instead of default ones.
 * In azure, 0 cost of gas and max gas set to 50000000
 */
public class PoAGasProvider implements ContractGasProvider {

    @Override
    public BigInteger getGasPrice(String s) {
        return BigInteger.valueOf(0);
    }

    @Override
    public BigInteger getGasPrice() {
        return BigInteger.valueOf(0);
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return BigInteger.valueOf(50000000);
    }

    @Override
    public BigInteger getGasLimit() {
        return BigInteger.valueOf(50000000);
    }
}
