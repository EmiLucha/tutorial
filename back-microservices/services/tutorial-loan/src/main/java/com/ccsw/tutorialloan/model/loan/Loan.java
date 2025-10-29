package com.ccsw.tutorialloan.model.loan;

import jakarta.persistence.*;

import java.util.Date;

/**
 * @author ccsw
 *
 */
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "check_out_date", nullable = false)
    private Date checkOutDate;

    @Column(name = "return_date", nullable = false)
    private Date returnDate;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return client
     */
    public Long getClient() {

        return this.clientId;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(Long client) {

        this.clientId = client;
    }

    /**
     * @return game
     */
    public Long getGame() {

        return this.gameId;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(Long game) {

        this.gameId = game;
    }

    /**
     * @return checkOutDate
     */
    public Date getCheckOutDate() {

        return this.checkOutDate;
    }

    /**
     * @param checkOutDate new value of {@link #getCheckOutDate}.
     */
    public void setCheckOutDate(Date checkOutDate) {

        this.checkOutDate = checkOutDate;
    }

    /**
     * @return returnDate
     */
    public Date getReturnDate() {

        return this.returnDate;
    }

    /**
     * @param returnDate new value of {@link #getReturnDate}.
     */
    public void setReturnDate(Date returnDate) {

        this.returnDate = returnDate;
    }

}