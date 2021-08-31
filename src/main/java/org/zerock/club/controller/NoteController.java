package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.security.dto.NoteDTO;
import org.zerock.club.security.service.NoteService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    /**
     *
     * @param noteDTO
     * @return
     */
    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {

        log.info("--------------register--------------");
        log.info(String.valueOf(noteDTO));

        Long num = noteService.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    /**
     * 특정 번호의 Note 확인하기
     * @param num
     * @return
     */
    @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num) {

        log.info("--------------read--------------");
        log.info(String.valueOf(num));

        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    /**
     * 특정 회원 모든 Note 확인하기
     * @param email
     * @return
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteDTO>> getList(String email) {

        log.info("-------------getList----------------");
        log.info(email);

        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num) {
        log.info("-------------remove----------------");
        log.info(String.valueOf(num));

        noteService.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {
        log.info("-------------modify----------------");
        log.info(String.valueOf(noteDTO));

        noteService.modify(noteDTO);

        return new ResponseEntity<>("modified", HttpStatus.OK);
    }
}