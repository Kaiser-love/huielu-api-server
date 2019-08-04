package com.ronghui.service.entity;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "directory", schema = "ronghui")
@Getter
@Setter
@NoArgsConstructor
public class Directory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "dirid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dirid;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @OneToMany(mappedBy = "directory", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Picture> pictures = new HashSet<Picture>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return dirid == directory.dirid &&
                Objects.equals(name, directory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dirid, name);
    }

    @Override
    public String toString() {
        return "Directory{dirid='" + dirid + "', name='" + name + "'}";
    }
}
