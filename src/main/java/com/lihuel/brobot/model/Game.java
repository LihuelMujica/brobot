package com.lihuel.brobot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    private String steamId;
    private Boolean hasPiratedMultiplayer;
    private String name;
    private String steamUrl;

}
