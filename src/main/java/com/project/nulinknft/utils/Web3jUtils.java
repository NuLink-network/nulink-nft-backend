package com.project.nulinknft.utils;

import com.project.nulinknft.config.ContractsConfig;
import com.project.nulinknft.config.ProfileConfig;
import com.project.nulinknft.contract.event.listener.filter.Monitor;
import com.project.nulinknft.exception.Web3jException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class Web3jUtils {

    public static Logger logger = LoggerFactory.getLogger(Web3jUtils.class);

    /**
     * inject by web3j-spring-boot-starter
     */
    @Resource
    private Web3j web3j;

    /**
     * inject by web3j-spring-boot-starter
     */
    @Resource
    private Admin admin;

    @Autowired
    private ProfileConfig profileConfig;


    @Autowired
    ContractsConfig contractsConfig;


    /*public enum KeystoreTypeEnum {

        Game,
        AirDrop,
        Reward,
    }

    private static Map<KeystoreTypeEnum, Credentials> keyStoreTypeMap = null;*/


   /* @PostConstruct
    public void init() throws IOException {
        keyStoreTypeMap = new HashMap<>();

        String gameKeystoreContent = getKeystoreContent(basePath + "/" + gameFileName); // "keystore/keystore_test"
        Credentials credentialsGame = Credentials.create(getPrivateKey(gameKeystoreContent, password));  //发奖励，Game 用的keystore
        keyStoreTypeMap.put(KeystoreTypeEnum.Game, credentialsGame);

        String rewardKeystoreContent = getKeystoreContent(basePath + "/" + rewardFileName); // "keystore/keystore_test"
        Credentials credentialsReward = Credentials.create(getPrivateKey(rewardKeystoreContent, password));  //发奖励，Game 用的keystore TODO: 分开这两个keystore
        keyStoreTypeMap.put(KeystoreTypeEnum.Reward, credentialsReward);

        String airDropKeystoreContent = getKeystoreContent(basePath + "/" + airDropFileName); // "keystore/keystore_airdrop_test";
        Credentials credentialsAirDrop = Credentials.create(getPrivateKey(airDropKeystoreContent, password)); //空投用的keystore
        keyStoreTypeMap.put(KeystoreTypeEnum.AirDrop, credentialsAirDrop);

    }*/

    public Web3j getWeb3j() {
        return web3j;
    }

    private String getKeystoreContent(String keystorePath) throws IOException {

        String activeProfile = profileConfig.getActiveProfile();
        logger.info("Active Profile：" + activeProfile);

        ClassPathResource classPathResource = new ClassPathResource(keystorePath);
        String data = "";

        byte[] bdata = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        data = new String(bdata, StandardCharsets.UTF_8);

        return data;
    }

    public BigInteger getBlockNumber(Integer delayBlocks) {

        BigInteger blockNumber = new BigInteger("0");
        try {
            blockNumber = web3j.ethBlockNumber().send().getBlockNumber();

            if (blockNumber.compareTo(BigInteger.valueOf(delayBlocks)) > 0) {
                blockNumber = blockNumber.subtract(BigInteger.valueOf(delayBlocks));
            }

            logger.info(" getBlockNumber the current block number is {}", blockNumber);

        } catch (IOException e) {
            logger.error("get block number failed, IOException: ", e);
        }
        return blockNumber;
    }

    public EthLog filterEthLog(BigInteger start, BigInteger end, Event event, String contractAddress) throws IOException {
        DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(start);
        DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(end);
        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, contractAddress);
        String topic = EventEncoder.encode(event);
        logger.info(" ==========> filterEthLog topic {}", topic);
        filter.addSingleTopic(topic);
        return web3j.ethGetLogs(filter).send();
    }

    /**
     * Retrieve Ethereum event logs
     *
     * @param addresses: can be null
     * @return EthLog
     */
    public EthLog getEthLogs(BigInteger start, BigInteger end, List<Event> events, List<String> addresses/*can be null */) throws IOException {
        org.web3j.protocol.core.methods.request.EthFilter filter = Monitor.getFilter(start, end, events, addresses/* null */);
        EthLog ethlog = web3j.ethGetLogs(filter).send();
        return ethlog;
    }

    /**
     * Get balance
     *
     * @param address Wallet address
     * @return  balance
     */
    private BigInteger getBalance(String address) {
        BigInteger balance = null;
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("address " + address + " balance " + balance + "wei");
        return balance;
    }

    /**
     * get Gas Price
     */
    private BigInteger getGasPrice() {
        BigInteger gasPrice = null;
        while (true) {
            try {
                EthGasPrice send = web3j.ethGasPrice().send();
                gasPrice = send.getGasPrice();
                return gasPrice;
            } catch (IOException e) {
                logger.error("can't get gasPrice from private chain exception log: ", e);
            }
        }
    }


    /**
     * Generate a regular transaction object
     *
     * @param fromAddress Sender address
     * @param toAddress Recipient address
     * @param nonce Transaction nonce
     * @param gasPrice Gas price
     * @param gasLimit Gas limit
     * @param value Amount
     * @return Transaction object
     */
    private org.web3j.protocol.core.methods.request.Transaction makeTransaction(String fromAddress, String toAddress, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) {
        org.web3j.protocol.core.methods.request.Transaction transaction;
        transaction = org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
        return transaction;
    }

    /**
     * Get gas limit for a regular transaction
     *
     * @param transaction Transaction object
     * @return gas limit
     */
    private BigInteger getTransactionGasLimit(org.web3j.protocol.core.methods.request.Transaction transaction) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            // e.printStackTrace();
            logger.error("getTransactionGasLimit IOException log: {}", e);
        }
        return gasLimit;
    }

    /**
     * Get account transaction nonce
     *
     * @param address wallet address
     * @return nonce
     */
    private BigInteger getTransactionNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            logger.error("getTransactionNonce IOException log: {}", e);
        }
        return nonce;
    }

    /**
     * Send transaction
     *
     * @return tx Hash
     */
    public String sendTransaction() throws IOException, TransactionException {
        String password = "yzw";
        String fromAddress = "0x51eEAA8FA3d2e83268c1E98eaf0dC52694619235";
        String toAddress = "0x51eEAA8FA3d2e83268c1E98eaf0dC52694619235";
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        BigDecimal amount = new BigDecimal("0.01");
        String txHash = null;
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, password, unlockDuration).send();
            if (personalUnlockAccount.accountUnlocked()) {
                BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
                org.web3j.protocol.core.methods.request.Transaction transaction = makeTransaction(fromAddress, toAddress, null, null, null, value);
                BigInteger gasLimit = getTransactionGasLimit(transaction);
                BigInteger nonce = getTransactionNonce(fromAddress);

                if (nonce.compareTo(BigInteger.ZERO) == 0) {
                    logger.error("getTransactionNonce is 0");
                    return null;
                }
                BigInteger gasPrice = Convert.toWei(getGasPrice().toString(), Convert.Unit.GWEI).toBigInteger();
                transaction = makeTransaction(fromAddress, toAddress, nonce, gasPrice, gasLimit, value);
                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
                txHash = ethSendTransaction.getTransactionHash();
            }
        } catch (IOException e) {
            logger.info(" ==========> web3j sendTransaction failed: {}", e);
            return null;
        }
        logger.info("sendTransaction tx hash {}", txHash);
        return txHash;
    }

   /* public String sendTransaction(Function function, String contractAddress, KeystoreTypeEnum keystoreType) throws IOException, ExecutionException, InterruptedException, Web3jException {
        *//*
         *   e.g.
         *   List<Type> inputParameters = new ArrayList<>();
         *   inputParameters.add(type);
         *   inputParameters.add(periods);
         *   inputParameters.add(new DynamicArray(winners));
         *   inputParameters.add(new DynamicArray(amounts));

         *   List<TypeReference<?>> outputParameters = new ArrayList<>();
         *   Function function = new Function("winReward", inputParameters, outputParameters);
         *
         *//*
        synchronized (Web3jUtils.class) {
            String encodedFunction = FunctionEncoder.encode(function);

            Credentials credentials = keyStoreTypeMap.get(keystoreType);

            if (null == credentials) {
                throw new RuntimeException("sendTransaction can't find keystore credentials");
            }

            String fromAddress = credentials.getAddress();
            try {
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                BigInteger ethGasPrice = getGasPrice();
//                BigInteger gasPrice = ethGasPrice.multiply(new BigInteger("20")).divide(new BigInteger("10"));
                BigInteger gasPrice = ethGasPrice;

                RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, DefaultGasProvider.GAS_LIMIT, contractAddress, encodedFunction);

                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 179, credentials);
                String hexValue = Numeric.toHexString(signedMessage);

                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
                logger.info("sendTransaction winReward txHash: {}", ethSendTransaction.getTransactionHash());
                if (ethSendTransaction.hasError()) {
                    logger.error("sendTransaction winReward Error:" + ethSendTransaction.getError().getMessage());
                    throw new Web3jException(ethSendTransaction.getError().getMessage());
                }
                return ethSendTransaction.getTransactionHash();
            } catch (Exception e) {
                logger.error("sendTransaction winReward Exception:" + e);
                throw e;
            }
        }

    }*/

    /*
     * Waiting for transaction receipt
     */
    public TransactionReceipt waitForTransactionReceipt(String txHash) {
        // Wait for transaction to be mined
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(web3j, TransactionManager.DEFAULT_POLLING_FREQUENCY, TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
        TransactionReceipt txReceipt = null;

        int m = 0, retryTimes = 20;
        while (m < retryTimes) {
            try {
                txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
                break;
            } catch (SocketTimeoutException e) {
                logger.error("waitForTransactionReceipt SocketException:", e);
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e1) {
                }
                m++;
                if (m < retryTimes) {
                    logger.info("waitForTransactionReceipt SocketException retrying ....");
                }
            } catch (IOException e) {
                // throw new RuntimeException(e);
                logger.error("waitForTransactionReceipt IOException:" + e);
                break;
            } catch (TransactionException e) {
                // throw new RuntimeException(e);
                logger.error("waitForTransactionReceipt TransactionException:" + e);
                break;
            }
        }
        return txReceipt;
    }

    /*public List<Type> callContractFunction(Function function, String contractAddress, KeystoreTypeEnum keystoreType) throws ExecutionException, InterruptedException {
        *//*
         *   e.g.
         *   List<Type> inputParameters = new ArrayList<>();
         *   inputParameters.add(type);
         *   inputParameters.add(periods);
         *   inputParameters.add(new DynamicArray(winners));
         *   inputParameters.add(new DynamicArray(amounts));

         *   List<TypeReference<?>> outputParameters = new ArrayList<>();
         *   Function function = new Function("winReward", inputParameters, outputParameters);
         *
         *//*

        Credentials credentials = keyStoreTypeMap.get(keystoreType);

        if (null == credentials) {
            throw new RuntimeException("sendTransaction can't find keystore credentials");
        }

        String fromAddress = credentials.getAddress();

        String encodedFunction = FunctionEncoder.encode(function);
        *//*org.web3j.protocol.core.methods.response.EthCall*//*
        EthCall response = web3j.ethCall(Transaction.createEthCallTransaction(fromAddress, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();

        return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
    }*/


    /**
     * get PrivateKey
     *
     * @param keystoreContent account keystore
     * @param password
     * @return privateKey
     */
    private String getPrivateKey(String keystoreContent, String password) {
        try {
            Credentials credentials = WalletUtils.loadJsonCredentials(password, keystoreContent);
            BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
            return privateKey.toString(16);
        } catch (IOException | CipherException e) {
            logger.error("getPrivateKey Exception:" + e);
        }
        return null;
    }

    /**
     * generate Keystore
     *
     * @param privateKey privateKey
     * @param password   password
     * @param directory  directory
     */
    private static void generateKeystore(byte[] privateKey, String password, String directory) {
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
        try {
            String keystoreName = WalletUtils.generateWalletFile(password, ecKeyPair, new File(directory), true);
            System.out.println("keystore name " + keystoreName);
        } catch (CipherException | IOException e) {
            logger.error("generateKeystore Exception:" + e);
        }
    }

    public static boolean isSignatureValid(final String address, final String signature, final String message) {

        final String personalMessagePrefix = "\u0019Ethereum Signed Message:\n";
        boolean match = false;

        final String prefix = personalMessagePrefix + message.length();
        final byte[] msgHash = Hash.sha3((prefix + message).getBytes());
        final byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        final Sign.SignatureData sd = new Sign.SignatureData(v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            final BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(
                    new BigInteger(1, sd.getR()),
                    new BigInteger(1, sd.getS())), msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);
                logger.info("recovery public address {} {} ", i + 1, addressRecovered);
                if (addressRecovered.equalsIgnoreCase(address)) {
                    match = true;
                    break;
                }
            }
        }

        return match;
    }

    public Timestamp getEventHappenedTimeStamp(String transactionHash) {

        while (true) {
            try {
                return new java.sql.Timestamp(web3j.ethGetBlockByHash(web3j.ethGetTransactionReceipt(transactionHash).send().getResult().getBlockHash(), true).send().getResult().getTimestamp().longValueExact() * 1000);
            } catch (IOException e) {
                logger.error("getEventHappenedTimeStamp IOException: {} retrying ...", e, e);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
