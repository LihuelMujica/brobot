package com.lihuel.brobot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "games")
@Data
public class Game {

    @Id
    private String steamId;
    private Boolean hasPiratedMultiplayer;
    private String name;
    private String steamUrl;

}
