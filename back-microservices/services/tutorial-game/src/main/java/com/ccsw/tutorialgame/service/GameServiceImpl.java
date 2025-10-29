package com.ccsw.tutorialgame.service;

import com.ccsw.tutorialgame.common.criteria.SearchCriteria;
import com.ccsw.tutorialgame.model.game.Game;
import com.ccsw.tutorialgame.model.game.GameDto;
import com.ccsw.tutorialgame.repository.GameRepository;
import com.ccsw.tutorialgame.repository.GameSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> findAll() {

        return (List<Game>) this.gameRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Game> find(String title, Long idCategory) {

        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));

        Specification<Game> spec = titleSpec.and(categorySpec);

        return this.gameRepository.findAll(spec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, GameDto dto) {

        Game game;

        if (id == null) {
            game = new Game();
        } else {
            game = this.gameRepository.findById(id).orElse(null);
        }

        BeanUtils.copyProperties(dto, game, "id", "author", "category");

        game.setIdAuthor(dto.getAuthor().getId());
        game.setIdCategory(dto.getCategory().getId());

        this.gameRepository.save(game);
    }

}