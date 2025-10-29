package com.ccsw.tutorialgame.controller;

import com.ccsw.tutorialgame.model.author.AuthorClient;
import com.ccsw.tutorialgame.model.author.AuthorDto;
import com.ccsw.tutorialgame.model.category.CategoryClient;
import com.ccsw.tutorialgame.model.category.CategoryDto;
import com.ccsw.tutorialgame.model.game.Game;
import com.ccsw.tutorialgame.model.game.GameDto;
import com.ccsw.tutorialgame.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Game", description = "API of Game")
@RequestMapping(value = "/game")
@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    ModelMapper mapper;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    AuthorClient authorClient;

    /**
     * Método para recuperar una lista de {@link Game}
     *
     * @param title título del juego
     * @param idCategory PK de la categoría
     * @return {@link List} de {@link GameDto}
     */
    @Operation(summary = "Find", description = "Method that return a filtered list of Games")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GameDto> find(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "idCategory", required = false) Long idCategory) {

        List<CategoryDto> categories = categoryClient.findAll();
        List<AuthorDto> authors = authorClient.findAll();

        return gameService.find(title, idCategory).stream().map(game -> {
            GameDto gameDto = new GameDto();

            gameDto.setId(game.getId());
            gameDto.setTitle(game.getTitle());
            gameDto.setAge(game.getAge());
            gameDto.setCategory(categories.stream().filter(category -> category.getId().equals(game.getIdCategory())).findFirst().orElse(null));
            gameDto.setAuthor(authors.stream().filter(author -> author.getId().equals(game.getIdAuthor())).findFirst().orElse(null));

            return gameDto;
        }).collect(Collectors.toList());
    }

    /**
     * Recupera un listado de juegos {@link Game}
     *
     * @return {@link List} de {@link GameDto}
     */
    @Operation(summary = "FindAll", description = "Method that return a list of Games")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<GameDto> findAll() {

        List<CategoryDto> categories = categoryClient.findAll();
        List<AuthorDto> authors = authorClient.findAll();

        return gameService.findAll().stream().map(game -> {
            GameDto gameDto = new GameDto();

            gameDto.setId(game.getId());
            gameDto.setTitle(game.getTitle());
            gameDto.setAge(game.getAge());
            gameDto.setCategory(categories.stream().filter(category -> category.getId().equals(game.getIdCategory())).findFirst().orElse(null));
            gameDto.setAuthor(authors.stream().filter(author -> author.getId().equals(game.getIdAuthor())).findFirst().orElse(null));

            return gameDto;
        }).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Game}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Game")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody GameDto dto) {

        gameService.save(id, dto);
    }

}