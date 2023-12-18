package com.project.nulinknft.contract.event.listener.consumer;

import com.project.nulinknft.contract.event.listener.filter.events.ContractsEventEnum;
import com.project.nulinknft.contract.event.listener.filter.events.impl.ContractsEventBuilder;
import com.project.nulinknft.entity.NFT;
import com.project.nulinknft.entity.NFTTransfer;
import com.project.nulinknft.service.NFTService;
import com.project.nulinknft.service.NFTTransferService;
import com.project.nulinknft.utils.EthLogsParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Component
public class NFTEventHandler {

    private static NFTService nftService;

    private static NFTTransferService nftTransferService;

    public NFTEventHandler(NFTService nftService, NFTTransferService nftTransferService) {
        NFTEventHandler.nftService = nftService;
        NFTEventHandler.nftTransferService = nftTransferService;
    }

    public static void descMint(Log evLog) {

        Event descEvent = new ContractsEventBuilder().build(ContractsEventEnum.MINT);

        List<Type> args = FunctionReturnDecoder.decode(evLog.getData(), descEvent.getParameters());
        List<String> topics = evLog.getTopics();

        if (!CollectionUtils.isEmpty(args)) {
            String transactionHash = evLog.getTransactionHash();
            NFT nft = new NFT();
            nft.setTxHash(transactionHash);
            nft.setOwner(args.get(0).getValue().toString());
            nft.setTokenId(Integer.parseInt(args.get(1).getValue().toString()));
            nft.setAirdropLevel(Integer.parseInt(args.get(2).getValue().toString()));
            nftService.create(nft);
        } else if (!CollectionUtils.isEmpty(topics)) {
            String from = EthLogsParser.hexToAddress(topics.get(1));
            String to = EthLogsParser.hexToAddress(topics.get(2));
            BigInteger tokenId = EthLogsParser.hexToBigInteger(topics.get(3));
            log.info("descBuyBlindBox from = {}\n to = {} \n tokenId = {}", from, to, tokenId);
        }
    }

    public static void descTransfer(Log evLog) {

        Event transferEvent = new ContractsEventBuilder().build(ContractsEventEnum.TRANSFER);

        String txHash = evLog.getTransactionHash();
        List<Type> args = FunctionReturnDecoder.decode(evLog.getData(), transferEvent.getParameters());
        List<String> topics = evLog.getTopics();

        String from = EthLogsParser.hexToAddress(topics.get(0));
        String to = EthLogsParser.hexToAddress(topics.get(1));
        BigInteger tokenId = EthLogsParser.hexToBigInteger(topics.get(2));

        NFTTransfer nftTransfer = new NFTTransfer();
        nftTransfer.setTxHash(txHash);
        nftTransfer.setFromAddress(from);
        nftTransfer.setToAddress(to);
        nftTransfer.setTokenId(tokenId.intValue());
        nftTransferService.create(nftTransfer);
    }
}
