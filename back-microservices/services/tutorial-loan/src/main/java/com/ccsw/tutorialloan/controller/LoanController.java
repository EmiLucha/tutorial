package com.ccsw.tutorialloan.controller;

import com.ccsw.tutorialloan.model.client.ClientClient;
import com.ccsw.tutorialloan.model.client.ClientDto;
import com.ccsw.tutorialloan.model.game.GameClient;
import com.ccsw.tutorialloan.model.game.GameDto;
import com.ccsw.tutorialloan.model.loan.Loan;
import com.ccsw.tutorialloan.model.loan.LoanDto;
import com.ccsw.tutorialloan.model.loan.LoanSearchDto;
import com.ccsw.tutorialloan.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    GameClient gameClient;

    @Autowired
    ClientClient clientClient;

    /**
     * Método para recuperar un listado paginado y filtrado de {@link Loan}
     *
     * @param idGame PK del juego
     * @param idClient PK del cliente
     * @param date que se quiere consultar
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link LoanDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a filtered page of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestParam(value = "idGame", required = false) Long idGame, @RequestParam(value = "idClient", required = false) Long idClient, @RequestParam(value = "date", required = false) Date date,
            @RequestBody LoanSearchDto dto) {

        Page<Loan> page = this.loanService.findPage(idGame, idClient, date, dto);

        List<ClientDto> clients = clientClient.findAll();
        List<GameDto> games = gameClient.findAll();

        List<LoanDto> loanDtos = page.getContent().stream().map(loan -> {

            LoanDto loanDto = new LoanDto();
            loanDto.setId(loan.getId());
            loanDto.setCheckOutDate(loan.getCheckOutDate());
            loan.setReturnDate(loan.getReturnDate());

            loanDto.setClient(clients.stream().filter(client -> client.getId().equals(loan.getClient())).findFirst().orElse(null));

            loanDto.setGame(games.stream().filter(game -> game.getId().equals(loan.getGame())).findFirst().orElse(null));

            return loanDto;

        }).collect(Collectors.toList());

        return new PageImpl<>(loanDtos, page.getPageable(), page.getTotalElements());

    }

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {

        this.loanService.save(id, dto);
    }

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

}
