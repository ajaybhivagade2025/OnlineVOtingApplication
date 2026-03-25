package com.onlineVotingApplication2.utility;


import lombok.Data;

//@Data
public class CandidateResultDTO {
    private String name;
    private int voteCount;
    private String party;


    public CandidateResultDTO(String name, int voteCount,String party) {
        this.name = name;
        this.voteCount = voteCount;
        this.party=party;
    }

    public String getName() {
        return name;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getParty(){
        return party;
    }

}
