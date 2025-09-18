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

        public boolean isChainValid() {
            List<Block> chain = getChain();
            for (int i = 1; i < chain.size(); i++) {
                Block current = chain.get(i);
                Block previous = chain.get(i-1);
                if (!current.getPreviousHash().equals(previous.getCurrentHash())) return false;
                if (!calculateHash(current).equals(current.getCurrentHash())) return false;
            }
            return true;
        }

        public Optional<Block> findByHash(String hash) {
            return blockRepo.findByCurrentHash(hash);
        }
    }



