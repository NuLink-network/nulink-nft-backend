package com.project.nulinknft.contract.event.listener.filter.events.impl;

import com.project.nulinknft.contract.event.listener.filter.events.ContractsEventEnum;
import com.project.nulinknft.contract.event.listener.filter.events.EventBuilder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import java.util.Arrays;

public class ContractsEventBuilder implements EventBuilder<ContractsEventEnum> {

    @Override
    public Event build(ContractsEventEnum type) {
        switch (type) {
            case BUY_BLIND_BOX:
                return getBuyBlindBoxEvent();
            case MINT:
                return getMintEvent();
            case TRANSFER:
                return getTransferEvent();
            default:
                return null;
        }
    }

    public static Event getBuyBlindBoxEvent() {
        return new Event("BuyBlindBox",
                Arrays.asList(
                        // _user
                        new TypeReference<Address>(false) {
                        },

                        // _boxAmount
                        new TypeReference<Uint256>(false) {
                        },

                        // _payAmount
                        new TypeReference<Uint256>(false) {
                        },

                        // _time
                        new TypeReference<Uint256>(false) {
                        },

                        // _recommender
                        new TypeReference<Address>(false) {
                        }
                ));
    }

    public static Event getMintEvent() {
        return new Event("Mint",
                Arrays.asList(
                        // _owner
                        new TypeReference<Address>(false) {
                        },

                        // _tokenID
                        new TypeReference<Uint256>(false) {
                        },

                        // _airdropLevel
                        new TypeReference<Uint8>(false) {
                        }
                ));
    }

    public static Event getTransferEvent() {
        return new Event("Transfer",
                Arrays.asList(
                        // from
                        new TypeReference<Address>(true) {
                        },

                        // to
                        new TypeReference<Address>(true) {
                        },

                        // tokenId
                        new TypeReference<Uint256>(true) {
                        }
                ));
    }

}
