package org.dota.steam.service;

import org.dota.model.Hero;
import org.dota.steam.service.http.json.DotaHeroJson;
import org.dota.steam.service.http.json.DotaHeroesPageJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DotaHeroesService {

    @Value(value = "classpath:heroes.json")
    private Resource heroesJsonFile;

    /* <hero_id, hero_model> */
    private Map<Integer, Hero> heroes = Collections.emptyMap();

    @PostConstruct
    private void loadHeroes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        List<DotaHeroJson> jsonHeroes = mapper
                .readValue(heroesJsonFile.getFile(), DotaHeroesPageJson.class)
                .getHeroes();

        this.heroes = jsonHeroes
                .stream()
                .map(this::fromJsonHero)
                .collect(Collectors.toMap(Hero::getId, Function.identity()));
        log.info("Load {} heroes from {} file", heroes.size(), heroesJsonFile.getFilename());
    }

    public Optional<Hero> getHeroById(Integer id) {
        return Optional.ofNullable(heroes.get(id));
    }

    public List<Hero> getHeroes() {
        return new ArrayList<>(heroes.values());
    }

    private Hero fromJsonHero(DotaHeroJson jsonHero) {
        return new Hero(jsonHero.getId(), jsonHero.getLocalizedName());
    }
}
