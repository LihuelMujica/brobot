package com.lihuel.brobot.repository;

import com.lihuel.brobot.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    List<Game> findBySteamIdInAndHasSteamPlayTogetherIsTrue(List<String> steamIds);
    List<Game> findByHasPiratedMultiplayerIsTrue();
}
