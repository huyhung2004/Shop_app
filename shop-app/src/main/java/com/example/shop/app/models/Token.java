package com.example.shop.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",length = 225)
    private String token;
    @Column(name = "token_type",length = 50)
    private String tokenType;
    @Column(name = "expiration_date",length = 225)
    private LocalDateTime expirationDate;

    private Boolean revoked;
    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

}
