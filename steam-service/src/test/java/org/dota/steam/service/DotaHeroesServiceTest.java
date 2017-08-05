package org.dota.steam.service;

import org.dota.model.Hero;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        DotaHeroesService.class
})
@TestPropertySource(properties = "steam.api.dev_key=test_dev_key")
public class DotaHeroesServiceTest {

    @Autowired
    private DotaHeroesService heroesService;

    @Test
    public void testNoSuchHero() {
        Optional<Hero> nonExistHero = heroesService.getHeroById(1_000_000);
        Optional<Hero> expected = Optional.empty();

        assertThat(nonExistHero, equalTo(expected));
    }

    @Test
    public void testGetVoidHero() {
        Integer voidId = 41;
        Optional<Hero> voidHero = heroesService.getHeroById(voidId);
        Optional<Hero> expected = Optional.of(new Hero(voidId, "Faceless Void"));

        assertThat(voidHero, equalTo(expected));
    }

    @Test
    public void testGetAllHeroes() {
        List<Hero> allHeroes = heroesService.getHeroes();
        Integer expectedNumberOfHeroes = 113;

        assertThat(allHeroes.size(), equalTo(expectedNumberOfHeroes));
    }
}
