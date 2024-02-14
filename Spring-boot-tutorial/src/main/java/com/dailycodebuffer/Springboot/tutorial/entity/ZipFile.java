package com.dailycodebuffer.Springboot.tutorial.entity;


import jakarta.persistence.*;

@Entity
public class ZipFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] content;

    // getters and setters
}