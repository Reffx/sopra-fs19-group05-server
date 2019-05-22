package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.controller.NonExistentGameException;
import ch.uzh.ifi.seal.soprafs19.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.entity.Board;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final GameRepository gameRepository;


    @Autowired
    public BoardService(BoardRepository boardRepository, GameRepository gameRepository) {

        this.boardRepository = boardRepository;
        this.gameRepository = gameRepository;

    }

    public Board initBoard(Long gameId) {

        Board board = new Board();
        board.setId(gameId);
        List<Field> allFields = new ArrayList<>();
        //  initialize the coordinates
        int num = 0;
        for( int i = 0; i <= 4; ++i) {
            for (int j = 0; j <= 4; ++j) {
                Field field = new Field();

                field.setFieldNum(num);
                field.setX_coordinate(i);
                field.setY_coordinate(j);
                ++num;
                allFields.add(field);
            }
        }

        board.setAllFields(allFields);
        boardRepository.save(board);
        return board;
    }

    public Board updateBoard (Board newBoard){
        Board tempBoard = boardRepository.findById(newBoard.getId());
        if(tempBoard ==null){
            throw new NonExistentGameException("Could not update board because it doesn't exist");
        }

        boardRepository.save(tempBoard);
        return newBoard;
    }

    //JuWe: updated the getBoard function in order to have the functionailty to create a board or retrieve one from the repo
    public Board getBoard(long gameId) {
        if (boardRepository.findById(gameId) != null) {
            return (boardRepository.findById(gameId));
        }
        else if(gameRepository.getById(gameId) == null){
            throw new NonExistentGameException("Game was not found, Could not retrieve board!");
        }
        else{
            //  do recording
            Board newBoard = initBoard(gameId);
            return newBoard;
        }
    }

    public Field getField(int fieldNum, long id){
        //this function returns the field object on the fieldNum-th position in the allFields list
        // e.g. fieldNum is 4, then it will return the 4th entry of the allFields list which is the 4th field
        return boardRepository.findById(id).getAllFields().get(fieldNum);
    }

}
