package com.project.nulinknft.contract.event.listener.consumer;

import com.project.nulinknft.contract.event.listener.filter.events.ContractsEventEnum;
import com.project.nulinknft.contract.event.listener.filter.events.impl.ContractsEventBuilder;
import com.project.nulinknft.entity.BlindBox;
import com.project.nulinknft.service.BlindBoxService;
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
public class BlindBoxEventHandler {

    private static BlindBoxService blindBoxService;

    public BlindBoxEventHandler(BlindBoxService blindBoxService) {
        BlindBoxEventHandler.blindBoxService = blindBoxService;
    }

    public static void descBuyBox(Log evLog) {

        Event descEvent = new ContractsEventBuilder().build(ContractsEventEnum.BUY_BLIND_BOX);

        List<Type> args = FunctionReturnDecoder.decode(evLog.getData(), descEvent.getParameters());
        List<String> topics = evLog.getTopics();

        if (!CollectionUtils.isEmpty(args)) {
            BlindBox box = new BlindBox();
            box.setTxHash(evLog.getTransactionHash());
            box.setUser(args.get(0).getValue().toString());
            box.setBoxAmount(Integer.parseInt(args.get(1).getValue().toString()));
            box.setPayAmount(args.get(2).getValue().toString());
            box.setTime(args.get(3).getValue().toString());
            box.setRecommender(args.get(4).getValue().toString());
            blindBoxService.create(box);
        } else if (!CollectionUtils.isEmpty(topics)) {
            String from = EthLogsParser.hexToAddress(topics.get(1));
            String to = EthLogsParser.hexToAddress(topics.get(2));
            BigInteger tokenId = EthLogsParser.hexToBigInteger(topics.get(3));
            log.info("descBuyBlindBox from = {}\n to = {} \n tokenId = {}", from, to, tokenId);
        }
    }
}
