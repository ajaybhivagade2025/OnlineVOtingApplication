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


   /* public boolean verifyChain() {
        List<Block> blocks = blockRepo.findAll();
        for (int i = 1; i < blocks.size(); i++) {
            Block current = blocks.get(i);
            Block previous = blocks.get(i - 1);

            if (!current.getCurrentHash().equals(current.calculateHash())) return false;
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) return false;
        }
        return true;
    }*/


  /*  public boolean verifyChain() {
        List<Block> blocks = blockRepo.findAll(Sort.by("id")); // ✅ FIX: sorted

        if (blocks.isEmpty()) return true;

        // ✅ FIX: Genesis block check
        if (!blocks.get(0).getPreviousHash().equals("0")) return false;

        for (int i = 1; i < blocks.size(); i++) {
            Block current = blocks.get(i);
            Block previous = blocks.get(i - 1);

            // ✅ FIX: hash validation
            if (!current.getCurrentHash().equals(calculateHash(current))) {
                return false;
            }

            // ✅ FIX: chain link validation
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) {
                return false;
            }
        }

        return true;
    }
*/

  public boolean verifyChain() {
      List<Block> blocks = blockRepo.findAll(Sort.by("id"));

      if (blocks.isEmpty()) return true;

      // Genesis block check
      if (!blocks.get(0).getPreviousHash().equals("0")) return false;

      for (int i = 1; i < blocks.size(); i++) {
          Block current = blocks.get(i);
          Block previous = blocks.get(i - 1);

          // Hash validation
          if (!current.getCurrentHash().equals(calculateHash(current))) {
              return false;
          }

          // Chain validation
          if (!current.getPreviousHash().equals(previous.getCurrentHash())) {
              return false;
          }
      }

      return true;
  }

 /*   public boolean isElectionChainValid(Long electionId) {
        List<Block> chain = blockRepo.findByElectionIdOrderByIdAsc(electionId);
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getCurrentHash().equals(current.calculateHash())) return false;
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) return false;
        }
        return true;
    }
*/
 public boolean isElectionChainValid(Long electionId) {
     List<Block> chain = blockRepo.findByElectionIdOrderByIdAsc(electionId);

     if (chain.isEmpty()) return true;

     if (!chain.get(0).getPreviousHash().equals("0")) return false;

     for (int i = 1; i < chain.size(); i++) {
         Block current = chain.get(i);
         Block previous = chain.get(i - 1);

         if (!current.getCurrentHash().equals(calculateHash(current))) {
             return false;
         }

         if (!current.getPreviousHash().equals(previous.getCurrentHash())) {
             return false;
         }
     }

     return true;
 }


    public List<Block> getAllBlocks() {
        return blockRepo.findAll(Sort.by("id"));
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
            return  blockRepo.save(b1);


        }

       public String calculateHash(Block block) {
           String time = block.getTimestamp()
                   .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

           String input = (block.getPreviousHash() == null ? "" : block.getPreviousHash())
                   + block.getData()
                   + time;

           return DigestUtils.sha256Hex(input);
       }


        public List<Block> getChain() {
            return blockRepo.findAll(Sort.by("id"));
        }

        public Optional<Block> findByHash(String hash) {
            return blockRepo.findByCurrentHash(hash);
        }

    public void addVoteBlock(Long electionId, Long candidateId, Long voterId, String blockData) {

        Optional<Block> latestBlockOpt = blockRepo.findTopByOrderByIdDesc();

        String previousHash = latestBlockOpt
                .map(Block::getCurrentHash)
                .orElse("0");

        Block newBlock = Block.builder()
                .previousHash(previousHash)
                .data(blockData)
                .timestamp(LocalDateTime.now())
                .build();

        // ✅ FIXED
        String hash = calculateHash(newBlock);
        newBlock.setCurrentHash(hash);

        newBlock.setElectionId(electionId);

        blockRepo.save(newBlock);
    }






}



