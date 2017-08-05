package org.dota.steam.service.http.client;


import org.dota.steam.service.http.json.DotaMatchesPageJson;
import org.dota.steam.service.http.json.DotaMatchShortInfosPageJson;
import org.dota.steam.service.http.json.DotaMatchJson;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "steam-dota-client",
        url = "https://api.steampowered.com",
        configuration = SteamClientConfiguration.class
)
public interface SteamClient {

    @RequestMapping(method = GET, path = "/IDOTA2Match_570/GetMatchHistory/V001/", params = "format=JSON")
    DotaMatchShortInfosPageJson fetchDotaMatchShortInfos(@RequestParam("key") String key,
                                                         @RequestParam("start_at_match_seq_num") Long startAtMatchOffset);

    /**
     * @param skill 0 - Any, 1 - Normal, 2 - High, 3 - Very High
     */
    @RequestMapping(method = GET, path = "/IDOTA2Match_570/GetMatchHistory/V001/", params = "format=JSON")
    DotaMatchShortInfosPageJson fetchDotaMatchShortInfosBySkill(@RequestParam("key") String key,
                                                                @RequestParam("start_at_match_seq_num") Long startAtMatchOffset,
                                                                @RequestParam("skill") Integer skill);

    @RequestMapping(method = GET, path = "/IDOTA2Match_570/GetMatchHistory/V001/", params = "format=JSON, tournament_games_only=1")
    DotaMatchShortInfosPageJson fetchTournamentDotaMatchShortInfos(@RequestParam("key") String key,
                                                                   @RequestParam("start_at_match_seq_num") Long startAtMatchOffset);

    @RequestMapping(method = GET, path = "/IDOTA2Match_570/GetMatchHistoryBySequenceNum/V001/", params = "format=JSON")
    DotaMatchesPageJson fetchDotaMatches(@RequestParam("key") String key,
                                         @RequestParam("start_at_match_seq_num") Long startAtMatchOffset);

    @RequestMapping(method = GET, path = "/IDOTA2Match_570/GetMatchDetails/V001/", params = "format=JSON")
    DotaMatchJson fetchDotaMatch(@RequestParam("key") String key,
                                 @RequestParam("match_id") Long matchId);


}
