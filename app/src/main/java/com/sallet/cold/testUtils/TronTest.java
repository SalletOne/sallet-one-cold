package com.sallet.cold.testUtils;

import com.hk.common.dto.TronTransDTO;
import com.hk.offline.currency.Tron;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class TronTest {
    public static void main(String[] args) {
        String s = "butter bracket hold road damp disease cube hope cream chair dune exhibit";
        List<String> list = Arrays.asList(s.split(" "));

        Tron tron = Tron.getInstance();

        System.out.println(tron.address(list));


        TronTransDTO tronTxDTO = new TronTransDTO();
        tronTxDTO.setSender("TCy6sMbfjyEWLiAcgpPMW7qaSVcXLZFHMj1");
        tronTxDTO.setReceiver("TTfHfErVpq7xGf5hfyvWeE6qxEpCTFs7tU");
        tronTxDTO.setAmount(BigInteger.valueOf(500_000L));

        tronTxDTO.setContractAddress("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t");

        tronTxDTO.setHash("000000000250963dcebc6f8d22e01b418e43cb17d22b827c8dd75905ce3b7f6e");
        tronTxDTO.setNumber(38835773L);
        tronTxDTO.setTimestamp(1647057318000L);

        String hex = tron.tronTxn(list, tronTxDTO);
        System.out.println(hex);
    }
}
