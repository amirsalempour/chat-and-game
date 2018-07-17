package com.amir.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by AmirSP on 7/13/2018.
 */
@Data
@ToString
@Entity
@Table(name = "gmusr100")
public class UserDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USE_SEQ")
    @SequenceGenerator(
            sequenceName = "user_sequence", allocationSize = 1
            , name = "USE_SEQ")
    @Column(name = "gmid100")
    private Long id;

    @Column(name = "gmCRTDT100")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "gmusrnme100")
    private String userName;

    @Column(name = "gmpnt100")
    private Long point=0L;
}
