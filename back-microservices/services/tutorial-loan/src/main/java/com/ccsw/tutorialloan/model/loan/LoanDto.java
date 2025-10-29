package com.ccsw.tutorialloan.model.loan;

import com.ccsw.tutorialloan.model.client.ClientDto;
import com.ccsw.tutorialloan.model.game.GameDto;

import java.util.Date;

public class LoanDto {

    private Long id;

    private Date checkOutDate;

    private Date returnDate;

    private ClientDto client;

    private GameDto game;

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
    public ClientDto getClient() {

        return this.client;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(ClientDto client) {

        this.client = client;
    }

    /**
     * @return game
     */
    public GameDto getGame() {

        return this.game;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(GameDto game) {

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
