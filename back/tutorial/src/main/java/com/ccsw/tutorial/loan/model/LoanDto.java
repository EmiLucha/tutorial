package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;

import java.util.Date;

public class LoanDto {

    private Long id;

    private Date checkOutDate;

    private Date returnDate;

    private Client client;

    private Game game;

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
    public Client getClient() {

        return this.client;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(Client client) {

        this.client = client;
    }

    /**
     * @return game
     */
    public Game getGame() {

        return this.game;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(Game game) {

        this.game = game;
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
