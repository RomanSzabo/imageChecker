package at.ac.univie.imagechecker.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class Images extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b5060018060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555061073e806100776000396000f3fe608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063177d2a741461006757806380599e4b146100b8578063e13f96e214610180578063e802bf7d14610252575b600080fd5b34801561007357600080fd5b506100b66004803603602081101561008a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102ec565b005b3480156100c457600080fd5b5061017e600480360360208110156100db57600080fd5b81019080803590602001906401000000008111156100f857600080fd5b82018360208201111561010a57600080fd5b8035906020019184600183028401116401000000008311171561012c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061042d565b005b34801561018c57600080fd5b50610250600480360360408110156101a357600080fd5b81019080803590602001906401000000008111156101c057600080fd5b8201836020820111156101d257600080fd5b803590602001918460018302840111640100000000831117156101f457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190505050610586565b005b34801561025e57600080fd5b506102d66004803603602081101561027557600080fd5b810190808035906020019064010000000081111561029257600080fd5b8201836020820111156102a457600080fd5b803590602001918460018302840111640100000000831117156102c657600080fd5b90919293919293905050506106e1565b6040518082815260200191505060405180910390f35b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff1615156103d3576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f4f6e6c7920617574686f72697a65642075736572732063616e20696e7465726181526020017f63742e000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60018060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610514576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f4f6e6c7920617574686f72697a65642075736572732063616e20696e7465726181526020017f63742e000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000816040518082805190602001908083835b60208310151561054c5780518252602082019150602081019050602083039250610527565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390206000905550565b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16151561066d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f4f6e6c7920617574686f72697a65642075736572732063616e20696e7465726181526020017f63742e000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000836040518082805190602001908083835b6020831015156106a65780518252602082019150602081019050602083039250610681565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055505050565b600080838360405180838380828437808301925050509250505090815260200160405180910390205490509291505056fea165627a7a72305820452ef003ab86d79968f358c0c1f8cfc5ebb3bb94fe9599f4d3a9774b5ae01e100029";

    public static final String FUNC_NEWIMAGE = "newImage";

    public static final String FUNC_ADDAUTHORIZEDUSER = "addAuthorizedUser";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_GETIMAGE = "getImage";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("", "<address>");
    }

    @Deprecated
    protected Images(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Images(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Images(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Images(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> newImage(String imageId, byte[] imageFingerprint) {
        final Function function = new Function(
                FUNC_NEWIMAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(imageId), 
                new org.web3j.abi.datatypes.generated.Bytes32(imageFingerprint)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addAuthorizedUser(String _address) {
        final Function function = new Function(
                FUNC_ADDAUTHORIZEDUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_address)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> remove(String id) {
        final Function function = new Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(id)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<byte[]> getImage(String id) {
        final Function function = new Function(FUNC_GETIMAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    @Deprecated
    public static Images load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Images(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Images load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Images(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Images load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Images(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Images load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Images(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Images> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Images.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Images> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Images.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Images> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Images.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Images> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Images.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
