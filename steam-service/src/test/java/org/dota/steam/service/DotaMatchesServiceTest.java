package org.dota.steam.service;

import org.dota.steam.service.http.client.SteamClient;
import org.dota.steam.service.http.client.SteamClientConfiguration;
import org.dota.steam.service.http.json.DotaMatchJson;
import org.dota.steam.service.http.json.DotaShortMatchInfoJson;
import org.dota.steam.service.http.json.DotaMatchShortInfosPageJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.dota.model.Match;
import org.dota.model.ShortMatchInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        SteamClientConfiguration.class,
        DotaMatchesService.class,
        DotaHeroesService.class,
        DotaInventoryItemsService.class
})
@TestPropertySource(properties = "steam.api.dev_key=test_dev_key")
public class DotaMatchesServiceTest {

    private final String DEV_KEY = "test_dev_key";
    private final Long MATCH_OFFSET = 0L;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DotaMatchesService service;

    @MockBean
    private SteamClient client;

    @Before
    public void initClient() {
        when(client.fetchDotaMatchShortInfos(eq(DEV_KEY), anyLong())).thenReturn(matchesPage());
    }

    @Test
    public void testNoMatchesProvidedBySteamApi() {
        Flowable<ShortMatchInfo> matches = service
                .loadMatchesShortInfoFromOffset(MATCH_OFFSET)
                .timeout(1, TimeUnit.SECONDS);

        TestSubscriber<ShortMatchInfo> subscriber = matches.test();

        subscriber
                .assertSubscribed()
                .assertNoValues()
                .assertNotComplete()
                .assertError(TimeoutException.class);
    }

    @Test
    public void testOneMatchInOnePageProvidedBySteamApi() {
        when(client.fetchDotaMatchShortInfos(DEV_KEY, MATCH_OFFSET)).thenReturn(matchesPage(matchJson(1)));

        Flowable<ShortMatchInfo> matches = service
                .loadMatchesShortInfoFromOffset(MATCH_OFFSET)
                .timeout(1, TimeUnit.SECONDS);

        TestSubscriber<ShortMatchInfo> subscriber = matches.test();

        subscriber
                .assertSubscribed()
                .assertValueCount(1)
                .assertNotComplete();
    }

    @Test
    public void testTwoMatchesInOnePageProvidedBySteamApi() {
        when(client.fetchDotaMatchShortInfos(DEV_KEY, MATCH_OFFSET)).thenReturn(
                matchesPage(
                        matchJson(1),
                        matchJson(2)
                )
        );

        Flowable<ShortMatchInfo> matches = service
                .loadMatchesShortInfoFromOffset(MATCH_OFFSET)
                .timeout(1, TimeUnit.SECONDS);

        TestSubscriber<ShortMatchInfo> subscriber = matches.test();

        subscriber
                .assertSubscribed()
                .assertValueCount(2)
                .assertNotComplete();
    }

    @Test
    public void testMatchesInTwoPageProvidedBySteamApi() {
        when(client.fetchDotaMatchShortInfos(DEV_KEY, MATCH_OFFSET)).thenReturn(
                matchesPage(
                        matchJson(1),
                        matchJson(2)
                )
        );
        when(client.fetchDotaMatchShortInfos(DEV_KEY, 2L)).thenReturn(
                matchesPage(
                        matchJson(3)
                )
        );

        Flowable<ShortMatchInfo> matches = service
                .loadMatchesShortInfoFromOffset(MATCH_OFFSET)
                .timeout(2, TimeUnit.SECONDS);

        TestSubscriber<ShortMatchInfo> subscriber = matches.test();

        subscriber
                .assertSubscribed()
                .assertValueCount(3)
                .assertNotComplete();
    }

    @Test
    public void testLoadMatchById() throws IOException {
        Long matchId = 1L;

        when(client.fetchDotaMatch(
                eq(DEV_KEY),
                eq(matchId))
        ).thenReturn(exampleOfMatch());

        Match match = service.loadMatchById(matchId);

        assertThat(match.getDurationInSeconds(), equalTo(3775));

        assertThat(match.getPlayers().size(), equalTo(10));

        assertThat(
                match.getRadiantBuildingsStatus().getTowersStatus().getTier3().getMiddleExists(),
                equalTo(false)
        );

        assertThat(
                match.getRadiantBuildingsStatus().getTowersStatus().getTier3().getBottomExists(),
                equalTo(true)
        );

        assertThat(
                match.getDireBuildingsStatus().getBarracksStatus().getTop().getRangedExists(),
                equalTo(true)
        );

        assertThat(
                match.getDireBuildingsStatus().getBarracksStatus().getMiddle().getRangedExists(),
                equalTo(false)
        );
    }

    private DotaMatchShortInfosPageJson matchesPage(DotaShortMatchInfoJson... matches) {
        return new DotaMatchShortInfosPageJson(
                1,
                matches.length,
                matches.length,
                0,
                Arrays.asList(matches));
    }

    private DotaShortMatchInfoJson matchJson(Integer matchSeqId) {
        Long defaultStartTime = 0L;
        Integer defaultLobby = 0;
        Integer defaultTeamId = 0;
        return new DotaShortMatchInfoJson(
                matchSeqId.longValue(),
                matchSeqId.longValue(),
                defaultStartTime,
                defaultLobby,
                defaultTeamId,
                defaultTeamId,
                Collections.emptyList()
        );
    }

    /**
     *
     * @return match: https://www.dotabuff.com/matches/3350367970
     */
    private DotaMatchJson exampleOfMatch() throws IOException {
        return mapper.readValue(
                new File("src/test/resources/example_of_match.json"),
                DotaMatchJson.class
        );
    }
}
