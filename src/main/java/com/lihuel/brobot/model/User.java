package com.lihuel.brobot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String discordId;
    private String steamUserId;
    private String steamProfileUrl;
}
