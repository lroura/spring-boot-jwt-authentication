package com.example.demo.models.management.userlogin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "gi_authority",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniqueAuthorityName",
                        columnNames = {"name"}
                )
        })
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
