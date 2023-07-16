package com.lihuel.brobot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "games")
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean hasPiratedMultiplayer;
    private String name;
    private String steamId;
    private String steamUrl;

}
