package com.dotcare.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Mother {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nic;

    @Column(nullable = false)
    private String name;

    private String status;

    @OneToMany(mappedBy = "mother", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Breaks the recursion
    private List<Referral> referrals;

    public Mother() {
    }

    public Mother(String nic, String name) {
        this.nic = nic;
        this.name = name;
    }
}
