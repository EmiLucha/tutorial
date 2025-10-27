package com.ccsw.tutorialgame.service;

import com.ccsw.tutorialgame.model.Game;
import com.ccsw.tutorialgame.model.GameDto;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface GameService {

    /**
     * Recupera los juegos filtrando opcionalmente por título y/o categoría
     *
     * @param title título del juego
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link Game}
     */
    List<Game> find(String title, Long idCategory);

    /**
     * Recupera un listado de juegos {@link Game}
     *
     * @return {@link List} de {@link Game}
     */
    List<Game> findAll();

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador está o no informado
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, GameDto dto);

}