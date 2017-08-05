package org.dota.steam.service.http.json;

public class DotaPlayerJson {

    private Long accountId;
    private Integer playerSlot;
    private Integer heroId;

    public Long getAccountId() {
        return accountId;
    }

    public Integer getPlayerSlot() {
        return playerSlot;
    }

    public Integer getHeroId() {
        return heroId;
    }

    @Override
    public String toString() {
        return "DotaPlayerJson{" +
                "accountId=" + accountId +
                ", playerSlot=" + playerSlot +
                ", heroId=" + heroId +
                '}';
    }
}
