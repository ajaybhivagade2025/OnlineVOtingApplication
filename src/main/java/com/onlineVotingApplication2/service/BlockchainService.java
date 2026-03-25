package com.onlineVotingApplication2.service;

import com.onlineVotingApplication2.entity.Block;
import com.onlineVotingApplication2.repository.BlockRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlockchainService {


        @Autowired
        private BlockRepository blockRepo;


    public boolean verifyChain() {
        List<Block> blocks = blockRepo.findAll();
        for (int i = 1; i < blocks.size(); i++) {
            Block current = blocks.get(i);
            Block previous = blocks.get(i - 1);

            if (!current.getCurrentHash().equals(current.calculateHash())) return false;
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) return false;
        }
        return true;
    }
    public boolean isElectionChainValid(Long electionId) {
        List<Block> chain = blockRepo.findByElectionIdOrderByIdAsc(electionId);
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getCurrentHash().equals(current.calculateHash())) return false;
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) return false;
        }
        return true;
    }



    public List<Block> getAllBlocks() {
        return blockRepo.findAll();
    }




       public Block addBlock(String data) {
            String prevHash = blockRepo.findTopByOrderByIdDesc()
                    .map(Block::getCurrentHash).orElse("0");

            Block b1 = Block.builder()
                    .previousHash(prevHash)
                    .data(data)
                    .timestamp(LocalDateTime.now())
                    .build();

            String hash = calculateHash(b1);
            b1.setCurrentHash(hash);
//            return blockRepo.save(b);
            return  blockRepo.save(b1);


        }

        public String calculateHash(Block block) {
            String input = (block.getPreviousHash() == null ? "" : block.getPreviousHash())
                    + block.getData()
                    + block.getTimestamp().toString();
            return DigestUtils.sha256Hex(input);
        }

        public List<Block> getChain() {
            return blockRepo.findAll(Sort.by("id"));
        }

        public Optional<Block> findByHash(String hash) {
            return blockRepo.findByCurrentHash(hash);
        }


    public void addVoteBlock(Long electionId, Long candidateId, Long voterId, String blockData) {
        // Last block nikal ke uska hash previousHash me set karenge
//        Block latestBlock = blockRepo.findTopByOrderByIdDesc();

        Optional<Block> latestBlockOpt = blockRepo.findTopByOrderByIdDesc();

        // Genesis case: agar koi block nahi mila to "0" set kare
        String previousHash = latestBlockOpt
                .map(Block::getCurrentHash)
                .orElse("0");// custom query
//        String previousHash = (latestBlock != null) ? latestBlock.getCurrentHash() : "0"; // Genesis block case

        Block newBlock = Block.builder()
                .previousHash(previousHash)
                .data(blockData)  // e.g. JSON string: { electionId, candidateId, voterId }
                .timestamp(LocalDateTime.now())
                .build();

        // Hash generate
        newBlock.generateHash();

        newBlock.setElectionId(electionId);
        // Save block in DB
        blockRepo.save(newBlock);
    }






}



