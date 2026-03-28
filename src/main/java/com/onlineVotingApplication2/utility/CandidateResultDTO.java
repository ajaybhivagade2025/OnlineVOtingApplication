package com.onlineVotingApplication2.utility;


import lombok.Data;

//@Data
public class CandidateResultDTO {
    private String name;
    private int voteCount;
    private String party;
    private String logo;


    public CandidateResultDTO(String name, int voteCount,String party,String logo) {
        this.name = name;
        this.voteCount = voteCount;
        this.party=party;
        this.logo=logo;
    }

    public String getLogo() {
        return logo;
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
